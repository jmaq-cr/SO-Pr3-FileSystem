package com.so.structures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jose Aguilar Quesada on 6/16/2017.
 */
public class File {

    private String name;
    private String extension;
    Date creationDate;
    private Date modificationDate;
    private int size;
    private String content;
    private ArrayList<Integer> linesInMemory;

    public File(String pName, String pExtension, String pContent){
        this.name = pName;
        this.extension = pExtension;
        this.size = 0;
        this.content = pContent;
        this.creationDate = new Date();
        this.modificationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName(){
        return name+"."+extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate() {
        this.modificationDate = new Date();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Integer> getLinesInMemory() {
        return linesInMemory;
    }

    public void setLinesInMemory(ArrayList<Integer> linesInMemory) {
        this.linesInMemory = linesInMemory;
    }

    public String toString(){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy - HH:mm:SS");
        String dateCreation = DATE_FORMAT.format(creationDate);
        String dateModification = DATE_FORMAT.format(modificationDate);
        String result = name+"."+extension+" size: "+ size + " creation date: "+dateCreation+" modification date: "+ dateModification;
        return result;
    }
}
