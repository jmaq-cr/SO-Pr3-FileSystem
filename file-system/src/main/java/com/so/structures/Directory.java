package com.so.structures;

import java.util.ArrayList;

/**
 * Created by Jose Aguilar Quesada on 6/16/2017.
 */
public class Directory {
    private String name;
    private Directory parentDirectory;
    private int level;
    ArrayList<Directory> childDirectories;
    ArrayList<File> files;

    public Directory(String pName, Directory pParentDirectory){
        this.setName(pName);
        this.parentDirectory = pParentDirectory;
        this.childDirectories = new ArrayList<Directory>();
        this.files = new ArrayList<File>();
        this.setLevel(this.parentDirectory.getLevel()+1);
    }

    public Directory(String pName){
        this.setName(pName);
        this.parentDirectory = null;
        this.childDirectories = new ArrayList<Directory>();
        this.files = new ArrayList<File>();
        this.setLevel(0);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory getParentDirectory() {
        return parentDirectory;
    }

    public void setParentDirectory(Directory parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    private int findFile(String fileName){
        for(int i = 0; i<this.files.size(); i++){
            File currentFile = this.files.get(i);
            if((currentFile.getName()+"."+currentFile.getExtension()).equals(fileName)){
                return i;
            }
        }
        return -1;
    }

    private int findDirectory(String dirName){
        for(int i = 0; i<this.childDirectories.size(); i++){
            Directory currentDir = this.childDirectories.get(i);
            if(currentDir.getName().equals(dirName)){
                return i;
            }
        }
        return -1;
    }

    public File getFile(String fileName){
        int index = findFile(fileName);
        if(index != -1){
            return this.files.get(index);
        }
        else{
            return null;
        }
    }

    public Directory getDirectory(String dirName){
        int index = findDirectory(dirName);
        if(index != -1){
            return this.childDirectories.get(index);
        }
        else{
            return null;
        }
    }

    public int createFile(String fileName, boolean replace, String pContent){
        String name = getFileName(fileName);
        String ext = getExtension(fileName);
        if(findFile(fileName) != -1 && replace == false){
            return -1;
        }
        else if(findFile(fileName) != -1 && replace == true){
            if (name != "" && ext != "") {
                this.files.set(findFile(fileName),new File(name, ext,pContent));
                return 1;
            } else {
                return -1;
            }
        }
        else {
            if (name != "" && ext != "") {
                this.files.add(new File(name,ext,pContent));
                return 1;
            } else {
                return -1;
            }
        }    
    }

    public String getExtension(String fileName){
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    public String getFileName(String fileName){
        String name = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            name = fileName.substring(0,i);
        }
        return name;
    }

    public int createDir(String dirName, boolean replace){
        if(findDirectory(dirName) != -1 && replace == false){
            return -1;
        }
        else if(findDirectory(dirName) != -1 && replace == true){
            this.childDirectories.set(findDirectory(dirName), new Directory(dirName,this));
            return 1;
        }
        else{
            this.childDirectories.add(new Directory(dirName,this));
            return 1;
        }
    }

    private String indenter(int pLevel){
        String result = "";
        for(int i = 0; i<pLevel; i++){
            result = result + "\t";
        }
        return result;
    }

    public String listDir(){
        String result = getName();
        for(int i = 0; i<childDirectories.size(); i++){
            result += "\n\tDir->" + childDirectories.get(i).getName();
        }
        for(int i = 0; i<files.size(); i++){
            result += "\n\tFile->" + files.get(i).getFullName();
        }
        return result;
    }

    public String fileProperties(String fileName){
        int index = findFile(fileName);
        if(index != -1){
            return files.get(index).toString();
        }
        else{
            return "Not Found";
        }
    }

    public int modifyFileContent(String fileName, String content){
        int index = findFile(fileName);
        if(index != -1){
            files.get(index).setContent(content);
            return 1;
        }
        else{
            return -1;
        }
    }

    public String viewFileContent(String fileName){
        int index = findFile(fileName);
        if(index != -1){
            return files.get(index).getContent();
        }
        else{
            return "Not found";
        }
    }

    public int removeFile(String fileName){
        int index = findFile(fileName);
        if(index != -1){
            files.remove(index);
            return 1;
        }
        else{
            return -1;
        }
    }

    public int removeDir(String dirName){
        int index = findDirectory(dirName);
        if(index != -1){
            childDirectories.remove(index);
            return 1;
        }
        else{
            return -1;
        }
    }

    public String treePrint(){
        String result = "\n";
        String mainIndent = indenter(level);
        String childIndent = indenter(level+1);
        result += mainIndent + getName();
        for(int i = 0; i<files.size(); i++){
            result += "\n"+childIndent+files.get(i).getFullName();
        }
        for(int i = 0; i<childDirectories.size(); i++){
            result += childDirectories.get(i).treePrint();
        }
        return result;

    }

    public void addFile(File file){
        files.add(file);
    }

    public void addDir(Directory dir){
        childDirectories.add(dir);
    }

}
