/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_exo4;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Dell
 */
public class AlgoGenV1 {

    public int[] recherche(float probaCross, float probaMutation, int nbGen, int taillePop, int[][] dis, int numCities) {

        int[] bestSolution = generateInitSolution(numCities, dis);
        System.out.println("best solution init");
        afficher(bestSolution);
        ArrayList<int[]> populationCrossOver;

        ArrayList<int[]> populationSelectionnee;

        ArrayList<int[]> populationMutated;

        ArrayList<int[]> population = generateInitPopulation(numCities, taillePop, dis);
        System.out.println("\npopulation initilale");
        afficherPop(population);

        for (int i = 0; i < nbGen; i++) {
            System.out.println("\n-------------------------------Genration " + i + "-------------------------------------");
            // selection des individus 
            populationSelectionnee = selectionnerPopu(population, taillePop, dis, numCities);
            System.out.println("\npopulation selectionne");
            afficherPop(populationSelectionnee);
            // crossover 
            populationCrossOver = crossover(populationSelectionnee, populationSelectionnee.size(), numCities);
            System.out.println("\npopulation apres application du crossover");
            afficherPop(populationCrossOver);
            // mutation
            populationMutated = mutation(populationCrossOver, populationCrossOver.size(), numCities);
            System.out.println("\npopulation apres application de la mutaion");
            afficherPop(populationMutated);
            //evaluation des individus crées
            int[] valeursF = evaluation(populationMutated, populationMutated.size(), dis, numCities);
            int indiceMin = Arrays.stream(valeursF)
                    .boxed()
                    .collect(java.util.stream.Collectors.toList())
                    .indexOf(Arrays.stream(valeursF).min().getAsInt());
            if (fitness(bestSolution, dis, numCities) > valeursF[indiceMin] && valide(populationMutated.get(indiceMin), numCities)) {
                bestSolution = populationMutated.get(indiceMin);
                System.out.println("nouvelle bestSolution");
                afficher(bestSolution);
            }
            //creation d'une nouvelle population
            population = remplacement(population, populationMutated, taillePop, dis, numCities);
            System.out.println("\npopulation apres remplacement");
            afficherPop(population);
        }

        return bestSolution;
    }

    public static ArrayList<int[]> remplacement(ArrayList<int[]> populationCourante, ArrayList<int[]> nouveauxInd, int taillePop, int[][] dis, int numCities) {
        // Fusion de la population courante avec les nouveaux individus
        ArrayList<int[]> populationFusionnee = new ArrayList<>(populationCourante);
        for (int[] individu : nouveauxInd) {
            if (valide(individu, numCities)) {
                populationFusionnee.add(individu);
            }
        }

        // Supprimer les individus invalides de la population fusionnée
        populationFusionnee.removeIf(individu -> !valide(individu, numCities));

        // Tri de la population fusionnée en fonction de la fitness de chaque individu
        populationFusionnee.sort(Comparator.comparingInt(individu -> fitness(individu, dis, numCities)));

        // Sélection des 'taillePop' premiers individus de la population triée (les meilleurs)
        ArrayList<int[]> nouvellePop = new ArrayList<>(populationFusionnee.subList(0, taillePop));

        return nouvellePop;
    }

    public static int[] evaluation(ArrayList<int[]> population, int taillePop, int[][] dis, int numCities) {
        int[] fitenessIndividus = new int[taillePop];

        for (int i = 0; i < taillePop; i++) {

            fitenessIndividus[i] = fitness(population.get(i), dis, numCities);

        }

        return fitenessIndividus;

    }

    public static ArrayList<int[]> selectionnerPopu(ArrayList<int[]> population, int taillePop, int[][] dis, int numCities) {

        // on prepare les donnees pour la selction par roulette 
        // pereparer les intervalles 
        Individus[] probaInd = new Individus[taillePop];
        int somf = 0;
        int f;
        for (int i = 0; i < taillePop; i++) {
            f = fitness(population.get(i), dis, numCities);
            probaInd[i] = new Individus(f, i); // pour chaque individus on stocke sa proba et son indice dans la population initiale
            somf += f;
        }
        for (int i = 0; i < taillePop; i++) {
            probaInd[i].proba /= somf;
        }
        System.out.println("vecteur de proba avant tri ");
        afficherP(probaInd);
        // trier les proba des individus afin d'avoir le max des proba dans
        //la première case et pouvoir construire les intervalles par la suite 
        Arrays.sort(probaInd, new Comparator<Individus>() {
            @Override
            public int compare(Individus i1, Individus i2) {
                // Tri en ordre décroissant
                return Float.compare(i2.proba, i1.proba);
            }
        });
        System.out.println("\nvecteur de proba aprés tri ");
        afficherP(probaInd);
        // construire les intervalles pour pouvoir les utiliser dans la selction
        Intervalle[] intervalles = new Intervalle[taillePop];

        intervalles[0] = new Intervalle((float) 0, probaInd[0].proba, probaInd[0].indiceDansPop);

        for (int i = 1; i < probaInd.length; i++) {

            intervalles[i] = new Intervalle(intervalles[i - 1].borneSup, intervalles[i - 1].borneSup + probaInd[i].proba, probaInd[i].indiceDansPop);
        }

        System.out.println("\nintervalles");
        afficherI(intervalles);
        // on selectionne la moitie de la population 
        // on procède à la selction par roulette 
        ArrayList<int[]> populationSelectionnee = new ArrayList<>();
        for (int i = 0; i < taillePop / 2; i++) {
            float r = (float) (Math.random() * (1 - Float.MIN_VALUE)) + Float.MIN_VALUE; // entre 0 et 1 ( non inclus)
            System.out.println("\nr généré:" + r);
            int j = 0;
            boolean individusSelectionne = false;
            while (j < intervalles.length && !individusSelectionne) {
                if (intervalles[j].borneInf <= r && r < intervalles[j].borneSup) {
                    populationSelectionnee.add(population.get(intervalles[j].indiceIndividus));
                    individusSelectionne = true;
                    System.out.println("\nIndividu sélectionné : ");
                    afficher(population.get(intervalles[j].indiceIndividus));
                }
                j++;
            }

        }
        return populationSelectionnee;
    }

    public static int fitness(int[] sol, int[][] dis, int numCities) {

        int somd = 0;

        for (int i = 0; i < numCities - 1; i++) {
            somd = somd + dis[sol[i] - 1][sol[i + 1] - 1];
        }
        return somd;
    }

    public static ArrayList<int[]> crossover( ArrayList<int[]> population, int taillePop, int numCities) {

        ArrayList<int[]> populationCrossOver = new ArrayList<>();
        Random random = new Random();

       

        for (int j = 0; j < population.size() - 1; j += 2) {
            int[] parent1 = population.get(j);
            int[] parent2 = population.get(j + 1);
            int point = random.nextInt(numCities);
            int[] enfant1 = new int[parent1.length];
            int[] enfant2 = new int[parent1.length];
            for (int i = 0; i <= point; i++) {
                enfant1[i] = parent1[i];
                enfant2[i] = parent2[i];
            }

            for (int i = point + 1; i < parent1.length; i++) {
                enfant1[i] = parent2[i];
                enfant2[i] = parent1[i];
            }

            populationCrossOver.add(enfant1);
            populationCrossOver.add(enfant2);
        }
        return populationCrossOver;
    }

    // operateur utilisé:permutaion  entre deux villes
    public static ArrayList<int[]> mutation(ArrayList<int[]> population, int taillePop, int numCities) {

        ArrayList<int[]> populationMutated = new ArrayList<>();
        int temp;
        Random random = new Random();

       

        for (int j = 0; j < taillePop; j++) {
            int indVille1 = random.nextInt(numCities);
            int indVille2 = random.nextInt(numCities);
            int[] solMutated = Arrays.copyOf(population.get(j), population.get(j).length);
            temp = solMutated[indVille1];
            solMutated[indVille1] = solMutated[indVille2];
            solMutated[indVille2] = temp;
            System.out.println("\nindividu muté");
            afficher(solMutated);
            populationMutated.add(solMutated);
        }

        return populationMutated;
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

    public static ArrayList<int[]> generateInitPopulation(int numCities, int taillePop, int[][] dis) {

        ArrayList<int[]> populationInit = new ArrayList<>();

        for (int i = 0; i < taillePop; i++) {

            populationInit.add(generateInitSolution(numCities, dis));
        }

        return populationInit;
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
            System.out.print(T[i] + " ");
        }

    }

    public static void afficherP(Individus[] T) {

        for (int i = 0; i < T.length; i++) {
            System.out.print(T[i].proba + "," + T[i].indiceDansPop + "|");
        }

    }

    public static void afficherI(Intervalle[] T) {

        for (int i = 0; i < T.length; i++) {
            System.out.println("[" + T[i].borneInf + "," + T[i].borneSup + "]" + " indceIndividus:" + T[i].indiceIndividus);
        }

    }

    public void afficherPop(ArrayList<int[]> population) {

        for (int i = 0; i < population.size(); i++) {
            int[] individu = population.get(i);
            System.out.print("individu num" + i + ":");
            afficher(individu);
        }

    }

    public static boolean valide(int[] T, int numCities) {
        boolean[] villesVisitees = new boolean[numCities + 1];

        // Vérifier que la première et la dernière ville sont les mêmes
        if (T[0] != T[numCities]) {
            return false;
        }

        // Vérifier l'unicité des villes visitées
        for (int i = 0; i < numCities; i++) {
            int ville = T[i];
            if (villesVisitees[ville]) {
                // La ville a déjà été visitée
                return false;
            }
            villesVisitees[ville] = true;
        }

        return true;
    }

}
