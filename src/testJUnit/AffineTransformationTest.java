package testJUnit;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;

public class AffineTransformationTest {
	@Test
	public void testNewTranslation(){
		AffineTransformation at = AffineTransformation.newTranslation(1,1);
		
	}

}
