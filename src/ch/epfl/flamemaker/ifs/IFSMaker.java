package ch.epfl.flamemaker.ifs;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSMaker {
	public static void main(String[] args) throws FileNotFoundException {
		
		List<AffineTransformation> listTransfo = new ArrayList<AffineTransformation>();
		listTransfo.add(new AffineTransformation(0, 0, 0, 0, 0.16, 0));
		listTransfo.add(new AffineTransformation(0.2, -0.26, 0, 0.23, 0.22, 1.6));
		listTransfo.add(new AffineTransformation(-0.15, 0.28, 0, 0.26, 0.24, 0.44));
		listTransfo.add(new AffineTransformation(0.85, 0.04, 0, -0.04, 0.85, 1.6));

		IFS barnsley = new IFS(listTransfo);
		IFSAccumulator ifsa = barnsley.compute(new Rectangle(new Point(0, 4.5),
				6, 10), 1200, 2000, 150);

		PrintStream ps = new PrintStream("barnsley.PBM");

		ps.println("P1");
		ps.println(ifsa.width() + " " + ifsa.height());
		for (int i = ifsa.height()-1; i >= 0; i--) {
			for (int j = 0; j < ifsa.width(); j++) {
				if (ifsa.isHit(j, i))
					ps.print("1 ");
				else
					ps.print("0 ");
			}
			ps.println();
		}
		ps.flush();
		ps.close();
	}
}
