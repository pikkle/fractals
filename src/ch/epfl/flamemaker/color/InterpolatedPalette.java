package ch.epfl.flamemaker.color;

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
		for (int i=0 ; i < this.listColor.size();i++){
			listColor.add(this.listColor.get(i));
		}
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
		int floor = (int) index*(listColor.size()-1);
		int roof = (floor +1);
		double proportion = index*(listColor.size()-1) -floor;
		Color colorOfIndex = listColor.get(floor).mixWitch(listColor.get(roof), proportion);
		return colorOfIndex;
	}

}
