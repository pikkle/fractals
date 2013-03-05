package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class Flame {
	private final List<FlameTransformation> listTransfo;

	public Flame(List<FlameTransformation> transformations) {
		this.listTransfo = new ArrayList<FlameTransformation>(transformations);
	}
	
	public FlameAccumulator compute(Rectangle frame, int width, int height, int density){
		FlameAccumulatorBuilder flameaccu = new FlameAccumulatorBuilder(frame, height, width);
		Point p = new Point(0, 0);
		
	}

}
