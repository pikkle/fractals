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
		double x = 0.0;
		double y = 0.0;
		
		for (int i = 0; i < Variation.ALL_VARIATIONS.size(); i++) {
			x += variationWeight[i]*Variation.ALL_VARIATIONS.get(i).transformPoint(affineTransformation.transformPoint(p)).x();
			y += variationWeight[i]*Variation.ALL_VARIATIONS.get(i).transformPoint(affineTransformation.transformPoint(p)).y();
			
		}
		return new Point(x, y);
	}

}
