package ch.epfl.flamemaker.ifs;

import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFS {
	public IFS(List<AffineTransformation> transformations) {
		// TODO Constructeur d'une fractale IFS
	}

	public IFSAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		// qui calcule, au moyen de l'algorithme du chaos, la fractale dans la
		// r�gion du plan d�limit�e par le cadre frame et stocke le r�sultat
		// dans un nouvel accumulateur de largeur width et hauteur height, qui
		// est retourn�. Le nombre d'it�rations � effectuer est calcul� en
		// fonction du param�tre density, comme expliqu� plus bas.
		return null;

	}
}
