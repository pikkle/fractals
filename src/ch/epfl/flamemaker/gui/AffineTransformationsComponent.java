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
import ch.epfl.flamemaker.geometry2d.Transformation;

public class AffineTransformationsComponent extends JComponent {
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
		AffineTransformation translation = AffineTransformation.newTranslation(getWidth()/2-frame.center().x()*(getWidth()/newFrame.width()), 
				getHeight()/2 - frame.center().y()*(getHeight()/newFrame.height()));
		AffineTransformation upscale = AffineTransformation.newScaling(getWidth()/newFrame.width(),- getHeight()/newFrame.height());
		AffineTransformation composee = translation.composeWith(upscale);
		
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
		
		Point abs1 = composee.transformPoint(new Point(newFrame.width()+1,0));
		Point abs2 = composee.transformPoint(new Point(-newFrame.width()-1,0));
		Line2D.Double abs = new Line2D.Double(abs1.x(), abs1.y(), abs2.x(), abs2.y());
		Point ord1 = composee.transformPoint(new Point(0, newFrame.height()+1));
		Point ord2 = composee.transformPoint(new Point(0, -newFrame.height()-1));
		Line2D.Double ord = new Line2D.Double(ord1.x(), ord1.y(), ord2.x(), ord2.y());
		
		g1.setColor(Color.white);
		g1.draw(abs);
		g1.draw(ord);
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
