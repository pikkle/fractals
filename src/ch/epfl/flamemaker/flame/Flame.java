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
	private final ArrayList<Double> colorTransfo;

	public Flame(List<FlameTransformation> transformations) {
		this.listTransfo = new ArrayList<FlameTransformation>(transformations);
		colorTransfo = new ArrayList<Double>();
		colorTransfo.add(0.0);
		colorTransfo.add(1.0);
		for (int i = 2; i < transformations.size(); i++) {
			double a = Math.pow(2, Math.ceil(Math.log(i) / Math.log(2)));
			colorTransfo.add((2 * i - 1 - a) / a);
		}
	}

	public FlameAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		FlameAccumulator.Builder flameAccu = new FlameAccumulator.Builder(
				frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random(2013);
		double c = 0.0;
		for (int k = 0; k < 20 + density * width * height; k++) {
			int i = r.nextInt(this.colorTransfo.size());
			p = this.listTransfo.get(i).transformPoint(p);
			c = (this.colorTransfo.get(i)+c)/2.0;
			if (k > 20) {
				if (c != 0.0){
				}
				flameAccu.hit(p,c);
			}
		}
		return flameAccu.build();

	}

}
