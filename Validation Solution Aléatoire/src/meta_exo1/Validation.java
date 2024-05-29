/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_exo1;
import java.util.Random;
/**
 *
 * @author Dell
 */
public class Validation {
    
    public boolean valider ( int [] T , int [][] d, int dmax){
        
         //System.out.println("entrée de valider \n");
         
        boolean v=true; // v pour valide
        int i=0;
        int j=1;
        int somd=0;
        
        if(T[0] != T[7]){
            System.out.println("mismaching entre ville 1 et ville2 \n");
          return false;
        }
        
        // verification que les villes ne se repete pas
        
        while (j<=6 && v){
                 if(T[0]==T[j]){
                      System.out.println(T[0]+" "+T[j]+" ville qui se repete!!! \n");
                      v=false;
                 }else{
                  j=j+1;
                 }
             }
        i++;
        while (i<=6 && v){
              j=i+1;
             while (j<=7 && v){
                 if(T[i]==T[j]){
                      System.out.println("une ville qui se repete \n");
                   v=false;
                 }else{
                  j=j+1;
                 }
             }
             
             i=i+1;
        }
        
        i=0;
        //evaluation
        while(i<=6 && v){
            somd=somd+d[T[i]-1][T[i+1]-1];
            if(somd > dmax){
                 System.out.println("somme distance >> \n");
             v=false;
            }else{
              i=i+1;
            }
        }
        
        if(v==false || somd > dmax){
             //System.out.println("somme distance >> \n");
         return false;
        }
        
        
        
        
        return true;
    }
   
   
      public void generer ( int [] T ){
          
           Random random = new Random();

        
        for(int i=0; i<8;i++){
           T[i]=random.nextInt(7)+1;
        }
          
       
      }
    
       public void afficher ( int [] T ){
       
         for(int i=0; i<8;i++){
           System.out.print(T[i]+" ");
        }
          
       
      }
}
