package com.so.structures;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jose Aguilar Quesada on 6/17/2017.
 */
public class FileSystem {

    Directory rootDir;
    Directory currentDir;
    int numberSectors;
    int sectorSize;


    /*CRT: Este comando lo utilizaremos para crear un disco virtual. Los parámetros serán la cantidad de
    sectores, el tamaño del sector y un nombre que le identificará la raíz.*/
    public FileSystem(String rootName, int numberSectors, int sectorSize){
        this.rootDir = new Directory(rootName);
        this.currentDir = rootDir;
        this.numberSectors = numberSectors;
        this.sectorSize = sectorSize;
    }

    /*FLE: Crear un Archivo. Se le debe definir el contenido del archivo y el nombre y extensión de este*/
    public String createFile(String fileName, boolean replace, String pContent){
        int res = currentDir.createFile(fileName, replace, pContent);
        if(res == -1){
            return "Error in creating";
        }
        else{
            return "File created";
        }
    }

    /*MKDIR: Este comando crea un directorio en el directorio Actual. El parámetro es el nombre del
    directorio*/
    public String createDir(String dirName, boolean replace){
        int res = currentDir.createDir(dirName,replace);
        if(res == -1){
            return "Directory already exist";
        }
        else{
            return "Directory created";
        }
    }

    /*CHDIR: Permite cambiar el directorio actual. Me debe permitir irme a un directorio cualquiera de la
    estructura de directorios actual*/
    public String changeDir(String path){
        String[] directories = path.split("/");
        Directory tempDir;
        if(directories[0].equals(rootDir.getName())){
            tempDir = rootDir;
        }
        else if(directories[0].equals(currentDir.getName())){
            tempDir = currentDir;
        }
        else{
            return "Not found";
        }
        for(int i = 1; i<directories.length; i++){
            Directory search = tempDir.getDirectory(directories[i]);
            if(search!=null){
                tempDir = search;
            }
            else{
                return "Not found";
            }
        }
        currentDir = tempDir;
        return ("Current directory: "+path);
    }

    /*LDIR: Lista los archivos y directorios dentro del directorio actual. Debe mostrar una diferencia clara
    entre los directorios y archivos.*/
    public String listDir(){
        return currentDir.listDir();
    }

    /*MFLE: Se puede seleccionar un archivo y cambiarle el contenido*/
    public String modifyFileContent(String fileName, String content){
        int result = currentDir.modifyFileContent(fileName, content);
        if(result == -1){
            return "Not found";
        }
        else{
            return "File changed";
        }
    }

    /*PPT: Permite ver las propiedades de un archivo. Nombre, Extensión, Fecha de Creación, Fecha de
    Modificación y tamaño.*/
    public String showFileProperties(String fileName){
        return currentDir.fileProperties(fileName);
    }

    /*VIEW: Para un determinado archivo se debe poder ver el contenido del archivo.*/
    public String viewFileContents(String fileName){
        return fileName + " -> " + currentDir.viewFileContent(fileName);
    }

    /*MOV: Mover un archivo o directorio. Nótese que el MV sirve como rename, pues se puede mover al
    mismo directorio con otro nombre.*/
    public String moveElement(String name, String newName, String path){
        if(name.indexOf(".") != -1 && newName.indexOf(".") != -1){
            File movedFile = currentDir.getFile(name);
            Directory state = currentDir;
            changeDir(path);
            movedFile.setName(currentDir.getFileName(newName));
            currentDir.addFile(movedFile);
            currentDir = state;
            state.removeFile(newName);
            return "File moved";
        }
        else if(name.indexOf(".") == -1 && newName.indexOf(".") == -1){
            Directory movedDir = currentDir.getDirectory(name);
            String oldName = movedDir.getName();
            Directory state = currentDir;
            changeDir(path);
            movedDir.setName(newName);
            movedDir.setParentDirectory(currentDir);
            movedDir.setLevel(currentDir.getLevel()+1);
            currentDir.addDir(movedDir);
            currentDir = state;
            currentDir.removeDir(oldName);
            return "Directory moved";
        }
        else{
            return "Error";
        }
    }

    /*TREE: Despliega, simulando un árbol, la estructura de archivos del File System. Debe estar visible
    siempre.*/
    public String tree(){
        return rootDir.treePrint();
    }

    //fileName file in the virtual FileSystem
    //path place to get the real file, to write the virtual real file or paste the virtual file
    //Modes 1 - real a virtual
    //      2 - virtual a real
    //      3 - virtual a virtual
    public String copyFile(String fileName, String path, int mode) throws IOException {
        if(mode == 1){
            try {
                String content = readFile(path);
                return createFile(fileName,false,content);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Error in file copy";
            }
        }else if (mode == 2){
            String content = currentDir.getFile(fileName).getContent();
            writeFile(path,content);
            return "File copied";
        }else if (mode == 3){
            if(fileName.indexOf(".") != -1){
                File movedFile = currentDir.getFile(fileName);
                Directory state = currentDir;
                changeDir(path);
                if(currentDir.getFile(fileName) == null){
                    currentDir.addFile(movedFile);
                    currentDir = state;
                    return "File copied";
                }
                else{
                    currentDir = state;
                    return "File already exist";
                }

            }
            else if(fileName.indexOf(".") == -1){
                Directory movedDir = currentDir.getDirectory(fileName);
                Directory state = currentDir;
                changeDir(path);
                if(currentDir.getDirectory(fileName) == null){
                    movedDir.setParentDirectory(currentDir);
                    movedDir.setLevel(currentDir.getLevel()+1);
                    currentDir.addDir(movedDir);
                    currentDir = state;
                    return "Directory copied";
                }else{
                    currentDir = state;
                    return "Directory already exist";
                }
            }
            else{
                return "Error";
            }
        }else{
            return "Wrong choice";
        }

    }

    private String readFile(String path) throws FileNotFoundException {
        String content = new Scanner(new java.io.File(path)).useDelimiter("\\Z").next();
        return content;
    }

    private void writeFile(String path, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter( new FileWriter(path));
        writer.write(content);
        writer.close();
    }


}
