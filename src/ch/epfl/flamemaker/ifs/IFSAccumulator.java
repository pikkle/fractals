package ch.epfl.flamemaker.ifs;

/**
 * Classe immuable representant un tableau bi-dimensionnel de booleens.
 * Le tableau represente l'image d'une fractale point par point.
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see #IFSAccumulator(boolean[][])
 */
public final class IFSAccumulator {
	private final boolean[][] isHit;

	/**
	 * Constructeur IFSAccumulator. Copie en profondeur
	 * le tableau de booleens passe en parametre
	 *
	 * @param isHit Le tableau de booleens caracterisant l'accumulateur
	 */
	public IFSAccumulator(boolean[][] isHit) {
		if (isHit.length == 0) throw new IndexOutOfBoundsException("La liste est vide.");
		// Copie en profondeur
		this.isHit = new boolean[isHit.length][isHit[0].length];
		for (int i = 0; i < isHit.length; i++) {
			this.isHit[i] = isHit[i].clone();
		}
	}

	/**
	 * Donne la largeur de l'accumulateur
	 *
	 * @return La largeur du tableau
	 */
	public int width() {
		return isHit.length;
	}

	/**
	 * Donne la hauteur de l'accumulateur
	 *
	 * @return La hauteur du tableau
	 */
	public int height() {
		return isHit[0].length;
	}

	/**
	 * Evalue si l'accumulateur est "marque" aux coordonnees donnees.
	 *
	 * @param x La position x du tableau
	 * @param y La position y du tableau
	 * @return <i>true</i> si l'accumulateur est marque.
	 * <br> <i>false</i> sinon.
	 */
	public boolean isHit(int x, int y) {
		if (x >= isHit.length || x < 0 || y >= isHit[0].length || y < 0) {
			throw new IndexOutOfBoundsException("Les coordonnees donnees sont exterieures au cadre.");
		}
		if (this.isHit[x][y]) return true;
		return false;
	}
}
