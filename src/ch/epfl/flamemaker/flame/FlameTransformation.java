package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * Classe modelisant une tranformation de Flame. La classe implemente l'interface {@link Transformation}.
 * @see {@link #FlameTransformation(AffineTransformation, double[]) Le constructeur FlameTransformation()}
 * @see Transformation
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class FlameTransformation implements Transformation {
	private final AffineTransformation affineTransformation;
	private final double[] variationWeight;

	/**
	 * Le constructeur de {@link FlameTransformation}
	 * @param affineTransformation La transformation affine ({@link AffineTransformation})
	 * @param variationWeight Le tableau des poids de variation.
	 * @throws IllegalArgumentException si le tableau de poids n'est pas de taille 6
	 */
	public FlameTransformation(AffineTransformation affineTransformation,
			double[] variationWeight) {
		if (variationWeight.length != Variation.ALL_VARIATIONS.size())
			throw new IllegalArgumentException(
					"Le tableau de poids n'a pas la bonne taille:"
							+ variationWeight.length + " au lieu de 6.");
		this.variationWeight = variationWeight.clone();
		this.affineTransformation = affineTransformation;
	}

	/**
	 * Ajoute toutes les variations au point ponderees par leur poids.
	 * @return Le point transforme par toutes les variations
	 */
	@Override
	public Point transformPoint(Point p) {
		double x = 0.0;
		double y = 0.0;

		for (int i = 0; i < Variation.ALL_VARIATIONS.size(); i++) {
			if(variationWeight[i] != 0){
				x += variationWeight[i] * Variation.ALL_VARIATIONS.get(i).transformPoint(
						this.affineTransformation.transformPoint(p)).x();
				y += variationWeight[i] * Variation.ALL_VARIATIONS.get(i).transformPoint(
						this.affineTransformation.transformPoint(p)).y();
			}
		}
		return new Point(x, y);
	}

	/**
	 * Modelise un batisseur de Transformation Flame
	 * @see FlameTransformation
	 * @see {@link #Builder(FlameTransformation flameTransformation) Le constructeur Builder()}
	 */
	public static class Builder{
		private AffineTransformation affineTransformationBuilder;
		private double[] variationWeightBuilder;

		/**
		 * Constructeur du batisseur de Transformation Flame
		 * @param flameTransformation La transformation Flame a batir
		 */
		public Builder(FlameTransformation flameTransformation){
			this.affineTransformationBuilder = flameTransformation.affineTransformation;
			this.variationWeightBuilder = flameTransformation.variationWeight.clone();
		}

		/**
		 * Donne la composante affine de la transformation flame
		 * @return
		 */
		public AffineTransformation getAffineTransformation(){
			return this.affineTransformationBuilder;
		}

		/**
		 * Donne le poids de variation d'index donne
		 * @param index L'index de la variation
		 * @return Le poids de variation
		 * @throws IndexOutOfBoundsException si l'index de variation est invalide.
		 */
		public double getVariationWeight(int index){
			if (index > Variation.ALL_VARIATIONS.size() || index < 0){
				throw new IndexOutOfBoundsException("l'index de variation est invalide");
			}
			return this.variationWeightBuilder[index];
		}

		/**
		 * Change la composante affine de la transformation Flame
		 * @param affineTransformation
		 */
		public void setAffineTransformation(AffineTransformation affineTransformation){
			this.affineTransformationBuilder = affineTransformation;
		}

		/**
		 * Change le poids de variation d'index donne
		 * @param index L'index de la variation a modifier
		 * @param newVariation Le nouveau poids de variation
		 */
		public void setVariationWeight(int index, double newVariation){
			this.variationWeightBuilder[index] = newVariation;
		}

		/**
		 * Construit et retourne la transformation Flame
		 * @return La transformation Flame construite.
		 */
		public FlameTransformation build(){
			FlameTransformation flameTransformation = new FlameTransformation(
					this.affineTransformationBuilder, 
					this.variationWeightBuilder);
			return flameTransformation;
		}
	}

}
