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
	
	
	/**
	 * M�thode permettant de d�finir si un point est contenu dans le rectangle.
	 * @param p Le point � v�rifier
	 * @return Un bool�en qui indique l'appartenance du point dans le rectangle.
	 */
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
	
	
	/**
	 * M�thode cr�ant un rectangle de taille sup�rieure ou �gale au rectangle appel�.
	 * Le nouveau rectangle a le m�me point central que le rectangle d'origine.
	 * @param aspectRatio Le ratio du nouveau rectangle.
	 * @return Le rectangle ainsi cr��.
	 */
	public Rectangle expandToAspectRatio(double aspectRatio){
		if (aspectRatio <= 0){
			throw new IllegalArgumentException("Le ratio ne peut pas �tre n�gatif.");
		}
		if (aspectRatio == this.aspectRatio()){
			return new Rectangle(this.center, this.width, this.width);
		}
		double width = 0, height = 0; // Les valeurs du rectangle modifi�
		if (aspectRatio == 1){
			//Carr� demand�, prend le plus grand c�t�.
			height = Math.max(this.height, width);
			width = height;
			}
		else if (aspectRatio > 1){
			//Rectangle horizontal demand�
			height = this.height;
			width = aspectRatio/height;
		}
		else{
			//Rectangle vertical demand�
			width = this.width;
			height = width/aspectRatio;
		}
		return new Rectangle(this.center, width, height);
	}
	
	/**
	 * Donne les donn�es du rectangle en String sous le format:
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
