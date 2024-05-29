/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_exo2;

import java.util.Arrays;

/**
 *
 * @author Dell
 */
public class Meta_exo2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        int numCities = 5;
        DFS algo = new DFS();

        int[] bestSolution = new int[numCities];
        algo.generer(bestSolution, numCities);
        Noeud BestSol = new Noeud(bestSolution, null);

        int[] initialSolution = {};
        Noeud initialNoeaud = new Noeud(initialSolution, null);

        int[][] distances = algo.generateDistanceMatrix(numCities);
        algo.printDistanceMatrix(distances);

        long startTime = System.nanoTime();

        Noeud etatBut = algo.recherche(initialNoeaud, distances, 300, BestSol, numCities);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  // Temps d'exécution en nanosecondes
        System.out.println("Temps d'exécution : " + duration + " nanosecondes");

        System.out.println("chemin de la solution obtenue:");

        Noeud n = etatBut;
        while (n.parent != null) {
            System.out.print("{");
            algo.afficher(n.sol);
            System.out.print("}");
            n = n.parent;
            System.out.print("->");
            
        }

    }

}


/*
nb=5
Temps d'ex�cution : 624549200 nanosecondes
la solution obtenue:
5 3 1 4 2 

nb=7
Temps d'ex�cution : 29097502800 nanosecondes
la solution obtenue:
7 3 5 2 4 6 1 


*/
