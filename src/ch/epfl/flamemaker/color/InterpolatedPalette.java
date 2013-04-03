package ch.epfl.flamemaker.color;

import java.util.List;

public class InterpolatedPalette {

	/**
	 * @param args
	 */
	public InterpolatedPalette(List<Color> listColor){
		if (listColor.size()<2){
			throw new IllegalArgumentException("la liste de couleur est trop courte");
		}
		
	}


}
