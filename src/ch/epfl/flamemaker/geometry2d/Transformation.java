package ch.epfl.flamemaker.geometry2d;

/**
 * Interface representant une transformation graphique
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public interface Transformation {
	/**
	 * Methode transformant un point p
	 *
	 * @param p Le point a transformer
	 * @return Le point transforme
	 */
	Point transformPoint(Point p);
}
