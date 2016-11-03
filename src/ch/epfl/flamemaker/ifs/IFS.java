package ch.epfl.flamemaker.ifs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * La classe IFS modélise une fractale IFS.
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see #IFS(List)
 */
public class IFS {
	private List<AffineTransformation> listTransfo;

	/**
	 * Constructeur de fractale IFS
	 *
	 * @param transformations La liste de transformations caractérisant la fractale
	 */
	public IFS(List<AffineTransformation> transformations) {
		listTransfo = new ArrayList<AffineTransformation>(transformations);
	}

	/**
	 * Calcule au moyen de l'algorithme du chaos la fractale
	 * dans la region du plan delimitee par le cadre frame
	 * et stocke le resultat dans un nouvel accumulateur.
	 *
	 * @param frame   La region du plan delimitee
	 * @param width   La largeur de l'accumulateur
	 * @param height  La hauteur de l'accumulateur
	 * @param density La densite de points calcules.
	 * @return
	 */
	public IFSAccumulator compute(Rectangle frame, int width, int height,
	                              int density) {
		IFSAccumulatorBuilder ifsAccB = new IFSAccumulatorBuilder(frame, width,
				height);
		Point p = new Point(0, 0);
		Random r = new Random(2013);
		for (int k = 0; k < 20 + density * width * height; k++) {
			int i = r.nextInt(this.listTransfo.size());
			p = this.listTransfo.get(i).transformPoint(p);
			if (k > 20) // 20 premiers points a blanc
				ifsAccB.hit(p);
		}
		return ifsAccB.build();
	}
}
