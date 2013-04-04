package ch.epfl.flamemaker.geometry2d;

public interface Transformation {
	/**
	 * M�thode transformant un point p selon les caract�ristiques de la transformation.
	 * @param p Le point � transformer
	 * @return Le point transform�
	 */
	Point transformPoint(Point p);
}
