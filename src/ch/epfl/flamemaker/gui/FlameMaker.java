package ch.epfl.flamemaker.gui;

/**
 * Classe principale lancant l'interface graphique Swing du Flame Maker
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 * @see FlameMakerGUI
 */
public class FlameMaker {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FlameMakerGUI().start();
			}
		});
	}
}