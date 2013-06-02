package ch.epfl.flamemaker.color;

/**
 * Interface modelisant une palette de couleur
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public interface  Palette {
	/**
	 * Methode retournant la couleur correspondante a l'index passé en parametre
	 * @param index L'index de la couleur demandee.
	 * @return La couleur correspondante.
	 */
	Color colorForIndex(double index);

}
