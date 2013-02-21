package testJUnit;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.flamemaker.geometry2d.Point;

public class PointTest {
	private static double DELTA = 0.000000001;

	@Test
	public void testPoint() {
		new Point(1, 1);
	}

	@Test
	public void testRayon() {
		assertEquals(2, new Point(2, 0).r(), DELTA);
	}

	@Test
	public void testTheta() {
		assertEquals(Math.PI / 2, new Point(0, 1).theta(), DELTA);
	}
}
