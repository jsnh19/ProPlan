package me.zero.tls.proplan.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.zero.tls.proplan.gui.components.GridCenter;
import me.zero.tls.proplan.gui.controller.DeleteController;
import me.zero.tls.proplan.gui.controller.EditController;
import me.zero.tls.proplan.gui.util.GraphicsUtil;
import me.zero.tls.proplan.gui.view.MessageUtil;
import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;
import me.zero.tls.proplan.settings.Settings;

public class Plan extends JComponent implements MouseListener, MouseMotionListener, Serializable {

	// Needed for Serializable
	private static final long serialVersionUID = -4382460341892888334L;
	// List of Processes this Plan has
	public ArrayList<Process> nodes = new ArrayList<>();
	// The Offset a Node should have
	private static final int OFFSET = 50;
	// Max Childs per Node
	public static int MAX_CHILDS = 10;
	// Max Parents per Node
	public static int MAX_PARENTS = 10;
	// Name of this Plan
	private final String name;
	// Description of this Plan
	private final String description;
	// Used to position the Grid System
	public static int display_middle_x;
	public static int display_middle_y;
	// Variables which store the dragged offset
	public static int movedX;
	public static int movedY;
	// Variable used to re-position Objects
	public int offsetToZeroX = 0;
	public int offsetToZeroY = 0;
	// The GridSystem
	private GridCenter center;
	// The last time the user clicked
	private Long lastClickedTime = 0L;
	// Position on screen where the user started dragging
	public int xStartDragging = -1;
	public int yStartDragging = -1;
	public static int width = 100;
	public static int height = 100;	
	public boolean changed = false;	
	private JLabel tabTitle;
	public String saveFileName = null;
	
	
	public Plan(String name, String description) {
		this.name = name;
		this.description = description;
		init();
		MAX_CHILDS = Double.valueOf(Settings.instance.settings.get(Settings.SETTINGS_KEY.MAX_CHILDS.name()).toString()).intValue();
		MAX_PARENTS = Double.valueOf(Settings.instance.settings.get(Settings.SETTINGS_KEY.MAX_PARENTS.name()).toString()).intValue();
	}

	/**
	 * Returns a String representing the Description of this Plan
	 * 
	 * @return descrption for this Plan
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Init function to move Code outsite of the Constructor
	 */
	private void init() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		Plan.display_middle_x = 389;
		Plan.display_middle_y = 327;
		center = new GridCenter(display_middle_x, display_middle_y);
	}

	/**
	 * Returns a Process searched by its id
	 * 
	 * @param id ,the id of the searched Process
	 * @return an instance of {@link Process}
	 */
	public Process getNodeByID(int id) {
		for (Process pro : nodes) {
			if (pro.getID() == id) {
				return pro;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Adds a new {@link Process} to the list of Processes Calls {@link #calcData()}
	 * Finally it repaints the scene
	 * 
	 * @param node Thew new node
	 */
	public void addNode(Process node) {
		nodes.add(node);
		calcData();
		this.repaint();
	}

	/**
	 * Calls {@link #startForward_scheduling()} Calls
	 * {@link #startBackward_scheduling()} Calls
	 * {@link #startTotalPufferCalculation()}
	 * 
	 */
	public void calcData() {
		startForward_scheduling();
		startBackward_scheduling();
		startTotalPufferCalculation();
	}

	/**
	 * Calculates the <br>
	 * FAZ<br>
	 * FEZ
	 * 
	 */
	public void startForward_scheduling() {
		// Nehme erstes Element
		/*Process first = getNodeX(getFirstNodeID());
		if (first != null) {
			first.setFAZ(0);
			first.setFEZ(first.getFAZ() + first.getDauer());
			first.calcFAZ(first.getFEZ());
		}*/
		
		boolean first = true;
		for(Process pro : nodes) {
			if(first) {				
				pro.setFAZ(0);
				pro.setFEZ(0);				
				first = false;
			}else {
				pro.setFAZ(pro.getHightestParentFEZ());
				pro.setFEZ(pro.FAZ + pro.getDauer());
			}
		}
	}

	/**
	 * Calculates the <br>
	 * SAZ<br>
	 * SAZ
	 */
	public void startBackward_scheduling() {
		// Nehme letzes Element
		/*Process last = getNodeX(getLastNodeID());
		if (last != null) {
			last.setSEZ(last.getFEZ());
			last.setSAZ(last.getSEZ() - last.getDauer());
			last.calcSEZ(last.getSAZ());
		}*/
		/*boolean isLast = true;
		for(int i = nodes.size()-1;i >= 0;i--) {
			Process node = nodes.get(i);
			if(isLast) {		
				node.setSEZ(node.getFEZ());
				node.setSAZ(node.getSEZ() - node.getDauer());
				isLast = false;
				System.out.println("Last Node is " + node.getName());
			}else {
				//Niedrigste SAZ aller Nachfolger ist SEZ dieses Prozesses
				if(i -1 >= 0) {
					node.setSEZ(node.getSmallestChildSAZ());
					node.setSAZ(node.getSEZ() - node.getDauer());		
				}else {
					node.setSEZ(0);
					node.setSAZ(0);		
				}	
			}
		}*/		
		
		Process lastProcess = getEndProcess();
		
		if(lastProcess != null) {
			lastProcess.setSEZ(lastProcess.getFEZ());
			lastProcess.setSAZ(lastProcess.getSEZ() - lastProcess.getDauer());
			lastProcess.startBackward_scheduling();
		}
		
		
		
		/*for(int i = nodes.size()-1;i >= 0;i--) {
			Process node = nodes.get(i);		
			
			if(node != lastProcess) {
				System.out.println("working on node = " + node.getName() + " (" + node.getID() + ")");
				System.out.println("childs: " + node.getSuccecessor().size());
				System.out.println("parents: " + node.getPredecessor().size());
				//Niedrigste SAZ aller Nachfolger ist SEZ dieses Prozesses
				if(i -1 >= 0) {
					node.setSEZ(node.getSmallestChildSAZ());
					node.setSAZ(node.getSEZ() - node.getDauer());		
				}else {
					node.setSEZ(0);
					node.setSAZ(0);
				}	
			}							
		}*/
	}

	/*private Process getStartProcess() {
		Process smallestProcess = null;
		for(Process pro : nodes) {
			if(smallestProcess == null || pro.getID() < smallestProcess.getID()) {
				smallestProcess = pro;
			}
		}
		return smallestProcess;
	}*/
	
	private Process getEndProcess() {
		for(Process pro : nodes) {
			if(pro.isEnd) {
				return pro;
			}
		}
		return null;
	}
	
	/**
	 * Calculates the <br>
	 * FreierPuffer<br>
	 * and theGesamtPuffer
	 */
	public void startTotalPufferCalculation() {
		for (Process p : nodes) {
			p.calcGesamtPuffer(p.SAZ, p.FAZ);
			p.calcFreierPuffer(p.FEZ, p.childs);
		}
	}
	/**
	 * Returns a String representing this Plan as a Json
	 * @return Plan as a Json
	 */
	public String asJson() {
		Gson GSON = new GsonBuilder().setPrettyPrinting().create();		
		HashMap<String, Object> data = new HashMap<>();				
		data.put("description", this.description);
		data.put("name", this.name);
		data.put("offsetX", this.offsetToZeroX);
		data.put("offsetY", this.offsetToZeroY);	
		ArrayList<Object> objects = new ArrayList<Object>();
		
		for(Process process: this.nodes) {			
			objects.add(process.asJson());			
		}	
		data.put("nodes", objects);
		return GSON.toJson(data);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (Process node : nodes) {
			// Prüfe ob dieses Node angeklickt wurde
			if (e.getX() >= (node.getStartX() - OFFSET)
					&& e.getX() <= ((node.getStartX() - OFFSET) + Plan.width + OFFSET)) {
				if (e.getY() >= (node.getStartY() - OFFSET)
						&& e.getY() <= ((node.getStartY() - OFFSET) + Plan.height + OFFSET)) {
					if (ProPlanUI.mode == 1) {
						// Sofortiger Delete
						new DeleteController(this, node).actionPerformed(null);
						this.changed = true;
						setChangedMarker();
						return;
					} else if (ProPlanUI.mode == 0) {
						// Sofortiger Edit
						new EditController(this, node).actionPerformed(null);
						this.changed = true;
						setChangedMarker();
					}
					// ist doppelclick ?
					if (lastClickedTime > 0L) {
						if (System.currentTimeMillis() - lastClickedTime < 500) {
							// doppelclick detected!
					//node.mouseClicked(e);
							lastClickedTime = System.currentTimeMillis();
							// Doppelclick edit
							new EditController(this, node).actionPerformed(null);
							this.changed = true;
							setChangedMarker();
						} else {
							lastClickedTime = System.currentTimeMillis();
						}
					} else {
						lastClickedTime = System.currentTimeMillis();
					}
				}
			}
		}
	}
	
	public void setChangedMarker() {
		if(tabTitle == null) {
			MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), "Can' change Changemarker it's null!");
		}
		tabTitle.setText(tabTitle.getText().replace(" *",""));
		if(changed) {
			tabTitle.setText(tabTitle.getText().concat(" *"));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		//unselect all Nodes
		for (Process node : nodes) {
			node.isDragging = false;
		}
		
		//Select one Node
		for (Process node : nodes) {
			// Pr�fe ob dieses Node angeklickt wurde
			if (e.getX() >= (node.getStartX() - OFFSET)&& e.getX() <= ((node.getStartX() - OFFSET) + Plan.width + OFFSET)) {
				if (e.getY() >= (node.getStartY() - OFFSET)&& e.getY() <= ((node.getStartY() - OFFSET) + Plan.height + OFFSET)) {
					node.mousePressed();
					break;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// anythingDragged = false;
		// Updaten der Position der Nodes...
		this.changed = true;
		setChangedMarker();
		for (Process node : nodes) {
			node.updatePosition(this.offsetToZeroX, this.offsetToZeroY);
		}
		center.updatePosition(this.offsetToZeroX, this.offsetToZeroY);

		xStartDragging = -1;
		yStartDragging = -1;
		offsetToZeroX = 0;
		offsetToZeroY = 0;
		boolean anythingReleased = false;
		
		
		for (Process node : nodes) {
			// Prüfe ob dieses Node angeklickt wurde
			if (e.getX() >= (node.getStartX() - OFFSET)&& e.getX() <= ((node.getStartX() - OFFSET) + Plan.width + OFFSET) && !anythingReleased) {
				if (e.getY() >= (node.getStartY() - OFFSET)&& e.getY() <= ((node.getStartY() - OFFSET) + Plan.height + OFFSET)) {
					node.mouseReleased(e);
					anythingReleased = true;
				}
			}
		}
		// Drag all ?
		if (!anythingReleased) {
			for (Process node : nodes) {
				node.mouseReleased(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// unused
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// unused
	}

	// boolean anythingDragged = false;

	@Override
	public void mouseDragged(MouseEvent e) {
		this.changed = true;
		setChangedMarker();
		boolean anythingDragged = false;
		ProPlanUI.proPlanFooter.mouseDragged(e);
		// Neues Dragging

		if (xStartDragging == -1) {
			xStartDragging = e.getX();
			yStartDragging = e.getY();
		} else {
			movedX += offsetToZeroX;
			movedY += offsetToZeroY;
		}

		this.offsetToZeroX = xStartDragging - e.getX();
		this.offsetToZeroY = yStartDragging - e.getY();
		
		for (Process node : nodes) {
			if(node.isDragging) {
				anythingDragged = true;
				node.updatePosition(offsetToZeroX * -1, offsetToZeroY * -1);
			}
		}

		/*for (Process node : nodes) {
			// Pr�fe ob dieses Node angeklickt wurde
			if (e.getX() >= (node.getStartX() - OFFSET) && e.getX() <= ((node.getStartX() - OFFSET) + Plan.width + OFFSET) && !anythingDragged) {
				if (e.getY() + node.getMouseDragginOffsetY() >= (node.getStartY() - OFFSET) && e.getY() <= ((node.getStartY() - OFFSET) + Plan.height + OFFSET)) {
					node.updatePosition(offsetToZeroX * -1, offsetToZeroY * -1);
					anythingDragged = true;
				}
			}
		}*/
		if (!anythingDragged) {
			// Drag all ?
			for (Process node : nodes) {
				node.setMouseDragginOffsetX(offsetToZeroX * -1);
				node.setMouseDragginOffsetY(offsetToZeroY * -1);
				node.updatePosition(offsetToZeroX * -1, offsetToZeroY * -1);
			}
			center.updatePosition(offsetToZeroX * -1, offsetToZeroY * -1);
		}
		xStartDragging = e.getX();
		yStartDragging = e.getY();
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		Plan.display_middle_x = getSize().width / 2;
		Plan.display_middle_y = getSize().height / 2;
		Graphics2D g2 = (Graphics2D) g;
		// Zeichne das Fadenkreuz für die Mitte
		g2.setColor(Color.lightGray);
		// Horizontale linie

		offsetToZeroX = offsetToZeroX * -1;
		offsetToZeroY = offsetToZeroY * -1;

		center.paint(g2, getSize().width, getSize().height);

		for (Process node : nodes) {

			// Falls kritisch Rot makieren
			if (node.isCritical()) {
				g2.setColor(Color.RED);
			} else {
				g2.setColor(Color.BLACK);
			}

			// Kasten Bezeichnung
			g2.drawRect(node.getStartX() + node.getMouseDragginOffsetX(),
					node.getStartY() + node.getMouseDragginOffsetY(), 100, 40);
			if(node.getName().length() > 15) {
				String drawnName = node.getName().substring(0,13);
				drawnName = drawnName.concat("...");
				
				g2.drawString(drawnName, node.getStartX() + 5 + node.getMouseDragginOffsetX(),
						node.getStartY() + 25 + node.getMouseDragginOffsetY());
			}else {
				g2.drawString(node.getName(), node.getStartX() + 5 + node.getMouseDragginOffsetX(),
						node.getStartY() + 25 + node.getMouseDragginOffsetY());
			}
			

			// Kasten Dauer
			g2.drawRect(node.getStartX() + node.getMouseDragginOffsetX(),
					node.getStartY() + 40 + node.getMouseDragginOffsetY(), 50, 40);
			g2.drawString(node.getDauer() + "", node.getStartX() + 5 + node.getMouseDragginOffsetX(),
					node.getStartY() + 65 + node.getMouseDragginOffsetY());
			// Kasten Gesamt Puffer
			g2.drawRect(node.getStartX() + 50 + node.getMouseDragginOffsetX(),
					node.getStartY() + 40 + node.getMouseDragginOffsetY(), 25, 40);
			// Kasten Freier Puffer
			g2.drawRect(node.getStartX() + 75 + node.getMouseDragginOffsetX(),
					node.getStartY() + 40 + node.getMouseDragginOffsetY(), 25, 40);

			g2.drawString(node.getGesamtPuffer() + "", node.getStartX() + 52 + node.getMouseDragginOffsetX(),
					node.getStartY() + 65 + node.getMouseDragginOffsetY());
			g2.drawString(node.getFreier_Puffer() + "", node.getStartX() + 77 + node.getMouseDragginOffsetX(),
					node.getStartY() + 65 + node.getMouseDragginOffsetY());

			// Zeichne FAZ
			g2.drawString(node.getFAZ() + "", node.getStartX() - 20 + node.getMouseDragginOffsetX(),
					node.getStartY() - 5 + node.getMouseDragginOffsetY());
			// Zeichne FEZ
			g2.drawString(node.getFEZ() + "", node.getStartX() + 100 + node.getMouseDragginOffsetX(),
					node.getStartY() - 5 + node.getMouseDragginOffsetY());
			// Zeichne SAZ
			g2.drawString(node.getSAZ() + "", node.getStartX() - 20 + node.getMouseDragginOffsetX(),
					node.getStartY() + 90 + node.getMouseDragginOffsetY());
			// Zeichne SEZ
			g2.drawString(node.getSEZ() + "", node.getStartX() + 100 + node.getMouseDragginOffsetX(),
					node.getStartY() + 90 + node.getMouseDragginOffsetY());

			// Zeiche Verbindungen
			int offset = 40;
			// Zeichne Pfad zu Eltern
			for (Process proc : node.getPredecessor()) {
				if (proc != null) {
					if (proc.isCritical() && node.isCritical()) {
						g2.setColor(Color.RED);
					} else {
						g2.setColor(Color.BLACK);
					}
					g2.drawLine(node.getStartX() - 10 + node.getMouseDragginOffsetX(),
							node.getStartY() + offset + node.getMouseDragginOffsetY(),
							proc.getStartX() + 100 + node.getMouseDragginOffsetX(),
							proc.getStartY() + offset + node.getMouseDragginOffsetY());
					g2.drawLine(node.getStartX() - 10 + node.getMouseDragginOffsetX(),
							node.getStartY() + offset + node.getMouseDragginOffsetY(),
							node.getStartX() + node.getMouseDragginOffsetX(),
							node.getStartY() + offset + node.getMouseDragginOffsetY());

					// Zeichne den Pfeilkopf
					g2.drawLine(node.getStartX() + node.getMouseDragginOffsetX(),
							node.getStartY() + offset + node.getMouseDragginOffsetY(),
							node.getStartX() - 10 + node.getMouseDragginOffsetX(),
							node.getStartY() + offset - 10 + node.getMouseDragginOffsetY());
					g2.drawLine(node.getStartX() + node.getMouseDragginOffsetX(),
							node.getStartY() + offset + node.getMouseDragginOffsetY(),
							node.getStartX() - 10 + node.getMouseDragginOffsetX(),
							node.getStartY() + offset + 10 + node.getMouseDragginOffsetY());

				} else {
					System.out.println("Null Process found!?!");
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		ProPlanUI.proPlanFooter.mouseMoved(e);
		boolean found = false;
		for (Process node : nodes) {
			// Prüfe ob dieses Node angeklickt wurde
			if (e.getX() >= (node.getStartX() - OFFSET)
					&& e.getX() <= ((node.getStartX() - OFFSET) + Plan.width + OFFSET)) {
				if (e.getY() >= (node.getStartY() - OFFSET)
						&& e.getY() <= ((node.getStartY() - OFFSET) + Plan.height + OFFSET)) {
					if (ProPlanUI.mode == 0) {
						// Edit
						this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
								GraphicsUtil.createImageIcon("/ressources/images/edit2.png").getImage(),
								new Point(0, 0), "edit_cursor"));
						found = true;
					} else if (ProPlanUI.mode == 1) {
						// Delete
						this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
								GraphicsUtil.createImageIcon("/ressources/images/trash2.png").getImage(),
								new Point(0, 0), "delete_cursor"));
						found = true;
					}
			//node.mouseMoved(e);
				}
			}
		}
		if (!found)
			this.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Returns the amount of moved pixels on the x scala
	 * @return actual value
	 */
	public int getOffsetX() {
		return movedX;
	}
	/**
	 * Returns the amount of moved pixels on the y scala
	 * @return actual value
	 */
	public int getOffsetY() {
		return movedY;
	}

	public JLabel getTabTitle() {
		return tabTitle;
	}

	public void setTabTitle(JLabel tabTitle) {
		this.tabTitle = tabTitle;
	}
}
