package com.so.server;

import java.io.*;
import java.util.ArrayList;

import com.so.structures.VirtualMemory;
import com.so.structures.FileSystem;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler extends IoHandlerAdapter {
    FileSystem system = null;
    boolean running = false;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        /*String str = message.toString();
        if( str.trim().equalsIgnoreCase("quit") ) {
            System.out.println("Close...");
            session.close();
            return;
        }

        Date date = new Date();
        session.write( date.toString() );
        session.write( "Hola\nMundo" );
        System.out.println("Message written...");*/


        String input = message.toString();
        String[] splitStr = input.split("\\s+");
        VirtualMemory[][] matriz = null;
        int sectorNumber = 0;
        int sectorSize = 0;
        File file = null;

        if ("EXIT".equals(splitStr[0])) {
            System.out.println("Close...");
            session.close(true);
            return;
        } else if ("CRT".equals(splitStr[0])) {
            //CRT [Root name:String] [Number of sectors:int] [Size of sectors:int]
            String rootName = splitStr[1];
            sectorNumber = Integer.parseInt(splitStr[2]);
            sectorSize = Integer.parseInt(splitStr[3]);
            system = new FileSystem(rootName, sectorNumber, sectorSize);
            running = true;

            matriz = new VirtualMemory[sectorNumber][sectorSize];
            String espacio = "";

            for (int filas = 0; filas < sectorNumber; filas++){ // Inicializo Matriz como Vacia
                for(int columnas = 0; columnas < sectorSize; columnas++){
                    VirtualMemory elementos = new VirtualMemory('-', "PosicionStringPadre", ".TXT"); // Objeto Para guardar datos
                    // 0: Caracter a guardar, 1: Posicion en el string padre, 2: Nombre del archivo padre
                    matriz[filas][columnas] = elementos;
                }
            }

            file = new File("memory.txt"); // Creo el Archivo
            PrintWriter pw = new PrintWriter(file);

            for(int cortes = 0; cortes < sectorSize; cortes++) { // Inicializo Filas Vacias para Archivo TXT
                espacio += " ";
            }

            for(int linea = 0; linea < sectorNumber; linea++){ // Inicializo Archivo TXT con filas y columnas vacias
                pw.println(espacio);
            }

            pw.close();
            PrintWriter pw2 = new PrintWriter(file);

            for (int filas = 0; filas < sectorNumber; filas++){ // Recorro la Matriz
                for(int columnas = 0; columnas < sectorSize; columnas++) {
                    pw2.print(matriz[filas][columnas].valor);
                }
                pw2.println("");
            }

            pw2.close();

            session.write("CRT Completed...");
        } else if ("FLE".equals(splitStr[0]) && running) {
            //FLE [File name:String] [overwrite:bool] [File contents:String begins with `{`]
            String fileName = splitStr[1];
            boolean overwrite = false;
            if (splitStr[2].equals("true")) {
                overwrite = true;
            }
            //contents
            String[] SepString = input.split("\\{");
            ArrayList<String> SepStringList = new ArrayList<String>();

            for (int i = 0; i < SepString.length; i++) {
                if (i != 0) {
                    SepStringList.add(SepString[i]);
                }
            }
            String joined = String.join("{", SepStringList);
            String fileContent= joined;
            session.write(system.createFile(fileName, overwrite, fileContent));
            matriz = VirtualMemory.verificarSectores(fileContent,sectorNumber,sectorSize,matriz);
            PrintWriter pw2 = new PrintWriter(file);

            for (int filas = 0; filas < sectorNumber; filas++){ // Muestro la Matriz
                for(int columnas = 0; columnas < sectorSize; columnas++){
                    System.out.print(matriz[filas][columnas].valor);
                }
                System.out.print("\n");
            }

            for (int filas = 0; filas < sectorNumber; filas++){ // Recorro la Matriz
                for(int columnas = 0; columnas < sectorSize; columnas++) {
                    pw2.print(matriz[filas][columnas].valor);
                }
                pw2.println("");
            }

            pw2.close();

        } else if ("MKDIR".equals(splitStr[0]) && running) {
            //MKDIR [Directory name:String] [overwrite:bool]
            String dirName = splitStr[1];
            boolean overwrite = false;
            if (splitStr[2].equals("true")) {
                overwrite = true;
            }
            session.write(system.createDir(dirName, overwrite));
        } else if ("CHDIR".equals(splitStr[0]) && running) {
            //CHDIR [Path:String]
            String path = splitStr[1];
            session.write(system.changeDir(path));
        } else if ("LDIR".equals(splitStr[0]) && running) {
            //LDIR
            session.write(system.listDir());
        } else if ("MFLE".equals(splitStr[0]) && running) {
            //MFLE [File name:String] [File contents:String begins with `{`]
            String fileName = splitStr[1];
            //contents
            String[] SepString = input.split("\\{");
            ArrayList<String> SepStringList = new ArrayList<String>();

            for (int i = 0; i < SepString.length; i++) {
                if (i != 0) {
                    SepStringList.add(SepString[i]);
                }
            }
            String joined = String.join("{", SepStringList);
            String fileContent= joined;
            session.write(system.modifyFileContent(fileName, fileContent));
        } else if ("PPT".equals(splitStr[0]) && running) {
            //PPT [File name:String]
            String fileName = splitStr[1];
            session.write(system.showFileProperties(fileName));
        } else if ("VIEW".equals(splitStr[0]) && running) {
            //View [File name:String]
            String fileName = splitStr[1];
            session.write(system.viewFileContents(fileName));
        } else if ("CPY".equals(splitStr[0]) && running) {
            /*CPY [Option(1,2,3):int] [File name (or dir):String] [Path:String]
            1. Real to virtual file
            2. Virtual to real file
            3. Virtual to virtual file or directory*/
            int option = Integer.parseInt(splitStr[1]);
            String fileName = splitStr[2];
            String path = splitStr[3];
            if (option == 1) {
                session.write(system.copyFile(fileName, path, 1));
            } else if (option == 2) {
                session.write(system.copyFile(fileName, path, 2));
            } else if (option == 3) {
                session.write(system.copyFile(fileName, path, 3));
            } else {
                session.write("Wrong option");
            }

        } else if ("MOV".equals(splitStr[0]) && running) {
            //MOV [File name:String] [Rename:bool=true] [New file name:String] [Path:String]
            //MOV [File name:String] [Rename:bool=false] [Path:String]
            String fileName = splitStr[1];
            String newName = fileName;
            String path;
            if (splitStr[2].equals("true")) {
                newName = splitStr[3];
                path = splitStr[4];
            } else {
                path = splitStr[3];
            }
            session.write(system.moveElement(fileName, newName, path));
        } else if ("REM".equals(splitStr[0]) && running) {
            session.write("In development");
        } else if ("TREE".equals(splitStr[0]) && running) {
            session.write(system.tree());
        } else if ("".equals(splitStr[0])) {
            //Do nothing
        } else {
            session.write("Wrong command");
        }

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }
}