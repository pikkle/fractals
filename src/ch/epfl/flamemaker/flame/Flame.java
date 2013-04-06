package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.flame.FlameTransformation;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Class modélisant une fractale Flame
 * @see {@link #Flame(List)} le constructeur
 */
public class Flame {
	private final List<FlameTransformation> listTransfo;
	private final ArrayList<Double> colorTransfo;

	/**
	 * Constructeur de Flame
	 * @param transformations La liste de {@link FlameTransformation} caractérisant la fractale.
	 */
	public Flame(List<FlameTransformation> transformations) {
		this.listTransfo = new ArrayList<FlameTransformation>(transformations); //Copie de la liste de Transformations
		
		colorTransfo = new ArrayList<Double>(); //Création de la liste de couleurs correspondant aux transformations
		colorTransfo.add(0.0); //C0 = 0
		colorTransfo.add(1.0); //C1 = 1
		for (int i = 2; i < transformations.size(); i++) {
			double a = Math.pow(2, Math.ceil(Math.log(i) / Math.log(2))); //Formule pour que C2 = 1/2; C3 = 1/4; C4 = 3/4; C5 = 1/8; C6 = 3/8; etc
			colorTransfo.add((2 * i - a -1) / a);
		}
	}
	
	/**
	 * Méthode calculant les points graphiques caractérisant la fractale.
	 * @param frame Le cadre qui délimite le calcul.
	 * @param width La largeur du rendu (affecte le nombre de points calculés). 
	 * @param height La hauteur du rendu (affecte le nombre de points calculés).
	 * @param density La densité de la fractale.
	 * @return La {@link FlameAccumulator} correspondant à la fractale créée
	 */
	public FlameAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		FlameAccumulator.Builder flameAccu = new FlameAccumulator.Builder(
				frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random(2013); //Utilise un random avec la seed 2013
		double c = 0.0;
		for (int k = 0; k < 20 + density * width * height; k++) {
			int i = r.nextInt(this.colorTransfo.size());
			p = this.listTransfo.get(i).transformPoint(p);
			c = (this.colorTransfo.get(i) + c) / 2.0;
			if (k > 20) { //20 premiers tours à blanc selon l'algorithme du chaos
				flameAccu.hit(p, c);
			}
		}
		return flameAccu.build();
	}

	/**
	 * Bâtisseur de fractale {@link Flame}
	 * @see Flame
	 */
	public static class Builder {
		private List<FlameTransformation> listTransfoBuilder;

		/**
		 * Constructeur de bâtisseur
		 * @param flame
		 * @see <a href="https://dl.dropbox.com/u/45709343/yodawg.png">Yo dawg</a>
		 */
		public Builder(Flame flame) {
			this.listTransfoBuilder = new ArrayList<FlameTransformation>(
					flame.listTransfo);

		}

		int transformationCount() {
			return listTransfoBuilder.size();
		}

		void addTransformation(FlameTransformation transformation) {
			listTransfoBuilder.add(transformation);
		}

		AffineTransformation affineTransformation(int index) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation flameTransformation = listTransfoBuilder
					.get(index);
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			return builder.getAffineTransformation();

		}

		void setAffineTransformation(int index,
				AffineTransformation newTransformation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			builder.setAffineTransformation(newTransformation);
			listTransfoBuilder.set(index, builder.build());

		}

		double variationWeight(int index, Variation variation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			return builder.getVariationWeight(index);

		}

		void setVariationWeight(int index, Variation variation, double newWeight) {

			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			builder.setVariationWeight(index,newWeight);
			listTransfoBuilder.set(index, builder.build());

		}

		void removeTransformation(int index) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			listTransfoBuilder.remove(index);

		}

		public Flame build() {
			Flame flame = new Flame(listTransfoBuilder);
			return flame;
		}

	}
}
