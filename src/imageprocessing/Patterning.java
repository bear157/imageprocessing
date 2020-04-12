import java.awt.Font;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Fundamental of Image Processing 
 * Patterning
 * Assignment 2
 * @author jx
 */
public class Patterning {
    static int width;
    static int height;
    
    final static int[][][] arrPattern = {
        //pattern 0
        {
            {0,0,0},
            {0,0,0},
            {0,0,0}
        },
        //pattern 1
        {
            {0,0,0},
            {0,0,0},
            {0,0,255}
        },
        //pattern 2
        {
            {255,0,0},
            {0,0,0},
            {0,0,255}
        },
        //pattern 3
        {
            {255,0,255},
            {0,0,0},
            {0,0,255}
        },
        //pattern 4
        {
            {255,0,255},
            {0,0,0},
            {255,0,255}
        },
        //pattern 5
        {
            {255,0,255},
            {0,0,0},
            {255,255,255}
        },
        //pattern 6
        {
            {255,0,255},
            {255,0,0},
            {255,255,255}
        },
        //pattern 7
        {
            {255,255,255},
            {255,0,0},
            {255,255,255}
        },
        //pattern 8
        {
            {255,255,255},
            {255,0,255},
            {255,255,255}
        },
        //pattern 9
        {
            {255,255,255},
            {255,255,255},
            {255,255,255}
        }
    };
    
    static int[][] arrOutput;
    static String fileName = "yoda";
    
    public static void main(String[] args) {
        setUIFont (new javax.swing.plaf.FontUIResource("Arial",Font.PLAIN,20));
        
        width = Integer.parseInt(JOptionPane.showInputDialog("Enter width? (Image: "+fileName+")"));
        height = Integer.parseInt(JOptionPane.showInputDialog("Enter height? (Image: "+fileName+")"));
        
        
        //store the pattern number
        arrOutput = new int[height][width];
        
        try{
            FileInputStream myInputFile = new FileInputStream("image/"+fileName+".raw");
            
            int rawData;
            //System.out.println(rawData);
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    rawData = myInputFile.read();
                    //System.out.println(rawData);
                    if(rawData == -1){
                        break;
                    }else{
                        arrOutput[h][w] = checkPattern(rawData);
                    }
                }//--- end loop width ---//
            }//--- end loop height ---//
            
            if(myInputFile.read() == -1){
                //save output file 
                save();
                //System.out.println("arrOutput height: " + arrOutput.length);
            }else{
                System.out.println("Error in width or length");
            }
            
            myInputFile.close();
        }catch(IOException ex){
            System.out.println("File input error");
        }
    }//--- end main() ---//
    
    public static int checkPattern(int rawData) {
        int patternNum = 0;
        for (int count = 0; count < 10; count++) {
            int patternRange = 26 * (count + 1);
            if(rawData <= patternRange){
                patternNum = count; 
                break;
            }//--- end if ---//
        }//--- end for loop ---//
        
        return patternNum;
    }//--- end checkPattern() ---//
    
    
    public static void save() {
        
        try{
            File f = new File("image/"+fileName+"_patterning.raw");
            //PrintWriter myOutputFile = new PrintWriter(new FileWriter(f,false));
            FileOutputStream myOutputFile = new FileOutputStream(f, false);
            
            for (int h = 0; h < height; h++) {
                for (int p = 0; p < 3; p++) {
                    for (int w = 0; w < width; w++) {
                        int pattern = arrOutput[h][w];
                        myOutputFile.write(arrPattern[pattern][p][0]);
                        myOutputFile.write(arrPattern[pattern][p][1]);
                        myOutputFile.write(arrPattern[pattern][p][2]);
                    }//--- end loop width ---//
                }//--- end loop pattern row ---//  
            }//--- end loop height ---//
            
            myOutputFile.close();
            System.out.println("Patterning is done.");
        }catch(IOException ex) {
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
