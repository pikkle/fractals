package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.Flame.Builder;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameMakerGUI {
	private Flame.Builder flameBuilder = new Builder(new Flame(Arrays.asList(new FlameTransformation[] {
			new FlameTransformation(new AffineTransformation(-0.4113504,
					-0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
					new double[] { 1, 0.1, 0, 0, 0, 0 }),
			new FlameTransformation(new AffineTransformation(-0.3957339, 0,
					-1.6, 0, -0.3957337, 0.2), 
					new double[] { 0, 0, 0, 0, 0.8,	1 }),
			new FlameTransformation(new AffineTransformation(0.4810169, 0, 1,
					0, 0.4810169, 0.9), 
					new double[] { 1, 0, 0, 0, 0, 0 })}
			)));	
	private Color background = Color.BLACK;
	private Palette palette = new InterpolatedPalette(
			Arrays.asList(new Color[] { Color.RED, Color.GREEN, Color.BLUE }));
	private Rectangle frame = new Rectangle(new Point(-0.25, 0), 5, 4);
	private int density = 1; //50

	public void start() {
		JFrame frame = new JFrame("Flame Maker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panneauSuperieur = new JPanel();
		frame.getContentPane().add(panneauSuperieur);
		panneauSuperieur.setLayout(new GridLayout(1,2));
		
		
		JPanel transPanel = new JPanel();
		panneauSuperieur.add(transPanel);
		transPanel.setLayout(new BorderLayout());
		Border transBorder = BorderFactory.createTitledBorder("Transformations affines");
		transPanel.setBorder(transBorder);
		AffineTransformationsComponent atc = new AffineTransformationsComponent(this.flameBuilder, this.frame);
		transPanel.add(atc);
		
		JPanel fracPanel = new JPanel();
		panneauSuperieur.add(fracPanel);
		fracPanel.setLayout(new BorderLayout());
		Border fracBorder = BorderFactory.createTitledBorder("Fractale");
		fracPanel.setBorder(fracBorder);
		FlameBuilderPreviewComponent fbpc = new FlameBuilderPreviewComponent(this.flameBuilder, this.background, this.palette, this.frame, this.density);
		fracPanel.add(fbpc,BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
}
