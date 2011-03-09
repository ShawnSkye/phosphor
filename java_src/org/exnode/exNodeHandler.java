package org.exnode;

import java.io.*;
import java.util.*;
import org.globus.ftp.*;
import org.json.*;

public class exNodeHandler{
    ArrayList servers;
    public exNodeHandler(){

    }
    public void replicate(exNode a, String server){

    }
    public void download(exNode a, File localFileLocation)throws Exception{
	JSONArray mappings = a.getMappings();
	int i = 0;
	String server;
	int port;
	while(!mappings.isNull(i)){
	    JSONObject jo = mappings.getJSONObject(i);
	    if(jo.getInt("extentStart")==0&&jo.getInt("extentEnd")==a.getSize()){
		port = jo.getInt("port");
		server = jo.getString("address");
		if(server.startsWith("ftp://")){
		    String username = jo.getString("username");
		    String password = jo.getString("password");
		    server=server.substring(6);
		    String path = server.substring(server.indexOf(":")+1);
		    server=server.substring(0,server.indexOf(":"));
		    FTPClient f = new FTPClient(server, port);
		    f.authorize(username, password);
		    f.setPassive();
		    f.setLocalActive();
		    f.get(path, localFileLocation);
		}
	    }
	    i++;
	}
    }
    public void download(exNode a, String localFileLocation){

    }
    
}