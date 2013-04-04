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
			x += variationWeight[i]
					* Variation.ALL_VARIATIONS
							.get(i)
							.transformPoint(
									this.affineTransformation.transformPoint(p))
							.x();
			y += variationWeight[i]
					* Variation.ALL_VARIATIONS
							.get(i)
							.transformPoint(
									this.affineTransformation.transformPoint(p))
							.y();

		}
		return new Point(x, y);
	}
	public static class Builder{
		private AffineTransformation affineTransformationBuilder;
		private double[] variationWeightBuilder;
		
		public Builder(FlameTransformation flameTransformation){
			this.affineTransformationBuilder = flameTransformation.affineTransformation; //à vérifier si copie en profondeur
			this.variationWeightBuilder = flameTransformation.variationWeight.clone(); // à vérifier si copie en profondeur
		}
		public AffineTransformation getAffineTransformation(){
			return affineTransformationBuilder;
		}
		public double getVariationWeight(int index){
			if (index > variationWeightBuilder.length || index < 0){
				throw new IndexOutOfBoundsException("l'index de variation est invalide");
			}
			return variationWeightBuilder[index];
		}
		public void setAffineTransformation(AffineTransformation affineTransformation){
			this.affineTransformationBuilder = affineTransformation;
		}
		public void setVariationWeight(int index, double newVariation){
			this.variationWeightBuilder[index] = newVariation;
		}
		public FlameTransformation build(){
			FlameTransformation flameTransformation = new FlameTransformation(affineTransformationBuilder, variationWeightBuilder);
			return flameTransformation;
		}
	}

}
