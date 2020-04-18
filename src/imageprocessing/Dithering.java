/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.Font;
import javax.swing.*;
import java.io.*;

/**
 * Fundamental of Image Processing 
 * Dithering
 * Assignment 2
 * @author Yee Jian Xiong (B180237C)
 */
public class Dithering {
    
    static int width;
    static int height;
    static int[][] arrOutput;
    static String mode;
    static String fileName;
    
    final static int[][] d1 = {
        {0,128},
        {192,64}
    };
    
    final static int[][] d2 = {
        {0,128,32,160},
        {192,64,224,96},
        {48,176,16,144},
        {240,112,208,80}
    };
    
    
    
    public static void main(String[] args) {
        setUIFont (new javax.swing.plaf.FontUIResource("Arial",Font.PLAIN,20));
        
        // get filename, width and height of image
        fileName = JOptionPane.showInputDialog("Enter image name? ('.raw' is not required)");
        width = Integer.parseInt(JOptionPane.showInputDialog("Enter width?"));
        height = Integer.parseInt(JOptionPane.showInputDialog("Enter height?"));
        
        System.out.println("Image name: " + fileName + ".raw");
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        
        arrOutput = new int[height][width];
        
        // read all the data 1st
        if(readImage()) {
            doDithering();
            save();
        }
        
        
    }//--- end main() ---//
    
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
                JOptionPane.showMessageDialog(null, "Error in width or length");
                valid = false;
            }
            
            
            myInputFile.close();
        } catch (IOException ex) {
            System.out.println("File read error");
            JOptionPane.showMessageDialog(null, "File read error.");
            return false;
        }
        
        return valid;
    }//--- end readImage() ---//
    
    public static void doDithering() {
        //user input to choose D1 or D2
        do {
            mode = JOptionPane.showInputDialog("D1 or D2? \n** D1 : 2x2 dither matrix \n** D2 : 4x4 dither matrix");
        } while (!mode.equalsIgnoreCase("D1") && !mode.equalsIgnoreCase("D2"));
        
        int[][] matrix = null;
        if(mode.equalsIgnoreCase("D1")){
            matrix = d1;
        }
        if(mode.equalsIgnoreCase("D2")){
            matrix = d2;
        }
        
        //process the data
        for (int h = 0; h < height; h+=matrix.length) {
            for (int w = 0; w < width; w+=matrix.length) {
                for (int dh = 0; dh < matrix.length; dh++) {
                    for (int dw = 0; dw < matrix.length; dw++) {
                        if(height <= (h+dh) || width <= (w+dw))
                            continue;
                        if(arrOutput[h+dh][w+dw] > matrix[dh][dw])
                            arrOutput[h+dh][w+dw] = 255;
                        else
                            arrOutput[h+dh][w+dw] = 0;                        
                    }
                }
            }//--- end loop width ---//
        }//--- end loop height ---//
        

        
        
        
    }//--- end doDithering ---//
    
    
    public static void save() {

        try {
            File f = new File("image/"+fileName+"_"+mode.toLowerCase()+".raw");
            //PrintWriter myOutputFile = new PrintWriter(new FileWriter(f,false));
            FileOutputStream myOutputFile = new FileOutputStream(f, false);

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    int pattern = arrOutput[h][w];
                    myOutputFile.write(pattern);
                }//--- end loop width ---//
            }//--- end loop height ---//

            myOutputFile.close();
            System.out.println("Dithering (" + mode + ") is done. \nOutput: "+fileName+"_"+mode.toLowerCase()+".raw");
            JOptionPane.showMessageDialog(null, "Dithering (" + mode + ") is done. \nOutput: "+fileName+"_"+mode.toLowerCase()+".raw");
        } catch (IOException ex) {
            System.out.println("File output error.");
            JOptionPane.showMessageDialog(null, "File output error.");
        }

    }//--- end save() ---//
    
    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
