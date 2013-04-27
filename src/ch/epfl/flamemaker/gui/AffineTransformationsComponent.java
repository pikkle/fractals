package ch.epfl.flamemaker.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class AffineTransformationsComponent extends JComponent{
	private Flame.Builder flameBuilder;
	private Rectangle frame;
	private int highlightedTransformationIndex;
	public AffineTransformationsComponent(Flame.Builder flameBuilder, Rectangle frame){
		this.flameBuilder = flameBuilder;
		this.frame = frame;
	}
	
	@Override
	public void paintComponent(Graphics g0){
		Graphics2D g1 = (Graphics2D) g0;
		Rectangle recComp = new Rectangle(this.frame.center(),this.getWidth(), this.getHeight());
		Rectangle rec = this.frame.expandToAspectRatio(recComp.aspectRatio());
		
		// Défini le centre du graphe par rapport au centre de la fractale
		Point center = new Point(this.getWidth()/2 + frame.center().x(), this.getHeight()/2 + frame.center().y());
		int cx = (int)center.x();
		int cy = (int)center.y();
		// Dessine le grillage
		g1.setColor(new Color((float)0.9, (float)0.9, (float)0.9));
		int dec = 20;
		for (int i = cy; i < this.getHeight(); i+=dec) {
			g1.draw(new Line2D.Double(0, i, this.getWidth(), i));
		}
		for (int i = cy; i > 0; i-=dec) {
			g1.draw(new Line2D.Double(0, i, this.getWidth(), i));
		}
		for (int i = cx; i < this.getWidth(); i+=dec) {
			g1.draw(new Line2D.Double(i, 0, i, this.getHeight()));
		}
		for (int i = cx; i > 0; i-=dec) {
			g1.draw(new Line2D.Double(i, 0, i, this.getHeight()));
		}
		
		// Dessine l'absisce et l'ordonnée
		g1.setColor(Color.white);
		g1.draw(new Line2D.Double(0, cy, this.getWidth(), cy));
		g1.draw(new Line2D.Double(cx, 0, cx, this.getHeight()));
		
		// Dessine les vecteurs de test
		g1.setColor(Color.black);
		g1.draw(new Line2D.Double(-1+cx,0+cy,1+cx,0+cy));
		g1.draw(new Line2D.Double(0+cx,-1+cy,0+cx,1+cy));
	}
	
	public int getHighlightedTransformationIndex() {
		return highlightedTransformationIndex;
	}

	public void setHighlightedTransformationIndex(int highlightedTransformationIndex) {
		this.highlightedTransformationIndex = highlightedTransformationIndex;
		this.repaint();
	}
}
