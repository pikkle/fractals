package testJUnit;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;

public class AffineTransformationTest {
	@Test
	public void testNewTranslation(){
		AffineTransformation at = AffineTransformation.newTranslation(1,1);
		
	}
	@Test
	public void testNewRotation(){
		AffineTransformation at = AffineTransformation.newRotation(Math.PI/2);
	}
	@Test
	public void testNewScaling(){
		AffineTransformation at = AffineTransformation.newScaling(1, 1);
	}
	@Test
	public void testNewShearX(){
		AffineTransformation at = AffineTransformation.newShearX(1);
	}
	@Test
	public void testNewShearY(){
		AffineTransformation at = AffineTransformation.newShearY(1);
	}
	@Test
	public void testTransformPoint(){
		AffineTransformation at = AffineTransformation.newTranslation(2, 2);
		Point p = new Point(1,1);
		p = AffineTransformation.transformPoint(p);
		
	}

}
