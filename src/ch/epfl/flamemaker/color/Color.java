package ch.epfl.flamemaker.color;

public final class Color {
	double r,g,b;
	final static Color BLACK = new Color(0,0,0);
	final static Color WHITE = new Color(1,1,1);
	final static Color RED = new Color(1,0,0);
	final static Color GREEN = new Color(0,1,0);
	final static Color BLUE = new Color(0,0,1);
	
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
	public Color mixWitch(Color that, double proportion){
		if (proportion < 0 || proportion > 1){
			throw new IllegalArgumentException("La proportion donnée n'est pas valide.");
		}
		double r2, g2, b2;
		r2 = proportion*this.r + (1-proportion)*that.r;
		g2 = proportion*this.g + (1-proportion)*that.g;
		b2 = proportion*this.b + (1-proportion)*that.b;
		
		return new Color(r2,g2,b2);
	}
	public int asPackedRGB(){
		int rInt = sRGBEncode(this.r, 255)<<16;
		int gInt = sRGBEncode(this.g, 255)<<8;
		int bInt = sRGBEncode(this.b, 255);
		return (rInt|gInt|bInt);
		
		
	}
	public static int sRGBEncode(double v, int max){
		if (v<= 0.0031308){
			return (int) (max*(12.92*v));
		}
		else{
			return (int) (max*((1.055*(Math.pow(v,(1/2.4)))-0.055)));
		}
		
	}
}
