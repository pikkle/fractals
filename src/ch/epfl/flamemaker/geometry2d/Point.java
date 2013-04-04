package ch.epfl.flamemaker.geometry2d;

/**
 * Classe modélisant un point avec deux double (x et y)
 * @see {@link #Point(double, double) Le constructeur Point()}
 */

public class Point {
	private final double X;
	private final double Y;
	public final static Point ORIGIN = new Point(0, 0);
	
	/**
	 * Constructeur de la classe Point
	 * @param unX La coordonnée du point sur l'abscisse.
	 * @param unY La coordonnée du point sur l'ordonnée.
	 */
	public Point(double unX, double unY) {
		this.X = unX;
		this.Y = unY;
	}

	/** Getter de X */
	public double x() {
		return this.X;
	}

	/** Getter de Y */
	public double y() {
		return this.Y;
	}

	/** Méthode qui retourne le module du point par rapport à l'origine
	 * @return Le module du point. 
	 */
	
	public double r() {
		return (Math.sqrt(this.X * this.X + this.Y * this.Y));
	}

	/** Méthode qui retourne l'angle théta du point par rapport à l'origine
	 * @return L'angle du point
	 */
	public double theta() {
		return Math.atan2(this.Y, this.X);
	}

	/**
	 * Méthode qui retourne un String représentant le point par ses deux
	 * coordonées x et y ex: (1.5, 6.3)
	 * @return Un string représentant les coordonnées du point.
	 */
	public String toString() {
		return ("(" + this.X + ", " + this.Y + ")");
	}
}
