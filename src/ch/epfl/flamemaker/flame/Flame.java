package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.flame.FlameTransformation;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class Flame {
	private final List<FlameTransformation> listTransfo;

	public Flame(List<FlameTransformation> transformations) {
		this.listTransfo = new ArrayList<FlameTransformation>(transformations);
	}

	public FlameAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		FlameAccumulator.Builder flameAccu = new FlameAccumulator.Builder(
				frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random();
		for (int k = 0; k < 20 + density * width * height; k++) {
			int i = r.nextInt(listTransfo.size());
			p = listTransfo.get(i).transformPoint(p);
			if (k > 20){
				flameAccu.hit(p);
			}
		}
		return flameAccu.build();

	}

}
