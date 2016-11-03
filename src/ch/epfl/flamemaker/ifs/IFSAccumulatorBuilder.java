package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Batisseur d'accumulateur
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see #IFSAccumulatorBuilder(Rectangle, int, int)
 */
public class IFSAccumulatorBuilder {
	private Rectangle frame;
	private boolean[][] hitArray;
	private AffineTransformation translationCadre, upscaleCadre, tuCadre;

	/**
	 * Constructeur du batisseur d'accumulateur
	 *
	 * @param frame  La cadre qui delimite le plan
	 * @param width  La largeur de l'accumulateur
	 * @param height La hauteur de l'accumulateur
	 */
	public IFSAccumulatorBuilder(Rectangle frame, int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException(
					"Les valeurs donnees ne sont pas strictement positives.");
		this.frame = frame;
		this.hitArray = new boolean[width][height];

		// La translation permet de passer d'une coordonnee graphique a une
		// position du tableau, en mettant le coin inferieur gauche du cadre a
		// l'origine.
		translationCadre = AffineTransformation.newTranslation(-(frame.left()),
				-(frame.bottom()));
		upscaleCadre = AffineTransformation.newScaling(width / frame.width(),
				height / frame.height());
		tuCadre = upscaleCadre.composeWith(translationCadre);
	}

	/**
	 * Marque le point dans le tableau a vrai
	 *
	 * @param p Le point a marquer
	 */
	public void hit(Point p) {
		int px, py;
		Point pT = tuCadre.transformPoint(p);
		px = (int) pT.x();
		py = (int) pT.y();
		if (this.frame.contains(p))
			this.hitArray[px][py] = true;
	}

	/**
	 * Renvoie l'accumulateur IFS correspondant a l'etat actuel du builder
	 *
	 * @return L'IFSAccumulator correspondant au tableau de booleens du builder.
	 */
	public IFSAccumulator build() {
		return new IFSAccumulator(this.hitArray);
	}
}
