package ch.epfl.flamemaker.geometry2d;
/**
 * Classe repr�sentant un rectangle g�om�trique.
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
	
	public double left(){
		double x = this.center.x()-(this.width/2);
		return x;
	}
	public double right(){
		double x = this.center.x()+(this.width/2);
		return x;
	}
	public double bottom(){
		double y = this.center.y()-(this.height/2);
		return y;
	}
	public double top(){
		double y = this.center.y()+(this.height/2);
		return y;
	}
}
