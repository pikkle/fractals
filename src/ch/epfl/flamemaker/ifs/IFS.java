package ch.epfl.flamemaker.ifs;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFS {
	private List<AffineTransformation> listTransfo;

	public IFS(List<AffineTransformation> transformations) {
		listTransfo = new ArrayList<AffineTransformation>(transformations);
	}

	public IFSAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		// qui calcule, au moyen de l'algorithme du chaos, la fractale dans la
		// région du plan délimitée par le cadre frame et stocke le résultat
		// dans un nouvel accumulateur de largeur width et hauteur height, qui
		// est retourné. Le nombre d'itérations à effectuer est calculé en
		// fonction du paramètre density, comme expliqué plus bas.
		return null;

	}
}
