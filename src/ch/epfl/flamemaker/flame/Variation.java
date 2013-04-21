package ch.epfl.flamemaker.flame;

import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * Classe abstraite modélisant les variations. La classe implémente l'interface
 * {@link Transformation}
 * 
 * @see {@link #Variation(int, String) Le constructeur Variation()}
 * @see Transformation
 */
public abstract class Variation implements Transformation {
	private final int index;
	private final String name;

	/**
	 * Le constructeur de variation, caractérisée par son index et son nom.
	 * @param index L'index de la variation
	 * @param name Le nom de la variation
	 */
	private Variation(int index, String name) {
		this.index = index;
		this.name = name;
	}


	/**
	 * Méthode transformant le point p selon la variation.
	 */
	abstract public Point transformPoint(Point p);
	

	/**
	 * La liste publique et statique des 6 variations (linear, sinusoidal,
	 * spherical, swirl, horseshoe, bubble).<br>
	 * Les 6 variations redéfinissent la méthode {@link #transformPoint(Point)}
	 */
	public final static List<Variation> ALL_VARIATIONS = Arrays.asList(
			new Variation(0, "Linear") {
				public Point transformPoint(Point p) {
					return p;
				}
			}, new Variation(1, "Sinusoidal") {
				public Point transformPoint(Point p) {
					double x = Math.sin(p.x());
					double y = Math.sin(p.y());
					return new Point(x, y);
				}
			}, new Variation(2, "Spherical") {
				public Point transformPoint(Point p) {
					double x = p.x() / Math.pow(p.r(), 2);
					double y = p.y() / Math.pow(p.r(), 2);
					return new Point(x, y);
				}
			}, new Variation(3, "Swirl") {
				public Point transformPoint(Point p) {
					double x = p.x() * Math.sin(Math.pow(p.r(), 2)) - p.y()
							* Math.cos(Math.pow(p.r(), 2));
					double y = p.x() * Math.cos(Math.pow(p.r(), 2)) + p.y()
							* Math.sin(Math.pow(p.r(), 2));
					return new Point(x, y);
				}
			}, new Variation(4, "Horseshoe") {
				public Point transformPoint(Point p) {
					double x = (p.x() - p.y()) * (p.x() + p.y()) / p.r();
					double y = (2 * p.x() * p.y()) / p.r();
					return new Point(x, y);
				}
			}, new Variation(5, "Bubble") {
				public Point transformPoint(Point p) {
					double x = (4 * p.x()) / (Math.pow(p.r(), 2) + 4);
					double y = (4 * p.y()) / (Math.pow(p.r(), 2) + 4);
					return new Point(x, y);
				}
			});
	public int getIndex() {return this.index;}
	public String getName() {return this.name;}
}