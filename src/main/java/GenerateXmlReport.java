package chubatova;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.nio.file.*;

public class GenerateXmlReport {


    public static void main(String[] args) {

        new Thread("UnitTestsThread"){
            public void run(){


                System.out.println("Thread: " + Thread.currentThread().getName() + " running");
                unzip("./UnitTestResult.zip", ".");

                try{
                    Path newFileUnitTest = Paths.get("UnitTestResult2.xml");
                    Files.createFile(newFileUnitTest);

                } catch (IOException ex) {
                    //exception
                }


                try {
                    Scanner scanner1 = new Scanner(new File("UnitTestResult.xml"));
                    //Scanner scanner2 = new Scanner(new File("IntTestResult.xml"));

                    while (scanner1.hasNextLine()) {
                        String contentToAppend = scanner1.nextLine();
                        System.out.println("Writing a new line to UnitTestResult2.xml...");
                        try {
                            Files.write(
                                    Paths.get("./UnitTestResult2.xml"),
                                    contentToAppend.getBytes(),
                                    StandardOpenOption.APPEND);
                        } catch (IOException ex) {
                            //exception
                        }


                    }

                    scanner1.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



            }
        }.start();

        new Thread("(IntTestsThread"){
            public void run(){


                System.out.println("Thread: " + Thread.currentThread().getName() + " running");
                unzip("./IntTestResult.zip", ".");



            }
        }.start();





    }


    //UNZIP//
    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

