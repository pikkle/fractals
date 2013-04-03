package ch.epfl.flamemaker.flame;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlamePPMMaker {
	public static void main(String[] args) throws FileNotFoundException {
		List<Color> listColor = Arrays.asList(new Color[] { Color.RED,
				Color.GREEN, Color.BLUE });
		InterpolatedPalette paletteInterpol = new InterpolatedPalette(listColor);
		// Shark fin
		System.out.println("Création de la fractale Shark fin...");
		List<FlameTransformation> listTransfoShark = new ArrayList<FlameTransformation>();
		listTransfoShark.add(new FlameTransformation(new AffineTransformation(
				-0.4113504, -0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
				new double[] { 1, 0.1, 0, 0, 0, 0 }));
		listTransfoShark.add(new FlameTransformation(new AffineTransformation(
				-0.3957339, 0, -1.6, 0, -0.3957337, 0.2), new double[] { 0, 0,
				0, 0, 0.8, 1 }));
		listTransfoShark.add(new FlameTransformation(new AffineTransformation(
				0.4810169, 0, 1, 0, 0.4810169, 0.9), new double[] { 1, 0, 0, 0,
				0, 0 }));
		Flame flameShark = new Flame(listTransfoShark);
		FlameAccumulator flameaccuShark = flameShark.compute(new Rectangle(
				new Point(-0.25, 0), 5, 4), 500, 400, 50);

		PrintStream ps1 = new PrintStream("sharkfin.PPM");
		ps1.println("P3");
		ps1.println(flameaccuShark.width() + " " + flameaccuShark.height());
		ps1.println("100");
		int h = flameaccuShark.height();
		int w = flameaccuShark.width();
		System.out.println("Création de l'image Shark fin...");
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				Color colorPrint = flameaccuShark.color(paletteInterpol,
						Color.BLACK, j, i);
				ps1.print(Color.sRGBEncode(colorPrint.red(), 100) + " "
						+ Color.sRGBEncode(colorPrint.green(), 100) + " "
						+ Color.sRGBEncode(colorPrint.blue(), 100) + "   ");
			}
			ps1.print("\n");
		}
		ps1.flush();
		ps1.close();

		System.out.println();

		/*
		 * Turbulences
		 * System.out.println("Création de la fractale Turbulences...");
		 * List<FlameTransformation> listTransfoTurb = new
		 * ArrayList<FlameTransformation>(); listTransfoTurb.add(new
		 * FlameTransformation(new AffineTransformation( 0.7124807, -0.4113509,
		 * -0.3, 0.4113513, 0.7124808, -0.7), new double[] { 0.5, 0, 0, 0.4, 0,
		 * 0 })); listTransfoTurb.add(new FlameTransformation(new
		 * AffineTransformation( 0.3731079, -0.6462417, 0.4, 0.6462414,
		 * 0.3731076, 0.3), new double[] { 1, 0, 0.1, 0, 0, 0 }));
		 * listTransfoTurb.add(new FlameTransformation(new AffineTransformation(
		 * 0.0842641, -0.314478, -0.1, 0.314478, 0.0842641, 0.3), new double[] {
		 * 1, 0, 0, 0, 0, 0 })); Flame flameTurb = new Flame(listTransfoTurb);
		 * FlameAccumulator flameaccuTurb = flameTurb.compute(new Rectangle(new
		 * Point( 0.1,0.1), 3,3), 500 , 500, 50);
		 * 
		 * PrintStream ps2 = new PrintStream("turbulences.PPM");
		 * ps2.println("P3"); ps2.println(flameaccuTurb.width() + " " +
		 * flameaccuTurb.height()); ps2.println("100"); int h2 =
		 * flameaccuTurb.height(); int w2 = flameaccuTurb.width();
		 * System.out.println("Création de l'image Turbulences..."); for (int i
		 * = 0; i < h2; i++) { for (int j = 0; j < w2; j++) { ps2.print(((int)
		 * (100*(flameaccuTurb.intensity(j, i)))) + " "); } ps2.print("\n"); }
		 * ps2.flush(); ps2.close();
		 */

		System.out.println("\nFin du programme.");
	}
}
