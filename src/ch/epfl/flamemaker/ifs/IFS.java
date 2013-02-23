package ch.epfl.flamemaker.ifs;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFS {
	private List<AffineTransformation> listTransfo;
	private IFSAccumulatorBuilder ifsAccB;

	public static void main(String[] args) throws FileNotFoundException {
		List<AffineTransformation> listTransfo = new ArrayList<AffineTransformation>();
		listTransfo.add(new AffineTransformation(0.5, 0, 0, 0, 0.5, 0));
		listTransfo.add(new AffineTransformation(0.5, 0, 0.5, 0, 0.5, 0));
		listTransfo.add(new AffineTransformation(0.5, 0, 0.25, 0, 0.5, 0.5));

		IFS sierpinski = new IFS(listTransfo);
		IFSAccumulator ifsa = sierpinski.compute(new Rectangle(new Point(0.5,
				0.5), 1, 1), 100, 100, 1);

		PrintStream ps = new PrintStream("sierpinski.PBM");

		ps.println("P1");
		ps.println(ifsa.width() + " " + ifsa.height());

		System.out.println("width: " + ifsa.width());
		System.out.println("height: " + ifsa.height());
		for (int i = 0; i < ifsa.width(); i++) {
			for (int j = 0; j < ifsa.height(); j++) {
				if (ifsa.isHit(i, j))
					ps.print("1 ");
				else
					ps.print("0 ");
			}
			ps.println();
		}
		ps.flush();
		ps.close();
	}

	public IFS(List<AffineTransformation> transformations) {
		listTransfo = new ArrayList<AffineTransformation>(transformations);
	}

	public IFSAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		ifsAccB = new IFSAccumulatorBuilder(frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random();
		for (int k = 0; k < 20; k++) {
			int i = r.nextInt((this.listTransfo.size() -1));
			p = this.listTransfo.get(i).transformPoint(p);
		}
		for (int k = 0; k < density * width * height; k++) {
			int i = r.nextInt(this.listTransfo.size() - 1);
			p = this.listTransfo.get(i).transformPoint(p);
			ifsAccB.hit(p); 
		}
		return ifsAccB.build();
	}
}
