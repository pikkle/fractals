package ch.epfl.flamemaker.geometry2d;

/**
 * Classe modelisant un rectangle geometrique.
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see {@link #Rectangle(Point, double, double) Le constructeur Rectangle()}
 */
public class Rectangle {
	private Point center;
	private double width, height;

	/**
	 * Constructeur du rectangle.
	 *
	 * @param center Le {@link Point} central du rectangle
	 * @param width  La largeur du rectangle
	 * @param height La hauteur du rectangle
	 * @throws IllegalArgumentException si la largeur ou la hauteur sont negatives.
	 */
	public Rectangle(Point center, double width, double height) {
		this.center = center;
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("La largeur ou la hauteur est negative. " +
					"(w = " + width + ", h = " + height + ")");
		} else {
			this.width = width;
			this.height = height;
		}
	}


	/**
	 * Methode permettant de definir si un point est contenu dans le rectangle.
	 *
	 * @param p Le point a verifier
	 * @return Un booleen qui indique l'appartenance du point dans le rectangle.
	 */
	public boolean contains(Point p) {
		return (p.x() < this.right() && p.x() >= this.left()
				&& p.y() < this.top() && p.y() >= this.bottom());
	}


	/**
	 * Donne le ratio largeur sur hauteur
	 *
	 * @return Le ratio largeur/hauteur
	 */
	public double aspectRatio() {
		return this.width / this.height;
	}


	/**
	 * Methode creant un rectangle de taille superieure ou egale au rectangle appele.
	 * Le nouveau rectangle a le meme {@link Point} central que le rectangle d'origine.
	 *
	 * @param aspectRatio Le ratio du nouveau rectangle.
	 * @return Le rectangle redimensionne.
	 */
	public Rectangle expandToAspectRatio(double aspectRatio) {
		if (aspectRatio <= 0) {
			throw new IllegalArgumentException("Le ratio ne peut pas etre negatif.");
		}
		double width = Math.max(this.width, this.height * aspectRatio);
		double height = Math.max(this.height, this.width / aspectRatio);

		return new Rectangle(this.center, width, height);
	}

	/**
	 * Donne les donnees du rectangle en String sous le format:
	 * <br>((centre.x, centre.y),largeur, hauteur)
	 */
	public String toString() {
		return ("(" + this.center + "," + this.width + "," + this.height + ")");
	}

	public double left() {
		return center.x() - (width / 2);
	}

	public double right() {
		return center.x() + (width / 2);
	}

	public double bottom() {
		double y = this.center.y() - (this.height / 2);
		return y;
	}

	public double top() {
		double y = this.center.y() + (this.height / 2);
		return y;
	}

	public double width() {
		return this.width;
	}

	public double height() {
		return this.height;
	}

	public Point center() {
		return this.center;
	}
}
