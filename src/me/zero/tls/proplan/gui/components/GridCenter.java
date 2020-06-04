/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.components;

import java.awt.Graphics2D;
import java.io.Serializable;

/**
 *
 * @author Julius.Schoenhut
 */
public class GridCenter implements Serializable {

	// Needed for Serializable
	private static final long serialVersionUID = -2671172242016444753L;
	// the actual X Position of the gridsystem
	private int posX = 0;
	// the actual Y Position of the gridsystem
	private int posY = 0;
	// the start X,Y Positions of the gridsystem
	int startX, startY;
	// offset variable, used for positioning
	private int offsetToZeroX, offsetToZeroY;

	public GridCenter(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;

		startX = posX;
		startY = posY;
	}

	/**
	 * Updates the Position of the GridSystem (the virtuell Center)
	 * 
	 * @param posX the changed value of X
	 * @param posY the changed value of Y
	 */
	public void updatePosition(int posX, int posY) {
		this.posX += posX;
		this.posY += posY;
	}

	/**
	 * Sets the offset X variable
	 * 
	 * @param offsetToZeroX the new X offset
	 */
	public void setOffSetX(int offsetToZeroX) {
		this.offsetToZeroX = offsetToZeroX;
	}

	/**
	 * Sets the offset Y variable
	 * 
	 * @param offsetToZeroY the new Y offset
	 */
	public void setOffSetY(int offsetToZeroY) {
		this.offsetToZeroY = offsetToZeroY;
	}

	/**
	 * Returns the offset X variable
	 * @return {@link Integer}
	 */
	public int getOffsetToZeroX() {
		return offsetToZeroX;
	}
	/**
	 * Returns the offset Y variable
	 * @return {@link Integer}
	 */
	public int getOffsetToZeroY() {
		return offsetToZeroY;
	}

	/**
	 * Used to draw the grid
	 * @param g2 {@link Graphics2D}
	 * @param screenWidth {@link Integer}
	 * @param screenHeight {@link Integer}
	 */
	public void paint(Graphics2D g2, int screenWidth, int screenHeight) {
		int movedByX = (posX - startX);
		int movedByY = (posY - startY);
		int linienabstand = 100;
		int gYCordP = (posY + screenHeight);
		int gYCordN = (posY - screenHeight);

		int gXCorN = posX - screenWidth;
		int gXCorP = posX + screenWidth;
		int amountLinesY, amountLinesX;

		if (movedByY < 0) {
			amountLinesX = ((((movedByY) * -1) + screenHeight) / linienabstand) + 1;
		} else {
			amountLinesX = ((movedByY + screenHeight) / linienabstand) + 1;
		}
		if (movedByX < 0) {
			amountLinesY = (((movedByX * -1) + screenWidth) / linienabstand) + 1;
		} else {
			amountLinesY = ((movedByX + screenWidth) / linienabstand) + 1;
		}
		if (amountLinesX < 8)
			amountLinesX = 8;
		if (amountLinesY < 8)
			amountLinesY = 8;

		if (movedByX <= 0) {
			// Nach oben
			for (int i = 0; i < amountLinesX; i++) {
				int offset = i * linienabstand;
				g2.drawLine(gXCorP - movedByX, (posY - offset), gXCorN - movedByX, (posY - offset));
			}
			// Nach unten
			for (int i = 0; i < amountLinesX; i++) {
				int offset = i * linienabstand;
				g2.drawLine(gXCorP - movedByX, (posY + offset), gXCorN - movedByX, (posY + offset));
			}

		} else if (movedByX > 0) {
			// Nach oben
			for (int i = 0; i < amountLinesX; i++) {
				int offset = i * linienabstand;
				g2.drawLine(gXCorP - movedByX, (posY - offset), gXCorN - movedByX, (posY - offset));
			}
			// Nach unten
			for (int i = 0; i < amountLinesX; i++) {
				int offset = i * linienabstand;
				g2.drawLine(gXCorP - movedByX, (posY + offset), gXCorN - movedByX, (posY + offset));
			}
		} else {
			System.out.println("Unhandled: " + movedByX + ":" + movedByY);
		}

		if (movedByY > 0) {
			// Nach oben
			for (int i = 0; i < amountLinesY; i++) {
				int offset = i * linienabstand;
				g2.drawLine(posX + offset, gYCordN - movedByY, posX + offset, gYCordP + movedByY);
			}
			// Nach unten
			for (int i = 0; i < amountLinesY; i++) {
				int offset = i * linienabstand;
				g2.drawLine(posX - offset, gYCordN - movedByY, posX - offset, gYCordP + movedByY);
			}
		} else if (movedByY <= 0) {
			// Nach oben
			for (int i = 0; i < amountLinesY; i++) {
				int offset = i * linienabstand;
				g2.drawLine(posX + offset, gYCordP - movedByY, posX + offset, gYCordN + movedByY);
			}
			// Nach unten
			for (int i = 0; i < amountLinesY; i++) {
				int offset = i * linienabstand;
				g2.drawLine(posX - offset, gYCordP - movedByY, posX - offset, gYCordN + movedByY);
			}

		} else {
			System.out.println("Unhandled: " + movedByX + ":" + movedByY);
		}
		g2.drawLine(posX - 10, posY, posX + 10, posY);
		g2.drawLine(posX, posY - 10, posX, posY + 10);
		g2.drawString("(0,0)", posX + 5, posY - 5);
	}

}
