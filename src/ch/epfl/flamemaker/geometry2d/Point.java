package ch.epfl.flamemaker.geometry2d;

/**
 * Classe modélisant un point avec deux double (x et y)
 * @see {@link #Point(double, double) Le constructeur Point()}
 */

public class Point {
	public final static Point ORIGIN = new Point(0, 0);
	private final double x;
	private final double y;
	
	
	/**
	 * Constructeur de la classe Point
	 * @param unX La coordonnée du point sur l'abscisse.
	 * @param unY La coordonnée du point sur l'ordonnée.
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/** Getter de X */
	public double x() {
		return this.x;
	}

	/** Getter de Y */
	public double y() {
		return this.y;
	}

	/** Méthode qui retourne le module du point par rapport à l'origine
	 * @return Le module du point. 
	 */
	
	public double r() {
		return (Math.sqrt(this.x * this.x + this.y * this.y));
	}

	/** Méthode qui retourne l'angle théta du point par rapport à l'origine
	 * @return L'angle du point
	 */
	public double theta() {
		return Math.atan2(this.y, this.x);
	}

	/**
	 * Méthode qui retourne un String représentant le point par ses deux
	 * coordonées x et y <br>
	 * ex: (1.5, 6.3)
	 * @return Un string représentant les coordonnées du point.
	 */
	public String toString() {
		return ("(" + this.x + ", " + this.y + ")");
	}
}
