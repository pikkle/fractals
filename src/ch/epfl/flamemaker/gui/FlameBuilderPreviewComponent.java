package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

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
	private boolean useMultiThread;

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
	 * Modifie si l'on utilise le MultiThread ou non
	 * @param b Un booleen qui indique si l'on utilise le multithread
	 */
	public void setUseMultiThread(boolean b){
		useMultiThread = b;
	}
	
	/**
	 * Change la palette de couleur de la fractale. 
	 * <br>Redessine le composant.
	 * @param p La nouvelle palette
	 */
	public void setPalette(Palette p) {
		palette = p;
		repaint();
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
		FlameAccumulator flameAc;

		long t1 = System.nanoTime();
		flameAc = (useMultiThread) ? flame.compute(rec,getWidth(),getHeight(),density, 20) : 
			flame.compute(rec,getWidth(),getHeight(),density); // Choisi selon useMultiThread l'utilisation
															   // du multithread ou non
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				Color c = flameAc.color(palette, background, j, i);
				buffIm.setRGB(j, i, c.asPackedRGB());
			}	
		}
		long t2 = System.nanoTime();
		long t = TimeUnit.MILLISECONDS.convert(t2-t1, TimeUnit.NANOSECONDS);
		
		g0.drawImage(buffIm, 0, 0, null);
		g0.setColor(java.awt.Color.LIGHT_GRAY);
		g0.drawString("" + t + "ms", 1, getHeight()-1); // Affiche le temps sur l'image
	}
}
