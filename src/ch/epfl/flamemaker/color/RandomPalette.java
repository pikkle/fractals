package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe modelisant une palette aleatoire
 * @see {@link #RandomPalette(int) RandomPalette()} le constructeur
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class RandomPalette implements Palette{
	InterpolatedPalette randomPalette;

	/**
	 * Constructeur d'une palette aleatoire.
	 * @param sizeListColor Le nombre de couleurs de base. Les couleurs intermediaires seront une interpolation de ces couleurs.
	 * @see InterpolatedPalette
	 */
	public RandomPalette(int sizeListColor){
		List<Color> listColor = new ArrayList<Color>();
		Random r =  new Random();
		for(int i=0; i< sizeListColor;i++){
			//genere une couleur avec des composantes aleatoires
			listColor.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
		}
		//cree une palette interpolee avec les couleurs aleatoires.
		InterpolatedPalette randomPalette = new InterpolatedPalette(listColor); 
		this.randomPalette = randomPalette;
	}

	@Override
	public Color colorForIndex(double index) {
		//retourne la couleur de la palette interpolee a l'index donnee en parametre
		return randomPalette.colorForIndex(index); 
	}

}
