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
		listTransfo.add(new AffineTransformation(0.5, 0, 0, 0, 0.5, 0));
		listTransfo.add(new AffineTransformation(0.5, 0, 0.5, 0, 0.5, 0));
		listTransfo.add(new AffineTransformation(0.5, 0, 0.25, 0, 0.5, 0.5));

		IFS sierpinski = new IFS(listTransfo);
		IFSAccumulator ifsa = sierpinski.compute(new Rectangle(new Point(0.5,0.5),
				1, 1), 1000, 1000, 1);

		PrintStream ps = new PrintStream("sierpinski.PBM");

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
