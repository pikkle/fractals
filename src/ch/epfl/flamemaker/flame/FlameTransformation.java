package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

public class FlameTransformation implements Transformation {
	private final AffineTransformation affineTransformation;
	private final double[] variationWeight;

	public FlameTransformation(AffineTransformation affineTransformation,
			double[] variationWeight) {
		if (variationWeight.length != 6)
			throw new IllegalArgumentException(
					"Le tableau de poids n'a pas la bonne taille:"
							+ variationWeight.length + " au lieu de 6.");
		this.variationWeight = variationWeight.clone();
		this.affineTransformation = affineTransformation;
	}


	@Override
	public Point transformPoint(Point p) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getVariationWeight(int i) {
		return variationWeight[i];
	}

}
