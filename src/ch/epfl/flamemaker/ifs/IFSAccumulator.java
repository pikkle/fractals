package ch.epfl.flamemaker.ifs;

public final class IFSAccumulator {
	boolean[][] isHit;

	public IFSAccumulator(boolean[][] isHit) {
		// TODO construit un accumulateur avec le tableau bi-dimensionnel donné.
		// Ce tableau doit absolument être copié en profondeur par le
		// constructeur afin de garantir l'immuabilité de l'accumulateur.
	}

	public int width() {
		return this.isHit.length;
	}

	public int height() {
		return this.isHit[0].length;
	}

	public boolean isHit(int x, int y) {
		// TODO retourne vrai si et seulement si la case de l'accumulateur aux
		// coordonnées données contient au moins un point de S (on dit alors que
		// la case a été touchée, d'où le nom de la méthode).Lève l'exception
		// IndexOutOfBoundsException si l'une de deux coordonnées est invalide.
		return false;
	}
}
