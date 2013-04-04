package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe mod�lisante une palette de couleurs interpol�es
 * La classe impl�mente l'interface {@link Palette}
 * @see {@link #InterpolatedPalette(List) InterpolatedPalette()}
 */
public class InterpolatedPalette implements Palette {
	double[] listIndex;
	List<Color> listColor;
	
	/**
	 * Constructeur de la palette interpol�e.
	 * Lance l'exception {@link IllegalArgumentException} si la liste de couleurs est trop courte.
	 * @param listColor La liste des {@link Color}
	 */
	public InterpolatedPalette(List<Color> listColor) {
		if (listColor.size() < 2) {
			throw new IllegalArgumentException(
					"la liste de couleur est trop courte");
		}
		
		this.listColor = new ArrayList<Color>(listColor);
		double[] listIndex = new double[listColor.size()]; //listIndex repr�sente la liste des index des couleurs donn�es dans listColor
		for (int i = 0; i < listColor.size(); i++) {	   //exemple: la listColor Rouge, Vert, Bleu donne la listIndex : {0, 0.5. 1}
			listIndex[i] = ((1 / (listColor.size()-1)) * i);
		}
	}
	/**
	 * M�thode retournant la couleur situ�e � l'index pass� en param�tre dans la palette.
	 * @param index L'index de la couleur demand�e.
	 * @return La couleur correspondante.
	 */
	@Override
	public Color colorForIndex(double index) {
		if (index <0 || index >1){
			throw new IllegalArgumentException("l'index n'est pas valide");
		}
		
		double indexColor = index * (listColor.size()-1);
		int floor = (int) Math.floor(indexColor); //D�termine l'index de la couleur de base
		int ceil = (int) Math.ceil(indexColor); //D�termine l'index de la seconde couleur
		
		if (floor == ceil){return listColor.get(floor);} //Si l'index demand� tombe sur un couleur de base de la palette, on retourne cette couleur.
		else {
			double proportion = ceil - indexColor;
			return listColor.get(floor).mixWith(listColor.get(ceil),proportion); //M�lange les deux couleurs.s
		}
	}

}
