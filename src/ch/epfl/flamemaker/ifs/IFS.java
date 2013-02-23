package ch.epfl.flamemaker.ifs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.ws.api.pipe.NextAction;

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
		//Application du pseudo-code
		Point p = new Point(0,0);
		Random r = new Random();
		for (int k = 0; k<density*width*height; k++){
			int i = r.nextInt(this.listTransfo.size()-1);
			p = this.listTransfo.get(i).transformPoint(p);
		}
		
		return new IFSAccumulator(null);

	}
}
