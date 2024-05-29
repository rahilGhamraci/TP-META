/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_interface_2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Dell
 */
public class AlgoGen {

    public int[][] recherche(float probaCross, float probaMutation, int nbGen, int taillePop, Objet[] objets,
            Sac[] sacs) {
        
        //System.out.println("etrée de la methode recherche");
        int[][] bestSolution = generateInitSolution(objets, sacs);
        //System.out.println("best solution init");
        //afficher(bestSolution,sacs,objets);
        ArrayList<int[][]> populationCrossOver;

        ArrayList<int[][]> populationSelectionnee;

        ArrayList<int[][]> populationMutated;

        ArrayList<int[][]> population = generateInitPopulation(taillePop, objets, sacs);
        //System.out.println("\npopulation initilale");
        //afficherPop(population, sacs, objets);

        for (int i = 0; i < nbGen; i++) {
            //System.out.println(
            //        "\n-------------------------------Genration " + i + "-------------------------------------");
            // selection des individus
            populationSelectionnee = selectionnerPopu(population, taillePop, sacs, objets);
            //System.out.println("\npopulation selectionne");
            //afficherPop(populationSelectionnee, sacs, objets);
            // crossover
            populationCrossOver = crossover(populationSelectionnee, populationSelectionnee.size(), sacs, objets,probaCross);
            //System.out.println("\npopulation apres application du crossover");
            //afficherPop(populationCrossOver, sacs, objets);
            // mutation
            populationMutated = mutation(populationCrossOver, populationCrossOver.size(), sacs, objets,probaMutation);
            //System.out.println("\npopulation apres application de la mutaion");
            //afficherPop(populationMutated, sacs, objets);
            // evaluation des individus crées
            double[] valeursF = evaluation(populationMutated, populationMutated.size(), sacs, objets);

            int indiceMax = Arrays.stream(valeursF)
                    .boxed()
                    .collect(java.util.stream.Collectors.toList())
                    .indexOf(Arrays.stream(valeursF).max().getAsDouble());
            if (fitness(bestSolution, sacs, objets) < valeursF[indiceMax]
                    && valide(populationMutated.get(indiceMax), sacs, objets)) {
                bestSolution = populationMutated.get(indiceMax);
                //System.out.println("nouvelle bestSolution");
                //afficher(bestSolution, sacs, objets);
            }
            // creation d'une nouvelle population
            population = remplacement(population, populationMutated, taillePop, sacs, objets);
            //System.out.println("\npopulation apres remplacement");
            //afficherPop(population, sacs, objets);
        }

        return bestSolution;
    }

    public static int[][] generateInitSolution(Objet[] objets, Sac[] sacs) {
        //System.out.println("etrée de la methode generateInitSolution");
        int[][] sol = new int[sacs.length][objets.length];
        Random random = new Random();
        int indiceSac,indice;
      
        for (int i = 0; i < objets.length; i++) { // essayer de placer chaque objet
            int[] indicesSacPossibles= new int[sacs.length];
            int p=0;
            for (int j = 0; j < sacs.length; j++) { // chercher les sacs possibles ou mettre l'objet
                if(calculer_poids(sol, sacs[j], objets) + objets[i].poids <= sacs[j].capaciteMax){
                    indicesSacPossibles[p]=j; // il est possible de placer l'objet dans ce sac
                    p++;
                }

            }
            if(p>0){
                indice = random.nextInt(p); 
                indiceSac=indicesSacPossibles[indice];
                sol[indiceSac][i] = 1;
            }
            // sinon l'objet n'est pas placé
           
        }

        return sol;
    }

    public static ArrayList<int[][]> generateInitPopulation(int taillePop, Objet[] objets, Sac[] sacs) {

        ArrayList<int[][]> populationInit = new ArrayList<>();

        for (int i = 0; i < taillePop; i++) {

            populationInit.add(generateInitSolution(objets, sacs));
        }

        return populationInit;
    }

    public static int calculer_poids(int[][] sol, Sac sac, Objet[] objets) {
        int poids = 0;
        for (int i = 0; i < objets.length; i++) {
            if (sol[sac.id][i] == 1) {
                poids += objets[i].poids;
            }
        }
        return poids;
    }
    // should've picked another fitness
    public static double fitness(int[][] sol, Sac[] sacs, Objet[] objets) {

        int somV = 0;
        int somP = 0;

        for (int i = 0; i < sacs.length; i++) {
            for (int j = 0; j < objets.length; j++) {
                if (sol[i][j] == 1) {
                    somV += objets[j].valeur;
                    somP += objets[j].poids;

                }
            }
        }

        return 1 / somP + somV; // à maximiser
    }

    public static ArrayList<int[][]> selectionnerPopu(ArrayList<int[][]> population, int taillePop, Sac[] sacs,
            Objet[] objets) {

        // on prepare les donnees pour la selction par roulette
        // pereparer les intervalles

    
        Individus[] probaInd = new Individus[taillePop];
        double somf = 0;
        double f;
        for (int i = 0; i < taillePop; i++) {
            f = fitness(population.get(i), sacs, objets);
            probaInd[i] = new Individus((float) f, i); // pour chaque individus on stocke sa proba et son indice dans la
                                                       // population initiale
            somf += f;
        }
        for (int i = 0; i < taillePop; i++) {
            probaInd[i].proba /= somf;
        }
        //System.out.println("vecteur de proba avant tri ");
        //afficherP(probaInd);
        // trier les proba des individus afin d'avoir le max des proba dans
        // la première case et pouvoir construire les intervalles par la suite
        Arrays.sort(probaInd, new Comparator<Individus>() {
            @Override
            public int compare(Individus i1, Individus i2) {
                // Tri en ordre décroissant
                return Float.compare(i2.proba, i1.proba);
            }
        });
        //System.out.println("\nvecteur de proba aprés tri ");
        //afficherP(probaInd);
        // construire les intervalles pour pouvoir les utiliser dans la selction
        Intervalle[] intervalles = new Intervalle[taillePop];

        intervalles[0] = new Intervalle((float) 0, probaInd[0].proba, probaInd[0].indiceDansPop);

        for (int i = 1; i < probaInd.length; i++) {

            intervalles[i] = new Intervalle(intervalles[i - 1].borneSup,
                    intervalles[i - 1].borneSup + probaInd[i].proba, probaInd[i].indiceDansPop);
        }

        //System.out.println("\nintervalles");
        //afficherI(intervalles);
        // on selectionne la moitie de la population
        // on procède à la selction par roulette
        ArrayList<int[][]> populationSelectionnee = new ArrayList<>();
        for (int i = 0; i < taillePop / 2; i++) {
            float r = (float) (Math.random() * (1 - Float.MIN_VALUE)) + Float.MIN_VALUE; // entre 0 et 1 ( non inclus)
            //System.out.println("\nr généré:" + r);
            int j = 0;
            boolean individusSelectionne = false;
            while (j < intervalles.length && !individusSelectionne) {
                if (intervalles[j].borneInf <= r && r < intervalles[j].borneSup) {
                    populationSelectionnee.add(population.get(intervalles[j].indiceIndividus));
                    individusSelectionne = true;
                    //System.out.println("\nIndividu sélectionné : ");
                    //afficher(population.get(intervalles[j].indiceIndividus), sacs, objets);
                }
                j++;
            }

        }
        return populationSelectionnee;
    }

    public static ArrayList<int[][]> crossover(ArrayList<int[][]> population, int taillePop, Sac[] sacs,
        Objet[] objets,float probaCross) {
        ArrayList<int[][]> populationCrossOver = new ArrayList<>();
        Random random = new Random();
        int nbIndividus = (int) (probaCross * population.size());

        population = new ArrayList<>(population.subList(0, nbIndividus));

        for (int j = 0; j < population.size() - 1; j += 2) {
            int[][] parent1 = population.get(j);
            int[][] parent2 = population.get(j + 1);
            int point = random.nextInt(objets.length);
            int[][] enfant1 = new int[sacs.length][objets.length];
            int[][] enfant2 = new int[sacs.length][objets.length];

            // pour chaque sacs copier les valeurs des objets qui ont un indice <= point de
            // croissement
            for (int i = 0; i < sacs.length; i++) {
                for (int k = 0; k <= point; k++) {
                    enfant1[i][k] = parent1[i][k];
                    enfant2[i][k] = parent2[i][k];
                }
            }
            // pour chaque sacs copier les valeurs des objets qui ont un indice > point de
            // croissement
            for (int i = 0; i < sacs.length; i++) {
                for (int k = point + 1; k < objets.length; k++) {
                    enfant1[i][k] = parent2[i][k];
                    enfant2[i][k] = parent1[i][k];
                }
            }

            populationCrossOver.add(enfant1);
            populationCrossOver.add(enfant2);
        }
        return populationCrossOver;

    }

    // opérateur : inverser la valeur d'une case
    public static ArrayList<int[][]> mutation(ArrayList<int[][]> population, int taillePop, Sac[] sacs,
            Objet[] objets,float probaMutation) {
        ArrayList<int[][]> populationMutated = new ArrayList<>();


        Random random = new Random();
        int nbIndividus = (int) (probaMutation * population.size());
        //System.out.println("probaMutation * population.size() " + probaMutation * population.size());
        //System.out.println("nbIndividus " + nbIndividus);

        if(nbIndividus == 0){
            populationMutated=population;
        }else{
            population = new ArrayList<>(population.subList(0, nbIndividus));

            for (int j = 0; j < nbIndividus; j++) {
                int indSac = random.nextInt(sacs.length);
                int indObjet = random.nextInt(objets.length);
    
                // copier la matrice de l'individu dans une nouvelle matrice
                int[][] solMutated = new int[sacs.length][objets.length];
                for (int i = 0; i < sacs.length; i++) {
                    for (int k = 0; k < objets.length; k++) {
                        solMutated[i][k] = population.get(j)[i][k];
    
                    }
                }
                // inversement de la case choisie randomly
                if (solMutated[indSac][indObjet] == 1) {
                    solMutated[indSac][indObjet] = 0;
                } else {
                    solMutated[indSac][indObjet] = 1;
                }
                //System.out.println("\nindividu muté");
                //afficher(solMutated, sacs, objets);
                populationMutated.add(solMutated);
            }

        }
       
       

        return populationMutated;

    }

    public static ArrayList<int[][]> remplacement(ArrayList<int[][]> populationCourante, ArrayList<int[][]> nouveauxInd,
            int taillePop, Sac[] sacs, Objet[] objets) {
        // Fusion de la population courante avec les nouveaux individus
        ArrayList<int[][]> populationFusionnee = new ArrayList<>(populationCourante);
        for (int[][] individu : nouveauxInd) {
            if (valide(individu, sacs,objets)) {
                populationFusionnee.add(individu);
            }
        }

        // Supprimer les individus invalides de la population fusionnée
        populationFusionnee.removeIf(individu -> !valide(individu, sacs,objets));

        // Tri de la population fusionnée en fonction de la fitness de chaque individu
        populationFusionnee.sort(Comparator.comparingDouble(individu -> fitness(individu, sacs, objets)));

        // Sélection des 'taillePop' premiers individus de la population triée (les
        // meilleurs)
        ArrayList<int[][]> nouvellePop = new ArrayList<>(populationFusionnee.subList(0, taillePop));

        return nouvellePop;
    }

    public static double[] evaluation(ArrayList<int[][]> population, int taillePop, Sac[] sacs, Objet[] objets) {
        double[] fitenessIndividus = new double[taillePop];

        for (int i = 0; i < taillePop; i++) {

            fitenessIndividus[i] = fitness(population.get(i), sacs, objets);

        }

        return fitenessIndividus;


    }

    public static boolean valide(int[][] T, Sac[] sacs, Objet[] objets) {
        int nb=0;
        boolean valide=true;
        // verification de la capacité
        for (int i = 0; i < sacs.length; i++) {
            if (calculer_poids(T,sacs[i],objets) > sacs[i].capaciteMax) {
               valide = false;
               break;
            }
        }
        // verification qu'un objet n'est pas répétés dans plusieurs sacs
        for (int i = 0; i < objets.length; i++) {
            nb=0;
            for (int j = 0; j < sacs.length; j++) {
                if (T[j][i] == 1) {
                  nb++;
                }
            }
            if(nb>1){
                valide = false;
                break;
            }
        }
        return valide;
    }

    public void afficherPop(ArrayList<int[][]> population, Sac[] sacs, Objet[] objets) {

        for (int p = 0; p < population.size(); p++) {
            System.out.println("individus " + p + ":");
            afficher(population.get(p), sacs, objets);

        }

    }

    public static void afficher(int[][] T, Sac[] sacs, Objet[] objets) {

        for (int i = 0; i < sacs.length; i++) {

            for (int j = 0; j < objets.length; j++) {
                System.out.print(T[i][j] + " ");
            }
            System.out.print("\n");
        }

    }

    public static void afficherP(Individus[] T) {

        for (int i = 0; i < T.length; i++) {
            System.out.print(T[i].proba + "," + T[i].indiceDansPop + "|");
        }

    }

    public static void afficherI(Intervalle[] T) {

        for (int i = 0; i < T.length; i++) {
            System.out.println(
                    "[" + T[i].borneInf + "," + T[i].borneSup + "]" + " indceIndividus:" + T[i].indiceIndividus);
        }

    }
    public static   String interpretationSol(int[][] sol,Sac[] sacs, Objet[] objets) {
        String output="";
        for (int i = 0; i < sacs.length; i++) {
            output = output.concat("Sac à dos " + sacs[i].id + ": Capacité = " + sacs[i].capaciteMax+"\n");
            for (int j = 0; j <objets.length; j++) {
                if(sol[i][j]==1){
                    output = output.concat("   Objet :" + objets[j].id + " Poids = " + objets[j].poids + ", Valeur = " + objets[j].valeur+"\n");
                }
               
                
            }
    
        }
        return output;

    }
}

