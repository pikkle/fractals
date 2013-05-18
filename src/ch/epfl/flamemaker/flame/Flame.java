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
	
	public List<FlameTransformation> getListTransfo(){
		return listTransfo;
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
		if (this.listTransfo.size() > 0){
			for (int k = 0; k < 20 + density * width * height; k++) {
				int i = r.nextInt(this.listTransfo.size());
				p = this.listTransfo.get(i).transformPoint(p);
				c = (this.colorTransfo.get(i) + c) / 2.0;
				if (k > 20) { //20 premiers tours à blanc selon l'algorithme du chaos
					flameAccu.hit(p, c);
				}
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
		 * Constructeur de bâtisseur de flame
		 * @param flame
		 * @see <a href="https://dl.dropbox.com/u/45709343/yodawg.png">Yo dawg</a>
		 */
		public Builder(Flame flame) {
			listTransfoBuilder = new ArrayList<FlameTransformation>(
					flame.listTransfo);

		}

		/**
		 * Donne le nombre de transformations Flame caractérisant 
		 * la fractale en cours de construction.
		 * @return Le nombre de transformations de la fractale
		 */
		public int transformationCount() {
			return listTransfoBuilder.size();
		}

		/**
		 * Ajoute la transformation en paramètre à la fractale en dernière position.
		 * @param transformation La transformation à ajouter à la fractale.
		 */
		public void addTransformation(FlameTransformation transformation) {
			listTransfoBuilder.add(transformation);
		}

		/**
		 * Retourne la composante affine de la transformation Flame d'index donné.
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
		 * Change la composante affine de la transformation Flame à l'index donné
		 * @param index L'index de la transformation Flame à modifier
		 * @param newTransformation La transformation affine à changer
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
		 * Retourne le poids de la variation donnée pour la transformation Flame d'index donné.
		 * @param index L'index de la transformation Flame
		 * @param variation La variation sur laquelle porte le poids.
		 * @return Le poids de la variation en {@code double}
		 * @throws IndexOutOfBoundsException Si l'index est invalide
		 */
		public double variationWeight(int index, Variation variation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(listTransfoBuilder.get(index));
			return builder.getVariationWeight(variation.getIndex());
		}

		/**
		 * Change le poids de la variation donnée pour la transformation Flame d'index donné.
		 * @param index L'index de la transformation Flame
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
			builder.setVariationWeight(variation.getIndex(),newWeight);
			listTransfoBuilder.set(index, builder.build());
		}

		/**
		 * Supprime la transformation Flame d'index donné.
		 * @param index L'index de la transformation Flame à supprimer.
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
		 * @return La fractale Flame.
		 */
		public Flame build() {
			Flame flame = new Flame(listTransfoBuilder);
			return flame;
		}
	}
}
