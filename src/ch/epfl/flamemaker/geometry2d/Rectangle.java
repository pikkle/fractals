package ch.epfl.flamemaker.geometry2d;
/**
 * Classe représentant un rectangle géométrique.
 */
public class Rectangle {
	Point center;
	double width, height;
	/**
	 * Constructeur du rectangle.
	 * @param center Le point central du rectangle
	 * @param width La largeur du rectangle
	 * @param height La hauteur du rectangle
	 */
	public Rectangle(Point center, double width, double height) {
		this.center = center;
		
		if (width < 0){
			throw new IllegalArgumentException("La largeur est négative.");
		}
		else{
			this.width = width;
		}
		if (height < 0){
			throw new IllegalArgumentException("La hauteur est négative.");
		}
		else{
			this.height = height;
		}
	}
}
