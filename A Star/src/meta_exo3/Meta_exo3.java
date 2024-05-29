/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_exo3;

import java.util.Arrays;

/**
 *
 * @author Dell
 */
public class Meta_exo3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int numCities = 7;
        A algo = new A();

        int[] bestSolution = new int[numCities];
        algo.generer(bestSolution, numCities);
        Noeud BestSol = new Noeud(bestSolution, null,300); // dans le noeud final h=0 donc f=au cout une estimation du meuilleur cout donne 300 à peu prés 

        int[] initialSolution = {};
        Noeud initialNoeaud = new Noeud(initialSolution, null,numCities);// cout=0 h=10 car aucune ville n'est encore visitée

        int[][] distances = algo.generateDistanceMatrix(numCities);
        algo.printDistanceMatrix(distances);

        long startTime = System.nanoTime();

        Noeud etatBut = algo.recherche(initialNoeaud, distances, 300, BestSol, numCities);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  // Temps d'exécution en nanosecondes
        System.out.println("Temps d'exécution : " + duration + " nanosecondes");
        
        System.out.println("la solution obtenue avec valeur de f="+etatBut.f);
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
Temps d'ex�cution : 67837500 nanosecondes

nb=7
Temps d'ex�cution : 25580113900 nanosecondes

nb=100
Temps d'ex�cution : 79745972860 nanosecondes

nb=1000
Temps d'ex�cution : 797459728600 nanosecondes

nb=1500
Temps d'ex�cution : 1196189593000 nanosecondes

*/




// consignes tp meta exo4

//paramètres
// nombre de generations 
// taille de la population
//proba crossover ( generalement c 0.5 ) et proba mutaion (  petite 0.1)
// données du problème (taille du problème : nombre de ville , matrice des distances)

// vesion d'algo à implementer 
// V1 appliquer le crossover/ mutation sur toute la population selctionnée
// v2 appliquer  crossover/ mutation sur un pourcentage de la population ; le pourcentage est determiné par la proba du crossover/mutation , exemple si pcrossover=0.5 alors on applique le crossover sur 50 % de la population ( idem pour la muation)
// V3 appliquer crossover (resp mutation)  sur les parents selectionnés si le nombre genéré aleatoirement r est < proba crossover ( resp proba mutation) sinon selectionner deux autres parents


// génération des solutions de la population initiale 
// pour la  permière ville c random 
// pour les autres: prendre les villes adjacentes de la dernière ville choisie à partir de la matrice des distances, et choisir une ville aléatoirement parmi ces villes.