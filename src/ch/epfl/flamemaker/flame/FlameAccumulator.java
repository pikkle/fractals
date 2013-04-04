package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe modélisant un accumulateur d'une fractale Flame
 * @see {@link Flame}La classe Flame
 * @see {@link #FlameAccumulator(int[][], double[][])}Le constructeur de FlameAccumulator
 */
public final class FlameAccumulator {
	private final int[][] hitCount;
	private final double[][] colorIndexSum;
	private final double m;

	/**
	 * Constructeur de {@link FlameAccumulator}
	 * @param hitCount Le tableau à deux dimensions représentant pour chaque pixel le nombre de fois où un point a été marqué.
	 * @param colorIndexSum Le tableau qui contient la somme des index de couleur de chaque case
	 */
	private FlameAccumulator(int[][] hitCount, double[][] colorIndexSum) {
		this.hitCount = new int[hitCount.length][hitCount[0].length];
		this.colorIndexSum = new double[colorIndexSum.length][colorIndexSum[0].length];

		for (int i = 0; i < colorIndexSum.length; i++) { //Copie en profondeur du tableau colorIndexSum
			this.colorIndexSum[i] = colorIndexSum[i].clone();
		}
		
		for (int i = 0; i < hitCount.length; i++) { //Copie en profondeur du tableau hitCount
			this.hitCount[i] = hitCount[i].clone();
		}
		
		//Determination du nombre de points maximums en un point marqués dans le tableau hitCount
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
	 * Méthode donnant l'intensité de la case de l'accumulateur.
	 * @param x Position x
	 * @param y Position y
	 * @return L'intensity en ce point.
	 * @throws IndexOutOfBoundsException si les coordonnées ne sont pas valides
	 */
	public double intensity(int x, int y) {
		if (x < 0 || x >= hitCount.length || y < 0 || y >= hitCount[0].length) {
			throw new IndexOutOfBoundsException(
					"Les coordonées données ne sont pas valides :(" + x
							+ "x : " + y + "y)");
		}
		return (Math.log(hitCount[x][y] + 1) / this.m);
	}
	
	/**
	 * Méthode retournant la couleur correspondante au point x et y dans la palette donnée avec un fond donné.
	 * @param palette La palette de couleur qui détermine la couleur donnée
	 * @param background La couleur de fond, auquelle la couleur de la palette va se mélanger
	 * @param x La position x
	 * @param y La position y
	 * @return La couleur correspondante
	 * @throws IndexOutOfBoundsException si les coordonnées ne sont pas valides.
	 * 
	 */
	public Color color(Palette palette, Color background, int x, int y) {
		if (x < 0 || x >= hitCount.length || y < 0 || y >= hitCount[0].length) {
			throw new IndexOutOfBoundsException(
					"Les coordonées données ne sont pas valides :(" + x
							+ "x : " + y + "y)");
		}
		
		double indexColor = colorIndexSum[x][y] / (double) hitCount[x][y];
		return palette.colorForIndex(indexColor).mixWith(background,
				intensity(x, y));
	}

	//TODO Javadoc Builder
	public static class Builder {
		private Rectangle frame;
		private int[][] hitCount;
		private double[][] colorIndexSum;
		private AffineTransformation translationCadre, upscaleCadre, tuCadre;

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

		public void hit(Point p, double index) {
			int px, py;
			Point pT = tuCadre.transformPoint(p);
			px = (int) pT.x();
			py = (int) pT.y();
			if (this.frame.contains(p)) {
				this.hitCount[px][hitCount[0].length - py -1]++;
				this.colorIndexSum[px][colorIndexSum[0].length - py -1] += index;
			}

		}

		public FlameAccumulator build() {
			return new FlameAccumulator(this.hitCount, this.colorIndexSum);
		}

	}

}
