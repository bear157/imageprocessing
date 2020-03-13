package imageprocessing;

import javax.swing.*;
import java.io.*;

/**
 * Fundamental of Image Processing 
 * Convolution
 * Assignment 4
 * @author jx
 */
public class Convolution {
    
    static int width;
    static int height;
    static int[][] arrOutput;
    final static String fileName = "yoda";
    
    final static int[][] kernel = {
        {-1,0,1},
        {-2,0,2},
        {-1,0,1}
    };
    
    public static void main(String[] args) {
        // get width and height of image
        width = Integer.parseInt(JOptionPane.showInputDialog("Enter width?"));
        height = Integer.parseInt(JOptionPane.showInputDialog("Enter height?"));

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        
        //declare array needed
        arrOutput = new int[height][width];
        
        if (readImage()) {
            // do action here
            doConvolution(); 
            
        }
    }//--- end main ---//
    
    public static boolean readImage() {
        boolean valid = true;
        try {
            FileInputStream myInputFile = new FileInputStream("image/"+fileName+".raw");
            int rawData;
            
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    rawData = myInputFile.read();
                    //System.out.println(rawData);
                    if (rawData == -1) {
                        break;
                    } else {
                        arrOutput[h][w] = rawData; //use arrOutput to temporarily store image data
                    }
                }//--- end loop width ---//
            }//--- end loop height ---//
            
            if (myInputFile.read() != -1) {
                System.out.println("Error in width or length");
                valid = false;
            }
            
            myInputFile.close();
        } catch (IOException ex) {
            System.out.println("File read error");
        }
        
        return valid;
    }//--- end readImage() ---//
    
    public static void doConvolution(){
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                
            }//--- end loop width ---//
        }//--- end loop height ---//
    }//--- end doConvolution() ---//
    
}
