package ch.epfl.flamemaker.ifs;

import java.util.Arrays;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSAccumulatorBuilder {
	private Rectangle frame;
	private int width, height;
	private boolean[][] ifsa;

	public IFSAccumulatorBuilder(Rectangle frame, int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException(
					"Les valeurs données ne sont pas strictement positives.");
		this.frame = frame;
		this.width = width;
		this.height = height;
		this.ifsa = new boolean[width][height];
		
	}

	public void hit(Point p) {
		int px, py;
		px = (int) Math.floor(p.x());
		py = (int) Math.floor(p.y());
		if (this.frame.contains(p))
			this.ifsa[px][py] = true;
	}

	public IFSAccumulator build() {
		return new IFSAccumulator(ifsa);
	}
}
