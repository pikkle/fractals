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
		FlameAccumulator.Builder flameaccu = new FlameAccumulator.Builder(frame, width, height);
		Point p = new Point (0,0);
		for (int i =0; i< density*width*height; i++){
			
		}
		
	}

}
