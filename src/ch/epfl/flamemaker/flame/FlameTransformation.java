package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * Classe modélisant une tranformation de Flame. La classe implémente l'interface {@link Transformation}.
 * @see {@link #FlameTransformation(AffineTransformation, double[]) Le constructeur FlameTransformation()}
 * @see Transformation
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
			if(variationWeight[i] != 0){
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
		}
		return new Point(x, y);
	}
	
	/**
	 * Modélise un bâtisseur de Transformation Flame
	 * @see FlameTransformation
	 * @see {@link #Builder(FlameTransformation flameTransformation) Le constructeur Builder()}
	 */
	public static class Builder{
		private AffineTransformation affineTransformationBuilder;
		private double[] variationWeightBuilder;
		
		/**
		 * Constructeur du bâtisseur de Transformation Flame
		 * @param flameTransformation La transformation Flame à bâtir
		 * @see <a href="https://dl.dropbox.com/u/45709343/yodawg.png">Yo dawg</a>
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
		 * Donne le poids de variation d'index donné
		 * @param index L'index de la variation
		 * @return Le poids de variation
		 * @throws IndexOutOfBoundsException si l'index de variation est invalide.
		 */
		public double getVariationWeight(int index){
			if (index > this.variationWeightBuilder.length || index < 0){
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
		 * Change le poids de variation d'index donné
		 * @param index L'index de la variation à modifier
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
			FlameTransformation flameTransformation = new FlameTransformation(this.affineTransformationBuilder, this.variationWeightBuilder);
			return flameTransformation;
		}
	}

}
