package ch.epfl.flamemaker.geometry2d;

/**
 * classe créant un point avec deux double (x et y)
 * 
 * @param X
 *            la coordonnée de l'axe des x
 * @param Y
 *            la coordonnée de l'axe des y
 */
public class Point {
	private final double X;
	private final double Y;
	public final static Point ORIGIN = new Point(0, 0);

	public Point(double unX, double unY) {
		this.X = unX;
		this.Y = unY;
	}

	/** getter de X */
	public double x() {
		return this.X;
	}

	/** getter de Y */
	public double y() {
		return this.Y;
	}

	/** Méthode qui retourne la coordonée r du point (son module) */
	public double r() {
		return (Math.sqrt(this.X * this.X + this.Y * this.Y));
	}

	/** Méthode qui retourne l'angle théta du point */
	public double theta() {
		return Math.atan2(this.X, this.Y);
	}

	/**
	 * Méthode qui retourne un String représentant le point par ses deux
	 * coordonées x et y ex: (1.5, 6.3)
	 */
	public String toString() {
		return ("(" + this.X + ", " + this.Y + ")");
	}
}
