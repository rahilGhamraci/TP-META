/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_interface;

import java.util.*;

/**
 *
 * @author Dell
 */
public class BFS {

    public List<SacADos> bfs(List<Objet> objets, List<SacADos> sacs) {
        Queue<List<SacADos>> queue = new LinkedList<>();
        List<List<SacADos>> ferme = new ArrayList();
        queue.add(sacs); // État initial : aucun objet placé dans les sacs i.e les sac sont vides
        List<SacADos> bestSolution = sacs;
        while (!queue.isEmpty()) {
            List<SacADos> etat = queue.poll();
            ferme.add(etat);
            // Vérifier si l'état actuel est une solution
            if (estSolution(etat, objets)) {
                if (comparer(etat, bestSolution) && nbObjetsPlaces(etat, objets) > nbObjetsPlaces(bestSolution, objets)) {
                    bestSolution = etat;
                }
                //afficherSolution(etatActuel);
                //  return; ne pas retourner la l'etat but car on chereche à developper tout l'espace de recherche afin de determiner la meilleure solution
            }
            // Générer les états suivants et les ajouter à la file
            List<List<SacADos>> succ = successuers(etat, objets);
            for (List<SacADos> s : succ) {
                if (!containsState(ferme, s)) {
                    //System.out.println("etat ajouté à ouvert");
                    //afficherDEtat(s);
                    queue.add(s);
                }

            }
        }
        return bestSolution;
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

    private int nbObjetsPlaces(List<SacADos> etat, List<Objet> objets) {
        int nb = 0;
        for (Objet objet : objets) {

            // Vérifier si l'objet est placé dans un des sacs
            for (SacADos sac : etat) {
                for (Objet o : sac.objets) {
                    if (o.id == objet.id) {
                        nb++;
                        break;
                    }
                }
            }

        }
        return nb;
    }

    // Affiche la solution
    public static String afficherSolution(List<SacADos> etat) {
        String output = "";
        output = output.concat("Solution trouvée :\n");
        for (SacADos sac : etat) {
            output = output.concat("Sac à dos " + sac.id + ": Capacité = " + sac.capaciteMax+"\n");
            for (Objet objet : sac.objets) {
                output = output.concat("   Objet :" + objet.id + " Poids = " + objet.poids + ", Valeur = " + objet.valeur+"\n");
            }
        }
        return output;
    }

    // for test purposes
    // Afficher etat produts et ajouté aux successurs
    private void afficherDEtat(List<SacADos> etat) {
        // System.out.println("Etat crée et ajouté :");
        for (SacADos sac : etat) {
            System.out.println("Sac à dos " + sac.id + " capacite=" + sac.capaciteMax);
            for (Objet objet : sac.objets) {
                System.out.println("   Objet " + objet.id + ": Poids = " + objet.poids + ", Valeur = " + objet.valeur);
            }
        }

    }

    private List<List<SacADos>> successuers(List<SacADos> etat, List<Objet> objets) {
        List<List<SacADos>> enfants = new ArrayList<>();
        boolean ajoute = false;

        for (Objet objet : objets) {
            // ne placer l'objet de l'un des sac des etats successeurs que si l'etat actuel
            // ne contient pas deja cet objet
            if (!contenu_etat(etat, objet)) {
                // essayer de placer l'objet dans les differents sac , si l'objet peut etre
                // placer dans un sac donné alors un nouvel etat sera creer
                for (SacADos sac : etat) {
                    if (sac.peutAjouter(objet)) { // verification de la capacite du sac
                        ajoute = true;
                        // creation d'un nouvel etat et recopier le contenu de l'etat actuel dans ce
                        // dernier , la creation se fait manuellment car autrement on aura qu'une
                        // reference vers l'eat actuel et non pas un nouvel etat qui a les memes sac
                        // contenant les meme objets que l'etat actuel
                        List<SacADos> nouvelEtat = new ArrayList<>();
                        for (SacADos s : etat) {
                            // Créer une copie de chaque sac et ajouter cette copie à nouvelEtat
                            SacADos nouveauSac = new SacADos(s.capaciteMax, s.id);

                            for (Objet o : s.objets) {
                                Objet nouveauObjet = new Objet(o.poids, o.valeur, o.id);
                                nouveauSac.objets.add(nouveauObjet);
                            }
                            nouvelEtat.add(nouveauSac);
                        }
                        // trouver l'indice du sac ou on va placer notre objet
                        int index = indice(nouvelEtat, sac);
                        // mettre a jour le sac en lui ajoutant l'objet
                        nouvelEtat.get(index).ajouterObjet(objet);
                        enfants.add(nouvelEtat);
                    }
                    if (ajoute) {
                        break; // quitter la boucle afin d'eviter de placer l'objet dans deux sacs d'un meme
                        // etat
                    }
                }

            }

        }
        return enfants;

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

    private boolean comparer(List<SacADos> etat1, List<SacADos> etat2) {
        int somVal1 = 0;
        int somVal2 = 0;

        for (int i = 0; i < etat1.size(); i++) { // meme boucle pour parcourir les deux etats en meme temps , car les deux etats ont exactement le meme nombre de sacs

            for (int j = 0; j < etat1.get(i).objets.size(); j++) {

                somVal1 += etat1.get(i).objets.get(j).valeur;
            }
            for (int j = 0; j < etat2.get(i).objets.size(); j++) {

                somVal2 += etat2.get(i).objets.get(j).valeur;
            }

        }
        return somVal1 > somVal2; // est ce que l'etat 1 est meilleur que l'etat 2 ?
    }

}
