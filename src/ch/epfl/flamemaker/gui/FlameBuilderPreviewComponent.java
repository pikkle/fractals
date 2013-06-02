package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe modelisant un JComponent permettant d'afficher 
 * une prévisualisation de la fractale en couleurs.
 * @see #FlameBuilderPreviewComponent(ObservableFlameBuilder, Color, Palette, Rectangle, int)
 * @see JComponent
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class FlameBuilderPreviewComponent extends JComponent{
	private static final long serialVersionUID = 1L;
	private ObservableFlameBuilder flameBuilder;
	private Color background;
	private Palette palette;
	private Rectangle frame;
	private int density;
	
	/**
	 * Constructeur du component de prévisualisation de la fractale
	 * @param flameBuilder Le constructeur de la fractale flame à afficher
	 * @param background La couleur de fond de l'image
	 * @param palette La palette de couleurs caractérisant la fractale
	 * @param frame Le cadre délimitant la fractale
	 * @param density La densité de points de la fractale
	 */
	public FlameBuilderPreviewComponent(ObservableFlameBuilder flameBuilder, Color background, Palette palette, Rectangle frame, int density){
		this.flameBuilder = flameBuilder;
		this.background = background;
		this.palette = palette;
		this.frame = frame;
		this.density = density;
	}
	
	/**
	 * Méthode utilisée par Swing donnant la taille initiale préférée par le Component.
	 */
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(178,135);
	}
	
	/**
	 * Utilise le contexte graphique de Swing pour afficher la fractale
	 * comme forme d'image.
	 */
	@Override
	public void paintComponent(Graphics g0){
		Flame flame = this.flameBuilder.build();
		Rectangle rec = frame.expandToAspectRatio((double)getWidth()/(double)getHeight());
		
		BufferedImage buffIm = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		FlameAccumulator flameAc = flame.compute(rec,getWidth(),(int)getHeight(),density);
		
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				Color c = flameAc.color(palette, background, j, i);
				buffIm.setRGB(j, i, c.asPackedRGB());
			}	
		}
		g0.drawImage(buffIm, 0, 0, null);
	}
}
