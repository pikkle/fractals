package ch.epfl.flamemaker.gui;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.Flame.Builder;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;

public class ObservableFlameBuilder{
	private Set<Observer> observers = new HashSet<Observer>();
	private Flame.Builder flameBuilder;
	
	public ObservableFlameBuilder(Flame flame){
		this.flameBuilder = new Builder(flame);
	}
	public int transformationCount(){
		return flameBuilder.transformationCount();
	}
	public AffineTransformation affineTransformation(int index){
		return flameBuilder.affineTransformation(index);
	}
	public double variationWeight(int index, Variation v){
		return flameBuilder.variationWeight(index, v);
	}
	
	public void addTransformation(FlameTransformation t){
		flameBuilder.addTransformation(t);
		notifyObservers();
	}
	public void removeTransformation(int index){
		flameBuilder.removeTransformation(index);
		notifyObservers();
	}
	public void setAffineTransformation(int index, AffineTransformation t){
		flameBuilder.setAffineTransformation(index, t);
		notifyObservers();
	}
	public void setVariationWeight(int index, Variation v, double w){
		flameBuilder.setVariationWeight(index, v, w);
		notifyObservers();
	}
	
	public Flame build(){
		return flameBuilder.build();
	}
	
	
	private void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}

	public void addObserver(Observer o){
		observers.add(o);
	}
	public void removeObserver(Observer o){
		observers.remove(o);
	}
	interface Observer{
		void update();
	}
}