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
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                System.out.print(">> ");
                String input = br.readLine();

                if ("EXIT".equals(input)) {
                    System.out.println("Bye!");
                    System.exit(0);
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

        /*FileSystem system = new FileSystem("root",20,20);
        system.createFile("test0.txt",false,"El gato negro");
        system.createFile("test1.txt",false,"El gato negro");
        system.createFile("test2.txt",false,"El gato negro");
        system.createDir("child1", false);
        system.createDir("child2", false);
        System.out.println(system.showFileProperties("test0.txt"));
        System.out.println(system.modifyFileContent("test0.txt", "el perro blanco"));
        System.out.println(system.viewFileContents("test0.txt"));
        System.out.println(system.moveElement("test1.txt","moved.txt","root/child1"));
        System.out.println(system.listDir());
        System.out.println(system.changeDir("root/child1"));
        System.out.println(system.listDir());
        System.out.println();
        System.out.println();
        System.out.println(system.tree());*/
    }
}
