/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_interface;

/**
 *
 * @author Dell
 */
import java.util.*;
public class SacADos {
    int capaciteMax;
    List<Objet> objets;
    int id;

    public SacADos(int capaciteMax,int id) {

        this.capaciteMax = capaciteMax;
        this.objets = new ArrayList<>();
        this.id=id;
    }

    // Vérifie si un objet est déjà dans le sac à dos
    public boolean contient(Objet objet) {
        for (Objet o : this.objets) { // on parcours les objets dusac
            if (o.id==objet.id) {// si on trouve un objet avec le meme id alors on retouren vrai
              return true;
            }
        }
        return false;
    }

    // Vérifie si un objet peut être ajouté au sac à dos sans dépasser sa capacité maximale
    public boolean peutAjouter(Objet objet) {
        int poidsTotal = objets.stream().mapToInt(o -> o.poids).sum();
        //System.out.println(poidsTotal + objet.poids <= capaciteMax);
        return poidsTotal + objet.poids <= capaciteMax;
    }

    // Ajoute un objet au sac à dos
    public SacADos ajouterObjet(Objet objet) {
        objets.add(objet);
        return this;
    }
}

