import java.io.*;

public class Patterning {
    public static void main(String[] args) {
        try{
            FileInputStream myInputFile = new FileInputStream("image/yoda.raw");
            
            int rawData;
            while((rawData = myInputFile.read()) != -1) {
                System.out.println(rawData);
            }//--- end while ---//
            
        }catch(IOException ex){
            System.out.println("File input error");
        }
    }//--- end main() ---//
}
