package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe modélisante une palette de couleurs interpolées
 * La classe implémente l'interface {@link Palette}
 * @see {@link #InterpolatedPalette(List) InterpolatedPalette()}
 */
public class InterpolatedPalette implements Palette {
	double[] listIndex;
	List<Color> listColor;
	
	/**
	 * Constructeur de la palette interpolée.
	 * Lance l'exception {@link IllegalArgumentException} si la liste de couleurs est trop courte.
	 * @param listColor La liste des {@link Color}
	 */
	public InterpolatedPalette(List<Color> listColor) {
		if (listColor.size() < 2) {
			throw new IllegalArgumentException(
					"la liste de couleur est trop courte");
		}
		
		this.listColor = new ArrayList<Color>(listColor);
		double[] listIndex = new double[listColor.size()]; //listIndex représente la liste des index des couleurs données dans listColor
		for (int i = 0; i < listColor.size(); i++) {	   //exemple: la listColor Rouge, Vert, Bleu donne la listIndex : {0, 0.5. 1}
			listIndex[i] = ((1 / (listColor.size()-1)) * i);
		}
	}
	/**
	 * Méthode retournant la couleur située à l'index passé en paramètre dans la palette.
	 * @param index L'index de la couleur demandée.
	 * @return La couleur correspondante.
	 */
	@Override
	public Color colorForIndex(double index) {
		if (index <0 || index >1){
			throw new IllegalArgumentException("l'index n'est pas valide");
		}
		
		double indexColor = index * (listColor.size()-1);
		int floor = (int) Math.floor(indexColor); //Détermine l'index de la couleur de base
		int ceil = (int) Math.ceil(indexColor); //Détermine l'index de la seconde couleur
		
		if (floor == ceil){return listColor.get(floor);} //Si l'index demandé tombe sur un couleur de base de la palette, on retourne cette couleur.
		else {
			double proportion = ceil - indexColor;
			return listColor.get(floor).mixWith(listColor.get(ceil),proportion); //Mélange les deux couleurs.s
		}
	}

}
