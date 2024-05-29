/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_exo4;

/**
 *
 * @author Dell
 */
public class Meta_exo4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int numCities = 10;
        int nbGen = 50;
        int taillePop = 100;
        AlgoGenV1 algo1 = new AlgoGenV1();
        AlgoGenV2 algo2 = new AlgoGenV2();
        AlgoGenV3 algo3 = new AlgoGenV3();

        int[][] distances = algo1.generateDistanceMatrix(numCities);
        algo1.printDistanceMatrix(distances);
     
        long startTime = System.nanoTime();

        int[] solution1 = algo1.recherche((float) 0.5, (float) 0.1, nbGen, taillePop, distances, numCities);

        long endTime = System.nanoTime();
        long duration1 = (endTime - startTime);  // Temps d'exécution en nanosecondes

        startTime = System.nanoTime();

        int[] solution2 = algo2.recherche((float) 0.5, (float) 0.1, nbGen, taillePop, distances, numCities);

        endTime = System.nanoTime();
        long duration2 = (endTime - startTime);  // Temps d'exécution en nanosecondes

        startTime = System.nanoTime();

        int[] solution3 = algo3.recherche((float) 0.5, (float) 0.1, nbGen, taillePop, distances, numCities);

        endTime = System.nanoTime();
        long duration3 = (endTime - startTime);  // Temps d'exécution en nanosecondes

        System.out.println("\n-------------------Solution trouvée  V1!!---------------");
        algo1.afficher(solution1);
        System.out.println("\n-------------------temps d'execution  V1 !!---------------");
        System.out.println("Temps d'exécution : " + duration1 + " nanosecondes");

        System.out.println("\n-------------------Solution trouvée V2 !!---------------");
        algo1.afficher(solution2);
        System.out.println("\n-------------------temps d'execution  V2 !!---------------");
        System.out.println("Temps d'exécution : " + duration2 + " nanosecondes");

        System.out.println("\n-------------------Solution trouvée V3!!---------------");
        algo1.afficher(solution3);
        System.out.println("\n-------------------temps d'execution  V3!!---------------");
        System.out.println("Temps d'exécution : " + duration3 + " nanosecondes");
    }

}

/*
nb=5
Temps d'ex�cution : 9922475800  0.16 min

nb=10
Temps d'ex�cution : 10850452500 nanosecondes 0.18 min

nb=100
Temps d'ex�cution : 40008936900 nanosecondes  0.68 min

nb=1000
Temps d'ex�cution : 310981357300 nanosecondes 5.28 min

nb=1500
Temps d'ex�cution : 481515757200 nanosecondes  8.19 min 

 */
