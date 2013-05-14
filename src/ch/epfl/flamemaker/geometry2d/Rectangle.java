package ch.epfl.flamemaker.geometry2d;
/**
 * Classe modélisant un rectangle géométrique.
 * @see {@link #Rectangle(Point, double, double) Le constructeur Rectangle()}
 */
public class Rectangle {
	private Point center;
	private double width, height;
	
	/**
	 * Constructeur du rectangle.
	 * @param center Le {@link Point} central du rectangle
	 * @param width La largeur du rectangle
	 * @param height La hauteur du rectangle
	 * @throws IllegalArgumentException si la largeur ou la hauteur sont négatives.
	 */
	public Rectangle(Point center, double width, double height) {
		this.center = center;
		if (width < 0 || height < 0){
			throw new IllegalArgumentException("La largeur ou la hauteur est négative. (w = " + width + ", h = "+ height +")");
		}
		else{
			this.width = width;
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
		return (p.x() < this.right() && p.x() >= this.left() 
				&& p.y() < this.top() && p.y() >= this.bottom());
	}
	
	
	/**
	 * Donne le ratio largeur sur hauteur
	 * @return Le ratio largeur/hauteur
	 */
	public double aspectRatio(){
		return this.width/this.height;
	}
	
	
	/**
	 * Méthode créant un rectangle de taille supérieure ou égale au rectangle appelé.
	 * Le nouveau rectangle a le même {@link Point} central que le rectangle d'origine.
	 * @param aspectRatio Le ratio du nouveau rectangle.
	 * @return Le rectangle redimensionné.
	 */
	public Rectangle expandToAspectRatio(double aspectRatio){
		if (aspectRatio <= 0){
			throw new IllegalArgumentException("Le ratio ne peut pas être négatif.");
		}
		double width = Math.max(this.width, this.height*aspectRatio);
		double height = Math.max(this.height, this.width/aspectRatio);
		
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
	public double width(){return this.width;}
	public double height(){return this.height;}
	public Point center(){return this.center;}
}
