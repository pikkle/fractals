package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.color.RandomPalette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Classe contenant l'interface graphique du programme FlameMaker
 *
 * @author Loic Serafin 214977
 * @author Christophe Gaudet-Blavignac 224410
 */
public class FlameMakerGUI {
	// Initialisée avec la fractale Shark-fin
	private ObservableFlameBuilder flameBuilder = new ObservableFlameBuilder(
			new Flame(Arrays.asList(new FlameTransformation[]{
					new FlameTransformation(
							new AffineTransformation(-0.4113504, -0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
							new double[]{1, 0.1, 0, 0, 0, 0}),
					new FlameTransformation(
							new AffineTransformation(-0.3957339, 0, -1.6, 0, -0.3957337, 0.2),
							new double[]{0, 0, 0, 0, 0.8, 1}),
					new FlameTransformation(
							new AffineTransformation(0.4810169, 0, 1, 0, 0.4810169, 0.9),
							new double[]{1, 0, 0, 0, 0, 0})
			})
			)
	);
	private Color background = Color.BLACK;
	private Palette palette = new InterpolatedPalette(Arrays.asList(
			new Color[]{Color.RED, Color.GREEN, Color.BLUE}));
	private Rectangle frame = new Rectangle(new Point(-0.25, 0), 5, 4);
	private int density = 50;
	private int selectedTransformationIndex;
	private Set<Observer> observers = new HashSet<Observer>();
	private JPanel panneauSuperieur, panneauInferieur;
	private FlameBuilderPreviewComponent fbpc;
	private JPanel bigPanneauInferieur;

	/**
	 * Méthode principale de l'interface graphique, compose la fenêtre
	 * en différents éléments.
	 * <br>Subdivise la fenêtre en quatre parties:
	 * <ul>
	 * <li>Le coin supérieur gauche, contenant les transformations affines dans un plan
	 * ({@link #panneauGraphiqueTransformationsAffines(JPanel)})</li>
	 * <li>Le coin supérieur droite, contenant l'affichage graphique de la fractale
	 * ({@link #panneauFractale(JPanel)})</li>
	 * <li>Le coin inférieur gauche, contenant la liste des transformations
	 * ({@link #panneauListeFlameTransformations(JPanel)}</li>
	 * <li>Le coin inférieur droite, contenant les modifications à apporter à la transformation sélectionnée
	 * ({@link #panneauTransformationSelect(JPanel)})</li>
	 * </ul>
	 * Chaque partie possède une méthode séparée qui compose son contenu.
	 */
	public void start() {
		// Ouverture de la fenêtre principale
		JFrame jframe = new JFrame("Flame Maker");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			//Met le thème par défaut
		}

		// Partie inférieure de la fenêtre
		bigPanneauInferieur = new JPanel(); // Le big panneau inférieur sépare
		// la partie principale contenant l'édition des transformation
		// et la partie secondaire contenant la partie des options complémentaires
		jframe.getContentPane().add(bigPanneauInferieur, BorderLayout.PAGE_END);
		bigPanneauInferieur.setLayout(new BorderLayout());

		this.panneauInferieur = new JPanel();
		bigPanneauInferieur.add(panneauInferieur, BorderLayout.CENTER);

		panneauInferieur.setLayout(new BoxLayout(panneauInferieur, BoxLayout.LINE_AXIS));

		// Partie supérieure, contenant une représentation graphiques des 
		// transformations et l'affichage de la fractale
		this.panneauSuperieur = new JPanel();
		jframe.getContentPane().add(panneauSuperieur, BorderLayout.CENTER);
		panneauSuperieur.setLayout(new GridLayout(1, 2));

		//Appel des quatre parties de la fenêtre + La partie bonus
		panneauSuperieur.add(panneauGraphiqueTransformationsAffines());
		panneauSuperieur.add(panneauFractale());
		panneauInferieur.add(panneauListeFlameTransformations());
		panneauInferieur.add(panneauTransformationSelect());
		bigPanneauInferieur.add(panneauBoutonsSup(), BorderLayout.PAGE_END);

		jframe.pack();
		jframe.setVisible(true);
	}

	/**
	 * Compose le coin supérieur gauche de la fenêtre
	 * contenant le graphe des transformations affines.
	 *
	 * @see AffineTransformationsComponent
	 */
	private JPanel panneauGraphiqueTransformationsAffines() {
		JPanel transPanel = new JPanel();
		transPanel.setLayout(new BorderLayout());
		Border transBorder = BorderFactory.createTitledBorder("Transformations affines");
		transPanel.setBorder(transBorder);

		final AffineTransformationsComponent atc = new AffineTransformationsComponent(flameBuilder, frame);
		addObserver(new Observer() {
			@Override
			public void update() {
				atc.setHighlightedTransformationIndex(selectedTransformationIndex);
			}
		});
		transPanel.add(atc);
		return transPanel;
	}

	/**
	 * Compose le coin supérieur droite de la fenêtre
	 * contenant la prévisualisation de la fractale
	 *
	 * @see FlameBuilderPreviewComponent
	 */
	private JPanel panneauFractale() {
		JPanel fracPanel = new JPanel();
		fracPanel.setLayout(new BorderLayout());
		Border fracBorder = BorderFactory.createTitledBorder("Fractale");
		fracPanel.setBorder(fracBorder);

		fbpc = new FlameBuilderPreviewComponent(
				this.flameBuilder, this.background, this.palette, this.frame, this.density);
		fracPanel.add(fbpc, BorderLayout.CENTER);
		flameBuilder.addObserver(new ObservableFlameBuilder.Observer() {
			@Override
			public void update() {
				fbpc.repaint(); //Redessine le component de la fractale
			}
		});
		return fracPanel;
	}

	/**
	 * Compose le coin inférieur gauche de la fenêtre
	 * contenant la liste des transformations flame
	 *
	 * @see TransformationsListModel
	 */
	private JPanel panneauListeFlameTransformations() {
		// Partie gauche inférieure, contenant l'affichage textuel des transformations
		JPanel transEditPanel = new JPanel();
		Border transEditBorder = BorderFactory.createTitledBorder("Liste des transformations");
		transEditPanel.setBorder(transEditBorder);
		transEditPanel.setLayout(new BorderLayout());

		final TransformationsListModel tlm = new TransformationsListModel();
		final JList<String> jListe = new JList<String>(tlm); //Liste contenant les noms des transformations
		jListe.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setSelectedTransformationIndex(jListe.getSelectedIndex()); // change l'index de la transformation selectionnée
			}
		});

		jListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //On ne peut sélectionner 
		//qu'un seul élément à la fois
		jListe.setVisibleRowCount(3);//On voit au minimum 3 éléments de la liste
		jListe.setSelectedIndex(0);//On séléctionne initialement le premier élément de la liste


		// Liste affichée des noms des transformations
		JScrollPane listPane = new JScrollPane(jListe);
		transEditPanel.add(listPane, BorderLayout.CENTER);

		// Panneau des deux boutons (ajouter & supprimer)
		JPanel buttonsPanel = new JPanel();
		transEditPanel.add(buttonsPanel, BorderLayout.PAGE_END);
		buttonsPanel.setLayout(new GridLayout(1, 2));

		//Bouton d'ajout & de suppression de transformation
		JButton addButton = new JButton("Ajouter");
		final JButton removeButton = new JButton("Supprimer");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeButton.setEnabled(!removeButton.isEnabled()); // Reactive le bouton supprimer
				tlm.addTransformation(); // Ajoute au component un élément
				setSelectedTransformationIndex(tlm.getSize() - 1); // Selectionne le nouvel élément ajouté
				jListe.setSelectedIndex(selectedTransformationIndex);
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int j = jListe.getSelectedIndex();
				if (selectedTransformationIndex == tlm.getSize() - 1) { // Défini si l'élément à supprimer
					// est le dernier de la liste
					jListe.setSelectedIndex(tlm.getSize() - 2); //Sélectionne l'avant-dernier élément de la liste
					tlm.removeTransformation(j); //Supprime le dernier élément de la liste
				} else {
					jListe.setSelectedIndex(j + 1); // Sinon on sélectionne la transformation après
					// la transformation qui sera supprimée
					tlm.removeTransformation(j);
				}
				removeButton.setEnabled(tlm.getSize() != 1); // On désactive le bouton s'il ne reste plus
				// qu'une transformation
				notifyObservers();
			}
		});
		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);
		return transEditPanel;
	}

	/**
	 * Compose le coin inférieur droite de la fenêtre
	 * contenant l'édition de la transformation sélectionnée
	 * en deux parties
	 * <ul>
	 * <li>celle contenant tous les boutons sur
	 * la partie affine de la transformation
	 * ({@link #panneauEditAffine()})</li>
	 * <li>celle contenant l'affectation des poids
	 * de variation de la transformation
	 * ({@link #panneauVariations()})</li>
	 * </ul>
	 */
	private JPanel panneauTransformationSelect() {
		// Partie inférieure droite, contenant l'édition de la transformation sélectionnée
		JPanel selectedTransfEditPanel = new JPanel();
		Border selectedTransfEditBorder = BorderFactory.createTitledBorder("Transformation courante");
		selectedTransfEditPanel.setBorder(selectedTransfEditBorder);
		selectedTransfEditPanel.setLayout(new BoxLayout(selectedTransfEditPanel, BoxLayout.PAGE_AXIS));

		selectedTransfEditPanel.add(panneauEditAffine());
		selectedTransfEditPanel.add(new JSeparator());
		selectedTransfEditPanel.add(panneauVariations());

		return selectedTransfEditPanel;
	}

	/**
	 * Compose la grille des boutons d'édition de la partie affine de la transformation sélectionnée
	 *
	 * @return Le panneau contenant la grille des boutons.
	 */
	private JPanel panneauEditAffine() {
		// Panneau d'édition de la partie affine
		JPanel affineEditPanel = new JPanel();
		GroupLayout groupLayTr = new GroupLayout(affineEditPanel);

		// Création des labels
		JLabel translationLabel = new JLabel("Translation");
		JLabel rotationLabel = new JLabel("Rotation");
		JLabel dilatationLabel = new JLabel("Dilatation");
		JLabel transvectionLabel = new JLabel("Transvection");

		// Création des zones de texte
		final JFormattedTextField translationTextF = new JFormattedTextField(new DecimalFormat("#0.##"));
		translationTextF.setValue(0.1);
		translationTextF.setHorizontalAlignment(SwingConstants.RIGHT);
		final JFormattedTextField rotationTextF = new JFormattedTextField(new DecimalFormat("#0.##"));
		rotationTextF.setValue(15);
		rotationTextF.setHorizontalAlignment(SwingConstants.RIGHT);
		final JFormattedTextField dilatationTextF = new JFormattedTextField(new DecimalFormat("#0.##"));
		dilatationTextF.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				AbstractFormatter f = dilatationTextF.getFormatter();
				// Si la valeur entrée par l'utilisateur est 0
				try {
					if (((Number) f.stringToValue(dilatationTextF.getText())).doubleValue() == 0) {
						// La valeur revient à son ancienne valeur valide
						dilatationTextF.setText(f.valueToString(dilatationTextF.getValue()));
					}
				} catch (ParseException e) {
					try {
						// La valeur entrée n'est pas valide, on revient à la valeur valide précédente
						dilatationTextF.setText(f.valueToString(dilatationTextF.getValue()));
					} catch (ParseException e1) {
						assert (false); // Impossible d'avoir cette erreur, car on prend la dernière valeur valide
						e1.printStackTrace();
					}
				}
				return true;
			}
		});
		dilatationTextF.setValue((double) 1.05);
		dilatationTextF.setHorizontalAlignment(SwingConstants.RIGHT);
		final JFormattedTextField transvectionTextF = new JFormattedTextField(new DecimalFormat("#0.##"));
		transvectionTextF.setValue(0.1);
		transvectionTextF.setHorizontalAlignment(SwingConstants.RIGHT);

		// Création des boutons de dilatation
		JButton dilatationPlusH = new JButton("+↔");
		dilatationPlusH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(val, 1);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(scale);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton dilatationMinusH = new JButton("-↔");
		dilatationMinusH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(1 / val, 1);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(scale);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton dilatationPlusV = new JButton("+↕");
		dilatationPlusV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(1, val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(scale);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton dilatationMinusV = new JButton("-↕");
		dilatationMinusV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(1, 1 / val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(scale);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});

		// Création des boutons de translation
		JButton translationLeft = new JButton("←");
		translationLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(-val, 0);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(selectedTransformationIndex));
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton translationRight = new JButton("→");
		translationRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(val, 0);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(selectedTransformationIndex));
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton translationUp = new JButton("↑");
		translationUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(0, val);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(selectedTransformationIndex));
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton translationDown = new JButton("↓");
		translationDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(0, -val);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(selectedTransformationIndex));
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});

		// Création des boutons de rotation
		JButton rotationAntiC = new JButton("↺");
		rotationAntiC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(rotationTextF.getValue().toString());
				AffineTransformation rot = AffineTransformation.newRotation(val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(rot);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton rotationC = new JButton("↻");
		rotationC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(rotationTextF.getValue().toString());
				AffineTransformation rot = AffineTransformation.newRotation(-val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(rot);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});

		// Création des boutons de transvection
		JButton transvectionLeft = new JButton("←");
		transvectionLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearX(-val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(transv);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton transvectionRight = new JButton("→");
		transvectionRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearX(val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(transv);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton transvectionUp = new JButton("↑");
		transvectionUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearY(val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(transv);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});
		JButton transvectionDown = new JButton("↓");
		transvectionDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearY(-val);
				AffineTransformation newT = flameBuilder.affineTransformation(selectedTransformationIndex).composeWith(transv);
				flameBuilder.setAffineTransformation(selectedTransformationIndex, newT);
				notifyObservers();
			}
		});

		// Groupe horizontal
		groupLayTr.setHorizontalGroup(groupLayTr.createSequentialGroup()
				.addGap(5)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(translationLabel)
						.addComponent(rotationLabel)
						.addComponent(dilatationLabel)
						.addComponent(transvectionLabel)
				)
				.addGap(5)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(translationTextF)
						.addComponent(rotationTextF)
						.addComponent(dilatationTextF)
						.addComponent(transvectionTextF)
				)
				.addGap(2)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(translationLeft)
						.addComponent(rotationAntiC)
						.addComponent(dilatationPlusH)
						.addComponent(transvectionLeft)
				)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(translationRight)
						.addComponent(rotationC)
						.addComponent(dilatationMinusH)
						.addComponent(transvectionRight)
				)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(translationUp)
						.addComponent(dilatationPlusV)
						.addComponent(transvectionUp)
				)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(translationDown)
						.addComponent(dilatationMinusV)
						.addComponent(transvectionDown)
				)
				.addGap(5)
		);

		// Groupe vertical
		groupLayTr.setVerticalGroup(groupLayTr.createSequentialGroup()
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(translationLabel)
						.addComponent(translationTextF)
						.addComponent(translationLeft)
						.addComponent(translationRight)
						.addComponent(translationUp)
						.addComponent(translationDown)
				)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(rotationLabel)
						.addComponent(rotationTextF)
						.addComponent(rotationAntiC)
						.addComponent(rotationC)
				)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dilatationLabel)
						.addComponent(dilatationTextF)
						.addComponent(dilatationPlusH)
						.addComponent(dilatationMinusH)
						.addComponent(dilatationPlusV)
						.addComponent(dilatationMinusV)
				)
				.addGroup(groupLayTr.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(transvectionLabel)
						.addComponent(transvectionTextF)
						.addComponent(transvectionLeft)
						.addComponent(transvectionRight)
						.addComponent(transvectionUp)
						.addComponent(transvectionDown)
				)
		);

		// On lie la taille des boutons dans les groupes horizontaux pour que leurs largeurs soient égales
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationLeft, rotationAntiC, dilatationPlusH, transvectionLeft);
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationRight, rotationC, dilatationMinusH, transvectionRight);
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationUp, dilatationPlusV, transvectionUp);
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationDown, dilatationMinusV, transvectionDown);
		affineEditPanel.setLayout(groupLayTr);
		return affineEditPanel;
	}

	/**
	 * Compose la grille d'édition de la partie des variations de la transformation sélectionnée
	 *
	 * @return Le panneau contenant la grille d'édition des variations.
	 */
	private JPanel panneauVariations() {
		JPanel variationsPanel = new JPanel();
		GroupLayout groupLayVar = new GroupLayout(variationsPanel);

		// Création des labels
		JLabel linearLabel = new JLabel("Linear");
		JLabel sinusoidalLabel = new JLabel("Sinusoidal");
		JLabel sphericalLabel = new JLabel("Spherical");
		JLabel swirlLabel = new JLabel("Swirl");
		JLabel horseshoeLabel = new JLabel("Horseshoe");
		JLabel bubbleLabel = new JLabel("Bubble");

		// Création des champs de texte stockés dans un tableau
		final JFormattedTextField[] textFieldArray = new JFormattedTextField[Variation.ALL_VARIATIONS.size()];
		for (int i = 0; i < textFieldArray.length; i++) {
			final int j = i;
			textFieldArray[i] = new JFormattedTextField(new DecimalFormat("#0.##"));
			textFieldArray[i].setHorizontalAlignment(SwingConstants.RIGHT);
			textFieldArray[i].setValue(flameBuilder.variationWeight(selectedTransformationIndex, Variation.ALL_VARIATIONS.get(j)));
			addObserver(new Observer() {
				@Override
				public void update() {
					// Met à jour le poids de la variation concernée
					textFieldArray[j].setValue(flameBuilder.variationWeight(selectedTransformationIndex, Variation.ALL_VARIATIONS.get(j)));
				}
			});
			textFieldArray[i].addPropertyChangeListener("value", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					AbstractFormatter f = textFieldArray[j].getFormatter();
					Number fieldValue;
					try {
						// On transforme la valeur entrée par l'utilisateur en nombre
						fieldValue = (Number) f.stringToValue(textFieldArray[j].getText());
						textFieldArray[j].setText(textFieldArray[j].getValue().toString());

					} catch (ParseException e) {
						// Erreur d'entrée, on remet la dernière valeur valide
						fieldValue = (Number) textFieldArray[j].getValue();
					}
					flameBuilder.setVariationWeight(selectedTransformationIndex, Variation.ALL_VARIATIONS.get(j), fieldValue.doubleValue());
					notifyObservers();
				}
			});
		}

		// Groupe horizontal
		groupLayVar.setHorizontalGroup(groupLayVar.createSequentialGroup()
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(linearLabel)
						.addComponent(swirlLabel)
				)
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(textFieldArray[0])
						.addComponent(textFieldArray[3])
				)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(sinusoidalLabel)
						.addComponent(horseshoeLabel)
				)
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(textFieldArray[1])
						.addComponent(textFieldArray[4])
				)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(sphericalLabel)
						.addComponent(bubbleLabel)
				)
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(textFieldArray[2])
						.addComponent(textFieldArray[5])
				)
		);

		//Groupe vertical
		groupLayVar.setVerticalGroup(groupLayVar.createSequentialGroup()
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(linearLabel)
						.addComponent(textFieldArray[0])
						.addComponent(sinusoidalLabel)
						.addComponent(textFieldArray[1])
						.addComponent(sphericalLabel)
						.addComponent(textFieldArray[2])
				)
				.addGroup(groupLayVar.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(swirlLabel)
						.addComponent(textFieldArray[3])
						.addComponent(horseshoeLabel)
						.addComponent(textFieldArray[4])
						.addComponent(bubbleLabel)
						.addComponent(textFieldArray[5])
				)
		);
		variationsPanel.setLayout(groupLayVar);
		return variationsPanel;
	}


	/**
	 * Ajoute un panneau supplémentaire en bas de la fenêtre
	 * qui ajoute des boutons supplémentaires au projet
	 */
	private JPanel panneauBoutonsSup() {
		JPanel supButtonsPanel = new JPanel();
		supButtonsPanel.setLayout(new GridLayout(1, 4));
		Border supButtonBorder = BorderFactory.createTitledBorder("Options supplémentaires");
		supButtonsPanel.setBorder(supButtonBorder);

		final JCheckBox multiThreadBox = new JCheckBox("Multi Thread");
		multiThreadBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				fbpc.setUseMultiThread(multiThreadBox.isSelected() && !multiThreadBox.getText().isEmpty());
				notifyObservers();
			}
		});

		final JButton saveB = new JButton("Save 4K2K");
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String signature = "";
				for (int i = 0; i < flameBuilder.transformationCount(); i++) {
					signature = signature.concat("" + flameBuilder.affineTransformation(i).hashCode());
					// Obtient une signature unique pour chaque transformation
				}
				writeImage(4096, 2160, "F-" + signature + ".png");
			}
		});

		final JButton paletteAleatoire = new JButton("Palette aléatoire");
		paletteAleatoire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Palette p = new RandomPalette(5);
				palette = p;
				fbpc.setPalette(p);
				notifyObservers();
			}
		});


		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("CfQptBjcP9gCWsZggkQ")
				.setOAuthConsumerSecret("8G3u24wBjQ3XVZEWNNdds0uvFjQ6f66f8LMRTsRbGc")
				.setOAuthAccessToken("544276119-dzNJKuyud2xzvpbwie047ItV5bnQh9zGTV2Ff4To")
				.setOAuthAccessTokenSecret("DYgjCR3S42PZ0v4lOWmrmaTXImGmG4oq0BFtqVFoE")
				.setMediaProviderAPIKey("2fe05b620c49259c1be13340dd1338b9");
		Configuration conf = cb.build();
		TwitterFactory tf = new TwitterFactory(conf);
		final Twitter t = tf.getInstance();
		final ImageUpload upload = new ImageUploadFactory(conf).getInstance(MediaProvider.TWITTER);

		final JButton tweet = new JButton("Tweet", new ImageIcon("twitter.png"));
		tweet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				File image = writeImage(720, 480, "twitPic.png");
				String url = "";
				try {
					url = upload.upload(image);
					int tweetCount = 0;
					tweetCount = t.verifyCredentials().getStatusesCount();
					t.updateStatus("#FlameMaker2013 n°" + (tweetCount + 1) + " " + url);
					URL twitURL = new URL("http://twitter.com/FlameMakerCGBLS");
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
					if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
						desktop.browse(twitURL.toURI());
					}
				} catch (TwitterException e1) {
					System.out.println("Erreur d'upload du fichier");
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				image.delete();
			}
		});
		supButtonsPanel.add(multiThreadBox);
		supButtonsPanel.add(paletteAleatoire);
		supButtonsPanel.add(saveB);
		supButtonsPanel.add(tweet);

		return supButtonsPanel;
	}


	/**
	 * Ecrit dans un fichier File une image fractale
	 *
	 * @param w      La largeur de l'image
	 * @param h      La hauteur de l'image
	 * @param output Le fichier de sortie
	 * @return Le fichier File contenant l'image
	 */
	private File writeImage(int w, int h, String output) {
		File outputFile = new File(output);

		Rectangle recBig = new Rectangle(new Point(w / 2, h / 2), w, h);
		Rectangle rec = frame.expandToAspectRatio(recBig.aspectRatio());

		Flame flame = flameBuilder.build();
		BufferedImage buffIm = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		long t1 = System.nanoTime();
		FlameAccumulator flameAc = flame.compute(rec, w, h, density, 20);

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				Color c = flameAc.color(palette, background, j, i);
				buffIm.setRGB(j, i, c.asPackedRGB());
			}
		}
		try {
			ImageIO.write(buffIm, "png", outputFile);
		} catch (IOException e1) {
			System.out.println("Erreur d'écriture du fichier");
			e1.printStackTrace();
			outputFile.delete();
		}
		long t2 = System.nanoTime();
		System.out.println(TimeUnit.MILLISECONDS.convert(t2 - t1, TimeUnit.NANOSECONDS));
		return outputFile;
	}

	/**
	 * Donne l'index de la transformation sélectionnée
	 *
	 * @return L'index de la transformation sélectionnée
	 */
	public int getSelectedTransformationIndex() {
		return selectedTransformationIndex;
	}

	/**
	 * Change l'index de la transformation sélectionnée et averti les observateurs de FlameMakerGUI
	 *
	 * @param selectedTransformationIndex Le nouvel index de transformation
	 */
	public void setSelectedTransformationIndex(int selectedTransformationIndex) {
		this.selectedTransformationIndex = selectedTransformationIndex;
		notifyObservers();
	}

	/**
	 * Demande à tous les observateurs de sa liste à se mettre à jour.
	 *
	 * @see Observer
	 */
	private void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}

	/**
	 * Ajoute un observateur à sa liste
	 *
	 * @param o Le nouvel observateur
	 * @see Observer
	 */
	public void addObserver(Observer o) {
		this.observers.add(o);
	}

	/**
	 * Enlève un observateur de sa liste
	 *
	 * @param o L'observateur à supprimer
	 * @see Observer
	 */
	public void removeObserver(Observer o) {
		this.observers.remove(o);
	}

	/**
	 * Classe modélisant une liste de transformations affine.
	 */
	class TransformationsListModel extends AbstractListModel<String> {
		private static final long serialVersionUID = 1L;

		@Override
		public int getSize() {
			return flameBuilder.transformationCount();
		}

		@Override
		public String getElementAt(int index) {
			return "Transformation n°" + (index + 1);
		}

		/**
		 * Ajoute une transformation neutre à la liste de transformation
		 */
		public void addTransformation() {
			AffineTransformation at = new AffineTransformation(1, 0, 0, 0, 1, 0);
			double[] vw = {1, 0, 0, 0, 0, 0};
			FlameTransformation ft = new FlameTransformation(at, vw);
			flameBuilder.addTransformation(ft);
			fireIntervalAdded(this, getSize(), getSize());
		}

		/**
		 * Supprime une transformation de la liste d'index donné
		 *
		 * @param index L'index de la transformation à supprimer
		 */
		public void removeTransformation(int index) {
			flameBuilder.removeTransformation(index);
			fireIntervalRemoved(this, index, index);
		}
	}

	/**
	 * Interface modélisant un observateur selon l'observer pattern
	 */
	interface Observer {
		/**
		 * Met à jour son contenu
		 */
		public void update();
	}
}