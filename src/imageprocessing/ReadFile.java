import java.io.*;

public class ReadFile {
    
        
    public static void main(String[] args) {
        
        
        try{
            FileInputStream myInputFile = new FileInputStream("image/yoda.tif");
            
            File f = new File("image/yodaInfo.txt");
            PrintWriter myOutputFile = new PrintWriter(new FileWriter(f,false));
            //int value;
            
            //----- get header info -----// 
            // byte order 
            int byteOrder; 
            String strByteOrder = "";
            
            byteOrder = myInputFile.read(); 
            strByteOrder = String.format("%02X", byteOrder); 
            byteOrder = myInputFile.read(); 
            strByteOrder = String.format("%02X", byteOrder) + strByteOrder; 
            
            // print header info
            //System.out.println("Byte order: " + strByteOrder);
            myOutputFile.println(prefixSuffix("Header Info", "-", "", 80));
            myOutputFile.println(String.format("%-15s", "Byte Order") + ":" + strByteOrder); 
            
            
            if(strByteOrder.equalsIgnoreCase("4949")){
                isLSB(myInputFile, myOutputFile); 
            }
            
            
            

            myInputFile.close(); 
            myOutputFile.close(); 
        } catch(IOException ex) {
            System.out.println("File input error");
        }
    }//--- end main() ---//
    
    
    public static void isLSB(FileInputStream myInputFile, PrintWriter myOutputFile) {
        try{
            // version 
            int version;
            String strVersion = "";

            version = myInputFile.read();
            strVersion += String.format("%02X", version);
            version = myInputFile.read();
            strVersion = String.format("%02X", version) + strVersion;
		
            strVersion = strVersion.replaceFirst("^0+(?!$)", ""); //remove leading zero regex lookahead
            //System.out.println("Version: " + strVersion);
            myOutputFile.println(String.format("%-15s", "Version") + ":" + strVersion); 
            
            
            // offset
            int offset; 
            String strOffset = "";
            for (int i = 0; i < 4; i++) {
                offset = myInputFile.read();
                strOffset = String.format("%02X", offset) + strOffset;
            }
            
            strOffset = strOffset.replaceFirst("^0+(?!$)", ""); //remove leading zero regex lookahead
            //System.out.println("Offset: " + strOffset);
            myOutputFile.println(String.format("%-15s", "Offset") + ":" + strOffset); 
            
            // number of directory entry (DE)
            int numOfDE; 
            String strNumOfDE = "";
            
            numOfDE = myInputFile.read(); 
            strNumOfDE = String.format("%02X", numOfDE); 
            numOfDE = myInputFile.read(); 
            strNumOfDE = String.format("%02X", numOfDE) + strNumOfDE; 
            
            numOfDE = Integer.parseInt(strNumOfDE, 16);
            //System.out.println("Number of DE: " + numOfDE);
            
            
            // Directory Entry
            String[][] arrDE = new String[numOfDE][4];
            for (int i = 0; i < numOfDE; i++) {
                // DE tag 
                int tag;
                String strTag = "";
                
                tag = myInputFile.read(); 
                strTag = String.format("%02X", tag) + strTag;
                tag = myInputFile.read(); 
                strTag = String.format("%02X", tag) + strTag; 
                
                // DE type 
                int type;
                String strType = "";
                
                type = myInputFile.read(); 
                strType = String.format("%02X", type) + strType;
                type = myInputFile.read(); 
                strType = String.format("%02X", type) + strType; 
                
                // DE length
                int length;
                String strLength = "";

                for (int j = 0; j < 4; j++) {
                    length = myInputFile.read();
                    strLength = String.format("%02X", length) + strLength;
                }
                
                // DE value
                int value; 
                String strValue = "";
                
                for (int j = 0; j < 4; j++) {
                    value = myInputFile.read();
                    strValue = String.format("%02X", value) + strValue;
                }
                
                /*
                System.out.println("------------------ DE" + (i+1) +" ------------------");
                System.out.println("Tag: " + strTag);
                System.out.println("Type: " + strType);
                System.out.println("Length: " + strLength);
                System.out.println("Value: " + strValue);
                */
                
                arrDE[i][0] = strTag; 
                arrDE[i][1] = strType; 
                arrDE[i][2] = strLength; 
                arrDE[i][3] = strValue; 
            }//--- end looping DE ---//
            
            myInputFile = ignoreLines(myInputFile, numOfDE, arrDE);
            
            
            // save info the txt file
            save(myInputFile, myOutputFile, arrDE);  
            
        }catch(IOException ex) {
            System.out.println("File error");
        }
        
        
        
    }//--- end isLSB() ---//
    
    public static void isMSB(FileInputStream myInputFile, PrintWriter myOutputFile) {
        try{
            // version 
            int version;
            String strVersion = "";

            version = myInputFile.read();
            strVersion += String.format("%02X", version);
            version = myInputFile.read();
            strVersion += String.format("%02X", version);
		
            strVersion = strVersion.replaceFirst("^0+(?!$)", ""); //remove leading zero regex lookahead
            //System.out.println("Version: " + strVersion);
            myOutputFile.println(String.format("%-15s", "Version") + ":" + strVersion); 
            
            
            // offset
            int offset; 
            String strOffset = "";
            for (int i = 0; i < 4; i++) {
                offset = myInputFile.read();
                strOffset += String.format("%02X", offset);
            }
            
            strOffset = strOffset.replaceFirst("^0+(?!$)", ""); //remove leading zero regex lookahead
            //System.out.println("Offset: " + strOffset);
            myOutputFile.println(String.format("%-15s", "Offset") + ":" + strOffset); 
            
            // number of directory entry (DE)
            int numOfDE; 
            String strNumOfDE = "";
            
            numOfDE = myInputFile.read(); 
            strNumOfDE = String.format("%02X", numOfDE); 
            numOfDE = myInputFile.read(); 
            strNumOfDE += String.format("%02X", numOfDE); 
            
            numOfDE = Integer.parseInt(strNumOfDE, 16);
            //System.out.println("Number of DE: " + numOfDE);
            
            
            // Directory Entry
            String[][] arrDE = new String[numOfDE][4];
            for (int i = 0; i < numOfDE; i++) {
                // DE tag 
                int tag;
                String strTag = "";
                
                tag = myInputFile.read(); 
                strTag += String.format("%02X", tag);
                tag = myInputFile.read(); 
                strTag += String.format("%02X", tag); 
                
                // DE type 
                int type;
                String strType = "";
                
                type = myInputFile.read(); 
                strType = String.format("%02X", type) + strType;
                type = myInputFile.read(); 
                strType = String.format("%02X", type) + strType; 
                
                // DE length
                int length;
                String strLength = "";

                for (int j = 0; j < 4; j++) {
                    length = myInputFile.read();
                    strLength = String.format("%02X", length) + strLength;
                }
                
                // DE value
                int value; 
                String strValue = "";
                
                for (int j = 0; j < 4; j++) {
                    value = myInputFile.read();
                    strValue = String.format("%02X", value) + strValue;
                }
                
                /*
                System.out.println("------------------ DE" + (i+1) +" ------------------");
                System.out.println("Tag: " + strTag);
                System.out.println("Type: " + strType);
                System.out.println("Length: " + strLength);
                System.out.println("Value: " + strValue);
                */
                
                arrDE[i][0] = strTag; 
                arrDE[i][1] = strType; 
                arrDE[i][2] = strLength; 
                arrDE[i][3] = strValue; 
            }//--- end looping DE ---//
            
            myInputFile = ignoreLines(myInputFile, numOfDE, arrDE);
            
            
            // save info the txt file
            save(myInputFile, myOutputFile, arrDE);  
            
        }catch(IOException ex) {
            System.out.println("File error");
        }
        
        
        
    }//--- end isMSB() ---//
    
    public static FileInputStream ignoreLines(FileInputStream myInputFile, int numOfDE, String[][] arrDE){
    	// get how many lines are not data (in bytes)
    	int stripOffset = 0;
    	
    	for(int i = 0; i < numOfDE; i++){
            String strTag = arrDE[i][0]; 
            int tag = Integer.parseInt(strTag, 16);
            if(tag == 273) {
                String strStripOffset = arrDE[i][3]; 
                stripOffset = Integer.parseInt(strStripOffset, 16); 
                break;
            }
        }//--- end for ---//
	    
        /** 
         * minus the other bytes that has been read 
         * 1 DE has 12 bytes 
         * image header has 8 bytes 
         * after header, 2 bytes for number of DE
         */
        stripOffset = stripOffset - (12 * numOfDE) - 8 - 2;
        try{
            for(int i = 0; i < stripOffset; i++){
                myInputFile.read(); 
            }
        }catch(IOException ex) {
            System.out.println("File input error");
        }


        return myInputFile; 
    }//--- end ignoreLines() ---//
    
    
    public static void save(FileInputStream myInputFile, PrintWriter myOutputFile, String[][] arrDE) {
    	try{
            myOutputFile.println(prefixSuffix("Data Entry", "-", "", 80));
            myOutputFile.format("|%-40s|%-15s|%-10s|%-10s|\n", "Tag", "Type", "Count", "Value");
            myOutputFile.println(prefixSuffix("", "-", "", 80));

            loopDE: for(int i = 0; i < arrDE.length; i++){
                // process de tag
                String strTag = arrDE[i][0].replaceFirst("^0+(?!$)", ""); //remove leading zero 
                switch (Integer.parseInt(strTag, 16)) {
                    case 254: strTag = strTag + " (New Subfile Type)"; break;
                    case 256: strTag = strTag + " (Image Width)"; break;
                    case 257: strTag = strTag + " (Image Length)"; break;
                    case 258: strTag = strTag + " (Bits Per Sample)"; break;
                    case 259: strTag = strTag + " (Compression)"; break;
                    case 262: strTag = strTag + " (Photometric Interpretation)"; break;
                    case 273: strTag = strTag + " (Strip Offsets)"; break;
                    case 277: strTag = strTag + " (Samples Per Pixel)"; break;
                    case 278: strTag = strTag + " (Rows Per Strip)"; break;
                    case 279: strTag = strTag + " (Strip Byte Counts)"; break;
                    case 282: strTag = strTag + " (X Resolution)"; break;
                    case 283: strTag = strTag + " (Y Resolution)"; break;
                    case 296: strTag = strTag + " (Resolution Unit)"; break;
                    default : continue loopDE;
                }//--- end switch ---//


                // process de type
                String strType = arrDE[i][1].replaceFirst("^0+(?!$)", ""); //remove leading zero
                switch (Integer.parseInt(strType, 16)) {
                    case 1: strType = strType + " (BYTE)"; break;
                    case 2: strType = strType + " (ASCII)"; break;
                    case 3: strType = strType + " (SHORT)"; break;
                    case 4: strType = strType + " (LONG)"; break;
                    case 5: strType = strType + " (RATIONAL)"; break;
                    case 6: strType = strType + " (SBYTE)"; break;
                    case 7: strType = strType + " (UNDEFINE)"; break;
                    case 8: strType = strType + " (SSHORT)"; break;
                    case 9: strType = strType + " (SLONG)"; break;
                    case 10: strType = strType + " (SRATIONAL)"; break;
                    case 11: strType = strType + " (FLOAT)"; break;
                    case 12: strType = strType + " (DOUBLE)"; break;
                    default : strType = strType; break;
                }

                // process de length & value
                int length = Integer.parseInt(arrDE[i][2], 16);
                int value = Integer.parseInt(arrDE[i][3], 16);


                myOutputFile.format("|%-40s|%-15s|%10d|%10d|\n", strTag, strType, length, value);
            } //--- end looping DE (end output DE) ---//

            // output image data
            myOutputFile.println(prefixSuffix("Image Data", "-", "", 80));
            myOutputFile.println();

            int rowItem = 0; // each row should contain 16 bytes of data
            int countItem = 0; // more spacing every 8 bytes of data 
            int imageData;
            while((imageData = myInputFile.read()) != -1) {

                myOutputFile.printf("%02X ", imageData);
                rowItem++; countItem++;

                if(rowItem == 16) {
                        myOutputFile.println();
                        rowItem = 0;
                        countItem = 0;
                }else if(countItem == 8) {
                        myOutputFile.print("\t");
                        countItem = 0;
                }
            } //--- end while loop ---//
		    
    	}catch(IOException ex) {
            System.out.println("File error"); 
    	}
    	
    }//--- end save() ---//
    
    public static String prefixSuffix(String targetText, String prefixText, String suffixText, int length) {
    	if(prefixText.equals("")) {
            prefixText = " ";
    	}
    	if(suffixText.equals("")) {
    		suffixText = prefixText; 
    	}
    	int diff = length - targetText.length(); 
    	for(int i = 0; i < diff && targetText.length() < 80; i++){
            if(i % 2 == 0) {
                    targetText = targetText + suffixText;
            } else {
                    targetText = prefixText + targetText;
            }
        }//--- end for ---//
		
    	return targetText;
    } //--- end prefixSuffix() ---//
}