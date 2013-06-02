package ch.epfl.flamemaker.gui;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.Flame.Builder;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;

/**
 * Classe sous le modele du patron Decorator de la classe Flame.Builder
 * <br>La classe ObservableFlameBuilder ajoute simplement en plus de la 
 * classe Flame.Builder la possibilité d'utiliser le patron Observer,
 * de sorte a notifier les obervers lors d'un changement de la fractale.
 * @see Flame.Builder
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class ObservableFlameBuilder{
	private Set<Observer> observers = new HashSet<Observer>();
	private Flame.Builder flameBuilder;
	
	/**
	 * Constructeur d'ObservableFlameBuilder
	 * @param flame La fractale flame a construire
	 */
	public ObservableFlameBuilder(Flame flame){
		this.flameBuilder = new Builder(flame);
	}
	
	/**
	 * Donne le nombre de transformations affines qui caractérisent la fractale
	 * @return Le nombre de transformations affines
	 */
	public int transformationCount(){
		return flameBuilder.transformationCount();
	}
	
	/**
	 * La transformation affine correspondant a l'index donne
	 * @param index L'index de la transformation affine
	 * @return La transformation affine correspondante
	 */
	public AffineTransformation affineTransformation(int index){
		return flameBuilder.affineTransformation(index);
	}
	
	/**
	 * Donne le poids de la variation donnee de la transformation donnee
	 * @param index L'index de la transformation flame
	 * @param v La variation
	 * @return Le poids de la variation de la transformation flame
	 */
	public double variationWeight(int index, Variation v){
		return flameBuilder.variationWeight(index, v);
	}
	
	/**
	 * Ajoute la transformation flame au builder. 
	 * <br>Previent les observateurs du changement.
	 * @param t La transformation flame a ajouter
	 */
	public void addTransformation(FlameTransformation t){
		flameBuilder.addTransformation(t);
		notifyObservers();
	}
	
	/**
	 * Supprime la transformation flame a l'index donne.
	 * <br>Previent les observateurs du changement.
	 * @param index L'index de la transformation a supprimer
	 */
	public void removeTransformation(int index){
		flameBuilder.removeTransformation(index);
		notifyObservers();
	}
	
	/**
	 * Modifie la transformation affine d'index donne.
	 * <br>Previent les observateurs du changement.
	 * @param index L'index de la transformation affine a modifier
	 * @param t La nouvelle transformation affine 
	 */
	public void setAffineTransformation(int index, AffineTransformation t){
		flameBuilder.setAffineTransformation(index, t);
		notifyObservers();
	}
	
	/**
	 * Change le poids de variation de la transformation flame d'index donne.
	 * <br>Previent les observateurs du changement.
	 * @param index L'index de la transformation flame
	 * @param v La variation a modifier.
	 * @param w Le nouveau poids de la variation v.
	 */
	public void setVariationWeight(int index, Variation v, double w){
		flameBuilder.setVariationWeight(index, v, w);
		notifyObservers();
	}
	
	/**
	 * Construit et retourne la fractale Flame
	 * @return La fractale Flame
	 */
	public Flame build(){
		return flameBuilder.build();
	}
	
	/**
	 * Methode sur le principe de l'observer pattern.
	 * <br>Notifie tous les membres de sa liste d'observateurs.
	 */
	private void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}

	/**
	 * Ajoute un observateur a sa liste
	 * @param o Le nouvel observateur
	 */
	public void addObserver(Observer o){
		observers.add(o);
	}
	
	/**
	 * Supprime un observateur de sa liste
	 * @param o L'observateur a supprimer
	 */
	public void removeObserver(Observer o){
		observers.remove(o);
	}
	
	
	/**
	 * Interface modelisant un observateur, sur le modele de l'observer pattern.
	 */
	interface Observer{
		/**
		 * Methode mettant a jour l'observateur.
		 * <br> La methode est appelee a chaque fois que 
		 * l'observe considere qu'il y a un changement
		 */
		void update();
	}
}