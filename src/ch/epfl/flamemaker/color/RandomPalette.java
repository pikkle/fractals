package ch.epfl.flamemaker.color;

import java.util.List;
import java.util.Random;

/**
 * Classe mod�lisant une palette al�atoire
 * @see {@link #RandomPalette(int) RandomPalette()} le constructeur
 */
public class RandomPalette implements Palette{
	List<Color> listColor;
	Random r =  new Random();
	InterpolatedPalette randomPalette;
	
	/**
	 * Constructeur d'une palette al�atoire.
	 * @param sizeListColor Le nombre de couleurs de base. Les couleurs interm�diaires seront une interpolation de ces couleurs.
	 * @see InterpolatedPalette
	 */
	public RandomPalette(int sizeListColor){
		for(int i=0; i< sizeListColor;i++){
			listColor.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble())); //g�n�re une couleur avec des composantes al�atoires
		}
		InterpolatedPalette randomPalette = new InterpolatedPalette(listColor); //cr�e une palette interpol�e avec les couleurs al�atoires.
		this.randomPalette = randomPalette;
		
	}
	
	@Override
	public Color colorForIndex(double index) {
		return randomPalette.colorForIndex(index); //retourne la couleur de la palette interpol�e � l'index donn�e en param�tre
	}

}
