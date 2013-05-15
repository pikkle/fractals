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
	private static final long serialVersionUID = 1L;
	private Flame.Builder flameBuilder;
	private Rectangle frame;
	private int highlightedTransformationIndex;

	public AffineTransformationsComponent(Flame.Builder flameBuilder,
			Rectangle frame) {
		this.flameBuilder = flameBuilder;
		this.frame = frame;
	}

	@Override
	public void paintComponent(Graphics g0) {
		Graphics2D g1 = (Graphics2D) g0;

		Rectangle newFrame = frame.expandToAspectRatio((double) getWidth()
				/ (double)getHeight());
		AffineTransformation translation = AffineTransformation.newTranslation(getWidth()/2-newFrame.center().x(), 
				getHeight()/2 - newFrame.center().y());
		AffineTransformation upscale = AffineTransformation.newScaling(getWidth()/newFrame.width(),- getHeight()/newFrame.height());
		AffineTransformation composee = translation.composeWith(upscale);
		
		// Dessine la grille
		g1.setColor(new Color((float) 0.9, (float) 0.9, (float) 0.9));
		for (int i = 0; i < newFrame.width(); i++) {
			Point pStart1 = composee.transformPoint(new Point(i,newFrame.height()+1));
			Point pEnd1 = composee.transformPoint(new Point(i,-newFrame.height()-1));
			g1.draw(new Line2D.Double(pStart1.x(),pStart1.y(),pEnd1.x(),pEnd1.y()));
			
			Point pStart2 = composee.transformPoint(new Point(-i,newFrame.height()+1));
			Point pEnd2 = composee.transformPoint(new Point(-i,-newFrame.height()-1));
			g1.draw(new Line2D.Double(pStart2.x(),pStart2.y(),pEnd2.x(),pEnd2.y()));
		}
		for (int i = 0; i < newFrame.height(); i++) {
			Point pStart1 = composee.transformPoint(new Point(newFrame.width()+1,i));
			Point pEnd1 = composee.transformPoint(new Point(-newFrame.width()-1,i));
			g1.draw(new Line2D.Double(pStart1.x(),pStart1.y(),pEnd1.x(),pEnd1.y()));
			
			Point pStart2 = composee.transformPoint(new Point(newFrame.width()+1,-i));
			Point pEnd2 = composee.transformPoint(new Point(-newFrame.width()-1,-i));
			g1.draw(new Line2D.Double(pStart2.x(),pStart2.y(),pEnd2.x(),pEnd2.y()));
		}
		
		// Dessine les axes
		Point abs1 = composee.transformPoint(new Point(newFrame.width()+1,0));
		Point abs2 = composee.transformPoint(new Point(-newFrame.width()-1,0));
		Line2D.Double abs = new Line2D.Double(abs1.x(), abs1.y(), abs2.x(), abs2.y());
		Point ord1 = composee.transformPoint(new Point(0, newFrame.height()+1));
		Point ord2 = composee.transformPoint(new Point(0, -newFrame.height()-1));
		Line2D.Double ord = new Line2D.Double(ord1.x(), ord1.y(), ord2.x(), ord2.y());
		
		g1.setColor(Color.white);
		g1.draw(abs);
		g1.draw(ord);
		
		// Dessine les transformations
		for (int i = 0; i < flameBuilder.transformationCount(); i++) {
			g1.setColor(Color.black);
			if (i == getHighlightedTransformationIndex()){
				g1.setColor(Color.red);
			}
			AffineTransformation t = flameBuilder.affineTransformation(i);
			Point f1p1 = composee.transformPoint(t.transformPoint(new Point(-1, 0)));
			Point f1p2 = composee.transformPoint(t.transformPoint(new Point(1, 0)));
			Point f1m1p = composee.transformPoint(t.transformPoint(new Point(0.9,0.1)));
			Point f1m2p = composee.transformPoint(t.transformPoint(new Point(0.9, -0.1)));
			g1.draw(new Line2D.Double(f1p1.x(),f1p1.y(),f1p2.x(),f1p2.y()));
			g1.draw(new Line2D.Double(f1m1p.x(),f1m1p.y(),f1p2.x(),f1p2.y()));
			g1.draw(new Line2D.Double(f1m2p.x(),f1m2p.y(),f1p2.x(),f1p2.y()));
			
			Point f2p1 = composee.transformPoint(t.transformPoint(new Point(0, -1)));
			Point f2p2 = composee.transformPoint(t.transformPoint(new Point(0, 1)));
			Point f2m1p = composee.transformPoint(t.transformPoint(new Point(0.1,0.9)));
			Point f2m2p = composee.transformPoint(t.transformPoint(new Point(-0.1, 0.9)));
			g1.draw(new Line2D.Double(f2p1.x(),f2p1.y(),f2p2.x(),f2p2.y()));
			g1.draw(new Line2D.Double(f2m1p.x(),f2m1p.y(),f2p2.x(),f2p2.y()));
			g1.draw(new Line2D.Double(f2m2p.x(),f2m2p.y(),f2p2.x(),f2p2.y()));
		}
			
	
	}

	public int getHighlightedTransformationIndex() {
		return highlightedTransformationIndex;
	}

	public void setHighlightedTransformationIndex(
			int highlightedTransformationIndex) {
		this.highlightedTransformationIndex = highlightedTransformationIndex;
		this.repaint();
	}
}
