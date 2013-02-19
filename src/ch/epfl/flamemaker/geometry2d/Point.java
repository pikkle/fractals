package ch.epfl.flamemaker.geometry2d;

public class Point {
	private final double X;
	private final double Y;
	public final static Point ORIGIN = new Point(0,0);

	
	public Point(double unX, double unY){
		this.X = unX;
		this.Y = unY;
	}
	public double x(){
		return X;
	}
	public double y()
	{
		return Y;
	}
	public double r(){
		return (Math.sqrt(X*X+Y*Y));
	}
	public double theta(){
		return Math.atan2(X, Y);
		
	}
	public String toString(){
		return("("+ X +", "+Y+")");
	}
	//TODO
}
