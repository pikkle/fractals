package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSAccumulatorBuilder {
	private Rectangle frame;
	private int width, height;
	private boolean[][] bool;
	private AffineTransformation translationCadre;

	public IFSAccumulatorBuilder(Rectangle frame, int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException(
					"Les valeurs donn�es ne sont pas strictement positives.");
		this.frame = frame;
		this.width = width;
		this.height = height;
		this.bool = new boolean[width][height];

		// La translation permet de passer d'une coordonn�e graphique � une
		// position du tableau, en mettant le coin inf�rieur gauche du cadre �
		// l'origine.
		translationCadre = AffineTransformation.newTranslation(-(frame.center()
				.x() - (frame.width() / 2)), -(frame.center().y() - (frame
				.height() / 2)));
	}

	public void hit(Point p) {
		int px, py;
		Point pT = translationCadre.transformPoint(p);
		px = (int) pT.x();
		py = (int) pT.y();
		if (this.frame.contains(p)){
			this.bool[px][py] = true;
		}
	}

	public IFSAccumulator build() {
		return new IFSAccumulator(this.bool);
	}
}
