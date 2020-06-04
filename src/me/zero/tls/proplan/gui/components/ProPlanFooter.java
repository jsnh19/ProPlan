/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.components;

import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.gui.view.ProPlanUI;

/**
 *
 * @author Julius.Schoenhut
 */
public class ProPlanFooter extends JPanel{
    
    //Used for Serialization
	private static final long serialVersionUID = 542054229744299710L;
	
	//private final JLabel mousePos;
	//A label displaying the offset to (0,0)
	private final JLabel offSetPos;
    
    public ProPlanFooter(){
        this.offSetPos = new JLabel();
        addFooterInfos();
    }
    /**
     * Adds the elements to the footer panel
     */
    private void addFooterInfos(){
        this.add(offSetPos);
    }
    /**
     * Called if the mouse moves, to update the displayed values
     * @param e {@link MouseEvent}
     */
    public void mouseMoved(MouseEvent e) {
        Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));      
        //this.mousePos.setText((e.getX() + plan.getOffsetX()) + ":" + (e.getY() + plan.getOffsetY()));                
        this.offSetPos.setText(plan.getOffsetX() + ":" + plan.getOffsetY());
        this.repaint();
    }
    /**
     * Called if the mouse draggs, to update the displayed values
     * @param e {@link MouseEvent}
     */
    public void mouseDragged(MouseEvent e){
        Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));  
        //this.mousePos.setText((e.getX() + plan.getOffsetX()) + ":" + (e.getY() + plan.getOffsetY()));                 
        this.offSetPos.setText(plan.getOffsetX() + ":" + plan.getOffsetY());
        this.repaint();
    }
}
