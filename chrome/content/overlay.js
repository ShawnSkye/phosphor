function loadPhosphor() {
    var theTab;
    var tablabel;
    var prefManager = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefBranch);
    var exnodePath = prefManager.getCharPref("extensions.phosphor.exnodeDir");
    var remoteExnodeDir = prefManager.getCharPref("extensions.phosphor.remoteExnodeDir");
    var remoteExnodeDirPort = prefManager.getIntPref("extensions.phosphor.remoteExnodeDirPort");
    var defDLdir = prefManager.getCharPref("extensions.phosphor.defDLdir");
    
    if(exnodePath==""){

	exnodePath = prompt("You appear to be a new user! Please enter the full path of a directory containing exnodes, or the directory in which you wish to store exNodes", "");
	if(exnodePath!=""){
	    prefManager.setCharPref("extensions.phosphor.exnodeDir", exnodePath);
	}
	else {
	    return;
	}
    }
    if(remoteExnodeDir == ""){

	remoteExnodeDir = prompt("If you wish to import exNodes from a remote location, please enter the full URL of the remote Directory:", "ftp://yourserver.domain.com:/home/use/exnodeDir/");
	if(remoteExnodeDir != ""){
	    remoteExnodeDirPort = prompt("Enter port on which to connect to this server", "21");
	    prefManager.setCharPref("extensions.phosphor.remoteExnodeDir", remoteExnodeDir);
	    if(remoteExnodeDirPort>=0&&remoteExnodeDirPort<65536){
		prefManager.setIntPref("extensions.phosphor.remoteExnodeDirPort", remoteExnodeDirPort);
	    }
	    else{
		alert("Port " + port + " not a valid port number.");
	    }
	}
    }

    var sep = getSep();
    var filesPath = "file://" + getPhosphorPath() + "chrome" + sep + "content" + sep + "files.html";
    theTab = gBrowser.addTab(filesPath);
    tablabel = "Phosphor - Files";
    gBrowser.selectedTab = theTab;
    
    // You have to wait to set the icon and label until the page finishes loading
    theTabBrowser = gBrowser.getBrowserForTab(theTab);
    theTabBrowser.addEventListener("load", function () {
				       theTab.label = tablabel;
				       gBrowser.setIcon(theTab, "chrome://phosphor/content/DAMSL-phosphor-clear.png");
				       theTabBrowser.contentDocument.getElementById("exnodepath").setAttribute("value", exnodePath);
				       theTabBrowser.contentDocument.getElementById("remoteExnodeDir").setAttribute("value", remoteExnodeDir);
				       theTabBrowser.contentDocument.getElementById("remoteExnodeDirPort").setAttribute("value", remoteExnodeDirPort);
				       theTabBrowser.contentDocument.getElementById("defDLdir").setAttribute("value", defDLdir);
				       theTabBrowser.contentDocument.getElementById("sep").setAttribute("value", sep);
				   }, true);
    
}

/*************HELPER FUNCTIONS*************/

function getSep() {
    var sep;
    var profDir = Components.classes["@mozilla.org/file/directory_service;1"].getService(Components.interfaces.nsIProperties).get("ProfD", Components.interfaces.nsILocalFile);
    if(profDir.path.indexOf("C:") == 0) {
        sep = "\\";
    }
    else  {
        //mac or linux
        sep = "/";
    }
    return sep;
}

function getOSType(){
    var OStype;
    var profDir = Components.classes["@mozilla.org/file/directory_service;1"].getService(Components.interfaces.nsIProperties).get("ProfD", Components.interfaces.nsILocalFile);
    if(profDir.path.indexOf("C:") == 0) {
	OStype = "windows";
    }
    else if(profDir.path.indexOf("/home") == 0) {
	OStype = "linux";
    }
    else if(profDir.path.indexOf("/Users") == 0) {
        OStype = "mac";
    }
    return OStype;
}

function getPhosphorPath() {
    var phosphorPath;
    var profDir = Components.classes["@mozilla.org/file/directory_service;1"].getService(Components.interfaces.nsIProperties).get("ProfD", Components.interfaces.nsILocalFile);
    if(profDir.path.indexOf("C:") == 0) {
        phosphorPath = profDir.path + "\\extensions\\jaffee@udel.edu\\";
    }
    else  {
	//mac or linux
        phosphorPath = profDir.path + "/extensions/jaffee@udel.edu/";
    }
    return phosphorPath;
}
