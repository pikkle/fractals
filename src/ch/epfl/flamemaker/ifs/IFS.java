package ch.epfl.flamemaker.ifs;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFS {
	private List<AffineTransformation> listTransfo;

	public IFS(List<AffineTransformation> transformations) {
		listTransfo = new ArrayList<AffineTransformation>(transformations);
	}

	public IFSAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		IFSAccumulatorBuilder ifsAccB = new IFSAccumulatorBuilder(frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random();
		for (int k = 0; k < 20 + density * width * height; k++) {
			int i = r.nextInt(this.listTransfo.size());
			p = this.listTransfo.get(i).transformPoint(p);
			if (k > 20)
				ifsAccB.hit(p);
		}
		return ifsAccB.build();
	}
}
