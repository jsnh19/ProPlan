package me.zero.tls.proplan.components;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import me.zero.tls.proplan.gui.view.MessageUtil;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;

public class Process extends ProcessXT implements Serializable{
    
    //Needed for Serializable
	private static final long serialVersionUID = -6346948767159241758L;
	//The Name of this Process
	private String Bezeichnung;
	//The duration of this Process
    private double Dauer = -1;
    //The X Coordinate where it got drawn originally
    private int startX = -1;
    //The Y Coordinate where it got drawn originally
    private int startY = -1;

    //The width of a Process
    public int width = 100;
    //The height of a Process
    public int height = 100;
    //The ID of this Process
    public int id;
    
    //A list of this Process parents and childs
    protected ArrayList<Process> parents;
    protected ArrayList<Process> childs;
    
    //Value used to position the Gui Elements
    private int mouseDragginOffsetY = 0;
    private int mouseDragginOffsetX = 0;
    
    public boolean isEnd = false;
    
    //Values used to determine if this Node is pressed
    boolean isDragging = false;
    
    public Process(double Dauer,String Bezeichnung,int startX,int startY,int id,boolean isEnd) {
    	this.parents = new ArrayList<Process>();
    	this.childs = new ArrayList<Process>();
        this.id = id;
        this.Dauer = Dauer;
        this.Bezeichnung = Bezeichnung;
        this.startX = startX;
        this.startY = startY;
        this.isEnd = isEnd;
    } 
    
    /**
     * Returns the highest FEZ of his parents {@link Process}
     * @return 0 or the actual value ({@link Double})
     */
    public double getHightestParentFEZ(){
    	double highestFEZ = 0;
        for(Process node : parents){
            if(node.FEZ > highestFEZ){
                highestFEZ = node.FEZ;
            }
        }
        return highestFEZ;
    }
    
    /**
     * Returns the smallestSAZ, if no Child it would return Integer.MAX_VALUE, but that should never happen by the rules of calulation this
     * @return
     */
    public double getSmallestChildSAZ(){
    	double smallestSAZ  = Integer.MAX_VALUE;
        for(Process node : childs){
            if(node.SAZ < smallestSAZ){
            	smallestSAZ = node.SAZ;
            }
        }
        return smallestSAZ;
    }
    
    public void startBackward_scheduling() {
    	for(Process node : parents) {
    		if(node.parents.size() > 0) {
    			node.setSEZ(node.getSmallestChildSAZ());
    			node.setSAZ(node.getSEZ() - node.getDauer());	
    		}else {    			
    			node.setSEZ(0);
    			node.setSAZ(0);	
    		}
			node.startBackward_scheduling();	
    	}
    }
    
    /**
     * Function which calculates the FAZ of this {@link Process}
     */
    public void calcFAZ(){
        FAZ = getHightestParentFEZ();
    }
    /**
     * Function which calculates the SEZ of this {@link Process}
     * FEZ and GesamtPuffer has to be calculates first to get a correct value
     */
    public void calcSEZ(){
        if(GesamtPuffer == -1){
            GesamtPuffer = 0;
        }
        SEZ = FEZ + GesamtPuffer;        
    }
    /**
     * Nimmt den FEZ des Vorg‰ngers, berechnet seinen eigenen FAZ sowie FEZ
     * und ruft f¸r alle Kinder diese Funktion auf
     * @param FEZ ,{@link Double} 
     */
    public void calcFAZ(double FEZ){
        this.FAZ = FEZ;
        this.FEZ = this.FAZ + this.Dauer;
        
        for(Process pro : childs){
            pro.calcFAZ(this.FEZ);
        }
        
    }
        /**
     * Nimmt den SAZ des Vorg‰ngers, berechnet seinen eigenen SEZ sowie SAZ
     * und ruft f¸r alle Kinder diese Funktion auf
     * @param SAZ ,{@link Double} 
     */
    public void calcSEZ(double SAZ){
        this.SEZ = SAZ;
        this.SAZ = this.SEZ - this.Dauer;
        
        for(Process pro : parents){
            pro.calcSEZ(this.SAZ);
        }
    }
    
    /**
     * Berechnet den GesamtPuffer, muss f√ºr jeden Knoten aufgerufen werden
     * @param SAZ the SAZ value used for calculating
     * @param FAZ the FAZ value used for calculating
     */
    public void calcGesamtPuffer(double SAZ,double FAZ){
        //this.GesamtPuffer = SAZ - FAZ;
        if(childs.size() > 0) {
        	 this.GesamtPuffer = getSmallestChildSAZ() - FEZ;
        }else {
        	 this.GesamtPuffer = 0;
        }
        
       
        
    }
    /**
     * Berechnet den FreienPuffer, muss f√ºr jeden Knoten aufgerufen werden
     * @param FEZ the FEZ value used for calculating
     * @param childs A list of Childs used for calculating
     */
    public void calcFreierPuffer(double FEZ,ArrayList<Process> childs){
        if(childs.isEmpty()){
            this.FreierPuffer = 0;
        }else{
        	double sfaz = getsmallestChildFAZ(childs);
            this.FreierPuffer = sfaz - FEZ;
        }
    }

    /**
     * Calculates the smallest FAZ of all childs
     * @param childs A list of Child Processes
     * @return the smallest FAZ found
     */
    private double getsmallestChildFAZ(ArrayList<Process> childs){
        double smallestFAZ = Integer.MAX_VALUE;
        for(Process node : childs){
            if(node.FAZ < smallestFAZ){
                smallestFAZ = node.FAZ;
            }
        }
        return smallestFAZ;
    }
    /**
     * Returns true if
     * GesamtPuffer equals 0
     * FreierPufer equals 0
     * childs.size() greater than 0
     * parents.size() greater than 0
     * @return {@link Boolean}
     */
    public boolean isCritical(){        
        return this.GesamtPuffer == 0 && this.FreierPuffer == 0 && childs.size() != 0 && parents.size() != 0;
    }
    /**
     * Returns the FAZ of this {@link Process}
     * @return FAZ ,{@link Double} 
     */
    public double getFAZ() {
        return FAZ;
    }
    /**
     * Returns the FEZ of this {@link Process}
     * @return FEZ ,{@link Double} 
     */
    public double getFEZ() {
        return FEZ;
    }
    /**
     * Returns the SEZ of this {@link Process}
     * @return SEZ ,{@link Double} 
     */
    public double getSEZ() {
        return SEZ;
    }
    /**
     * Returns the SAZ of this {@link Process}
     * @return SAZ ,{@link Double} 
     */
    public double getSAZ() {
        return SAZ;
    }
    /**
     * Returns the name of this {@link Process}
     * @return FAZ ,{@link Double} 
     */
    public String getName() {
        return Bezeichnung;
    }
    /**
     * Returns the ID of this {@link Process}
     * @return {@link Integer}
     */
    public int getID() {
        return id;
    }
    /**
     * Returns the duration of this {@link Process}
     * @return {@link Double}
     */
    public double getDauer() {
        return Dauer;
    }
    /**
     * Returns the Free Puffer of this {@link Process}
     * @return {@link Double}
     */
    public double getFreier_Puffer() {
        return FreierPuffer;
    }
    /**
     * Returns a List of this {@link Process} Parents
     * @return {@link List}
     */
    public List<Process> getProPlanParent() {
        return parents;
    }

    /**
    * Called from {@link Plan} to notify if the Mouse Button got Released
    * @param e A MouseEvent
    */
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }
    public void mousePressed() {
		isDragging = true;		
	}
    /**
     * Moves this {@link Process} to another Position on the screen,<br>its position will be added to the given parameters
     * @param posX The new X Position
     * @param posY The new Y Position
     */
    public void updatePosition(int posX,int posY){
        this.startX += posX;
        this.startY += posY;

    }
    /**
     * Returns the total Puffer
     * @return {@link Double}
     */
    public double getGesamtPuffer() {
        return GesamtPuffer;
    }
    /**
     * Returns the startXPosition of this {@link Process}
     * @return The x Coordinate, used to draw onscreen
     */
    public int getStartX() {
        return startX;
    }
    /**
     * Returns the startYPosition of this {@link Process}
     * @return The y Coordinate, used to draw onscreen
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Sets the dragginOffsetX coordinate
     * @param offsetX The X offset to the Screen center
     */
    public void setMouseDragginOffsetX(int offsetX) {
        this.mouseDragginOffsetX = offsetX;
    }
    /**
     * Sets the dragginOffsetY coordinate
     * @param offsetY The Y offset to the Screen center
     */
    public void setMouseDragginOffsetY(int offsetY) {
        this.mouseDragginOffsetY = offsetY;
    }
    /**
     * Returns the dragginOffsetY coordinate
     * @return The dragging Y offset
     */
    public int getMouseDragginOffsetY() {
        return mouseDragginOffsetY;
    }
    /**
     * Returns the dragginOffsetX coordinate
     * @return The dragging X offset
     */
    public int getMouseDragginOffsetX() {
        return mouseDragginOffsetX;
    }
    /**
     * Returns the isDraggin variable
     * @return true if is dragging
     */
    public boolean isIsDragging() {
        return isDragging;
    }
    /**
     * Sets the Id of this {@link Process}
     * @param id the new id
     */
    public void setID(int id){
        this.id = id;
    }
    /**
     * Sets the name of this {@link Process}
     * @param name the new name
     */
    public void setName(String name){
        this.Bezeichnung = name;
    }
    /**
     * Sets the duration of this {@link Process}
     * @param dauer the new duration
     */
    public void setDauer(double dauer){
        this.Dauer = dauer;
    }
    
    /**
     * Returns false if the process is allready stored, or wrong ID or too much parents allready stored
     * Check with isPredecessor if the Process is allready a child or Parent
     * @param process The new Parent Process
     * @return true if added
     */
    public boolean addPredecessor(Process process){
        if(!isPredecessor(process) && parents.size() < Plan.MAX_PARENTS && !isSuccecessor(process)){
            //Pr√ºfe ob die ID vorhanden ist
            for(Process proc : childs){
                if(proc.getID() == process.getID()){
                    String message = Lang.getLanguageValue(Language.ALLREADY_A_CHILD).replace("%newprocess%", process.getName()).replace("%process%", this.getName());
                    MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
                    return false;
                }
            }
            for(Process proc : parents){
                if(proc.getID() == process.getID()){
                	 String message = Lang.getLanguageValue(Language.ALLREADY_A_PARENT).replace("%newprocess%", process.getName()).replace("%process%", this.getName());
                	 MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
                	 return false;
                }
            }
           parents.add(process);
           return true;
        }
        
        return false;
    }
        /**
     * Returns false if the process is allready stored, or wrong ID or too much childs allready stored
     * Check with isSuccecessor if the Process is allready a child or Parent
     * @param process The new child Process
     * @return true if added
     */
    public boolean addSuccessor(Process process){
        if(!isSuccecessor(process) && childs.size() < Plan.MAX_CHILDS && !isPredecessor(process)){
            //Pr√ºfe ob die ID vorhanden ist
            for(Process proc : childs){
                if(proc.getID() == process.getID()){
                	String message = Lang.getLanguageValue(Language.ALLREADY_A_CHILD).replace("%newprocess%", process.getName()).replace("%process%", this.getName());
                	MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
                	return false;
                }
            }
            for(Process proc : parents){
                if(proc.getID() == process.getID()){
                	String message = Lang.getLanguageValue(Language.ALLREADY_A_PARENT).replace("%newprocess%", process.getName()).replace("%process%", this.getName());
                	MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
                	return false;
                }
            }
           childs.add(process);
           return true;
        }
        
        return false;
    }
    /**
     * Removes all childs from this {@link Process}
     */
    public void clearSuccessor(){
        this.childs.clear();
    }
    /**
     * Removes all parents from this {@link Process}
     */
    public void clearPredecessor(){
        this.parents.clear();
    }
    /**
     * Removes a child from this {@link Process}
     * @param pro The Process which should be removed from child list
     */
    public void removeSuccessor(Process pro){
        this.childs.remove(pro);
    }
    /**
     * Removes a parent from this {@link Process}
     * @param pro The Process which should be removed from parent
     */
    public void removePredecessor(Process pro){
        this.parents.remove(pro);
    }
    /**
     * Checks if a Process is a parent of this {@link Process}
     * @param process The Process which should be checked
     * @return true if the process is in the parent list
     */
    public boolean isPredecessor(Process process){
        return parents.contains(process);
    }
    /**
     * Checks if a Process is a child of this {@link Process}
     * @param process The Process which should be checked
     * @return true if the process is in the child list
     */
    public boolean isSuccecessor(Process process){
        return childs.contains(process);
    }
    /**
     * Returns true if no parents are stored
     * @return boolean true if no parents are stored
     */
    public boolean isStartProcess(){
        return parents.isEmpty() || this.isEnd;
    }
    /**
     * Returns true if no childs are stored
     * @return boolean true if no childs are stored
     */
    public boolean isEndProcess(){
        return childs.isEmpty();
    }
    /**
     * Returns a List of Parent Process
     * @return a list of all parents
     */
    public ArrayList<Process> getPredecessor(){
        return parents;
    }
    /**
     * Returns a List of Child Process
     * @return a list of all childs
     */
    public ArrayList<Process> getSuccecessor(){
        return childs;
    }
    /**
     * Sets the FAZ of this {@link Process}
     * @param FAZ the new FAZ Value
     */
    public void setFAZ(double FAZ) {
        this.FAZ = FAZ;
    }
    /**
     * Sets the FEZ of this {@link Process}
     * @param FEZ the new FEZ Value
     */
    public void setFEZ(double FEZ) {
        this.FEZ = FEZ;
    }
    /**
     * Sets the SEZ of this {@link Process}
     * @param SEZ the new SEZ Value
     */
    public void setSEZ(double SEZ) {
        this.SEZ = SEZ;
    }
    /**
     * Sets the SAZ of this {@link Process}
     * @param SAZ the new SAZ Value
     */
    public void setSAZ(double SAZ) {
        this.SAZ = SAZ;
    }
    /**
     * Sets the total puffer of this {@link Process}
     * @param GesamtPuffer the news GesamtPuffer Value
     */
    public void setGesamtPuffer(double GesamtPuffer) {
        this.GesamtPuffer = GesamtPuffer;
    }

    @Override
    public String toString() {
        String tostring = "Process{" + 
                "FAZ=" + FAZ + 
                ", FEZ=" + FEZ + 
                ", SEZ=" + SEZ + 
                ", SAZ=" + SAZ + 
                ", Bezeichnung=" + Bezeichnung + 
                ", Dauer=" + Dauer + 
                ", GesamtPuffer=" + GesamtPuffer + 
                ", startX=" + startX + 
                ", startY=" + startY + 
                ", parents=" + parents.size()+ 
                ", childs=" + childs.size() + 
                ", mouseDragginOffsetX=" + mouseDragginOffsetX + 
                ", mouseDragginOffsetY=" + mouseDragginOffsetY + 
                ", id=" + id + 
                ", isDragging=" + isDragging + '}';
        return tostring;
    }

	public HashMap<String, Object> asJson() {
		HashMap<String, Object> data = new HashMap<>();
		
		data.put("description", this.Bezeichnung);
		data.put("duration", this.Dauer+"");
		data.put("startX", this.startX+"");
		data.put("startY", this.startY+"");
		data.put("width", this.width+"");
		data.put("height", this.height+"");
		data.put("id", this.id+"");
		data.put("description", this.Bezeichnung);
		data.put("faz", this.FAZ+"");
		data.put("fez", this.FEZ+"");
		data.put("saz", this.SAZ+"");
		data.put("sez", this.SEZ+"");
		data.put("freebuffer", this.FreierPuffer+"");
		data.put("totalbuffer", this.GesamtPuffer+"");
		data.put("isEnd", this.isEnd+"");
		
		ArrayList<Integer> parents = new ArrayList<>();
		for(Process process : this.parents) {
			parents.add(process.getID());
		}
		ArrayList<Integer> childs = new ArrayList<>();
		data.put("parents", parents);
		for(Process process : this.childs) {
			childs.add(process.getID());
		}
		data.put("childs", childs);
		
		return data;
	}

	
    
    
}
