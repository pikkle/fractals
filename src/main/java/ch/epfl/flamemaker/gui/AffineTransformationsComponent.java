package ch.epfl.flamemaker.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe modelisant un JComponent permettant d'afficher
 * dans une grille les transformations affines d'une fractale flame
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see #AffineTransformationsComponent(ObservableFlameBuilder, Rectangle)
 * @see JComponent
 */
public class AffineTransformationsComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private ObservableFlameBuilder flameBuilder;
	private Rectangle frame;
	private int highlightedTransformationIndex;

	/**
	 * Constructeur du component des transformations affine
	 *
	 * @param flameBuilder Le FlameBuilder correspondant à la fractale
	 * @param frame        Le cadre délimitant la fractale
	 */
	public AffineTransformationsComponent(ObservableFlameBuilder flameBuilder,
	                                      Rectangle frame) {
		this.flameBuilder = flameBuilder;
		this.frame = frame;
	}

	/**
	 * Utilise le contexte graphique de Swing pour afficher le grillage du plan
	 * et toutes les transformations existantes dans le flameBuilder.
	 */
	@Override
	public void paintComponent(Graphics g0) {
		Graphics2D g1 = (Graphics2D) g0;

		Rectangle newFrame = frame.expandToAspectRatio((double) getWidth() / (double) getHeight());
		AffineTransformation translation = AffineTransformation.newTranslation(getWidth() / 2 - newFrame.center().x(),
				getHeight() / 2 - newFrame.center().y());
		AffineTransformation upscale = AffineTransformation.newScaling(getWidth() / newFrame.width(), -getHeight() / newFrame.height());
		AffineTransformation composee = translation.composeWith(upscale);


		g1.setColor(new Color((float) 0.9, (float) 0.9, (float) 0.9));
		for (int i = 0; i < newFrame.width(); i++) { // Dessine les axes verticaux
			Point pStart1 = composee.transformPoint(new Point(i, newFrame.height() + 1));
			Point pEnd1 = composee.transformPoint(new Point(i, -newFrame.height() - 1));
			g1.draw(new Line2D.Double(pStart1.x(), pStart1.y(), pEnd1.x(), pEnd1.y()));

			Point pStart2 = composee.transformPoint(new Point(-i, newFrame.height() + 1));
			Point pEnd2 = composee.transformPoint(new Point(-i, -newFrame.height() - 1));
			g1.draw(new Line2D.Double(pStart2.x(), pStart2.y(), pEnd2.x(), pEnd2.y()));
		}
		for (int i = 0; i < newFrame.height(); i++) { // Dessine les axes horizontaux
			Point pStart1 = composee.transformPoint(new Point(newFrame.width() + 1, i));
			Point pEnd1 = composee.transformPoint(new Point(-newFrame.width() - 1, i));
			g1.draw(new Line2D.Double(pStart1.x(), pStart1.y(), pEnd1.x(), pEnd1.y()));

			Point pStart2 = composee.transformPoint(new Point(newFrame.width() + 1, -i));
			Point pEnd2 = composee.transformPoint(new Point(-newFrame.width() - 1, -i));
			g1.draw(new Line2D.Double(pStart2.x(), pStart2.y(), pEnd2.x(), pEnd2.y()));
		}

		// Dessine l'axe des abscisses
		Point abs1 = composee.transformPoint(new Point(newFrame.width() + 1, 0));
		Point abs2 = composee.transformPoint(new Point(-newFrame.width() - 1, 0));
		Line2D.Double abs = new Line2D.Double(abs1.x(), abs1.y(), abs2.x(), abs2.y());
		// Dessine l'axe des ordonnées
		Point ord1 = composee.transformPoint(new Point(0, newFrame.height() + 1));
		Point ord2 = composee.transformPoint(new Point(0, -newFrame.height() - 1));
		Line2D.Double ord = new Line2D.Double(ord1.x(), ord1.y(), ord2.x(), ord2.y());

		g1.setColor(Color.white);
		g1.draw(abs);
		g1.draw(ord);

		// Dessine les transformations
		for (int i = 0; i < flameBuilder.transformationCount(); i++) {
			g1.setColor(Color.black);
			if (i == getHighlightedTransformationIndex()) {
				g1.setColor(Color.red);
				// Modifie la couleur en rouge pour la transformation sélectionnée par l'utilisateur
			}
			AffineTransformation t = flameBuilder.affineTransformation(i);

			// Première flèche allant du point f1p1 à f1p2 
			// avec les deux points f1m1 et f1m2 créant l'empennage 
			Point f1p1 = composee.transformPoint(t.transformPoint(new Point(-1, 0)));
			Point f1p2 = composee.transformPoint(t.transformPoint(new Point(1, 0)));
			Point f1m1 = composee.transformPoint(t.transformPoint(new Point(0.9, 0.1)));
			Point f1m2 = composee.transformPoint(t.transformPoint(new Point(0.9, -0.1)));
			g1.draw(new Line2D.Double(f1p1.x(), f1p1.y(), f1p2.x(), f1p2.y())); // Ligne principale
			g1.draw(new Line2D.Double(f1m1.x(), f1m1.y(), f1p2.x(), f1p2.y())); // Empennage 1
			g1.draw(new Line2D.Double(f1m2.x(), f1m2.y(), f1p2.x(), f1p2.y())); // Empennage 2

			// Seconde flèche allant du point f2p1 à f2p2 
			// avec les deux points f2m1 et f2m2 créant l'empennage 
			Point f2p1 = composee.transformPoint(t.transformPoint(new Point(0, -1)));
			Point f2p2 = composee.transformPoint(t.transformPoint(new Point(0, 1)));
			Point f2m1 = composee.transformPoint(t.transformPoint(new Point(0.1, 0.9)));
			Point f2m2 = composee.transformPoint(t.transformPoint(new Point(-0.1, 0.9)));
			g1.draw(new Line2D.Double(f2p1.x(), f2p1.y(), f2p2.x(), f2p2.y())); // Ligne principale
			g1.draw(new Line2D.Double(f2m1.x(), f2m1.y(), f2p2.x(), f2p2.y())); // Empennage 1
			g1.draw(new Line2D.Double(f2m2.x(), f2m2.y(), f2p2.x(), f2p2.y())); // Empennage 2
		}


	}

	/**
	 * Donne l'index de la transformation selectionnée
	 *
	 * @return
	 */
	public int getHighlightedTransformationIndex() {
		return highlightedTransformationIndex;
	}

	/**
	 * Change l'index de la transformation sélectionnée.
	 * <br>Cela cause une mise-à-jour de l'affichage graphique
	 * du component avec la méthode {@link #repaint()}
	 *
	 * @param highlightedTransformationIndex Le nouvel index de transformation sélectionnée
	 */
	public void setHighlightedTransformationIndex(
			int highlightedTransformationIndex) {
		this.highlightedTransformationIndex = highlightedTransformationIndex;
		this.repaint();
	}
}
