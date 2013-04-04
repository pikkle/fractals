package ch.epfl.flamemaker.color;

/**
 * Interface modélisant une palette de couleur
 */
public interface  Palette {
	/**
	 * Méthode retournant la couleur correspondant à l'index passé en paramètre
	 * @param index L'index de la couleur demandée.
	 * @return La couleur correspondante.
	 */
	Color colorForIndex(double index);

}
