package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameBuilderPreviewComponent extends JComponent{
	private Flame.Builder flameBuilder;
	private Color background;
	private Palette palette;
	private Rectangle frame;
	private int density;
	
	private Flame flame;
	public FlameBuilderPreviewComponent(Flame.Builder flameBuilder, Color background, Palette palette, Rectangle frame, int density){
		this.flameBuilder = flameBuilder;
		this.background = background;
		this.palette = palette;
		this.frame = frame;
		this.density = density;
		this.flame = this.flameBuilder.build();
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(178,135);
	}
	@Override
	public void paintComponent(Graphics g0){
		Rectangle recComp = new Rectangle(new Point(getWidth()/2,getHeight()/2),getWidth(), getHeight());
		Rectangle rec = frame.expandToAspectRatio(recComp.aspectRatio());
		
		BufferedImage buffIm = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		FlameAccumulator flameAc = flame.compute(rec, getWidth(), getHeight(), density);
		
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				Color c = flameAc.color(palette, background, j, i);
				buffIm.setRGB(j, i, c.asPackedRGB());
			}	
		}
		g0.drawImage(buffIm, 0, 0, null);
	}
}
