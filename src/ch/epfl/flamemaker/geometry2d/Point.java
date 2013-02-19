package ch.epfl.flamemaker.geometry2d;

public class Point {
	private final double x;
	private final double y;
	public final static ORIGIN = new Point(0,0);

	
	public Point(double unX, double unY){
		this.x = unX;
		this.y = unY;
	}
	public double x(){
		return x;
	}
	public double y()
	{
		return y;
	}
	public double r(){
		return (Math.sqrt(x*x+y*y));
	}
	public double theta(){
		return Math.atan2(x, y);
		
	}
	public String toString(){
		return("("+ x +", "+y+")");
	}
	//TODO
}
