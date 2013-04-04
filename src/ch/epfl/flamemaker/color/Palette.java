package ch.epfl.flamemaker.color;

/**
 * Interface mod�lisant une palette de couleur
 */
public interface  Palette {
	/**
	 * M�thode retournant la couleur correspondant � l'index pass� en param�tre
	 * @param index L'index de la couleur demand�e.
	 * @return La couleur correspondante.
	 */
	Color colorForIndex(double index);

}
