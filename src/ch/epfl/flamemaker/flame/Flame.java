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
		FlameAccumulator.Builder flameaccu = new FlameAccumulator.Builder(
				frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random();
		for (int k = 0; k < density * width * height; k++) {
			int i = r.nextInt(listTransfo.size());
			double newX =0;
			double newY =0;
			for (int j = 0; j < Variation.ALL_VARIATIONS.size(); j++) {
				double weight = this.listTransfo.get(i).getVariationWeight(j);
				Point pTemp = Variation.ALL_VARIATIONS.get(j).transformPoint(
						this.listTransfo.get(i).transformPoint(p));
				newX += weight * pTemp.x();
				newY += weight * pTemp.y();
			}
			p = new Point(newX,newY);	
			flameaccu.hit(p);
		}
		return flameaccu.build();

	}

}
