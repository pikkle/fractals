package ch.epfl.flamemaker.geometry2d;

/**
 * Classe mod�lisant une transformation affine. <br>
 * La transformaiton affine est repr�sent�e comme une matrice 3x3 avec comme variable les deux premi�res lignes de la matrice.<br>
 * La classe impl�mente l'interface {@link Transformation}.
 * @see {@link #AffineTransformation(double, double, double, double, double, double) Le constructeur AffineTransformation()}
 * @see Transformation
 */
public final class AffineTransformation implements Transformation {
	private final double[][] transformation;
	public static final AffineTransformation IDENTITY = new AffineTransformation(
			1, 0, 0, 0, 1, 0);

	/**
	 * Constructeur d'une transformation affine. Les diff�rents param�tres caract�risent la matrice de transformation 3x3 de la forme:<br>
	 * [ A , B , C ]<br>
	 * [ D , E , F ]<br>
	 * [ 0 , 0 , 1 ]
	 * @param a Le param�tre A
	 * @param b Le param�tre B
	 * @param c Le param�tre C
	 * @param d Le param�tre D
	 * @param e Le param�tre E
	 * @param f Le param�tre F
	 */
	public AffineTransformation(double a, double b, double c, double d,
			double e, double f) {
		transformation = new double[][] { { a, b, c }, { d, e, f }, { 0, 0, 1 } };
	}

	/**
	 * Donne une transformation affine de type translation de x unit�s parall�lement � l'abscisse et y unit�s parall�lement � l'ordonn�e.
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
		return new AffineTransformation(Math.cos(theta), -Math.sin(theta), 0,
				Math.sin(theta), Math.cos(theta), 0);
	}

	/**
	 * Donne une transformation affine de type dilatation d'un facteur sx parall�lement � l'abscisse et d'un facteur sy parall�lement � l'ordonn�e.
	 * @param sx Le dilatation sur l'axe X
	 * @param sy Le dilatation sur l'axe Y
	 * @return La dilatation sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newScaling(double sx, double sy) {
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}

	/**
	 * Donne une transformation affine de type transvection d'un facteur sx parall�lement � l'abscisse.
	 * @param sx Le facteur de transvection sur l'axe X
	 * @return La transvection sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newShearX(double sx) {
		return new AffineTransformation(1, sx, 0, 0, 1, 0);
	}
	
	/**
	 * Donne une transformation affine de type transvection d'un facteur sy parall�lement � l'ordonn�e.
	 * @param sy Le facteur de transvection sur l'axe Y
	 * @return La transvection sous forme de {@link AffineTransformation}
	 */
	public static AffineTransformation newShearY(double sy) {
		return new AffineTransformation(1, 0, 0, sy, 1, 0);
	}

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
	 * D�termine la transformation affine compos�e avec le param�tre that
	 * @param that La deuxi�me transformation affine avec laquelle composer une nouvelle transformation affine
	 * @return La transformation affine compos�e
	 */
	public AffineTransformation composeWith(AffineTransformation that) {
		double[][] newTransformation = new double[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				newTransformation[i][j] = 0.0;
				for (int k = 0; k < 3; k++) {
					newTransformation[i][j] += this.transformation[i][k]
							* that.transformation[k][j];
				}
			}
		}
		return new AffineTransformation(newTransformation[0][0],
				newTransformation[0][1], newTransformation[0][2],
				newTransformation[1][0], newTransformation[1][1],
				newTransformation[1][2]);
	}

	public double translationX() {
		return this.transformation[0][2];
	}

	double translationY() {
		return this.transformation[1][2];
	}

}
