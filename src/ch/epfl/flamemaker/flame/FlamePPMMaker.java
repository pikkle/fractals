package ch.epfl.flamemaker.flame;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe principale cr�ant les fractales shark-fin et turbulence
 * @see {@link #main(String[])}
 */
public class FlamePPMMaker {
	/**
	 * Méthode permettant d'écrire dans un fichier la fractale la fractale Flame
	 * M�thode permettant d'�crire dans un fichier la fractale Flame.
	 * @param flameAccu L'accumulateur de la Flame
	 * @param palette La palette de couleur utilis�e.
	 * @param background La couleur de fond.
	 * @param file Le fichier o� l'image doit �tre stock�e.
	 */
	private static void printer(FlameAccumulator flameAccu, Palette palette,
			Color background, String file) {
		System.out.println("Ecriture du fichier " + file);
		try {
			PrintStream ps = new PrintStream(file);
			
			//3 premi�res lignes qui indiquent que le fichier est une image .PPM
			String start = "P3\n" + flameAccu.width() + " "
					+ flameAccu.height() + "\n100";
			ps.println(start);

			for (int i = 0; i < flameAccu.height(); i++) {
				for (int j = 0; j < flameAccu.width(); j++) {
					//Ecriture ligne par ligne, colonne par colonne des pixels
					Color colorPrint = flameAccu.color(palette, background, j,
							i); //On redefinit pour chaque pixel la couleur du dit pixel.
					ps.print(Color.sRGBEncode(colorPrint.red(), 100) + " "
							+ Color.sRGBEncode(colorPrint.green(), 100) + " "
							+ Color.sRGBEncode(colorPrint.blue(), 100) + " "); // Triplet R G B
				}
				ps.print("\n"); // Retour � la ligne pour la ligne de pixels suivante.
			}
			ps.flush();
			ps.close(); // Fermeture du stream.
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier n'existe pas.");
			e.printStackTrace();
		}

	}
	
	/**
	 * La m�thode main principale cr�e les fractales shark-fin et turbulence dans les fichiers shark-fin.ppm et turbulence respectivement.
	 * @param args
	 */
	public static void main(String[] args){
		
		//M�thode principale qui cr�e les deux fractales Shark-fin et Turbulence
		List<Color> listColor = Arrays.asList(new Color[] { Color.RED,
				Color.GREEN, Color.BLUE });
		InterpolatedPalette paletteInterpol = new InterpolatedPalette(listColor);
		
		// Shark fin
		System.out.println("Cr�ation de la fractale Shark-fin...");
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
		FlameAccumulator flameAccuShark = flameShark.compute(new Rectangle(
				new Point(-0.25, 0), 5, 4), 500, 400, 50);
		FlamePPMMaker.printer(flameAccuShark, paletteInterpol, Color.BLACK, "shark-fin.ppm");
		
		System.out.println();
		
		//Turbulence
		System.out.println("Cr�ation de la fractale Turbulence...");
		List<FlameTransformation> listTransfoTurb = new ArrayList<FlameTransformation>();				
		listTransfoTurb.add(new FlameTransformation(new AffineTransformation(
				0.7124807, -0.4113509, -0.3, 0.4113513, 0.7124808, -0.7),
				new double[] { 0.5, 0, 0, 0.4, 0, 0 }));
		listTransfoTurb.add(new FlameTransformation(new AffineTransformation(
				0.3731079, -0.6462417, 0.4, 0.6462414, 0.3731076, 0.3), new double[] { 1, 0,
				0.1, 0, 0, 0 }));
		listTransfoTurb.add(new FlameTransformation(new AffineTransformation(
				0.0842641, -0.314478, -0.1, 0.314478, 0.0842641, 0.3), new double[] { 1, 0, 0, 0,
				0, 0 }));
		Flame flameTurb = new Flame(listTransfoTurb);
		FlameAccumulator flameAccuTurb = flameTurb.compute(new Rectangle(new Point(0.1,0.1),3,3), 500,500,50);
		FlamePPMMaker.printer(flameAccuTurb, paletteInterpol, Color.BLACK, "turbulence.ppm");
		
		System.out.println("\nFin du programme.");
	}
}
