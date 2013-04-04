package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.flame.FlameTransformation;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class Flame {
	private final List<FlameTransformation> listTransfo;
	private final ArrayList<Double> colorTransfo;

	public Flame(List<FlameTransformation> transformations) {
		this.listTransfo = new ArrayList<FlameTransformation>(transformations);
		colorTransfo = new ArrayList<Double>();
		colorTransfo.add(0.0);
		colorTransfo.add(1.0);
		for (int i = 2; i < transformations.size(); i++) {
			double a = Math.pow(2, Math.ceil(Math.log(i) / Math.log(2)));
			colorTransfo.add((2 * i - 1 - a) / a);
		}
	}

	public FlameAccumulator compute(Rectangle frame, int width, int height,
			int density) {
		FlameAccumulator.Builder flameAccu = new FlameAccumulator.Builder(
				frame, width, height);
		Point p = new Point(0, 0);
		Random r = new Random(2013);
		double c = 0.0;
		for (int k = 0; k < 20 + density * width * height; k++) {
			int i = r.nextInt(this.colorTransfo.size());
			p = this.listTransfo.get(i).transformPoint(p);
			c = (this.colorTransfo.get(i) + c) / 2.0;
			if (k > 20) {
				flameAccu.hit(p, c);
			}
		}
		return flameAccu.build();

	}

	public static class Builder {
		private List<FlameTransformation> listTransfoBuilder;

		public Builder(Flame flame) {
			this.listTransfoBuilder = new ArrayList<FlameTransformation>(
					flame.listTransfo);

		}

		int transformationCount() {
			return listTransfoBuilder.size();
		}

		void addTransformation(FlameTransformation transformation) {
			listTransfoBuilder.add(transformation);
		}

		AffineTransformation affineTransformation(int index) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation flameTransformation = listTransfoBuilder
					.get(index);
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			return builder.getAffineTransformation();

		}

		void setAffineTransformation(int index,
				AffineTransformation newTransformation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			builder.setAffineTransformation(newTransformation);
			listTransfoBuilder.set(index, builder.build());

		}

		double variationWeight(int index, Variation variation) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			return builder.getVariationWeight(index);

		}

		void setVariationWeight(int index, Variation variation, double newWeight) {

			FlameTransformation.Builder builder = new FlameTransformation.Builder(
					listTransfoBuilder.get(index));
			builder.setVariationWeight(index,newWeight);
			listTransfoBuilder.set(index, builder.build());

		}

		void removeTransformation(int index) {
			if (index > listTransfoBuilder.size() || index < 0) {
				throw new IndexOutOfBoundsException("l'index est invalide");
			}
			listTransfoBuilder.remove(index);

		}

		public Flame build() {
			Flame flame = new Flame(listTransfoBuilder);
			return flame;
		}

	}
}
