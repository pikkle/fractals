package ch.epfl.flamemaker.color;

public final class Color {
	double r,g,b;
	public Color(double r, double g, double b){
		if (r < 0 || r > 1 || g < 0 || g > 1 || b < 0 || b > 1){
			throw new IllegalArgumentException("Les composantes de couleur données ne sont pas valides.");
		}
		this.r = r;
		this.g = g;
		this.b = b;
	}
	double red(){return this.r;}
	double green(){return this.g;}
	double blue(){return this.b;}
	Color mixWitch(Color that, double proportion){
		if (proportion < 0 || proportion > 1){
			throw new IllegalArgumentException("La proportion donnée n'est pas valide.");
		}
		double r2, g2, b2;
		r2 = proportion*this.r + (1-proportion)*that.r;
		g2 = proportion*this.g + (1-proportion)*that.g;
		b2 = proportion*this.b + (1-proportion)*that.b;
		
		return new Color(r2,g2,b2);
	}
}
