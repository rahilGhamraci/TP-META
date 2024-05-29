/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meta_exo3;

/**
 *
 * @author Dell
 */
public class Noeud {
     int [] sol;
    Noeud parent;
    int f;
    
    public Noeud(int [] s,Noeud p,int f){
       this.sol=s;
       this.parent=p;
        this.f=f;
    }
    
}
