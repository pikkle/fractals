package ch.epfl.flamemaker.flame;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameMaker {
	public static void main(String[] args) throws FileNotFoundException {
		List<FlameTransformation> listTransfo = new ArrayList<FlameTransformation>();
		// Shark fin
		listTransfo.add(new FlameTransformation(new AffineTransformation(
				-0.4113504, -0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
				new double[] { 1, 0.1, 0, 0, 0, 0 }));
		listTransfo.add(new FlameTransformation(new AffineTransformation(
				-0.3957339, 0, -1.6, 0, -0.3957337, 0.2), new double[] { 0, 0,
				0, 0, 0.8, 1 }));
		listTransfo.add(new FlameTransformation(new AffineTransformation(
				0.4810169, 0, 1, 0, 0.4810169, 0.9), new double[] { 1, 0, 0, 0,
				0, 0 }));
		Flame flame = new Flame(listTransfo);
		// Shark fin
		FlameAccumulator flameaccu = flame.compute(new Rectangle(new Point(
				-0.25, 0), 5, 4), 500, 400, 50);

		PrintStream ps = new PrintStream("sharkfin.PGM");
		ps.println("P2");
		ps.println(flameaccu.width() + " " + flameaccu.height());
		ps.println("100");
		for (int i = flameaccu.height(); i >= 0; i++) {
			for (int j = 0; j < flameaccu.width(); j++) {
				ps.print(flameaccu.intensity(i, j));
			}
			ps.println();
		}
		ps.flush();
		ps.close();
	}
}
