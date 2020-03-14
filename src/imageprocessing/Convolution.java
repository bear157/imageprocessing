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
    final static String fileName = "Picture";
    
    final static int[][] kernel = {
        {-1,0,1},
        {-2,0,2},
        {-1,0,1}
    };
    
    static int[][] arrImageData;
    static int[][] arrOutput;
    
    public static void main(String[] args) {
        // get width and height of image
        width = Integer.parseInt(JOptionPane.showInputDialog("Enter width?"));
        height = Integer.parseInt(JOptionPane.showInputDialog("Enter height?"));

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
                    //System.out.println(rawData);
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
                for (int kh = startIndex; kh <= endIndex; kh++) {
                    for (int kw = startIndex; kw <= endIndex; kw++) {
                        
                        if( (h+kh) < 0 || (h+kh) >= height 
                                || (w+kw) < 0 || (w+kw) >= width )
                        {
                            continue;
                        }
                        sum += arrImageData[h+kh][w+kw] * kernel[kh+indexNumber][kw+indexNumber];
                        
                    }//--- end loop kernel width ---//
                }//--- end loop kernel length ---//
                
                if(sum < 0)
                    sum = 0;
                if(sum > 255)
                    sum = 255;
                
                arrOutput[h][w] = sum;
            }//--- end loop width ---//
        }//--- end loop height ---//
    }//--- end doConvolution() ---//
    
    public static void save() {
        try {
            File f = new File("image/" + fileName + "_convolution.raw");
            FileOutputStream myOutputFile = new FileOutputStream(f, false);

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    myOutputFile.write(arrOutput[h][w]);
                }//--- end loop width ---//
            }//--- end loop height ---//

            myOutputFile.close();
            System.out.println("Convolution is done.");
        } catch (IOException ex) {
            System.out.println("File output error.");
        }
    }//--- end save() ---//
}