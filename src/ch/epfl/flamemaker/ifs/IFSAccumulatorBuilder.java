package ch.epfl.flamemaker.ifs;

import java.util.Arrays;

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
					"Les valeurs données ne sont pas strictement positives.");
		this.frame = frame;
		this.width = width;
		this.height = height;
		this.bool = new boolean[width][height];

		// La translation permet de passer d'une coordonnée graphique à une
		// position du tableau, en mettant le coin inférieur gauche du cadre à
		// l'origine.
		translationCadre = AffineTransformation.newTranslation(-(frame.center()
				.x() - (frame.width() / 2)), -(frame.center().y() - (frame
				.height() / 2)));
	}

	public void hit(Point p) {
		int px, py;
		px = (int) Math.floor(p.x());
		py = (int) Math.floor(p.y());
		Point pT = translationCadre.transformPoint(p);
		if (this.frame.contains(p))
			this.bool[(int) pT.x()][(int) pT.y()] = true;
	}

	public IFSAccumulator build() {
		return new IFSAccumulator(bool);
	}
}
