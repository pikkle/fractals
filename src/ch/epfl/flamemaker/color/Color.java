package ch.epfl.flamemaker.color;

/**
 * Classe modelisant une couleur avec ses composantes Rouge, Verte et Bleue
 * <br>Voir le constructeur {@link #Color(double, double, double) Color()}
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public final class Color {
	double r,g,b;
	final static public Color BLACK = new Color(0,0,0);
	final static public Color WHITE = new Color(1,1,1);
	final static public Color RED = new Color(1,0,0);
	final static public Color GREEN = new Color(0,1,0);
	final static public Color BLUE = new Color(0,0,1);

	/**
	 * Constructeur de couleur avec ses composantes RGB
	 * (Les composantes sont comprises entre 0 et 1)
	 * @param r Composante rouge
	 * @param g Composante verte
	 * @param b Composante bleue
	 * @throws IllegalArgumentException si les composantes de couleur ne sont pas valides
	 */
	public Color(double r, double g, double b){
		if (r < 0 || r > 1 || g < 0 || g > 1 || b < 0 || b > 1){
			throw new IllegalArgumentException("Les composantes de couleur donnees ne sont pas valides.");
		}
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public double red(){
		return this.r;}
	public double green(){
		return this.g;}
	public double blue(){
		return this.b;}

	/**
	 * Melange la couleur avec la couleur passee en parametre.
	 * La proportion s'applique sur la couleur appelee (et donc, le reste de la proportion sur la couleur en parametre)
	 * @param that La couleur qui se melange avec la couleur appelee.
	 * @param proportion La proportion de la couleur de base.
	 * @return La couleur melangee.
	 */
	public Color mixWith(Color that, double proportion){
		if (proportion < 0 || proportion > 1){
			throw new IllegalArgumentException("La proportion donnee n'est pas valide. = " + proportion + ")");
		}
		double r2, g2, b2;
		//Additionne les composantes des deux couleurs avec les proportions adequates.
		r2 = proportion*this.r + (1-proportion)*that.r;
		g2 = proportion*this.g + (1-proportion)*that.g;
		b2 = proportion*this.b + (1-proportion)*that.b;

		return new Color(r2,g2,b2);
	}

	/**
	 * Retourne la couleur encodee dans un entier.
	 * Chaque couleur occupe 8 bits et sont rangees dans l'ordre Rouge Vert Bleu.
	 * Les composantes sont encodees via la methode {@link #sRGBEncode(double, int) sRGBEncode()}
	 * @return L'entier representant la couleur.
	 */
	public int asPackedRGB(){
		int rInt = sRGBEncode(this.r, 255)<<16;
		int gInt = sRGBEncode(this.g, 255)<<8;
		int bInt = sRGBEncode(this.b, 255);
		return (rInt|gInt|bInt);


	}

	/**
	 * Calcule la valeur gamma encodee selon la norme sRGB.
	 * @param v La valeur a encoder
	 * @param max La valeur maximale de l'entier a retourner
	 * @return La valeur encodee.
	 */
	public static int sRGBEncode(double v, int max){
		// Condition ternaire qui retourne une valeur ou l'autre selon la valeur de v.
		return (v <= 0.0031308) ? (int) (max*(12.92*v)) // si v est plus petit que 0.0031308
				: (int) (max*((1.055*(Math.pow(v,(1/2.4)))-0.055))) ; //si v est plus grand que 0.0031308
	}
}
