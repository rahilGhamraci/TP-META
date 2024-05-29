/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_interface_2;
import java.util.Random;
/**
 *
 * @author Dell
 */
public class Meta_interface_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AlgoGen algo = new AlgoGen();
        BSO bso = new BSO();

        int nbObjets = 10; // Nombre d'objets quelconque
        int nbSacs =3; // Nombre de sacs quelconque


        // Génération aléatoire des poids et des capacités
        Random random = new Random();
        Objet[] objets = new Objet[nbObjets];
        Sac[] sacs = new Sac[nbSacs];
       
       

        // Génération aléatoire des objets
        for (int i = 0; i < nbObjets; i++) {
            int poids = random.nextInt(10) + 1; // Poids aléatoire entre 1 et 10
            int valeur = random.nextInt(20) + 1; // Valeur aléatoire entre 1 et 20
            objets[i]=new Objet(poids, valeur,i);
            
        }

        // Génération aléatoire des sacs à dos
        for (int i = 0; i < nbSacs; i++) {
            int capacite = random.nextInt(10) + 10; // Capacité aléatoire légèrement supérieure à la somme des poids
            sacs[i]=new Sac(capacite,i);
        
            
        }
         // Affichage des objets et des sacs à dos générés
         System.out.println("Objets générés : ");
         for (Objet objet : objets) {
             System.out.println("Poids : " + objet.poids + ", Valeur : " + objet.valeur);
         }
 
         System.out.println("\nSacs à dos générés : ");
         for (Sac sac : sacs) {
             System.out.println("Capacité : " + sac.capaciteMax);
            
         }
        
        int nbGen = 25;
        int taillePop = 100;

        long startTime = System.nanoTime();
        int[][] solution = algo.recherche((float) 0.5, (float) 0.1, nbGen, taillePop, objets,  sacs);
        long endTime = System.nanoTime();
        long duration1 = (endTime - startTime);  // Temps d'exécution en nanosecondes
       

        int k= 10;
        int maxItr=35;
        int flip= 5;
        int nbNeighbors=5;
        int nbChances=25;
        int maxChances=25;

       
        startTime = System.nanoTime();
        int[][] solution2 = bso.recherche(k, maxItr,flip, nbNeighbors,nbChances,maxChances,objets,  sacs);
        endTime = System.nanoTime();
        long duration2 = (endTime - startTime);  // Temps d'exécution en nanosecondes

        
        System.out.println("\n-------------------Solution trouvée  Algo Gen!!---------------");
        algo.afficher(solution, sacs, objets);
        System.out.println("interprétation de la solution" );
        String output=algo.interpretationSol(solution, sacs, objets);
        System.out.println(output);
        System.out.println("\n-------------------temps d'execution  !!---------------");
        System.out.println("Temps d'exécution : " + duration1 + " nanosecondes");
         

        System.out.println("\n-------------------Solution trouvée  BSO !!---------------");
        bso.afficher(solution2, sacs, objets);
        System.out.println("interprétation de la solution" );
        String output2=bso.interpretationSol(solution2, sacs, objets);
        System.out.println(output2);
        System.out.println("\n-------------------temps d'execution  !!---------------");
        System.out.println("Temps d'exécution : " + duration2 + " nanosecondes");
       
    }
    
}


/*
 *  algo gen
 * (10,3) 14613062700  * 1.7×10-11 = 0.24 min
 * (100,25) 663250752000 * 1.7×10-11 = 6.6 min 
 * (500,125) 10619101977900 * 1.7×10-11 = 180.5 min ----->  3h
 * (1000,225) 21584131985300 -------> 5h
 * 
 */