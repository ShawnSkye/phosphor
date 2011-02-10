package phosphor;

import java.util.*;
import org.globus.ftp.*;

public class FTPListing{
    Vector listVector;
    String listType;
    FileInfo fiElement;
    MlsxEntry mElement;
    public FTPListing(Vector v){
	listVector = v;
	if(!listVector.isEmpty()){
	    try{
		fiElement = (FileInfo)listVector.get(0);
		listType = "FileInfo";
	    }
	    catch(Exception e){
		System.out.println("its not a fileInfo");
		//e.printStackTrace();
	    }
	    try{
		mElement = (MlsxEntry)listVector.get(0);
		listType = "MlsxEntry";
	    }
	    catch(Exception d){
		System.out.println("its not an mlsx entry");
		//d.printStackTrace();
	    }
	}
	    
    }
    
    public boolean isDirectory(int index){
	if(listType=="MlsxEntry"){
	    MlsxEntry m = (MlsxEntry)listVector.get(index);
	    String type = m.get("type");
	    if(type=="dir"||type=="cdir"||type=="pdir"){
		return true;
	    }
	    else return false;
	}
     	else if(listType=="FileInfo"){
	    FileInfo fi = (FileInfo)listVector.get(index);
	    if(fi.isDirectory()) return true;
	    else return false;
	}
	else {
	    System.out.println("Issue in FTPListing !!!!!!!");
	    return false;
	}
    }

    public String getName(int index)throws Exception{
	String name = "";
	if(listType=="MlsxEntry"){
	    MlsxEntry m = (MlsxEntry)listVector.get(index);
	    name = m.getFileName();
	}
	else if(listType=="FileInfo"){
	    FileInfo fi = (FileInfo)listVector.get(index);
	    name = fi.getName();
	}
	return name;
    }
    
    public String getType(int index)throws Exception{
	String type = "";
	if(listType=="MlsxEntry"){
	    MlsxEntry m = (MlsxEntry)listVector.get(index);
	    type = m.get("type");
	}
	else if(listType=="FileInfo"){
	    FileInfo fi = (FileInfo)listVector.get(index);
	    if(fi.isDirectory()) type="dir";
	    else if(fi.isFile()) type="file";
	}
	return type;
    }

}