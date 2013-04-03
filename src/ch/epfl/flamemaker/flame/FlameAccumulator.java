package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public final class FlameAccumulator {
	private final int[][] hitCount;
	private final double[][] colorIndexSum;
	private final double m;

	private FlameAccumulator(int[][] hitCount, double[][] colorIndexSum) {
		this.hitCount = new int[hitCount.length][hitCount[0].length];
		this.colorIndexSum = new double[colorIndexSum.length][colorIndexSum[0].length];

		for (int i = 0; i < colorIndexSum.length; i++) {
			this.colorIndexSum[i] = colorIndexSum[i].clone();
		}
		
		for (int i = 0; i < hitCount.length; i++) {
			this.hitCount[i] = hitCount[i].clone();
		}
		
		int max = 0;
		for (int i = 0; i < hitCount.length; i++) {
			for (int j = 0; j < hitCount[i].length; j++) {
				if (hitCount[i][j] > max) {
					max = hitCount[i][j];
				}
			}
		}
		this.m = max;
	}

	public int width() {
		return this.hitCount.length;
	}

	public int height() {
		return this.hitCount[0].length;
	}

	public double intensity(int x, int y) {
		if (x < 0 || x >= hitCount.length || y < 0 || y >= hitCount[0].length) {
			throw new IndexOutOfBoundsException(
					"Les coordonées données ne sont pas valides :(" + x
							+ "x : " + y + "y)");
		}
		return (Math.log(hitCount[x][y] + 1) / Math.log(m + 1));

	}

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
