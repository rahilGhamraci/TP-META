/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_exo5;

/**
 *
 * @author Dell
 */
public class Meta_exo5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        int numCities = 10;
        int taillePop = 100;
        int nbMaxIT=100;
        int c1=2;
        int c2=1;
        int w=1;
        PSO algo = new PSO();
       

        int[][] distances = algo.generateDistanceMatrix(numCities);
        algo.printDistanceMatrix(distances);
        
        int [] test={1,2,3,4,5,6,7,8,9,10};
        int [] testCoded ;
        int [] testDecoded;
        
        System.out.println("codage");
        testCoded=algo.codage(test, numCities);
        algo.afficher(testCoded);
        
        System.out.println("Decodage");
        testDecoded=algo.decodage(testCoded, numCities);
        algo.afficher(testDecoded);
        
        
         long startTime = System.nanoTime();

        int[] solution = algo.recherche(taillePop, nbMaxIT, c1, c2, w,distances, numCities);

        long endTime = System.nanoTime();
        long duration1 = (endTime - startTime);  // Temps d'exécution en nanosecondes
        
        System.out.println("\n-------------------Solution trouvée!!---------------");
        algo.afficher(solution);
        System.out.println("\n-------------------temps d'execution!!---------------");
        System.out.println("Temps d'exécution : " + duration1 + " nanosecondes");
    }
    
}


/*

nb=10
Temps d'ex�cution : 692224700 nanosecondes 0,012 min

nb=100
Temps d'ex�cution : 17248736800 nanosecondes  0,28 min

nb=1000
Temps d'ex�cution : 2095272957400 nanosecondes 34,92121595666666 min

nb=1500
Temps d'ex�cution : 4856231254200 nanosecondes  80,93718757000001 min => 1h20min 

 */