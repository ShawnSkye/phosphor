package org.exnode;

//imports
import org.json.*;
import java.io.*;

//end imports

public class exNode {
    JSONObject exnodeObj;
    JSONObject exnodeFile;
    
    public exNode() throws Exception{
	exnodeFile = new JSONObject();
	exnodeObj = new JSONObject();
	exnodeFile.put("exNode", exnodeObj);
    }

    //takes in a json exnode file
    public exNode(String filename) {
	try{
	    FileReader fr = new FileReader(filename);
	    JSONTokener jt = new JSONTokener(fr);
	    exnodeFile = new JSONObject(jt);
	    exnodeObj = new JSONObject(exnodeFile.get("exNode").toString());
	}
	catch(Exception e){
	   
	}
    }

    public exNode(File file){
	try{
	    FileReader fr = new FileReader(file.getName());
	    JSONTokener jt = new JSONTokener(fr);
	    exnodeFile = new JSONObject(jt);
	    exnodeObj = new JSONObject(exnodeFile.get("exNode").toString());
	}
	catch(Exception e){
	   
	}
    }
    

    public void exnodify(String filepath) throws Exception {
	//takes in a file and creates an exnode for it
	long size;
	String name;
	File dataFile = new File(filepath);
	if(dataFile.exists()){
	    size = dataFile.length();
	    name = dataFile.getName();
	    exnodeObj = new JSONObject();
	    exnodeObj.put("filename", name);
	    exnodeObj.put("size", size);
	    JSONArray mappingsArray = new JSONArray();
	    JSONObject mappingObject = new JSONObject();
	    mappingObject.put("address", "file://" + filepath);
	    mappingObject.put("extentStart", "0");
	    mappingObject.put("extentEnd", Long.toString(size));
	    mappingsArray.put(mappingObject);
	    exnodeObj.put("mappings", mappingsArray);
	    exnodeFile = new JSONObject();
	    exnodeFile.put("exNode", exnodeObj);
	}
	else { 
	    System.out.println("File does not exist");
	}
    }

    public void addMapping(String address, long extentStart, long extentEnd) throws Exception {
	if(extentEnd==-1) extentEnd = exnodeObj.getLong("size");
	JSONObject mappingObject = new JSONObject();
	if(!exnodeObj.has("mappings")){
	    JSONArray mappingsArray = new JSONArray();
	    exnodeObj.put("mappings", mappingsArray);
	}
	mappingObject.put("address", address);
	mappingObject.put("extentStart", extentStart);
	mappingObject.put("extentEnd", extentEnd);	
	exnodeObj.getJSONArray("mappings").put(mappingObject);
    }
    
    public void removeMapping(String address) throws Exception{
	if(exnodeObj.has("mappings")){
	    int len = exnodeObj.getJSONArray("mappings").length();	
	    for(int i=0; i<len; i++){
		if(exnodeObj.getJSONArray("mappings").getJSONObject(i).getString("address")==address){
		    exnodeObj.getJSONArray("mappings").remove(i);
		}
	    }
	}

    }




    // Methods for modifying existing mappings. must be referred to by address,
    // otherwise add a new mapping before modifying. If the key is found, the 
    // value will be changed. If it is not found, it will be added.
    /*public void modifyMapping(String address, String key, String value){

      }

      public void modifyMapping(String address, String key, long value){

      }

      public void modifyMapping(String address, String key, int value){

      }*/

    // The following functions will return the exNode as a String
    // The first will be nicely formatted with indents of int "indent" spaces
    public String writeExnode(int indent) throws Exception {
	return exnodeFile.toString(indent);
    }

    public String writeExnode() throws Exception {
	return exnodeFile.toString();
    }


    public String getFilename() throws Exception {
	return exnodeObj.getString("filename");
    }
	
    public void setFilename(String name) throws Exception{
	exnodeObj.put("filename", name);
    }

    public long getSize() throws Exception {
	return exnodeObj.getLong("size");
    }
    public void setSize(long size) throws Exception{
	exnodeObj.put("size", size);
    }



    // A validation function, first it will make sure that it is valid json
    // Then it will make sure that it follows the format for being an exNode... once we know what that is
    public boolean isExnode(){
	//todo
	if(exnodeFile!=null&&exnodeFile.has("exNode")) return true;
	else return false;
    }
    
}
