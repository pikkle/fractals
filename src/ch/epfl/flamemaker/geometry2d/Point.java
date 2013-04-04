package ch.epfl.flamemaker.geometry2d;

/**
 * Classe mod�lisant un point avec deux double (x et y)
 * @see {@link #Point(double, double) Le constructeur Point()}
 */

public class Point {
	private final double X;
	private final double Y;
	public final static Point ORIGIN = new Point(0, 0);
	
	/**
	 * Constructeur de la classe Point
	 * @param unX La coordonn�e du point sur l'abscisse.
	 * @param unY La coordonn�e du point sur l'ordonn�e.
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

	/** M�thode qui retourne le module du point par rapport � l'origine
	 * @return Le module du point. 
	 */
	
	public double r() {
		return (Math.sqrt(this.X * this.X + this.Y * this.Y));
	}

	/** M�thode qui retourne l'angle th�ta du point par rapport � l'origine
	 * @return L'angle du point
	 */
	public double theta() {
		return Math.atan2(this.Y, this.X);
	}

	/**
	 * M�thode qui retourne un String repr�sentant le point par ses deux
	 * coordon�es x et y ex: (1.5, 6.3)
	 * @return Un string repr�sentant les coordonn�es du point.
	 */
	public String toString() {
		return ("(" + this.X + ", " + this.Y + ")");
	}
}
