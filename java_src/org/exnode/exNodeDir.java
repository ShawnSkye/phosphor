package org.exnode;

import org.json.*;
import java.io.*;
import java.util.*;

public class exNodeDir {
    File exnodeDir;

    public exNodeDir(){
	
    }
    public exNodeDir(File dir){
	exnodeDir = dir;

    }
    public exNodeDir(String path){
	exnodeDir = new File(path);

    }

    /*private void buildLists(){
	File[] fileList = exnodeDir.listFiles();
	dirList = new ArrayList();
	exnodeList = new ArrayList();
	nameList = new ArrayList();
	for(int i=0; i<fileList.length; i++){
	    if(!fileList[i].isDirectory()){
		try{
		    exNode a = new exNode(fileList[i]);
		    if(a.isExnode()){
			nameList.add(exnodeList.size(), a.getFilename());
			exnodeList.add(a);
		    }
		}
		catch(Exception e){}
	    }
	    else{
		if(!fileList[i].getName().startsWith(".")){
		    dirList.add(new exNodeDir(fileList[i]));
		    nameList.add(fileList[i].getName());
		}
		
	    }
	}
	}*/

    public String getName(){
	return exnodeDir.getName();
    }
    public exNodeDir getParentFile(){
	return new exNodeDir(exnodeDir.getParentFile());
    }
    public ArrayList getNameList(){
	File[] fileList = exnodeDir.listFiles();
	ArrayList dirList = new ArrayList();
	ArrayList exnodeList = new ArrayList();
	ArrayList nameList = new ArrayList();
	for(int i=0; i<fileList.length; i++){
	    if(!fileList[i].isDirectory()){
		try{
		    exNode a = new exNode(fileList[i]);
		    if(a.isExnode()){
			nameList.add(exnodeList.size(), a.getFilename());
			exnodeList.add(a);
		    }
		}
		catch(Exception e){}
	    }
	    else{
		if(!fileList[i].getName().startsWith(".")){
		    nameList.add(fileList[i].getName());
		}
		
	    }
	}
	return nameList;
    }
    public ArrayList getExnodes(){
	File[] fileList = exnodeDir.listFiles();
	ArrayList exnodeList = new ArrayList();
	for(int i=0; i<fileList.length; i++){
	    if(!fileList[i].isDirectory()){
		try{
		    exNode a = new exNode(fileList[i]);
		    if(a.isExnode()){
			exnodeList.add(a);
		    }
		}
		catch(Exception e){}
	    }
	}
	return exnodeList;
    }

    public ArrayList getDirs(){
       	File[] fileList = exnodeDir.listFiles();
	ArrayList dirList = new ArrayList();
	for(int i=0; i<fileList.length; i++){
	    if(fileList[i].isDirectory()){
		if(!fileList[i].getName().startsWith(".")){
		    dirList.add(new exNodeDir(fileList[i]));
		}
	    }
	}
	return dirList;
    }

}
    