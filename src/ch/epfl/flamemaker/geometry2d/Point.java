package ch.epfl.flamemaker.geometry2d;

/**
 * classe cr�ant un point avec deux double (x et y)
 * 
 * @param X
 *            la coordonn�e de l'axe des x
 * @param Y
 *            la coordonn�e de l'axe des y
 */
public class Point {
	private final double X;
	private final double Y;
	public final static Point ORIGIN = new Point(0, 0);
	
	/**
	 * Constructeur de la classe Point
	 * @param unX La coordonn�e de x sur l'axe des abscisse.
	 * @param unY La coordonn�e de y sur l'axe des ordonn�e.
	 */
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

	/** M�thode qui retourne la coordon�e r du point (son module) */
	public double r() {
		return (Math.sqrt(this.X * this.X + this.Y * this.Y));
	}

	/** M�thode qui retourne l'angle th�ta du point */
	public double theta() {
		// Modif par Lo�c, il fallait inverser x et y selon la javadoc de Math.atan2(), thanks to JUnit
		return Math.atan2(this.Y, this.X);
	}

	/**
	 * M�thode qui retourne un String repr�sentant le point par ses deux
	 * coordon�es x et y ex: (1.5, 6.3)
	 */
	public String toString() {
		return ("(" + this.X + ", " + this.Y + ")");
	}
}
