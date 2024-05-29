/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_interface;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author Dell
 */
public class Meta_interface {

    /**
     * @param args the command line arguments
     */
  public static void main(String[] args) {
        // generation automatique
        // Déclaration du nombre d'objets et de sacs à dos
        int nbObjets = 10; // Nombre d'objets quelconque
        int nbSacs =4; // Nombre de sacs quelconque

        // Génération aléatoire des poids et des capacités
        Random random = new Random();
        List<Objet> objets = new ArrayList<>();
        List<SacADos> sacs = new ArrayList<>();
        List<SacADos> sacs1 = new ArrayList<>();
        int sommePoids = 0;
        int sommeCapacites = 0;
        int W=0;

        // Génération aléatoire des objets
        for (int i = 0; i < nbObjets; i++) {
            int poids = random.nextInt(10) + 1; // Poids aléatoire entre 1 et 10
            int valeur = random.nextInt(20) + 1; // Valeur aléatoire entre 1 et 20
            sommePoids += poids;
            objets.add(new Objet(poids, valeur,i));
            
        }

        // Génération aléatoire des sacs à dos
        for (int i = 0; i < nbSacs; i++) {
            int capacite = random.nextInt(11) + 10; // Capacité aléatoire légèrement supérieure à la somme des poids
            sommeCapacites += capacite;
            sacs.add(new SacADos(capacite,i));
            sacs1.add(new SacADos(capacite,i));
            
        }
        /* // Assurer que la somme des capacités des sacs est supérieure ou égale à la
        // somme des poids des objets
        while (sommeCapacites < sommePoids) {
            int index = random.nextInt(sacs.size()); // Sélectionner un sac aléatoire
            SacADos sac = sacs.get(index);
            sac.capaciteMax += random.nextInt(10) + 1; // Augmenter sa capacité aléatoirement
            sommeCapacites += random.nextInt(10) + 1; // l'ajout d'une capacité supplémentaire modifie la somme des
                                                      // capacités
        } */

        // Affichage des objets et des sacs à dos générés
        System.out.println("Objets générés : ");
        for (Objet objet : objets) {
            System.out.println("Poids : " + objet.poids + ", Valeur : " + objet.valeur);
        }

        System.out.println("\nSacs à dos générés : ");
        for (SacADos sac : sacs) {
            System.out.println("Capacité : " + sac.capaciteMax);
            W += sac.capaciteMax;
        }
        int x = sommeCapacites - sommePoids;
        System.out.println("Capacité - poids: " + x);
        
        long startTime ,endTime,duration1,duration2,duration3;
        DFS dfs = new DFS();
        BFS bfs = new BFS();
        A a = new A();
       
        
        
        
        System.out.println("dfs");
        startTime = System.nanoTime();
        List<SacADos> solutionDfs=dfs.dfs(objets, sacs);
        String output = dfs.afficherSolution(solutionDfs);
        System.out.println(output);
        endTime = System.nanoTime();
        duration1 = (endTime - startTime);
        
        System.out.println("bfs");
        startTime = System.nanoTime();
        List<SacADos> solutionBfs=bfs.bfs(objets, sacs);
        output = bfs.afficherSolution(solutionBfs);
        System.out.println(output);
        endTime = System.nanoTime();
        duration2 = (endTime - startTime);

        System.out.println("A*");
        startTime = System.nanoTime();
        Etat e =a.a(objets, sacs,W);
        output = a.afficherSolution(e);
        System.out.println(output);
        endTime = System.nanoTime();
        duration3 = (endTime - startTime);


         
        
        
     
       
       
      
        
       
        System.out.println("Temps d'exécution DFS: " + duration1 + " nanosecondes");
        System.out.println("Temps d'exécution BFS: " + duration2 + " nanosecondes");
        System.out.println("Temps d'exécution A*: " +  duration3 + " nanosecondes");
       

           
        
      
        
        
  
        
        
        
  
       
    }
    
}
