/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.gui.view.ProPlanUI;

import me.zero.tls.proplan.components.Process;

/**
 *
 * @author Julius.Schoenhut
 */
public class DeleteController implements ActionListener{

	//Plan to work with
    private Plan plan;
    //Process to worki with
    private Process node;
    
    public DeleteController(Plan plan,Process node){
        this.plan = plan;
        this.node = node;
    }
    public DeleteController(){}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Löschen angesagt
        if(e == null){
            for(Process pro : node.getPredecessor()){
                pro.removeSuccessor(node);
            }
            for(me.zero.tls.proplan.components.Process pro : node.getSuccecessor()){
                pro.removePredecessor(node);
            }
            plan.nodes.remove(node);
            plan.startForward_scheduling();
            plan.startBackward_scheduling();
            plan.startTotalPufferCalculation();
            plan.repaint();
            ProPlanUI.mode = -1;
        }else{
          ProPlanUI.mode = 1;  
        }
    }
    
}
