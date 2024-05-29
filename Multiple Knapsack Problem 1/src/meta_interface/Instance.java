/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_interface;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Dell
 */
public class Instance {
    
    public static String generer (int nbObjets, int nbSacs,List<Objet> objets,List<SacADos> sacs,int W ){
          String output="objets:\n";
            Random random = new Random();
           for (int i = 0; i < nbObjets; i++) {
            int poids = random.nextInt(10) + 1; // Poids aléatoire entre 1 et 10
            int valeur = random.nextInt(20) + 1; // Valeur aléatoire entre 1 et 20
             W += valeur;
            objets.add(new Objet(poids, valeur,i));
            output = output.concat(" objet "+i+": poid "+poids+" valeur "+valeur+"\n");
            
            
        }
        output = output.concat(" sacs\n");
        // Génération aléatoire des sacs à dos
        for (int i = 0; i < nbSacs; i++) {
            int capacite = random.nextInt(11) + 10; // Capacité aléatoire légèrement supérieure à la somme des poids
          
            sacs.add(new SacADos(capacite,i));
             output = output.concat(" sac "+i+": capacité "+capacite+"\n");
         
            
        }
        System.out.println(output);
        return output;
    }
    
}
