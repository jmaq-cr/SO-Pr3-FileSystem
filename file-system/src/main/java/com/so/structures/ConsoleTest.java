package com.so.structures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Jose Aguilar Quesada on 6/17/2017.
 */
public class ConsoleTest {
    public static void main(String [] args)
    {
        FileSystem system = null;
        boolean running = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                System.out.print(">> ");
                String input = br.readLine();

                if ("EXIT".equals(input)) {
                    System.out.println("Bye!");
                    System.exit(0);
                }else if("CRT".equals(input)){
                    System.out.print("Root name ->");
                    String rootName = br.readLine();
                    System.out.print("Number of sectors ->");
                    int sectorNumber = Integer.parseInt(br.readLine());
                    System.out.print("Size of sectors ->");
                    int sectorSize = Integer.parseInt(br.readLine());
                    system = new FileSystem(rootName,sectorNumber,sectorSize);
                    running = true;
                }else if("FLE".equals(input) && running){
                    System.out.print("File name ->");
                    String fileName = br.readLine();
                    System.out.print("Overwrite Y/N ->");
                    boolean overwrite = false;
                    if(br.readLine().equals("Y")){
                        overwrite = true;
                    }
                    System.out.print("File contents ->");
                    String fileContent = br.readLine();
                    System.out.println(system.createFile(fileName,overwrite,fileContent));
                }else if("MKDIR".equals(input) && running){
                    System.out.print("Directory name ->");
                    String dirName = br.readLine();
                    System.out.print("Overwrite Y/N ->");
                    boolean overwrite = false;
                    if(br.readLine().equals("Y")){
                        overwrite = true;
                    }
                    System.out.println(system.createDir(dirName,overwrite));
                }else if("CHDIR".equals(input) && running){
                    System.out.print("Path ->");
                    String path = br.readLine();
                    System.out.println(system.changeDir(path));
                }else if("LDIR".equals(input) && running){
                    System.out.println(system.listDir());
                }else if("MFLE".equals(input) && running){
                    System.out.print("File name ->");
                    String fileName = br.readLine();
                    System.out.print("File contents ->");
                    String fileContent = br.readLine();
                    System.out.println(system.modifyFileContent(fileName,fileContent));
                }else if("PPT".equals(input) && running){
                    System.out.print("File name ->");
                    String fileName = br.readLine();
                    System.out.print(system.showFileProperties(fileName));
                }else if("VIEW".equals(input) && running){
                    System.out.print("File name ->");
                    String fileName = br.readLine();
                    System.out.println(system.viewFileContents(fileName));
                }else if("CPY".equals(input) && running){
                    System.out.println("In development");
                }else if("MOV".equals(input) && running){
                    System.out.print("File name ->");
                    String fileName = br.readLine();
                    System.out.print("Rename Y/N ->");
                    String newName = fileName;
                    if(br.readLine().equals("Y")){
                        System.out.print("New file name ->");
                        newName = br.readLine();
                    }
                    System.out.print("Path ->");
                    String path = br.readLine();
                    System.out.println(system.moveElement(fileName,newName,path));
                }else if("REM".equals(input) && running){
                    System.out.println("In development");
                }else if("TREE".equals(input) && running){
                    System.out.println(system.tree());
                }else{
                    System.out.println("Wrong command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
