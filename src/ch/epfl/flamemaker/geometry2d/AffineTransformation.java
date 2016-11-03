package ch.epfl.flamemaker.geometry2d;

/**
 * Classe modelisant une transformation affine. <br>
 * La transformaiton affine est representee comme une matrice 3x3 
 * avec comme variable les deux premieres lignes de la matrice.<br>
 * La classe implemente l'interface {@link Transformation}.
 * @see {@link #AffineTransformation(double, double, double, double, double, double) Le constructeur AffineTransformation()}
 * @see Transformation
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public final class AffineTransformation implements Transformation {
	public static final AffineTransformation IDENTITY = new AffineTransformation(
			1, 0, 0, 0, 1, 0);
	private final double[][] transformation;


	/**
	 * Constructeur d'une transformation affine. Les differents parametres
	 * caracterisent la matrice de transformation 3x3 de la forme:<br>
	 * [ A , B , C ]<br>
	 * [ D , E , F ]<br>
	 * [ 0 , 0 , 1 ]
	 * @param a Le parametre A
	 * @param b Le parametre B
	 * @param c Le parametre C
	 * @param d Le parametre D
	 * @param e Le parametre E
	 * @param f Le parametre F
	 */
	public AffineTransformation(double a, double b, double c, double d,
			double e, double f) {
		transformation = new double[][] {{ a, b, c }, { d, e, f }, {0,0,1}};
	}

	/**
	 * Donne une transformation affine de type translation de x unites 
	 * parallelement a l'abscisse et y unites parallelement a l'ordonnee.
	 * @param dx La translation sur l'axe X
	 * @param dy La translation sur l'axe Y
	 * @return La translation sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newTranslation(double dx, double dy) {
		return new AffineTransformation(1, 0, dx, 0, 1, dy);
	}

	/**
	 * Donne une transformation affine de type rotation d'angle theta autour de l'origine.
	 * @param theta L'angle de rotation en radians
	 * @return La rotation sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newRotation(double theta) {
		double rad = Math.PI*(theta/180);
		return new AffineTransformation(Math.cos(rad), -Math.sin(rad), 0,
				Math.sin(rad), Math.cos(rad), 0);
	}

	/**
	 * Donne une transformation affine de type dilatation d'un facteur <b>sx</b> parallelement a l'abscisse
	 * et d'un facteur <b>sy</b> parallelement a l'ordonnee.
	 * @param sx Le dilatation sur l'axe X
	 * @param sy Le dilatation sur l'axe Y
	 * @return La dilatation sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newScaling(double sx, double sy) {
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}

	/**
	 * Donne une transformation affine de type transvection d'un facteur sx parallelement a l'abscisse.
	 * @param sx Le facteur de transvection sur l'axe X
	 * @return La transvection sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newShearX(double sx) {
		return new AffineTransformation(1, sx, 0, 0, 1, 0);
	}

	/**
	 * Donne une transformation affine de type transvection d'un facteur sy parallelement a l'ordonnee.
	 * @param sy Le facteur de transvection sur l'axe Y
	 * @return La transvection sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newShearY(double sy) {
		return new AffineTransformation(1, 0, 0, sy, 1, 0);
	}

	/**
	 * La transformation affine utilise la matrice de transformation 
	 * pour modifier un point sur le plan
	 */
	@Override
	public Point transformPoint(Point p) {
		double x, y;
		x = this.transformation[0][0] * p.x() + this.transformation[0][1]
				* p.y() + this.transformation[0][2];

		y = this.transformation[1][0] * p.x() + this.transformation[1][1]
				* p.y() + this.transformation[1][2];
		return new Point(x, y);
	}

	/**
	 * Determine la transformation affine composee avec le parametre <b>that</b>
	 * @param that La deuxieme transformation affine
	 * @return La transformation affine composee
	 */
	public AffineTransformation composeWith(AffineTransformation that) {
		double[][] newT = new double[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				newT[i][j] = 0.0;
				for (int k = 0; k < 3; k++) {
					newT[i][j] += this.transformation[i][k]
							* that.transformation[k][j];
				}
			}
		}
		return new AffineTransformation(newT[0][0],newT[0][1], newT[0][2],
				newT[1][0], newT[1][1],newT[1][2]);
	}

	/**
	 * Donne la translation sur l'axe des abscisses
	 * @return La translation X
	 */
	public double translationX() {
		return this.transformation[0][2];
	}

	/**
	 * Donne la translation sur l'axe des ordonnees
	 * @return La translation Y
	 */
	public double translationY() {
		return this.transformation[1][2];
	}

}
