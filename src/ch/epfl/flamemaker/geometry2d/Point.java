package ch.epfl.flamemaker.geometry2d;

/**
 * Classe modelisant un point avec deux double (x et y)
 * @see {@link #Point(double, double) Le constructeur Point()}
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class Point {
	public final static Point ORIGIN = new Point(0, 0);
	private final double x;
	private final double y;

	/**
	 * Constructeur de la classe Point
	 * @param unX La coordonnee du point sur l'abscisse.
	 * @param unY La coordonnee du point sur l'ordonnee.
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

	/** 
	 * Methode qui retourne le module du point par rapport a l'origine.
	 * @return Le module du point. 
	 */

	public double r() {
		return (Math.sqrt(this.x * this.x + this.y * this.y));
	}

	/**
	 * Methode qui retourne l'angle theta du point par rapport a l'origine
	 * @return L'angle du point
	 */
	public double theta() {
		return Math.atan2(this.y, this.x);
	}

	/**
	 * Methode qui retourne un String representant le point par ses deux coordonees x et y. 
	 * <br>Eex: (1.5, 6.3)
	 * @return Un string representant les coordonnees du point.
	 */
	public String toString() {
		return ("(" + this.x + ", " + this.y + ")");
	}
}
