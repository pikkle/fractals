package testJUnit.geometry2d;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;

public class AffineTransformationTest {
	private static double DELTA = 0.000000001;

	@Test
	public void testNewTranslation() {
		AffineTransformation at = AffineTransformation.newTranslation(1, 1);
		Point p = new Point(1, 1);
		p = at.transformPoint(p);
		assertEquals(p.x(), 2, DELTA);
		assertEquals(p.y(), 2, DELTA);
	}

	@Test
	public void testNewRotation() {
		AffineTransformation at = AffineTransformation.newRotation(Math.PI / 2);
		Point p = new Point(1, 2);
		p = at.transformPoint(p);
		assertEquals(p.x(), -2, DELTA);
		assertEquals(p.y(), 1, DELTA);
	}

	@Test
	public void testNewScaling() {
		AffineTransformation at = AffineTransformation.newScaling(2, 2);
		Point p = new Point(1, 1);
		p = at.transformPoint(p);
		assertEquals(p.x(), 2, DELTA);
		assertEquals(p.y(), 2, DELTA);
	}

	@Test
	public void testNewShearX() {
		AffineTransformation at = AffineTransformation.newShearX(1);
		Point p = new Point(1, 1);
		p = at.transformPoint(p);
		assertEquals(p.x(), 2, DELTA);
		assertEquals(p.y(), 1, DELTA);
	}

	@Test
	public void testNewShearY() {
		AffineTransformation at = AffineTransformation.newShearY(1);
		Point p = new Point(1, 1);
		p = at.transformPoint(p);
		assertEquals(p.x(), 1, DELTA);
		assertEquals(p.y(), 2, DELTA);
	}

	@Test
	public void testComposeWith() {
		AffineTransformation at1 = AffineTransformation.newTranslation(1, 1);
		AffineTransformation at2 = AffineTransformation
				.newRotation(Math.PI / 2);
		Point p = new Point(2, 2);
		AffineTransformation at3 = at1.composeWith(at2);
		Point p2 = at3.transformPoint(p);
		assertEquals(p2.x(), -1, DELTA);
		assertEquals(p2.y(), 3, DELTA);

	}

}
