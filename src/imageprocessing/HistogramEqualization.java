package imageprocessing;

import java.awt.Font;
import javax.swing.*;
import java.io.*;

/**
 * Fundamental of Image Processing 
 * Histogram Equalization
 * Assignment 3
 * @author Yee Jian Xiong (B180237C)
 */
public class HistogramEqualization {
    
    static int width;
    static int height;
    static int[][] arrOutput;
    static String fileName;
    final static double imageBits = 8; // 8-bits image
    final static int level = (int) Math.pow(2, imageBits);
    
    static int[] arrRunSum;
    static int[] arrNumOfPixel;
    static int[] arrNormalize;
    
    public static void main(String[] args) {
        setUIFont (new javax.swing.plaf.FontUIResource("Arial",Font.PLAIN,20));
        
        // get filename, width and height of image
        fileName = JOptionPane.showInputDialog("Enter image name? ('.raw' is not required)");
        width = Integer.parseInt(JOptionPane.showInputDialog("Enter width?"));
        height = Integer.parseInt(JOptionPane.showInputDialog("Enter height?"));
        
        System.out.println("Image name: " + fileName + ".raw");
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        
        //declare array needed
        arrOutput = new int[height][width];
        arrRunSum = new int[level];
        arrNumOfPixel = new int[level];
        arrNormalize = new int[level];
        
        // read all the data 1st
        if (readImage()) {
            // calculate run sum
            calcRunSum();
            doNormalizeAndMultiply();
            save();
        }
    }//-- end main() ---//
    
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
                        arrNumOfPixel[rawData]++;
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
    
    public static void calcRunSum() {
        int runSum = 0;
        for (int i = 0; i < arrNumOfPixel.length; i++) {
            runSum += arrNumOfPixel[i];
            arrRunSum[i] = runSum;
        }//--- end for loop ---//        
    }//--- end calcRunSum() ---//
    
    public static void doNormalizeAndMultiply() {
        int maxGrayLevel = level - 1;
        int totalPixel = arrRunSum[maxGrayLevel];
        
        for (int i = 0; i < arrRunSum.length; i++) {
            double normalizedValue = (double) arrRunSum[i] / (double) totalPixel * (double) maxGrayLevel;
            arrNormalize[i] = (int) Math.round(normalizedValue);
        }
    }//--- end doNormalizeAndMultiply() ---//
    
    public static void save() {
        try {
            File f = new File("image/" + fileName + "_HE.raw");
            FileOutputStream myOutputFile = new FileOutputStream(f, false);

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    int data = arrOutput[h][w];
                    arrOutput[h][w] = arrNormalize[data];
                    myOutputFile.write(arrOutput[h][w]);
                }//--- end loop width ---//
            }//--- end loop height ---//

            myOutputFile.close();
            System.out.println("Histogram equalization is done. \nOutput: " + fileName + "_HE.raw");
            JOptionPane.showMessageDialog(null, "Histogram equalization is done. \nOutput: " + fileName + "_HE.raw");
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
