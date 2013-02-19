package ch.epfl.flamemaker.geometry2d;

public class Rectangle {
	Point center;
	double width, height;
	/**
	 * Classe qui repr�sente un rectangle g�om�trique.
	 * @param center Le point central du rectangle
	 * @param width La largeur du rectangle
	 * @param height La hauteur du rectangle
	 */
	public Rectangle(Point center, double width, double height) {
		this.center = center;
		
		if (width < 0){
			throw new IllegalArgumentException("La largeur est n�gative.");
		}
		else{
			this.width = width;
		}
		if (height < 0){
			throw new IllegalArgumentException("La hauteur est n�gative.");
		}
		else{
			this.height = height;
		}
	}
}
