package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameBuilderPreviewComponent extends JComponent{
	private static final long serialVersionUID = 1L;
	private ObservableFlameBuilder flameBuilder;
	private Color background;
	private Palette palette;
	private Rectangle frame;
	private int density;
	private double d;
	private boolean useMultiThread;
	
	public FlameBuilderPreviewComponent(ObservableFlameBuilder flameBuilder, Color background, Palette palette, Rectangle frame, int density){
		this.flameBuilder = flameBuilder;
		this.background = background;
		this.palette = palette;
		this.frame = frame;
		this.density = density;
		this.d = 1;
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(178,135);
	}
	public void setZoom(double d){
		if(d <= 0){
			throw new IllegalArgumentException("Le facteur de zoom d doit être strictement positif.");
		}
		this.d = d;
		repaint();
	}
	public double getZoom(){
		return d;
	}
	public void setUseMultiThread(boolean b){
		useMultiThread = b;
	}
	@Override
	public void paintComponent(Graphics g0){
		Flame flame = this.flameBuilder.build();
		Rectangle recComp = new Rectangle(new Point(getWidth()/2,getHeight()/2),getWidth(), getHeight());
		Rectangle rec = frame.expandToAspectRatio(recComp.aspectRatio());
		
		BufferedImage buffIm = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		FlameAccumulator flameAc;
		if (useMultiThread){
			flameAc = flame.compute(rec,(int)(getWidth()*d),(int)(getHeight()*d),density, 20);
		}
		else {
			flameAc = flame.compute(rec,(int)(getWidth()*d),(int)(getHeight()*d),density);
		}
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				Color c = flameAc.color(palette, background, j, i);
				buffIm.setRGB(j, i, c.asPackedRGB());
			}	
		}
		g0.drawImage(buffIm, 0, 0, null);
	}
}
