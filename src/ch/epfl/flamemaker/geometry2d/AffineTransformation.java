package ch.epfl.flamemaker.geometry2d;

public class AffineTransformation implements Transformation {
	private final double a,b,c,d,e,f;
	AffineTransformation(double a, double b, double c, double d, double e, double f){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}
	@Override
	public Point transformPoint(Point p) {
		//TODO retourne le point p transformé
		return p;
	}
	
}
