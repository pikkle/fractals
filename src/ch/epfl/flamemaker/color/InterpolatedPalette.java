package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;

public class InterpolatedPalette implements Palette {

	/**
	 * @param args
	 */
	double[] listIndex;
	List<Color> listColor;
	
	public InterpolatedPalette(List<Color> listColor) {
	
		if (listColor.size() < 2) {
			throw new IllegalArgumentException(
					"la liste de couleur est trop courte");
		}
		
		this.listColor = new ArrayList(listColor);
		double[] listIndex = new double[listColor.size()];
		for (int i = 0; i < listColor.size(); i++) {
			listIndex[i] = ((1 / (listColor.size()-1)) * i);
		}
	}
	
	@Override
	public Color colorForIndex(double index) {
		if (index <0 || index >1){
			throw new IllegalArgumentException("l'index n'est pas valide");
		}
		
		double indexColor = index * (listColor.size()-1);
		int floor = (int) Math.floor(indexColor);
		int ceil = (int) Math.ceil(indexColor);
		
		if (floor == ceil){return listColor.get(floor);}
		else {
			double proportion = ceil - index;
			return listColor.get(floor).mixWith(listColor.get(ceil),proportion);
		}
	}

}
