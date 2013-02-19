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
	public boolean contains(Point p){
		// Strictement inférieur à x et y MAX
		// Supérieur ou égal à x et y MIN
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
		//TODO retourne le plus petit rectangle ayant le même centre que le récepteur, le rapport largeur/hauteur aspectRatio et contenant totalement le récepteur (c-à-d que tout point contenu dans le récepteur est également contenu dans le rectangle retourné). Lève l'exception IllegalArgumentException si le rapport passé est négatif ou nul.
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
