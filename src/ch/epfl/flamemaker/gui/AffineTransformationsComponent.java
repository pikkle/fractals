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
	
	@Override
	public void paintComponent(Graphics g0){
		Graphics2D g1 = (Graphics2D) g0;
	}
	
	public int getHighlightedTransformationIndex() {
		return highlightedTransformationIndex;
	}

	public void setHighlightedTransformationIndex(int highlightedTransformationIndex) {
		this.highlightedTransformationIndex = highlightedTransformationIndex;
		this.repaint();
	}
}
