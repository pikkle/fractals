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
	
	
	/**
	 * Méthode permettant de définir si un point est contenu dans le rectangle.
	 * @param p Le point à vérifier
	 * @return Un booléen qui indique l'appartenance du point dans le rectangle.
	 */
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
	
	
	/**
	 * Méthode créant un rectangle de taille supérieure ou égale au rectangle appelé.
	 * Le nouveau rectangle a le même point central que le rectangle d'origine.
	 * @param aspectRatio Le ratio du nouveau rectangle.
	 * @return Le rectangle ainsi créé.
	 */
	public Rectangle expandToAspectRatio(double aspectRatio){
		if (aspectRatio <= 0){
			throw new IllegalArgumentException("Le ratio ne peut pas être négatif.");
		}
		if (aspectRatio == this.aspectRatio()){
			return new Rectangle(this.center, this.width, this.width);
		}
		double width = 0, height = 0; // Les valeurs du rectangle modifié
		if (aspectRatio == 1){
			//Carré demandé, prend le plus grand côté.
			height = Math.max(this.height, width);
			width = height;
			}
		else if (aspectRatio > 1){
			//Rectangle horizontal demandé
			height = this.height;
			width = aspectRatio/height;
		}
		else{
			//Rectangle vertical demandé
			width = this.width;
			height = width/aspectRatio;
		}
		return new Rectangle(this.center, width, height);
	}
	
	/**
	 * Donne les données du rectangle en String sous le format:
	 * ((centre.x, centre.y),largeur, hauteur)
	 */
	public String toString(){
		return ("(("+this.center.x()+","+this.center.y()+")"+this.width+","+this.height+")");
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
