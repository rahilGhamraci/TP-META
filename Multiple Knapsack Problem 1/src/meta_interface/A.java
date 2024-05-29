/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_interface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Dell
 */
public class A {

    public Etat a(List<Objet> objets, List<SacADos> sacs, int W) {

        List<Etat> ouvert = new ArrayList<>();
        List<List<SacADos>> ferme = new ArrayList();

        // creer l'etat initial
        Etat e = new Etat(sacs);
        e.setF(W); // equivalent a faire e.setF(e.getG() + h(e, objets)); car e.getG() =0 vu
        // qu'acun objet n'est lacé pour le moment donc la somme des poids  est égale à 0 et h(e, objets) qui est
        // cencée retourner la valeurs  des objets qui ne sont pas encore placés dans un
        // etat va retourner la valeur  de tous les objets car nous avons rien mis dans les sacs
        // pour l'instant

        ouvert.add(e);

        while (!ouvert.isEmpty()) {
            Etat etat = ouvert.get(0);
            ouvert.remove(0);
            ferme.add(etat.sacs);
            // Vérifier si l'état actuel est une solution
            if (estSolution(etat.getSacs(), objets)) {
                //afficherSolution(etat);
                return etat;

            }
            // Générer les états suivants et les ajouter à la file
            List<Etat> successuers = successuers(etat, objets, W);
            for (Etat successuer : successuers) {
                if (!containsState(ferme, successuer.sacs)) {
                    ouvert.add(successuer);
                    //afficherDEtat(successuer);
                    // trier la liste ouvert selon la valeur de f
                    Collections.sort(ouvert, new Comparator<Etat>() {
                        @Override
                        public int compare(Etat e1, Etat e2) {
                            return Integer.compare(e1.f, e2.f);
                        }
                    });

                }

            }
        }

        return null;
    }

    // Vérifie si l'état actuel est une solution
    private boolean estSolution(List<SacADos> etat, List<Objet> objets) {
        boolean tousPlaces = true; // Variable pour suivre si tous les objets sont placés
        List<Objet> objetsNonPlaces = new ArrayList<Objet>();
        // Parcourir tous les objets
        for (Objet objet : objets) {
            boolean estPlace = false; // Variable pour suivre si l'objet est placé dans un des sacs
            // Vérifier si l'objet est placé dans un des sacs
            for (SacADos sac : etat) {
                for (Objet o : sac.objets) {
                    if (o.id == objet.id) {
                        estPlace = true;
                        break;
                    }
                }
            }
            if (!estPlace) {
                tousPlaces = false; // S'il y a un objet non placé, on marque tousPlaces à false
                objetsNonPlaces.add(new Objet(objet.poids, objet.valeur, objet.id));
            }
        }

        // Si tous les objets sont placés ou certains ne peuvent pas être placés car trop lourds
        if (tousPlaces) {
            return true; // Déclarer l'état comme une solution
        } else {
            // parcourir les objets non placés
            for (Objet objet : objetsNonPlaces) {
                for (SacADos sac : etat) {
                    if (sac.peutAjouter(objet)) {
                        return false;  // si l'objet peut etre placé alors ce n'est pas une solution
                    }
                }
            }
            //System.out.println("pas tous les objets sont placés mais c une solution");
            //afficherDEtat(etat);
            return true; // Si tous les objets non placés ne peuvent pas être placés dans les sacs, déclarer l'état comme une solution
        }
    }

    // Affiche la solution
    public static String afficherSolution(Etat etat) {
        String output = "";
        output = output.concat("Solution trouvée avec cout egale à " + etat.getG() + " et valeur de f egale à " + etat.getF()+"\n");
        for (SacADos sac : etat.getSacs()) {
            output = output.concat("Sac à dos " + sac.id + ": Capacité = " + sac.capaciteMax+"\n");
            for (Objet objet : sac.objets) {
                output = output.concat("   Objet :" + objet.id + " Poids = " + objet.poids + ", Valeur = " + objet.valeur+"\n");
            }
        }
        return output;
    }

    // for test purposes
    // Afficher etat produts et ajouté aux successurs
    private void afficherDEtat(Etat etat) {
        System.out.println("Etat crée et ajouté à ouvert avec f=" + etat.getF());
        for (SacADos sac : etat.getSacs()) {
            System.out.println("Sac à dos : Capacité = " + sac.capaciteMax);
            for (Objet objet : sac.objets) {
                System.out.println("   Objet : Poids = " + objet.poids + ", Valeur = " + objet.valeur);
            }
        }

    }

    private List<Etat> successuers(Etat etat, List<Objet> objets, int W) {
        List<Etat> successuers = new ArrayList<>();
        boolean ajoute = false;

        for (Objet objet : objets) {
            // ne placer l'objet de l'un des sac des etats successeurs que si l'etat acutuel
            // ne contient pas deja cet objet
            if (!contenu_etat(etat.getSacs(), objet)) {
                // essayer de placer l'objet dans les differents sac , si l'objet peut etre
                // placer dans un sac donné alors un nouvel etat sera creer
                for (SacADos sac : etat.getSacs()) {
                    if (sac.peutAjouter(objet)) { // verification de la capacite du sac
                        ajoute = true;
                        // creation d'un nouvel etat et recopier le contenu de l'etat actuel dans ce
                        // dernier
                        List<SacADos> nouvelEtatSacs = new ArrayList<>();

                        for (SacADos s : etat.getSacs()) {
                            // Créer une copie de chaque sac et ajouter cette copie à nouvelEtat
                            SacADos nouveauSac = new SacADos(s.capaciteMax, s.id);

                            for (Objet o : s.objets) {
                                Objet nouveauObjet = new Objet(o.poids, o.valeur, o.id);
                                nouveauSac.objets.add(nouveauObjet);
                            }
                            nouvelEtatSacs.add(nouveauSac);
                        }

                        Etat nouvelEtat = new Etat(nouvelEtatSacs);

                        // trouver l'indice du sac ou on va placer notre objet
                        int index = indice(nouvelEtat.sacs, sac);

                        // mettre a jour le sac en lui rajoutant l'objet
                        nouvelEtat.getSacs().get(index).ajouterObjet(objet);
                        // calculer g 
                        nouvelEtat.setG(g(nouvelEtat));
                        // calculer la valeur f de l'etat
                        nouvelEtat.setF(nouvelEtat.getG() + h(nouvelEtat, objets));

                        // afficher l'etat
                        //afficherDEtat(nouvelEtat);
                        // ajouter l'etat au successeurs
                        successuers.add(nouvelEtat);
                    }
                    if (ajoute) {
                        break; // quitter la boucle afin d'eviter de placer l'objet dans deux sacs d'un meme
                        // etat
                    }
                }

            }

        }
        return successuers;

    }

    // pour verifier si un objet donné est deja placé dans l'un des sacs d'un etat
    private boolean contenu_etat(List<SacADos> etat, Objet objet) {
        boolean trouve = false;
        for (SacADos sac : etat) {// on parcours les sacs de l'etat 
            for (Objet o : sac.objets) { // on parcours les objets de chaque sac
                if (o.id == objet.id) {// si on trouve un objet avec le meme id alors on retouren vrai
                    trouve = true;
                    break;
                }
            }
        }

        return trouve;
    }

    private int indice(List<SacADos> nouvelEtat, SacADos sac) {
        int index = -1; // Initialiser l'index à une valeur par défaut

        for (int i = 0; i < nouvelEtat.size(); i++) {

            if (nouvelEtat.get(i).id == sac.id) { // Remplacez idRecherche par l'ID que vous recherchez
                index = i; // Affecter l'index si l'ID est trouvé
                break; // Sortir de la boucle une fois que l'ID est trouvé
            }
        }
        return index;
    }

    public boolean containsState(List<List<SacADos>> etatsVisistes, List<SacADos> etat) {

        for (List<SacADos> e : etatsVisistes) {

            if (identiques(e, etat)) {
                return true;
            }
        }
        return false;
    }

    public boolean identiques(List<SacADos> l1, List<SacADos> l2) {

        // on compare sac par sac
        for (int i = 0; i < l1.size(); i++) { // forcement les deux listes vont contenir le meme ordre de sacs car
            // l'etat initial contient la liste des sacs vides qui a ete crée dans le
            // main et on place des objets dans le sac sans changer leur ordre
            // trier les objets selon les id pour pouvoir les comparer
            Collections.sort(l1.get(i).objets, new Comparator<Objet>() {
                @Override
                public int compare(Objet o1, Objet o2) {
                    return Integer.compare(o1.id, o2.id);
                }
            });
            Collections.sort(l2.get(i).objets, new Comparator<Objet>() {
                @Override
                public int compare(Objet o1, Objet o2) {
                    return Integer.compare(o1.id, o2.id);
                }
            });
            if (l1.get(i).objets.size() != l2.get(i).objets.size()) {
                return false;
            } else {
                for (int j = 0; j < l1.get(i).objets.size(); j++) {
                    if (l1.get(i).objets.get(j).id != l2.get(i).objets.get(j).id) {
                        return false;

                    }
                }

            }

        }

        return true;
    }

    // somme des poids des objets  placés
    private int g(Etat e) {
        int somPoids = 0;
        for (SacADos sac : e.sacs) {
            for (Objet objet : sac.objets) {
                somPoids += objet.poids;
            }
        }
        return somPoids;
    }

    // somme des valeurs  des objets non placés
    private int h(Etat e, List<Objet> objets) {
        int valeurs_obj_non_places = 0;
        for (Objet objet : objets) {
            if (!contenu_etat(e.getSacs(), objet)) {

                valeurs_obj_non_places += objet.valeur;
            }
        }

        return valeurs_obj_non_places;
    }
}
