package ch.epfl.flamemaker.color;

import java.util.List;
import java.util.Random;

public class RandomPalette implements Palette{
	
	List<Color> listColor;
	Random r =  new Random();
	InterpolatedPalette randomPalette;
	
	public RandomPalette(int sizeListColor){
		for(int i=0; i< sizeListColor;i++){
			listColor.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
		}
		InterpolatedPalette randomPalette = new InterpolatedPalette(listColor);
		this.randomPalette = randomPalette;
		
	}
	@Override
	public Color colorForIndex(double index) {
		return randomPalette.colorForIndex(index);
	}

}
