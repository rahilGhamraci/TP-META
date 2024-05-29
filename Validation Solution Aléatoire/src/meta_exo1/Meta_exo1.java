/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package meta_exo1;

/**
 *
 * @author Dell
 */
public class Meta_exo1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        int [][] distances= new int [7][7];
        distances[0][0]=0;
        distances[0][1]=40;
        distances[0][2]=5;
        distances[0][3]=10;
        distances[0][4]=30;
        distances[0][5]=43;
        distances[0][6]=20;
       
        
        
        distances[1][0]=40;
        distances[1][1]=0;
        distances[1][2]=32;
        distances[1][3]=29;
        distances[1][4]=85;
        distances[1][5]=5;
        distances[1][6]=22;
        
        
        distances[2][0]=5;
        distances[2][1]=32;
        distances[2][2]=0;
        distances[2][3]=5;
        distances[2][4]=38;
        distances[2][5]=35;
        distances[2][6]=17;
        
        
        distances[3][0]=10;
        distances[3][1]=29;
        distances[3][2]=5;
        distances[3][3]=0;
       
        
        distances[4][0]=30;
        distances[4][1]=85;
        distances[4][2]=38;
        distances[4][3]=42;
        distances[4][4]=0;
        
        
        distances[5][0]=43;
        distances[5][1]=5;
        distances[5][2]=35;
        distances[5][3]=29;
        distances[5][4]=82;
        distances[5][5]=0;
        distances[5][6]=21;
        
        distances[6][0]=20;
        distances[6][1]=22;
        distances[6][2]=17;
        distances[6][3]=11;
        distances[6][4]=58;
        distances[6][5]=21;
        distances[6][6]=0;
        
        int [] sol1={1,2,3,4,5,6,7,1};
        int [] sol2={5,1,3,4,7,6,2,5};
        int [] solG=new int[8];
        
        Validation v1= new Validation();
        
        boolean val=v1.valider(sol1, distances, 300);
        
        v1.generer(solG);
        System.out.println("solution generee");
        v1.afficher(solG);
        boolean val1=v1.valider(solG, distances, 300);
        
        if(val){
          System.out.println("solution valide");
        }else{
          System.out.println("solution non valide");
        }
        
         if(val1){
          System.out.println("solution generee valide");
        }else{
          System.out.println("solution generee non valide");
        }
        
        
    }
    
}
