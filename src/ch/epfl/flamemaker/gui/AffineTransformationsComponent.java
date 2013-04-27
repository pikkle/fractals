package ch.epfl.flamemaker.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
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
		Rectangle rec = this.frame.expandToAspectRatio(recComp.aspectRatio()); //Frame adapté au ratio du component
		
		AffineTransformation upscaleCadre = AffineTransformation.newScaling( //Transformation passant du cadre rec au component
				this.getWidth()/rec.width(), this.getHeight()/rec.height());
		AffineTransformation translation = AffineTransformation.newTranslation(0, this.getHeight());
		AffineTransformation fullTransformation = translation.composeWith(upscaleCadre);
		Point abs1 = new Point(rec.center().x(), 0);
		Point abs2 = new Point(rec.center().x(), rec.bottom());
		Point ord1 = new Point(0,rec.center().y());
		Point ord2 = new Point(rec.right(), rec.center().y());
		
		g1.setColor(Color.black);
		Point[][] grille = new Point[(int)rec.right()][2];
		for (int i = 0; i < grille.length; i++) {
			grille[i][0] = new Point(i,0);
			grille[i][1] = new Point(i,rec.bottom());
		}
		for (Point[] points : grille) {
			Point p1 = fullTransformation.transformPoint(points[0]);
			Point p2 = fullTransformation.transformPoint(points[1]);
			Line2D ligne = new Line2D.Double(p1.x(), p1.y(), p2.x(), p2.y());
			g1.draw(ligne);
		}
	}
	
	public int getHighlightedTransformationIndex() {
		return highlightedTransformationIndex;
	}

	public void setHighlightedTransformationIndex(int highlightedTransformationIndex) {
		this.highlightedTransformationIndex = highlightedTransformationIndex;
		this.repaint();
	}
}
