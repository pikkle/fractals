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

public class AffineTransformationsComponent extends JComponent{
	private Flame.Builder flameBuilder;
	private Rectangle frame;
	private int highlightedTransformationIndex;
	public AffineTransformationsComponent(Flame.Builder flameBuilder, Rectangle frame){
		this.flameBuilder = flameBuilder;
		this.frame = frame;
	}
	
	private void paintGrille(Graphics2D g0, AffineTransformation fTrans, Rectangle rec){
		//Defini l'axe des ordonnées (vertical)
		Point ordC1 = new Point(0, rec.top());
		Point ordC2 = new Point(0, rec.bottom());
		//Defini l'axe des absisses (horizontal)
		Point absC1 = new Point(rec.left(),0);
		Point absC2 = new Point(rec.right(), 0);
		
		g0.setColor(new Color((float)0.9, (float)0.9, (float)0.9));
		for (int i = 0; i < rec.width()+1; i++) {
			Point ord1 = fTrans.transformPoint(new Point(ordC1.x()+i,ordC1.y()));
			Point ord2 = fTrans.transformPoint(new Point(ordC2.x()+i,ordC2.y()));
			Point ord3 = fTrans.transformPoint(new Point(ordC1.x()-i,ordC1.y()));
			Point ord4 = fTrans.transformPoint(new Point(ordC2.x()-i,ordC2.y()));
			g0.draw(new Line2D.Double(ord1.x(),ord1.y(),ord2.x(),ord2.y()));
			g0.draw(new Line2D.Double(ord3.x(),ord3.y(),ord4.x(),ord4.y()));
		}
		for (int i = 0; i < rec.height()+1; i++) {
			Point abs1 = fTrans.transformPoint(new Point(absC1.x(),absC1.y()+i));
			Point abs2 = fTrans.transformPoint(new Point(absC2.x(),absC2.y()+i));
			Point abs3 = fTrans.transformPoint(new Point(absC1.x(),absC1.y()-i));
			Point abs4 = fTrans.transformPoint(new Point(absC2.x(),absC2.y()-i));
			g0.draw(new Line2D.Double(abs1.x(),abs1.y(),abs2.x(),abs2.y()));
			g0.draw(new Line2D.Double(abs3.x(),abs3.y(),abs4.x(),abs4.y()));
		}
		
		Point ordo1 = fTrans.transformPoint(ordC1); //horizontal
		Point ordo2 = fTrans.transformPoint(ordC2);
		Point abs1 = fTrans.transformPoint(absC1);//vertical
		Point abs2 = fTrans.transformPoint(absC2);
		
		g0.setColor(Color.white);
		g0.draw(new Line2D.Double(ordo1.x(),ordo1.y(),ordo2.x(),ordo2.y()));
		g0.draw(new Line2D.Double(abs1.x(),abs1.y(),abs2.x(),abs2.y()));
	}
	
	private void paintFleche(Graphics2D g0, Point p1, Point p2, Transformation trans){
		g0.setColor(Color.black);
		Point pt1 = trans.transformPoint(p1);
		Point pt2 = trans.transformPoint(p2);
		System.out.println(pt1 + ", " + pt2);
		g0.draw(new Line2D.Double(pt1.x(),pt1.y(),pt2.x(),pt2.y()));
	}
	
	@Override
	public void paintComponent(Graphics g0){
		Graphics2D g1 = (Graphics2D) g0;
		System.out.println(getWidth() + ", " + getHeight());
		Rectangle recComp = new Rectangle(new Point(getWidth()/2,getHeight()/2),getWidth(), getHeight()); // Représente le rectangle du component
		Rectangle rec = this.frame.expandToAspectRatio(recComp.aspectRatio()); //Frame adapté au ratio du component
		
		AffineTransformation upscaleCadre = AffineTransformation.newScaling(getWidth()/rec.width(), getHeight()/rec.height());
		AffineTransformation translationCadre = AffineTransformation.newTranslation(-rec.left(),-rec.bottom());
		AffineTransformation tuCadre = upscaleCadre.composeWith(translationCadre);
		
		paintGrille(g1, tuCadre, rec);
		for (int i = 0; i < flameBuilder.transformationCount(); i++) {
			AffineTransformation fTransf = flameBuilder.affineTransformation(i).composeWith(tuCadre);
			paintFleche(g1,new Point(-1,0),new Point(1,0), fTransf);
			paintFleche(g1,new Point(0,-1),new Point(0,1), fTransf);
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
