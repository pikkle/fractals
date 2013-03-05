package ch.epfl.flamemaker.flame;

public final class FlameAccumulator {
	private final int[][] hitCount;
	private final double m;

	private FlameAccumulator(int[][] hitCount) {
		for (int i = 0; i < hitCount.length; i++) {
			this.hitCount[i] = hitCount[i].clone();
		}

	}

	public int width() {
		return this.hitCount.length;
	}

	public int height() {
		return this.hitCount[0].length;
	}

	public double intensity(int x, int y) {
		if (x < 0 | x > hitCount.length | y < 0 | y > hitCount[0].length) {
			throw new IndexOutOfBoundsException(
					"Les coordonées données ne sont pas valides");
		}
		return (Math.log(hitCount[x][y] + 1) / Math.log(m + 1));

	}

}
