package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe modelisant un accumulateur d'une fractale Flame
 * @see {@link Flame}La classe Flame
 * @see {@link #FlameAccumulator(int[][], double[][])}Le constructeur de FlameAccumulator
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public final class FlameAccumulator {
	private final int[][] hitCount;
	private final double[][] colorIndexSum;
	private final double m;

	/**
	 * Constructeur de {@link FlameAccumulator}
	 * @param hitCount Le tableau a deux dimensions representant pour chaque pixel le nombre de fois ou un point a ete marque.
	 * @param colorIndexSum Le tableau qui contient la somme des index de couleur de chaque case
	 */
	private FlameAccumulator(int[][] hitCount, double[][] colorIndexSum) {
		this.hitCount = new int[hitCount.length][];
		this.colorIndexSum = new double[colorIndexSum.length][];

		for (int i = 0; i < colorIndexSum.length; i++) { //Copie en profondeur du tableau colorIndexSum
			this.colorIndexSum[i] = colorIndexSum[i].clone();
		}

		for (int i = 0; i < hitCount.length; i++) { //Copie en profondeur du tableau hitCount
			this.hitCount[i] = hitCount[i].clone();
		}

		//Determination du nombre de points maximums en un point marques dans le tableau hitCount
		int max = 0;
		for (int i = 0; i < hitCount.length; i++) {
			for (int j = 0; j < hitCount[i].length; j++) { 
				if (hitCount[i][j] > max) {
					max = hitCount[i][j];
				}
			}
		}
		this.m = Math.log(max + 1);
	}

	public int width() {
		return this.hitCount.length;
	}
	public int height() {
		return this.hitCount[0].length;
	}

	/**
	 * Méthode donnant l'intensite de la case de l'accumulateur.
	 * @param x Position x
	 * @param y Position y
	 * @return L'intensity en ce point.
	 * @throws IndexOutOfBoundsException si les coordonnees ne sont pas valides
	 */
	public double intensity(int x, int y) {
		if (x < 0 || x >= hitCount.length || y < 0 || y >= hitCount[0].length) {
			throw new IndexOutOfBoundsException(
					"Les coordonees donnees ne sont pas valides :(" + x
					+ "x : " + y + "y)");
		}
		return (Math.log(hitCount[x][y] + 1) / this.m);
	}

	/**
	 * Methode retournant la couleur correspondante au point x et y dans la palette donne avec un fond donne.
	 * @param palette La palette de couleur qui determine la couleur donnee
	 * @param background La couleur de fond, auquelle la couleur de la palette va se melanger
	 * @param x La position x
	 * @param y La position y
	 * @return La couleur correspondante
	 * @throws IndexOutOfBoundsException si les coordonnees ne sont pas valides.
	 * 
	 */
	public Color color(Palette palette, Color background, int x, int y) {
		if (x < 0 || x >= hitCount.length || y < 0 || y >= hitCount[0].length) {
			throw new IndexOutOfBoundsException(
					"Les coordonees donnees ne sont pas valides :(" + x
					+ "x : " + y + "y)");
		}
		if (hitCount[x][y]==0){
			return background;
		}
		double indexColor = colorIndexSum[x][y] / (double) hitCount[x][y];
		return palette.colorForIndex(indexColor).mixWith(background,
				intensity(x, y));
	}

	/**
	 * Modelise un batisseur d'accumulateur de Flame
	 * @see FlameAccumulator
	 * @see {@link #FlameAccumulator.Builder(Rectangle, int, int) Le constructeur Builder()}
	 */
	public static class Builder {
		private Rectangle frame;
		private int[][] hitCount;
		private double[][] colorIndexSum;
		private AffineTransformation translationCadre, upscaleCadre, tuCadre;

		/**
		 * Constructeur du batisseur d'accumulateur de Flame
		 * @param frame Le cadre qui delimite le calcul
		 * @param width La largeur du rendu.
		 * @param height La hauteur du rendu.
		 * @see <a href="https://dl.dropbox.com/u/45709343/yodawg.png">Yo dawg</a>
		 * @throws IllegalArgumentException si les valeurs ne sont pas strictement positives.
		 */
		public Builder(Rectangle frame, int width, int height) {
			if (width <= 0 || height <= 0) {
				throw new IllegalArgumentException(
						"Les valeurs ne sont pas strictement positives");
			}
			this.frame = frame;
			this.hitCount = new int[width][height];
			this.colorIndexSum = new double[width][height];

			translationCadre = AffineTransformation.newTranslation(
					-(frame.left()), -(frame.bottom()));
			upscaleCadre = AffineTransformation.newScaling(
					width / frame.width(), height / frame.height());
			tuCadre = upscaleCadre.composeWith(translationCadre);
		}

		/**
		 * Incremente le compteur de la case correspondant au point
		 * @param p Le point touche
		 * @param index L'index de la palette de couleur a incrementer
		 */
		public void hit(Point p, double index) {
			if (this.frame.contains(p)) {
				int px, py;
				Point pT = tuCadre.transformPoint(p);
				px = (int) pT.x();
				py = (int) pT.y();
				this.hitCount[px][hitCount[0].length - py -1]++;
				this.colorIndexSum[px][colorIndexSum[0].length - py -1] += index;
			}

		}

		/**
		 * Construit et retourne l'accumulateur de Flame.
		 * @return L'accumulateur de Flame
		 */
		public FlameAccumulator build() {
			return new FlameAccumulator(this.hitCount, this.colorIndexSum);
		}

	}

}
