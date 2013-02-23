package ch.epfl.flamemaker.geometry2d;

public final class AffineTransformation implements Transformation {
	private final double[][] transformation;
	public static final AffineTransformation IDENTITY = new AffineTransformation(
			1, 0, 0, 0, 1, 0);

	private AffineTransformation(double a, double b, double c, double d,
			double e, double f) {
		transformation = new double[][] { { a, b, c }, { d, e, f }, { 0, 0, 1 } };
	}

	public static AffineTransformation newTranslation(double dx, double dy) {
		return new AffineTransformation(1, 0, dx, 0, 1, dy);
	}

	public static AffineTransformation newRotation(double theta) {
		return new AffineTransformation(Math.cos(theta), -Math.sin(theta), 0,
				Math.sin(theta), Math.cos(theta), 0);
	}

	public static AffineTransformation newScaling(double sx, double sy) {
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}

	public static AffineTransformation newShearX(double sx) {
		return new AffineTransformation(1, sx, 0, 0, 1, 0);
	}

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

	// Getters
	public double translationX() {
		return this.transformation[0][2];
	}

	double translationY() {
		return this.transformation[1][2];
	}

}
