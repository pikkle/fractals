package ch.epfl.flamemaker.ifs;

public final class IFSAccumulator {
	private final boolean[][] isHit;

	public IFSAccumulator(boolean[][] isHit) {
		if (isHit.length == 0) throw new IndexOutOfBoundsException("La liste est vide.");
		// Copie en profondeur
		this.isHit = new boolean[isHit.length][isHit[0].length];
		for (int i = 0; i < isHit.length; i++) {
			this.isHit[i] = isHit[i].clone();
		}
	}

	public int width() {
		return this.isHit.length;
	}

	public int height() {
		return this.isHit[0].length;
	}

	public boolean isHit(int x, int y) {
		if (x > isHit.length || x < 0 || y > isHit[0].length || y < 0){
			throw new IndexOutOfBoundsException("Les coordonnées données sont extérieures au cadre.");
		}
		if(this.isHit[x][y] == true)return true;
		return false;
	}
}
