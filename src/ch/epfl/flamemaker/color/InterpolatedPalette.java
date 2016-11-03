package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe modelisante une palette de couleurs interpolees
 * La classe implemente l'interface {@link Palette}
 * @see {@link #InterpolatedPalette(List) InterpolatedPalette()}
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class InterpolatedPalette implements Palette {
	double[] listIndex;
	List<Color> listColor;

	/**
	 * Constructeur de la palette interpolee.
	 * @param listColor La liste des {@link Color}
	 * @throws IllegalArgumentException si la liste de couleur est trop courte
	 */
	public InterpolatedPalette(List<Color> listColor) {
		if (listColor.size() < 2) {
			throw new IllegalArgumentException(
					"la liste de couleur est trop courte");
		}

		this.listColor = new ArrayList<Color>(listColor);
		double[] listIndex = new double[listColor.size()]; //listIndex represente la liste des index des couleurs donnees dans listColor
		for (int i = 0; i < listColor.size(); i++) {	   //exemple: la listColor Rouge, Vert, Bleu donne la listIndex : {0, 0.5. 1}
			listIndex[i] = ((1 / (listColor.size()-1)) * i);
		}
	}

	/**
	 * @throws IllegalArgumentException si l'index n'est pas valide
	 */
	@Override
	public Color colorForIndex(double index) {
		if (index <0 || index >1){
			throw new IllegalArgumentException("l'index n'est pas valide " + index);
		}

		double indexColor = index * (listColor.size()-1);
		int floor = (int) Math.floor(indexColor); //Determine l'index de la couleur de base
		int ceil = (int) Math.ceil(indexColor); //Determine l'index de la seconde couleur

		if (floor == ceil){
			return listColor.get(floor);
		} //Si l'index demande tombe sur un couleur de base de la palette, on retourne cette couleur.
		else {
			double proportion = ceil - indexColor;
			return listColor.get(floor).mixWith(listColor.get(ceil),proportion); //Melange les deux couleurs
		}
	}

}
