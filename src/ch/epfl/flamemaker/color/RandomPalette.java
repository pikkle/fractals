package ch.epfl.flamemaker.color;

import java.util.List;
import java.util.Random;

/**
 * Classe modélisant une palette aléatoire
 * @see {@link #RandomPalette(int) RandomPalette()} le constructeur
 */
public class RandomPalette implements Palette{
	List<Color> listColor;
	Random r =  new Random();
	InterpolatedPalette randomPalette;
	
	/**
	 * Constructeur d'une palette aléatoire.
	 * @param sizeListColor Le nombre de couleurs de base. Les couleurs intermédiaires seront une interpolation de ces couleurs.
	 * @see InterpolatedPalette
	 */
	public RandomPalette(int sizeListColor){
		for(int i=0; i< sizeListColor;i++){
			listColor.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble())); //génère une couleur avec des composantes aléatoires
		}
		InterpolatedPalette randomPalette = new InterpolatedPalette(listColor); //crée une palette interpolée avec les couleurs aléatoires.
		this.randomPalette = randomPalette;
		
	}
	
	@Override
	public Color colorForIndex(double index) {
		return randomPalette.colorForIndex(index); //retourne la couleur de la palette interpolée à l'index donnée en paramètre
	}

}
