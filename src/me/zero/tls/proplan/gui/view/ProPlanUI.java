package me.zero.tls.proplan.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.gui.components.ProPlanFooter;
import me.zero.tls.proplan.gui.components.ProPlanToolBar;
import me.zero.tls.proplan.gui.components.Tabs;
import me.zero.tls.proplan.gui.util.GraphicsUtil;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;

public class ProPlanUI extends JFrame{
	
    //Used by Serizable
	private static final long serialVersionUID = 6100080029472202183L;
	//Tab variable containing all Plans
	public static Tabs tabs;
	//Toolbar containing all Buttons
    public static ProPlanToolBar proPlanToolbar;
    //Footer containing all informations
    public static ProPlanFooter proPlanFooter;
    
    /* 0 = Edit;1 = delete*/
    public static int mode = -1;
    
    public ProPlanUI(){
        super();
        init();
    }
    /**
     * Init the Plan
     */
    private void init(){
    	Image img = GraphicsUtil.createImageIcon("/ressources/images/map_icon.png").getImage();
    	this.setIconImage(img);
    	
        proPlanToolbar = new ProPlanToolBar();
        proPlanFooter = new ProPlanFooter();
        tabs = new Tabs();
        this.add(proPlanToolbar, BorderLayout.NORTH);
        this.add(tabs, BorderLayout.CENTER);
        this.add(proPlanFooter, BorderLayout.SOUTH);
        
        
        this.pack();
        this.setTitle("ProPlan");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	
            	for(String planname : ProPlanUI.tabs.tabToPanel.keySet()) {
            		Plan plan = ProPlanUI.tabs.tabToPanel.get(planname);
            		if(plan.changed) {
            			int confirm = JOptionPane.showOptionDialog(null, Lang.getLanguageValue(Language.UNSAFED_CHANGES).replace("%plan%", plan.getName()), 
                                Lang.getLanguageValue(Language.UNSAFED_CHANGES_TITLE), JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE, null, null, null);
                           if (confirm == 0) {
                              System.exit(0);
                           }
            		}else {
                        System.exit(0);
            		}
            	}
            	if(ProPlanUI.tabs.tabToPanel.size() == 0) {
            		System.exit(0);
            	}
            }
        });
        this.setSize(800, 800);
        //this.setLocation(WIDTH, WIDTH);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);
    }
    
}
