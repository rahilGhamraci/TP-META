/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_interface_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Dell
 */
public class BSO {

    public int[][] recherche(int k, int maxItr, int flip, int nbNeighbors, int nbChances, int maxChances,
            Objet[] objets, Sac[] sacs) {
        //objets = new Objet[nbObjets];
        //sacs = new Sac[nbSacs];

        int[][] sref = BeeInit(objets, sacs);
        ArrayList<int[][]> tabouList = new ArrayList<>();
        ArrayList<int[][]> searchPoints;
        ArrayList<int[][]> dance = new ArrayList<>();
        int[][] bestSolution;

        while (maxItr > 0) {
            int[][] copieSref = new int[sacs.length][objets.length];
            for (int i = 0; i < sacs.length; i++) {
                for (int j = 0; j < objets.length; j++) {
                    copieSref[i][j] = sref[i][j];
                }
            }
            tabouList.add(sref.clone());
            //tabouList.add(copieSref);
            //System.out.println("sref");
            //afficher(sref, sacs, objets);
            //System.out.println("");
            searchPoints = determineSearchArea(k, flip, sref, objets, sacs);
            //System.out.println("searchPoints search done");
            for (int i = 0; i < k; i++) {
                bestSolution = localSearch(searchPoints.get(i), objets, sacs, nbNeighbors);
                dance.add(bestSolution);

            }

            // determiner Sbest
            bestSolution = dance.get(0);

            for (int i = 1; i < k; i++) {
                if (fitness(dance.get(i), sacs, objets) > fitness(bestSolution, sacs, objets)) {
                    bestSolution = dance.get(i);
                }

            }

            sref = srefSelection(bestSolution, sref, tabouList, dance, nbChances, maxChances, objets,
                    sacs);

            maxItr--;
        }
        //afficherTabou(tabouList, sacs, objets);
        return sref;
    }

    public static int[][] BeeInit(Objet[] objets, Sac[] sacs) {
        // System.out.println("etrée de la methode generateInitSolution");
        int[][] sol = new int[sacs.length][objets.length];
        Random random = new Random();
        int indiceSac, indice;

        for (int i = 0; i < objets.length; i++) { // essayer de placer chaque objet
            int[] indicesSacPossibles = new int[sacs.length];
            int p = 0;
            for (int j = 0; j < sacs.length; j++) { // chercher les sacs possibles ou mettre l'objet
                if (calculer_poids(sol, sacs[j], objets) + objets[i].poids <= sacs[j].capaciteMax) {
                    indicesSacPossibles[p] = j; // il est possible de placer l'objet dans ce sac
                    p++;
                }

            }
            if (p > 0) {
                indice = random.nextInt(p);
                indiceSac = indicesSacPossibles[indice];
                sol[indiceSac][i] = 1;
            }
            // sinon l'objet n'est pas placé

        }

        return sol;
    }

    public static int calculer_poids(int[][] sol, Sac sac, Objet[] objets) {
        int poids = 0;
        for (int i = 0; i < objets.length; i++) {
            if (sol[sac.id][i] == 1) {
                poids += objets[i].poids;
            }
        }
        return poids;
    }
    // should've picked another fitness
    public static double fitness(int[][] sol, Sac[] sacs, Objet[] objets) {

        int somV = 0;
        int somP = 0;

        for (int i = 0; i < sacs.length; i++) {
            for (int j = 0; j < objets.length; j++) {
                if (sol[i][j] == 1) {
                    somV += objets[j].valeur;
                    somP += objets[j].poids;

                }
            }
        }

        return 1 / somP + somV; // à maximiser
    }

    public static ArrayList<int[][]> determineSearchArea(int k, int flip, int[][] sref, Objet[] objets, Sac[] sacs) {
        ArrayList<int[][]> searchPoints = new ArrayList<>();
        int nbFilpToDo = objets.length / flip;

        for (int i = 0; i < k; i++) {
            int[][] copie = new int[sacs.length][objets.length];
            copy(sref, copie);

            int nbFlipDone = 0;
            for (int j = i; nbFlipDone < nbFilpToDo; j = (j + flip) % objets.length) {

                rotate_column(copie, j, objets, sacs);
                 //System.out.println("colonne filipped "+j);
                nbFlipDone++;
            }
                 //System.out.println("sref");
                 //afficher(sref,sacs, objets);
                 //System.out.println("search point");
                 //afficher(copie,sacs, objets);
                 //System.out.println();
                
            searchPoints.add(copie);
        }

        /*
         * System.out.println("search points trouvés");
         * for(int[][] searchPoint:searchPoints){
         * afficher(searchPoint,sacs, objets);
         * System.out.println();
         * }
         */
        return searchPoints;
    }

    public static void copy(int[][] source, int[][] destination) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                destination[i][j] = source[i][j];
            }
        }
    }

    public static void rotate_column(int[][] s, int j, Objet[] objets, Sac[] sacs) {
        boolean allZeros = true;
        boolean flipped=false;
        for (int i = 0; i < sacs.length; i++) {
            if (s[i][j] != 0) {
                allZeros = false;
                break;
            }
        }

        if (allZeros) {
            //System.out.println("allZeros");
            // Si la colonne est constituée uniquement de zéros, choisissez aléatoirement un
            // bit et inversez-le
            do{
            Random random = new Random();
            int randomRow = random.nextInt(s.length);
            s[randomRow][j] = (s[randomRow][j] == 1) ? 0 : 1;
           // flipped=true;
            if(!valide(s, sacs, objets)){
               s[randomRow][j] = (s[randomRow][j] == 1) ? 0 : 1;
               //flipped=false;
            }
            
            }while(!valide(s, sacs, objets));
        } else {

            do {

                // Sinon, effectuez la rotation de la colonne
                int lastValue = s[s.length - 1][j]; // Stocke la valeur du dernier élément de la colonne
                // Déplace chaque élément de la colonne vers le bas d'une position
                for (int i = s.length - 1; i > 0; i--) {
                    s[i][j] = s[i - 1][j];
                }
                // Place le dernier élément de la colonne à la première position
                s[0][j] = lastValue;

            }while (!valide(s, sacs, objets));

        }
    }

    public static int[][] localSearch(int[][] s, Objet[] objets, Sac[] sacs, int nbNeighbors) {
        //System.out.println("localsearch");
        Random random = new Random();
        ArrayList<int[][]> neighbors = new ArrayList<>();
        int[][] bestSolution = s;

        for (int i = 0; i < nbNeighbors; i++) {
            int[][] neighbor = new int[sacs.length][objets.length];
            copy(s, neighbor);
            // Choisir aléatoirement un bit dans neighbor à inverser
            int indSac = random.nextInt(sacs.length);
            int indObjet = random.nextInt(objets.length);
            // Inverser le bit aléatoirement
            neighbor[indSac][indObjet] = (neighbor[indSac][indObjet] == 1) ? 0 : 1;

            /*
             * System.out.println("s");
             * afficher(s, sacs, objets);
             * System.out.println("neighbor");
             * afficher(neighbor, sacs, objets);
             * System.out.println();
             */
            if (valide(neighbor, sacs, objets)) {
                neighbors.add(neighbor);
            }

        }

        /*
         * System.out.println("neighbors trouvés");
         * for(int[][] n:neighbors){
         * afficher(n,sacs, objets);
         * System.out.println();
         * }
         */
        for (int i = 0; i < neighbors.size(); i++) {
            if (fitness(neighbors.get(i), sacs, objets) > fitness(bestSolution, sacs, objets)) {
                bestSolution = neighbors.get(i);
            }

        }

        return bestSolution;
    }

    public static int[][] srefSelection(int[][] sreft1, int[][] sreft, ArrayList<int[][]> tabouList, ArrayList<int[][]> dance, int nbChances,
            int maxChances, Objet[] objets, Sac[] sacs) {
        int[][] sref = new int[sacs.length][objets.length];
        double deltaf = fitness(sreft1, sacs, objets) - fitness(sreft, sacs, objets); // deltaf=f(sref (t))- f(sref (t-1))
        if (deltaf > 0) { // fitness(sreft1, sacs, objets) > fitness(sreft, sacs, objets)
            // the best solution in quality
            sref = dance.get(0);
            for (int i = 1; i < dance.size(); i++) {
                if (fitness(dance.get(i), sacs, objets) > fitness(sref, sacs, objets)) {
                    sref = dance.get(i);
                }
            }
            if (nbChances < maxChances) {
                nbChances = maxChances;
            }
        } else { // fitness(sreft1, sacs, objets) < fitness(sreft, sacs, objets)
            nbChances--;

            if (nbChances > 0) {
                // the best solution in quality
                sref = dance.get(0);
                for (int i = 1; i < dance.size(); i++) {
                    if (fitness(dance.get(i), sacs, objets) > fitness(sref, sacs, objets)) {
                        sref = dance.get(i);
                    }

                }
            } else {
                // the best solution in diversity
                sref = dance.get(0);
                for (int i = 1; i < dance.size(); i++) {
                    if (diversity(dance.get(i), tabouList) > diversity(sref, tabouList)) {
                        sref = dance.get(i);
                    }

                }
                nbChances = maxChances;
            }
        }
        return sref;
    }

    public static int diversity(int[][] sref, ArrayList<int[][]> tabouList) {

        int minDiffrence = diffrence(sref, tabouList.get(0));
        int diffrence;

        for (int i = 1; i < tabouList.size(); i++) {
            diffrence = diffrence(sref, tabouList.get(i));
            if (diffrence < minDiffrence) {
                minDiffrence = diffrence;
            }
        }

        return minDiffrence;

    }

    public static int diffrence(int[][] x, int[][] y) {
        int d = 0;
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {

                if (x[i][j] != y[i][j]) {
                    d++;
                }
            }
        }
        return d;
    }

    public static boolean valide(int[][] T, Sac[] sacs, Objet[] objets) {
        int nb = 0;
        boolean valide = true;
        // verification de la capacité
        for (int i = 0; i < sacs.length; i++) {
            if (calculer_poids(T, sacs[i], objets) > sacs[i].capaciteMax) {
                valide = false;
                break;
            }
        }
        // verification qu'un objet n'est pas répétés dans plusieurs sacs
        for (int i = 0; i < objets.length; i++) {
            nb = 0;
            for (int j = 0; j < sacs.length; j++) {
                if (T[j][i] == 1) {
                    nb++;
                }
            }
            if (nb > 1) {
                valide = false;
                break;
            }
        }
        return valide;
    }

    public static String interpretationSol(int[][] sol, Sac[] sacs, Objet[] objets) {
        String output = "";
        for (int i = 0; i < sacs.length; i++) {
            output = output.concat("Sac à dos " + sacs[i].id + ": Capacité = " + sacs[i].capaciteMax + "\n");
            for (int j = 0; j < objets.length; j++) {
                if (sol[i][j] == 1) {
                    output = output.concat("   Objet :" + objets[j].id + " Poids = " + objets[j].poids + ", Valeur = "
                            + objets[j].valeur + "\n");
                }

            }

        }
        return output;

    }

    public static void afficher(int[][] T, Sac[] sacs, Objet[] objets) {

        for (int i = 0; i < sacs.length; i++) {

            for (int j = 0; j < objets.length; j++) {
                System.out.print(T[i][j] + " ");
            }
            System.out.print("\n");
        }

    }

    public static void afficherTabou(ArrayList<int[][]> tabouList, Sac[] sacs, Objet[] objets) {

        for (int i = 0; i < tabouList.size(); i++) {
            System.out.print("\n");
            
            System.out.print("----------------");
        }

    }

}
