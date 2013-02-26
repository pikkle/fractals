package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSAccumulatorBuilder {
	private Rectangle frame;
	private int width, height;
	private boolean[][] bool;
	private AffineTransformation translationCadre, upscaleCadre, tuCadre;
	

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
		translationCadre = AffineTransformation.newTranslation(-(frame.left()),-(frame.bottom()));
		upscaleCadre =  AffineTransformation.newScaling(width/frame.width(), height/frame.height());
		tuCadre = upscaleCadre.composeWith(translationCadre);
	}

	public void hit(Point p) {
		int px, py;
		Point pT = tuCadre.transformPoint(p);
		px = (int) pT.x();
		py = (int) pT.y();
		if (this.frame.contains(p))
			this.bool[px][py] = true;
	}

	public IFSAccumulator build() {
		return new IFSAccumulator(this.bool);
	}
}
