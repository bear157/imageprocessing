/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;
import java.util.*;

/**
 *
 * @author Student
 */
public class ImageProcessing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            for (int j = i; j < 5 ; j++) {
                System.out.print("*");
            }
            System.out.println("");
        }
        int [][] array = {
            {1,2,3,4,5}, 
            {1,0,3,4,8} 
        };
        
        System.out.println(array[1][4]);
    }
    
}
