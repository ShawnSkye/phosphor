package phosphor;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import org.exnode.*;
import java.lang.Math;
import java.net.*;
import org.globus.ftp.FTPClient;

public class phosphor extends Applet implements MouseListener, MouseMotionListener{
    Image offscreen;
    Graphics bG;
    MediaTracker mt;
    URL base1;
    int xpos; //mouse x coordinate
    int ypos; //mouse y coordinate
    int xClick;
    int yClick;
    File[] list;
    String downloadDir;
    ArrayList exnodeList;
    int exnodeListSize;
    ArrayList dirList;
    int dirListSize;
    ArrayList nameList;
    String exnodeRootPath;
    exNodeDir exnodeRootDir;
    exNodeDir curDir;
    int iconDim = 50;
    int hSpacing = 30;
    int vSpacing = 30;
    Image fileImg;
    Image folderImg;
    Image homeImg;
    Image backImg;
    int appWidth;
    int appHeight;
    int iconPop;
    int fontSize;
    String sep;


    public void init()
    {
	Dimension app = this.getSize();
	appWidth = app.width;
	appHeight = app.height;
	offscreen = createImage(appWidth, appHeight);
	bG = offscreen.getGraphics();
	mt = new MediaTracker(this);
	try {
	    base1 = getDocumentBase();
	}
	catch(Exception e){}
	fileImg = getImage(base1, "file_icon_green(nonfree).png");
	folderImg = getImage(base1, "folder.png");
	homeImg = getImage(base1, "homesq.png");
	backImg = getImage(base1, "back-button.png");
	mt.addImage(fileImg, 1);
	mt.addImage(folderImg, 2);
	mt.addImage(homeImg, 3);
	mt.addImage(backImg, 4);

	sep = getParameter("sep");//get the separator for this OS which was found out earlier in javascript

	String remotePath = getParameter("remoteExnodeDir");//full URI of remote directory containing exnodes
	exnodeRootPath = getParameter("exnodepath");//local folder for exnode storage
	
	if(remotePath!=""){
	    int port = Integer.parseInt(getParameter("remoteExnodeDirPort"));
	    try{
		downloadExnodes(remotePath, port, exnodeRootPath);
	    }
	    catch(Exception e){e.printStackTrace();}
	}

      	downloadDir = getParameter("defDLdir");

	exnodeRootDir = new exNodeDir(exnodeRootPath);
	getExnodes(exnodeRootDir);
	resetDimensions();
	
	iconPop = iconDim/10;//this could be a parameter one day (how much the icons expand on mouseover)
	fontSize = 12;       //this too
	
	try{
	    mt.waitForAll();
	}
	catch(Exception e){}
	addMouseMotionListener(this);
	addMouseListener(this);
    }



    public void getExnodes(exNodeDir thisDir){
	//populates the exnodeList and dirList
	try{
	    curDir = thisDir;
	    exnodeList = curDir.getExnodes();
	    dirList = curDir.getDirs();
	    nameList = curDir.getNameList();
	    
	    exnodeListSize = exnodeList.size();
	    dirListSize = dirList.size();
	}
	catch(Exception e){System.out.println("Exception in getExnodes()  " + e.getMessage());}
    }

    public void downloadExnodes(String remotePath, int port, String path)throws Exception{
	if(remotePath.startsWith("ftp://")){
	    remotePath = remotePath.substring(6); //cut off the "ftp://"
	    String server = remotePath.substring(0, remotePath.indexOf(":"));
	    System.out.println("remote path = " + remotePath.substring(0, remotePath.indexOf(":")));
	    FTPClient f = new FTPClient(server, port);
	    f.authorize("globus", "globusgridftp");
	    String remoteDir = remotePath.substring(remotePath.indexOf(":")+1);
	    f.changeDir(remoteDir);
	    cpdashrRemote(f, path);
	}
    }

    /*Recursive function to "cp -r" the remote directory to a local one
     */
    public void cpdashrRemote(FTPClient f, String localPath)throws Exception{
	String curDirName = f.getCurrentDir().substring(f.getCurrentDir().lastIndexOf(sep)+1);
	File b = new File(localPath+sep+curDirName);
	b.mkdir();

	Vector v = new Vector();//holds results of remote directory listing
	try{
	    v = f.list();
	}
	catch(Exception e){System.out.println("f.list() didnt work");}
	if(v.isEmpty()){
	    try{v = f.mlsd();}
	    catch(Exception e){System.out.println("f.mlsd() didnt work");}
	}
	FTPListing fl = new FTPListing(v); //FTPListing is an object to abstract away differences in listing schemes

	for(int i=0; i<v.size(); i++){
	    String name = fl.getName(i);
	    if(fl.isDirectory(i)){
		f.changeDir(name);
		cpdashrRemote(f, b.getAbsolutePath());//recursively call in each directory within the initial directory
		f.goUpDir();
	    }
	    else {
		File a = new File(b.getAbsolutePath()+sep+name);
		System.out.println("b.getAbsolutePath()+name = "+b.getAbsolutePath()+sep+name);
		if(!a.createNewFile()){
		    a.delete();
		    a.createNewFile();
		}
		f.setPassive();
		f.setLocalActive();
		f.get(name, a);
	    }
	    
	}
    }
		

    public void resetDimensions(){
	int numThings = exnodeList.size() + dirList.size() + 2;
	int numPerLine = (appWidth-hSpacing)/(iconDim+hSpacing);
	int numRows = numThings/numPerLine;
	if(numThings%numPerLine>0) numRows++;
	int vertPixelsNeeded = numRows*(iconDim+vSpacing)+vSpacing;
	System.out.println("numThings " + numThings + ", numPerLine " + numPerLine
			   + ", numRows " + numRows + ", vertPixelsNeeded " + vertPixelsNeeded +
			   " (appHeight,appWidth) (" + appHeight + "," + appWidth + ")");
	if(vertPixelsNeeded>appHeight){
	    System.out.println("RESIZE CALLLLED");
	    resize(appWidth, vertPixelsNeeded);
	    appHeight=vertPixelsNeeded;
	    offscreen = createImage(appWidth, appHeight);
	}
    }

    public void paint(Graphics g) {
	int j = 0;
	String name = new String();
	try{
	    bG.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	    bG.clearRect(0,0,appWidth,appHeight);
	    int x = hSpacing;
	    int y = vSpacing;
	    for(int i=-2; i<exnodeListSize; i++){
		if(x>appWidth-(iconDim+hSpacing)){
		    x=hSpacing;
		    y+=iconDim+vSpacing;
		}
		if(xpos>x&&xpos<x+iconDim&&ypos>y&&ypos<y+iconDim){
		    if(i==-2)
			bG.drawImage(homeImg,x-iconPop/2,y-iconPop/2,iconDim+iconPop,iconDim+iconPop,this);
		    else if(i==-1)
			bG.drawImage(backImg,x-iconPop/2,y-iconPop/2,iconDim+iconPop,iconDim+iconPop,this);
		    else
			bG.drawImage(fileImg,x-iconPop/2,y-iconPop/2,iconDim+iconPop,iconDim+iconPop,this);
	
		}
		else {
		    if(i==-2)
			bG.drawImage(homeImg,x,y,iconDim,iconDim,this);
		    else if(i==-1)
			bG.drawImage(backImg,x,y,iconDim,iconDim,this);
		    else
			bG.drawImage(fileImg,x,y,iconDim,iconDim,this);
		}
		if(i>=0){
		    name = trunc((String)nameList.get(j));
		    j++;
		    bG.drawString(name,x-hSpacing/4,y+iconDim+vSpacing/2);
		}
		x+=iconDim+hSpacing;
	       
	    }
	    for(int i=0; i<dirListSize; i++){
		if(x>=appWidth-(iconDim+hSpacing)){
		    x=hSpacing;
		    y+=iconDim+vSpacing;
		}
		if(xpos>x&&xpos<x+iconDim&&ypos>y&&ypos<y+iconDim){
		    bG.drawImage(folderImg,x-iconPop/2,y-iconPop/2,iconDim+iconPop,iconDim+iconPop,this);
		}
		else{
		    bG.drawImage(folderImg,x,y,iconDim,iconDim,this);
		}
		name = trunc((String)nameList.get(j));
		j++;
		bG.drawString(name,x-hSpacing/4,y+iconDim+vSpacing/2);
		x+=iconDim+hSpacing; 
	    }
	}
	catch(Exception e) {System.out.println("Exception in paint()  " + e.getMessage());}
	g.drawImage(offscreen,0,0,this);
    }
    
    public void update(Graphics g){
	paint(g);
    }

    public void mouseMoved(MouseEvent me){
	xpos = me.getX();
	ypos = me.getY();
	repaint();
    }
    public void mouseDragged(MouseEvent me){ }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){
	xClick = e.getX();
	yClick = e.getY();
	if(e.getClickCount() == 2){
	    checkClickCoords();
	}
    }
    
    public void checkClickCoords(){
	try{
	    int x = hSpacing;
	    int y = vSpacing;
	    for(int i=-2; i<exnodeListSize; i++){
		if(x>appWidth-(iconDim+hSpacing)){
		    x=hSpacing;
		    y+=iconDim+vSpacing;
		}
		if(xClick>x&&xClick<x+iconDim&&yClick>y&&yClick<y+iconDim){
		    if(i>=0){
			exNode a = new exNode();
			a = (exNode)exnodeList.get(i);
			System.out.println(a.writeExnode(2));
			exNodeHandler eh = new exNodeHandler();
			
			File targetFile = new File(downloadDir + sep + a.getFilename());
			targetFile.createNewFile();
			eh.download(a, targetFile);
		    }
		    else if(i==-2){
			getExnodes(exnodeRootDir);
			resetDimensions();
		    }
		    else if(i==-1){
			getExnodes(curDir.getParentFile());
			resetDimensions();
		    }
		}
		x+=iconDim+hSpacing;
	    }
	    for(int i=0; i<dirListSize; i++){
		if(x>=appWidth-(iconDim+hSpacing)){
		    x=hSpacing;
		    y+=iconDim+vSpacing;
		}
		if(xClick>x&&xClick<x+iconDim&&yClick>y&&yClick<y+iconDim){
		    getExnodes((exNodeDir)dirList.get(i));
		    resetDimensions();
		}
		x+=iconDim+hSpacing; 
	    }
	}
	catch(Exception e){System.out.println("Exception in checkClickCoords()  "); e.printStackTrace();}
    }



    public String trunc(String s){
	//just a quick hack so that words aren't running over eachother
	//this needs to be fixed so that it takes the iconDim, spacing, and font into account
	
	//int spacAvail = iconDim + hSpacing/2;
	String ans = new String();
	if(s.length()>9){
	    ans = s.substring(0,6) + "...";
	    return ans;
	}
	else if(s.length()<9){
	    ans = s;
	    while(ans.length()<9){
		if(ans.length()%2==0) ans = " " + ans;
		else ans = ans + " ";
	    }
	    return ans;
	}
	else{
	    return s;
	}
    }


}
