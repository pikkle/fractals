package ch.epfl.flamemaker.geometry2d;

public interface Transformation {
	/**
	 * Méthode transformant un point p selon les caractéristiques de la transformation.
	 * @param p Le point à transformer
	 * @return Le point transformé
	 */
	Point transformPoint(Point p);
}
