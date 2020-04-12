package imageprocessing;

import java.awt.Font;
import javax.swing.*;
import java.io.*;


/**
 * Fundamental of Image Processing 
 * High Pass Filter
 * Assignment 5
 * @author jx
 */
public class HighPassFilter {
    
    static int width;
    static int height;
    final static String fileName = "yoda";
    
    final static int[][] kernel = {

        {-1,-1,-1},
        {-1,8,-1},
        {-1,-1,-1}
    };
    
    static int[][] arrImageData;
    static int[][] arrOutput;
    
    public static void main(String[] args) {
        setUIFont (new javax.swing.plaf.FontUIResource("Arial",Font.PLAIN,20));
        
        // get width and height of image
        width = Integer.parseInt(JOptionPane.showInputDialog("Enter width? (Image: "+fileName+")"));
        height = Integer.parseInt(JOptionPane.showInputDialog("Enter height? (Image: "+fileName+")"));

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        
        //declare array needed
        arrImageData = new int[height][width];
        arrOutput = new int[height][width];
        
        if (readImage()) {
            // do action here
            doConvolution(); 
            save();
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
                    if (rawData == -1) {
                        break;
                    } else {
                        arrImageData[h][w] = rawData; //use arrOutput to temporarily store image data
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
        int startIndex = -kernel.length / 2;
        int endIndex = kernel.length / 2;
        int indexNumber = kernel.length / 2; // use for getting kernel value

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int sum = 0;
                int numOfPixel = 0;
//                System.out.println("-----------------------------------------------------------------");
//                System.out.println("current pixel index [h][w]: " + h + " " + w);
//                System.out.printf("|%-15s|%-10s|%-10s|%-10s|\n", "IMAGE", "KERNEL", "PRODUCT", "RUN SUM");
                for (int kh = startIndex; kh <= endIndex; kh++) {
                    for (int kw = startIndex; kw <= endIndex; kw++) {
                        if( (h-kh) < 0 || (h-kh) >= height 
                                || (w-kw) < 0 || (w-kw) >= width )
                        {
                            continue;
                        }
                        
                        
                        sum += arrImageData[h-kh][w-kw] * kernel[kh+indexNumber][kw+indexNumber];
                        numOfPixel++;
//                        System.out.printf("|%-15s|%-10s|%-10s|%-10s|\n", 
//                                arrImageData[h+kh][w+kw] + " (" + (h+kh) + ") (" + (w+kw) + ")", 
//                                kernel[kh+indexNumber][kw+indexNumber], 
//                                arrImageData[h+kh][w+kw] * kernel[kh+indexNumber][kw+indexNumber], 
//                                sum);
                    }//--- end loop kernel width ---//
                }//--- end loop kernel length ---//
                
//                sum = sum / (int) Math.pow(kernel.length, 2);
                if (sum < 0) 
                    sum = 0; 
                if (sum > 255)
                    sum = 255;
                
                arrOutput[h][w] = sum ;
            }//--- end loop width ---//
        }//--- end loop height ---//
    }//--- end doConvolution() ---//
    
    public static void save() {
        try {
            File f = new File("image/" + fileName + "_highpass.raw");
            FileOutputStream myOutputFile = new FileOutputStream(f, false);

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    myOutputFile.write(arrOutput[h][w]);
                }//--- end loop width ---//
            }//--- end loop height ---//

            myOutputFile.close();
            System.out.println("High pass filter is done.");
        } catch (IOException ex) {
            System.out.println("File output error.");
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
