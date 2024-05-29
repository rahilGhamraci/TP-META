/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_exo3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Dell
 */
public class A {

    public Noeud recherche(Noeud etatInitial, int[][] dis, int dmax, Noeud BestSol, int nbV) {
        ArrayList<Noeud> OUVERT = new ArrayList<>();
        ArrayList<Noeud> FERME = new ArrayList<>();
        ArrayList<Noeud> enfants = new ArrayList<>();

        Noeud d, n;

        OUVERT.add(etatInitial);

        while (!OUVERT.isEmpty()) {
            n = OUVERT.get(0);
            OUVERT.remove(0);
            FERME.add(n);
            if (etatFinal(n, dis, dmax, nbV) && evaluation(n, dis, nbV) < evaluation(BestSol, dis, nbV)) {
                BestSol = n;
            }

            enfants = Successeurs(n, nbV, dis);
            for (Noeud enfant : enfants) {
                if (!contien(FERME, enfant)) {
                    System.out.println("enfant ajoute a ouvert avec f="+enfant.f);
                    afficher(enfant.sol);
                    System.out.println("");
                    OUVERT.add(enfant);
                    Collections.sort(OUVERT, new Comparator<Noeud>() {
                        @Override
                        public int compare(Noeud n1, Noeud n2) {
                            return Integer.compare(n1.f, n2.f);
                        }
                    });

                }

            }
        }
        return BestSol;

    }

    // a definir
    public boolean etatFinal(Noeud n, int[][] d, int dmax, int nbV) {
        boolean v = true; // v pour valide
        int i = 0;
        int j = 1;
        int somd = 0;

        if (n.sol.length != nbV) {
            //System.out.println("test\n" + n.sol.length);
            v = false;
        } else {
            //System.out.println("n.sol=");
            // afficher(n.sol);
            //System.out.println(" ");
            while (i < nbV - 1 && v) {
                j = i + 1;
                while (j <= nbV - 1 && v) {
                    if (n.sol[i] == n.sol[j]) {
                        System.out.println("une ville qui se repete \n");
                        v = false;
                    } else {
                        j = j + 1;
                    }
                }

                i = i + 1;
            }

        }

        return v;
    }

    public int evaluation(Noeud n, int[][] dis, int nbV) {
        int i = 0;
        int somd = 0;
        //System.out.println("nombre de ville  " + nbV);
        //System.out.println("n.sol.length " + n.sol.length);
        while (i < nbV - 1) {
            somd = somd + dis[n.sol[i] - 1][n.sol[i + 1] - 1];
            i++;
        }
        somd = somd + dis[n.sol[n.sol.length - 1] - 1][n.sol[0] - 1];
        //System.out.println("sommedistance de la solution est "+somd);
        return somd;
    }

    public ArrayList<Noeud> Successeurs(Noeud n, int nbV, int[][] d) {
        ArrayList<Noeud> succ = new ArrayList<>();

        // Obtenir les villes non visitées à partir de l'état actuel du nœud
        boolean[] visites = new boolean[nbV + 1];
        if (n.sol.length > 0) {
            for (int ville : n.sol) {
                visites[ville] = true;
            }
        }
        // Générer les successeurs en ajoutant chaque ville non visitée à la fin de la solution actuelle
        for (int i = 1; i <= nbV; i++) {
            if (!visites[i]) {
                if (n.sol.length > 0) {
                    if (d[n.sol[n.sol.length - 1] - 1][i - 1] > 0) {
                        int[] newSolution = Arrays.copyOf(n.sol, n.sol.length + 1);
                        newSolution[newSolution.length - 1] = i;
                        int f = g(newSolution, d, nbV) + h(newSolution, nbV);
                        Noeud successor = new Noeud(newSolution, n, f);
                        succ.add(successor);

                    }

                } else {
                    int[] newSolution = Arrays.copyOf(n.sol, n.sol.length + 1);
                    newSolution[newSolution.length - 1] = i;
                    int f = g(newSolution, d, nbV) + h(newSolution, nbV);
                    Noeud successor = new Noeud(newSolution, n, f);
                    succ.add(successor);

                }

            }
        }

        return succ;
    }

    public void afficher(int[] T) {

        for (int i = 0; i < T.length; i++) {
            System.out.print(T[i] + " ");
        }

    }

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

    public boolean contien(ArrayList<Noeud> FERME, Noeud E) {

        int i = 0;
        boolean trouve = false;

        while (i < FERME.size() && !trouve) {
            if (Arrays.equals(FERME.get(i).sol, E.sol)) {
                trouve = true;
            }
            i++;
        }

        return trouve;

    }

    public void generer(int[] T, int nb) {

        Random random = new Random();

        for (int i = 0; i < nb; i++) {
            T[i] = random.nextInt(nb) + 1;
        }

    }
    // l'heuristique : le nombre de villes non encore visitées 

    private int h(int[] T, int nbV) {
        int nb_vulles_non_visites = 0;

        for (int i = 1; i <= nbV; i++) {
            if (!contains(T, i)) {

                nb_vulles_non_visites += 1;
            }
        }

        return nb_vulles_non_visites;
    }

    public static boolean contains(int[] T, int x) {
        for (int num : T) {
            if (num == x) {
                return true;
            }
        }
        return false;
    }

    // le cout  : la distance entre les villes constituant le vecteur sol
    private int g(int[] T, int[][] d, int nbV) {

        int somd = 0;

        for (int i = 0; i < T.length - 1; i++) {
            somd = somd + d[T[i] - 1][T[i + 1] - 1];

        }
        if (T.length == nbV) { // si l'etat est potentiellement un etat but alors il faut ajouter à la som des distance , la distance entre la derniere et la première ville du tableau sol
            somd = somd + d[T[T.length - 1] - 1][T[0] - 1];

        }

        return somd;

    }

}
