/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_exo5;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Dell
 */
public class PSO {

    public int[] recherche(int n, int tmax, int c1, int c2, int w, int[][] dis, int numCities) {
        //System.out.println("\n-------------------Deroulement---------------");
        Partical[] particalTable = new Partical[n];
        int[] gbest;

        particalTable = initialization(n, dis, numCities);
        //afficherParticules(particalTable);
        //System.out.println("\n" + particalTable.length);

        gbest = updateGbest(particalTable, dis, numCities);

        for (int i = 0; i < tmax; i++) {

            for (int j = 0; j < n; j++) {
                particalTable[j].v = updateVelocity(particalTable[j].x, particalTable[j].v, w, c1, c2, particalTable[j].pbest, gbest);
                particalTable[j].x = updatePosition(particalTable[j].v, particalTable[j].x, numCities);
                particalTable[j].pbest = updatePbest(particalTable[j].pbest, particalTable[j].x, dis, numCities);
            }

            //System.out.println("\n particules aprés mise à jour");
            //afficherParticules(particalTable);

            gbest = updateGbest(particalTable, dis, numCities);
        }

        return decodage(gbest, numCities);
    }

    public static int[] generateInitSolution(int numCities, int[][] dis) {

        int[] sol = new int[numCities + 1];

        Random random = new Random();
        int derniereVilleVisitee, villeVoisine;
        int ville = random.nextInt(numCities) + 1;
        sol[0] = ville; // première ville est choisie aleatoirement
        sol[numCities] = ville;  // première ville doit etre la dernière ville aussi 
        boolean[] visites = new boolean[numCities];
        visites[ville - 1] = true;
        // choisir les autre ville 
        for (int i = 1; i < numCities; i++) {

            derniereVilleVisitee = sol[i - 1];
            do {
                villeVoisine = random.nextInt(numCities) + 1; // choisir un indice aleatoire pour selectionner une ville voisine de la ville courante

            } while (dis[derniereVilleVisitee - 1][villeVoisine - 1] == 0 || visites[villeVoisine - 1] == true);

            sol[i] = villeVoisine;
            visites[villeVoisine - 1] = true;
        }
        return sol;
    }

    public static ArrayList<int[]> generateInitParticals(int numCities, int n, int[][] dis) {

        ArrayList<int[]> populationInit = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            populationInit.add(generateInitSolution(numCities, dis));
        }

        return populationInit;
    }

    public int[] codage(int[] partical, int numCities) {
        int[] particalCoded = new int[partical.length * (Integer.SIZE - Integer.numberOfLeadingZeros(numCities))];

        // Itérer sur chaque élément de partical
        for (int i = 0; i < partical.length; i++) {
            // Convertir l'élément en binaire sous forme de chaîne de caractères
            String binaryString = Integer.toBinaryString(partical[i]);

            // Ajouter les zéros de remplissage nécessaires pour avoir une longueur fixe
            int leadingZeros = (Integer.SIZE - Integer.numberOfLeadingZeros(numCities)) - binaryString.length();
            String paddedBinaryString = "0".repeat(leadingZeros) + binaryString;

            // Ajouter chaque chiffre binaire dans particalCoded
            for (int j = 0; j < paddedBinaryString.length(); j++) {
                particalCoded[i * paddedBinaryString.length() + j] = Character.getNumericValue(paddedBinaryString.charAt(j));
            }
        }

        return particalCoded;
    }

    public int[] decodage(int[] particalCoded, int numCities) {
        int[] partical = new int[particalCoded.length / (Integer.SIZE - Integer.numberOfLeadingZeros(numCities))];

        // Longueur de chaque nombre binaire dans particalCoded
        int binaryLength = Integer.SIZE - Integer.numberOfLeadingZeros(numCities);

        // Itérer sur chaque sous-tableau de particalCoded correspondant à un nombre binaire
        for (int i = 0; i < partical.length; i++) {
            StringBuilder binaryStringBuilder = new StringBuilder();

            // Construire la représentation binaire du nombre à partir de particalCoded
            for (int j = 0; j < binaryLength; j++) {
                binaryStringBuilder.append(particalCoded[i * binaryLength + j]);
            }

            // Convertir la représentation binaire en décimal
            int decimalValue = Integer.parseInt(binaryStringBuilder.toString(), 2);

            // Stocker la valeur décimale dans partical
            partical[i] = decimalValue;
        }

        return partical;
    }

    public Partical[] initialization(int n, int[][] dis, int numCities) {
        Random random = new Random();
        Partical[] particalTable = new Partical[n];
        // generer les xi
        ArrayList<int[]> x = generateInitParticals(numCities, n, dis);
        // codage des xi pour pouvoir appliquer le pso 

        for (int i = 0; i < n; i++) {
            particalTable[i] = new Partical();
            particalTable[i].x = codage(x.get(i), numCities);
            particalTable[i].v = random.nextInt(numCities) + 1;
            particalTable[i].pbest = particalTable[i].x;
        }
        return particalTable;
    }

    public int[] updateGbest(Partical[] particalTable, int[][] dis, int numCities) {
        //System.out.print("gebest updating ...");
        int[] gbest = particalTable[0].pbest;

        for (int i = 1; i < particalTable.length; i++) {
            if (fitness(particalTable[i].pbest, dis, numCities) < fitness(gbest, dis, numCities)) {

                gbest = particalTable[i].pbest;
            }
        }
        // System.out.print("gebest updated ...");
        // afficher(gbest);
        return gbest;
    }

    public int fitness(int[] partical, int[][] dis, int numCities) {

        int[] p = decodage(partical, numCities);
        int somd = 0;
        //afficher(p);
        //afficher(partical);
        //System.out.println();
        for (int i = 0; i < numCities; i++) {
            somd = somd + dis[p[i] - 1][p[i + 1] - 1];
        }
        //System.out.println(somd);
        return somd;
    }

    public int updateVelocity(int[] x, int v, int w, int c1, int c2, int[] pbest, int[] gbest) {
        Random random = new Random();
        float r1 = random.nextInt(1);
        float r2 = random.nextInt(1);
        //System.out.print((int) (w * v + c1 * r1 * distance(pbest, x) + c2 * r2 * distance(gbest, x)));
        return (int) (w * v + c1 * r1 * distance(pbest, x) + c2 * r2 * distance(gbest, x));
    }
    
    // lr nombre des bits differents entre x et y
    public int distance(int[] x, int[] y) {
        int d = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] != y[i]) {
                d++;
            }
        }

        return d;
    }

    public int[] updatePbest(int[] pbest, int[] x, int[][] dis, int numCities) {

        if (fitness(x, dis, numCities) < fitness(pbest, dis, numCities)) {

            pbest = x;
        }

        return pbest;
    }

    // inverser un nombre de bits égale à v du vecteur x , v etant la velocity 
    public int[] updatePosition(int v, int[] x, int numCities) {
        Random random = new Random();
        int ind = 0;
        //System.out.print("updating position..");
        for (int i = 0; i < v; i++) {

            ind = random.nextInt(x.length);
            x[ind] = (x[ind] == 1) ? 0 : 1;
            if (!valide(x, numCities)) {
                x[ind] = (x[ind] == 1) ? 0 : 1;
            }

        }
        //System.out.println();
        //afficher(decodage(x, numCities));
        return x;
    }

    public boolean valide(int[] x, int numCities) {
        boolean[] villesVisitees = new boolean[numCities + 1];
        int[] xdecoded = decodage(x, numCities);
        //System.out.println("à valider");
        //afficher(xdecoded);
        // Vérifier que la première et la dernière ville sont les mêmes
        if (xdecoded[0] > numCities || xdecoded[0] < 1 || xdecoded[numCities] > numCities || xdecoded[numCities] < 1) {
            return false;
        } else {
            if (xdecoded[0] != xdecoded[numCities]) {
                return false;
            }
        }

        // Vérifier l'unicité des villes visitées
        for (int i = 0; i < numCities; i++) {
            int ville = xdecoded[i];
            if (xdecoded[i] > numCities || xdecoded[i] < 1) {
                return false;
            }
            if (villesVisitees[ville]) {
                // La ville a déjà été visitée
                return false;
            }
            villesVisitees[ville] = true;
        }

        return true;
    }

    // instance du problème 
    public static int[][] generateDistanceMatrix(int numCities) {
        int[][] distances = new int[numCities][numCities];
        Random random = new Random();

        // Générer des distances aléatoires entre les villes
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                int distance = random.nextInt(100) + 1; // Distance aléatoire 
                distances[i][j] = distance;
                distances[j][i] = distance; // Symétrie de la matrice
            }
        }
        return distances;
    }

    public static void printDistanceMatrix(int[][] distances) {
        System.out.println("Matrice des distances entre les villes :");
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
                System.out.print(distances[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void afficher(int[] T) {

        for (int i = 0; i < T.length; i++) {
            System.out.print(T[i] + " ,");
        }

    }

    public static void afficherParticule(Partical p) {

        System.out.print("posittion du particule:");
        afficher(p.x);
        System.out.println();
        System.out.println("velocity : " + p.v + ",");
        System.out.print("pbest du particule:");
        afficher(p.x);
        System.out.println();

    }

    public static void afficherParticules(Partical[] p) {

        for (int i = 0; i < p.length; i++) {
            afficherParticule(p[i]);
        }

    }
}
