
import java.io.*;


public class WriteFile {
    public static void main(String[] args) {
        try{
            FileOutputStream myOutputFile = new FileOutputStream("myFile.txt"); 
            myOutputFile.write(1);
            myOutputFile.write(2);
            myOutputFile.write('T');
            myOutputFile.write('h');
            myOutputFile.write('i');
            myOutputFile.write('s');

            myOutputFile.close();
        } catch(IOException ex) {
            System.out.println("File output error");
        }
    }
}
