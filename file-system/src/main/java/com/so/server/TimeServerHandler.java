package com.so.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import com.so.structures.FileSystem;
import org.apache.mina.core.buffer.IoBuffer;
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

        if ("EXIT".equals(splitStr[0])) {
            System.out.println("Close...");
            session.close(true);
            return;
        } else if ("CRT".equals(splitStr[0])) {
            //CRT [Root name:String] [Number of sectors:int] [Size of sectors:int]
            String rootName = splitStr[1];
            int sectorNumber = Integer.parseInt(splitStr[2]);
            int sectorSize = Integer.parseInt(splitStr[3]);
            system = new FileSystem(rootName, sectorNumber, sectorSize);
            running = true;
            session.write("CRT Completed...");
        } else if ("FLE".equals(splitStr[0]) && running) {
            //FLE [File name:String] [overwrite:bool] [File contents:String]
            String fileName = splitStr[1];
            boolean overwrite = false;
            if (splitStr[2].equals("true")) {
                overwrite = true;
            }
            String fileContent = splitStr[3];
            session.write(system.createFile(fileName, overwrite, fileContent));
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
            //MFLE [File name:String] [File contents: String]
            String fileName = splitStr[1];
            String fileContent = splitStr[2];
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
            }else{
                path = splitStr[3];
            }
            session.write(system.moveElement(fileName, newName, path));
        } else if ("REM".equals(splitStr[0]) && running) {
            session.write("In development");
        } else if ("TREE".equals(splitStr[0]) && running) {
            session.write(system.tree());
        } else if ("".equals(splitStr[0])) {
            //Do nothing
        } else{
            session.write("Wrong command");
        }

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }
}