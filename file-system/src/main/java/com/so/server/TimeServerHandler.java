package com.so.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.so.structures.FileSystem;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler extends IoHandlerAdapter
{
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
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

        FileSystem system = null;
        boolean running = false;

                session.write(">> ");
                String input = message.toString().trim();

                if ("EXIT".equals(input)) {
                    session.write("Bye!");
                    session.close();
                    System.exit(0);
                }else if("CRT".equals(input)){
                    session.write("Root name ->");
                    String rootName = message.toString().trim();
                    session.write("Number of sectors ->");
                    int sectorNumber = Integer.parseInt(message.toString().trim());
                    session.write("Size of sectors ->");
                    int sectorSize = Integer.parseInt(message.toString().trim());
                    system = new FileSystem(rootName,sectorNumber,sectorSize);
                    running = true;
                }else if("FLE".equals(input) && running){
                    session.write("File name ->");
                    String fileName = message.toString().trim();
                    session.write("Overwrite Y/N ->");
                    boolean overwrite = false;
                    if(message.toString().trim().equals("Y")){
                        overwrite = true;
                    }
                    session.write("File contents ->");
                    String fileContent = message.toString().trim();
                    session.write(system.createFile(fileName,overwrite,fileContent));
                }else if("MKDIR".equals(input) && running){
                    session.write("Directory name ->");
                    String dirName = message.toString().trim();
                    session.write("Overwrite Y/N ->");
                    boolean overwrite = false;
                    if(message.toString().trim().equals("Y")){
                        overwrite = true;
                    }
                    session.write(system.createDir(dirName,overwrite));
                }else if("CHDIR".equals(input) && running){
                    session.write("Path ->");
                    String path = message.toString().trim();
                    session.write(system.changeDir(path));
                }else if("LDIR".equals(input) && running){
                    session.write(system.listDir());
                }else if("MFLE".equals(input) && running){
                    session.write("File name ->");
                    String fileName = message.toString().trim();
                    session.write("File contents ->");
                    String fileContent = message.toString().trim();
                    session.write(system.modifyFileContent(fileName,fileContent));
                }else if("PPT".equals(input) && running){
                    session.write("File name ->");
                    String fileName = message.toString().trim();
                    session.write(system.showFileProperties(fileName));
                }else if("VIEW".equals(input) && running){
                    session.write("File name ->");
                    String fileName = message.toString().trim();
                    session.write(system.viewFileContents(fileName));
                }else if("CPY".equals(input) && running){
                    session.write("1. Real to virtual file");
                    session.write("2. Virtual to real file");
                    session.write("3. Virtual to virtual file or directory");
                    session.write(">>");
                    int option = Integer.parseInt(message.toString().trim());
                    if(option == 1){
                        session.write("File name ->");
                        String fileName = message.toString().trim();
                        session.write("Real source path ->");
                        String path = message.toString().trim();
                        session.write(system.copyFile(fileName,path,1));
                    }else if(option == 2){
                        session.write("File name ->");
                        String fileName = message.toString().trim();
                        session.write("Real destiny path ->");
                        String path = message.toString().trim();
                        session.write(system.copyFile(fileName,path,2));
                    }else if(option == 3){
                        session.write("File or directory name ->");
                        String fileName = message.toString().trim();
                        session.write("Virtual destiny path ->");
                        String path = message.toString().trim();
                        session.write(system.copyFile(fileName,path,3));
                    }else{
                        session.write("Wrong option");
                    }

                }else if("MOV".equals(input) && running){
                    session.write("File name ->");
                    String fileName = message.toString().trim();
                    session.write("Rename Y/N ->");
                    String newName = fileName;
                    if(message.toString().trim().equals("Y")){
                        session.write("New file name ->");
                        newName = message.toString().trim();
                    }
                    session.write("Path ->");
                    String path = message.toString().trim();
                    session.write(system.moveElement(fileName,newName,path));
                }else if("REM".equals(input) && running){
                    session.write("In development");
                }else if("TREE".equals(input) && running){
                    session.write(system.tree());
                }else{
                    session.write("Wrong command");
                }

    }

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
        System.out.println( "IDLE " + session.getIdleCount( status ));
    }
}