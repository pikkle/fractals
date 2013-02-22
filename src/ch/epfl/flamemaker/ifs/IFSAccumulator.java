package ch.epfl.flamemaker.ifs;

public final class IFSAccumulator {
	boolean[][] isHit;

	public IFSAccumulator(boolean[][] isHit) {
		// TODO construit un accumulateur avec le tableau bi-dimensionnel donn�.
		// Ce tableau doit absolument �tre copi� en profondeur par le
		// constructeur afin de garantir l'immuabilit� de l'accumulateur.
	}

	public int width() {
		return this.isHit.length;
	}

	public int height() {
		return this.isHit[0].length;
	}

	public boolean isHit(int x, int y) {
		// TODO retourne vrai si et seulement si la case de l'accumulateur aux
		// coordonn�es donn�es contient au moins un point de S (on dit alors que
		// la case a �t� touch�e, d'o� le nom de la m�thode).L�ve l'exception
		// IndexOutOfBoundsException si l'une de deux coordonn�es est invalide.
		return false;
	}
}
