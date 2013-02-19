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
	public boolean contains(Point p){
		// Strictement inf�rieur � x et y MAX
		// Sup�rieur ou �gal � x et y MIN
		if (p.x() < this.right() || p.x() >= this.left()){
			if (p.y() < this.top() || p.x() >= this.bottom()){
				return true;
			}
		}
		return false;
	}
	/**
	 * Ratio largeur/hauteur
	 * @return ratio
	 */
	public double aspectRatio(){
		return this.width/this.height;
	}
	
	public Rectangle expandToAspectRatio(doule aspectRatio){
		//TODO retourne le plus petit rectangle ayant le m�me centre que le r�cepteur, le rapport largeur/hauteur aspectRatio et contenant totalement le r�cepteur (c-�-d que tout point contenu dans le r�cepteur est �galement contenu dans le rectangle retourn�). L�ve l'exception IllegalArgumentException si le rapport pass� est n�gatif ou nul.
	}
	
	
	
	// Getters
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
	public double width(){
		return this.width;
	}
	public double height(){
		return this.height;
	}
	public Point center(){
		return this.center;
	}
}
