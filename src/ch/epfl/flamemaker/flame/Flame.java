package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.flame.FlameTransformation;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe modélisant une fractale Flame
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see {@link #Flame(List)} le constructeur
 */
public class Flame {
	private final List<FlameTransformation> listTransfo;
	private final ArrayList<Double> colorTransfo;

	/**
	 * Constructeur de Flame
	 *
	 * @param transformations La liste de {@link FlameTransformation} caractérisant la fractale.
	 */
	public Flame(List<FlameTransformation> transformations) {
		this.listTransfo = new ArrayList<FlameTransformation>(transformations); //Copie de la liste de Transformations

		colorTransfo = new ArrayList<Double>(); //Creation de la liste de couleurs correspondant aux transformations
		colorTransfo.add(0.0); //C0 = 0
		colorTransfo.add(1.0); //C1 = 1

		for (int i = 2; i < transformations.size(); i++) {
			double a = Math.pow(2, Math.ceil(Math.log(i) / Math.log(2))); //Formule pour que C2 = 1/2; C3 = 1/4; C4 = 3/4; C5 = 1/8; C6 = 3/8; etc
			colorTransfo.add((2 * i - a - 1) / a);
		}
	}

	/**
	 * Donne la liste de transformations affines
	 *
	 * @return La liste de transformations affines
	 */
	public List<FlameTransformation> getListTransfo() {
		return listTransfo;
	}

	/**
	 * Méthode calculant les points graphiques caractérisant la fractale
	 *
	 * @param frame   Le cadre qui délimite le calcul.
	 * @param width   La largeur du rendu (affecte le nombre de points calculés).
	 * @param height  La hauteur du rendu (affecte le nombre de points calculés).
	 * @param density La densité de la fractale.
	 * @return La {@link FlameAccumulator} correspondant a la fractale créée
	 */
	public FlameAccumulator compute(Rectangle frame, int width, int height,
	                                int density) {
		FlameAccumulator.Builder flameAccu = new FlameAccumulator.Builder(
				frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random(2013); //Utilise un random avec la seed 2013
		double c = 0.0;
		if (this.listTransfo.size() > 0) {
			for (int k = 0; k < 20 + density * width * height; k++) {
				int i = r.nextInt(this.listTransfo.size());
				p = this.listTransfo.get(i).transformPoint(p);
				// transforme le point avec une transformation aleatoire de la liste
				c = (this.colorTransfo.get(i) + c) / 2.0;
				if (k > 20) { //20 premiers tours a blanc selon l'algorithme du chaos
					flameAccu.hit(p, c);
				}
			}
		}
		return flameAccu.build();
	}

	/**
	 * Méthode calculant les points graphiques caractérisant la fractale avec multi-thread
	 *
	 * @param frame   Le cadre qui délimite le calcul.
	 * @param width   La largeur du rendu (affecte le nombre de points calculés).
	 * @param height  La hauteur du rendu (affecte le nombre de points calculés).
	 * @param density La densité de la fractale.
	 * @return La {@link FlameAccumulator} correspondant a la fractale créée
	 */
	public FlameAccumulator compute(Rectangle frame, int width, int height,
	                                int density, final int nbThread) {
		final FlameAccumulator.Builder flameAccu = new FlameAccumulator.Builder(
				frame, width, height);
		final Point p0 = new Point(0, 0);
		final double c0 = 0.0;
		final int nbCalc = (density * width * height / nbThread) + 20; // 20 premiers tours pour l'algorithme du chaos
		ExecutorService executor = Executors.newFixedThreadPool(nbThread); // Pool de thread
		if (this.listTransfo.size() > 0) {
			for (int i = 0; i < nbThread; i++) {
				Runnable worker = new Runnable() { // représente chaque thread qui se séparent la tâche
					@Override
					public void run() {
						Random r = new Random();
						Point p = p0;
						double c = c0;
						for (int k = 0; k < nbCalc; k++) {
							int i = r.nextInt(listTransfo.size());
							p = listTransfo.get(i).transformPoint(p);
							c = (colorTransfo.get(i) + c) / 2.0;
							if (k > 20) { //20 premiers tours a blanc selon l'algorithme du chaos
								flameAccu.hit(p, c);
							}
						}
					}
				};
				executor.execute(worker);
			}
			executor.shutdown(); // n'accepte plus de nouvelle tâche et force à éxécuter les tâches restantes
			while (!executor.isTerminated()) { // Attend que tous les workers ont fini leurs calculs
			}

		}
		return flameAccu.build();
	}

	/**
	 * Batisseur de fractale {@link Flame}
	 *
	 * @see Flame
	 */
	public static class Builder {
		private List<FlameTransformation> listTransfoBuilder;

		/**
		 * Constructeur de batisseur de flame
		 *
		 * @param flame La flame à accumuler.
		 */
		public Builder(Flame flame) {
			listTransfoBuilder = new ArrayList<FlameTransformation>(
					flame.listTransfo);

		}

		/**
		 * Donne le nombre de transformations Flame caracterisant
		 * la fractale en cours de construction.
		 *
		 * @return Le nombre de transformations de la fractale
		 */
		public int transformationCount() {
			return listTransfoBuilder.size();
		}

		/**
		 * Ajoute la transformation en parametre a la fractale en derniere position.
		 *
		 * @param transformation La transformation a ajouter a la fractale.
		 */
		public void addTransformation(FlameTransformation transformation) {
			listTransfoBuilder.add(transformation);
		}

		/**
		 * Retourne la composante affine de la transformation Flame d'index donne.
		 *
		 * @param index L'index de la transformation Flame
		 * @return La composante affine de la transformation Flame
		 * @throws IndexOutOfBoundsException Si l'index est invalide
		 */
		public AffineTransformation affineTransformation(int index) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			return new FlameTransformation.Builder(listTransfoBuilder.get(index)).getAffineTransformation();
		}

		/**
		 * Change la composante affine de la transformation Flame a l'index donne
		 *
		 * @param index             L'index de la transformation Flame a modifier
		 * @param newTransformation La transformation affine a changer
		 */
		public void setAffineTransformation(int index,
		                                    AffineTransformation newTransformation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(listTransfoBuilder.get(index));
			builder.setAffineTransformation(newTransformation);
			listTransfoBuilder.set(index, builder.build());
		}

		/**
		 * Retourne le poids de la variation donnee pour la transformation Flame d'index donne.
		 *
		 * @param index     L'index de la transformation Flame
		 * @param variation La variation sur laquelle porte le poids.
		 * @return Le poids de la variation en {@code double}
		 * @throws IndexOutOfBoundsException Si l'index est invalide
		 */
		public double variationWeight(int index, Variation variation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide " + index);
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(listTransfoBuilder.get(index));
			return builder.getVariationWeight(variation.getIndex());
		}

		/**
		 * Change le poids de la variation donnee pour la transformation Flame d'index donne.
		 *
		 * @param index     L'index de la transformation Flame
		 * @param variation La variation sur laquelle on change le poids
		 * @param newWeight Le nouveau poids de variation
		 * @throws IndexOutOfBoundsException Si l'index est invalide
		 */
		public void setVariationWeight(int index, Variation variation, double newWeight) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			builder.setVariationWeight(variation.getIndex(), newWeight);
			listTransfoBuilder.set(index, builder.build());
		}

		/**
		 * Supprime la transformation Flame d'index donne.
		 *
		 * @param index L'index de la transformation Flame a supprimer.
		 * @throws IndexOutOfBoundsException si l'index est invalide.
		 */
		public void removeTransformation(int index) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			listTransfoBuilder.remove(index);

		}

		/**
		 * Construit et retourne la fractale Flame.
		 *
		 * @return La fractale Flame.
		 */
		public Flame build() {
			Flame flame = new Flame(listTransfoBuilder);
			return flame;
		}
	}
}
