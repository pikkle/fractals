package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameMakerGUI {
	private ObservableFlameBuilder flameBuilder = new ObservableFlameBuilder(new Flame(Arrays.asList(new FlameTransformation[] {
			new FlameTransformation(new AffineTransformation(-0.4113504,-0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
					new double[] { 1, 0.1, 0, 0, 0, 0 }),
			new FlameTransformation(new AffineTransformation(-0.3957339, 0, -1.6, 0, -0.3957337, 0.2), 
					new double[] { 0, 0, 0, 0, 0.8,	1 }),
			new FlameTransformation(new AffineTransformation(0.4810169, 0, 1, 0, 0.4810169, 0.9), 
					new double[] { 1, 0, 0, 0, 0, 0 })}
			)));	
	private Color background = Color.BLACK;
	private Palette palette = new InterpolatedPalette(Arrays.asList(new Color[] { Color.RED, Color.GREEN, Color.BLUE }));
	private Rectangle frame = new Rectangle(new Point(-0.25, 0), 5, 4);
	private int density = 50;

	private int selectedTransformationIndex;
	private Set<Observer> observers = new HashSet<Observer>();
	private void panneauGraphiqueTransformationsAffines(JPanel panneauSuperieur){
		// Partie supérieure gauche, contenant le panneau de Transformation dans une grille
		JPanel transPanel = new JPanel();
		panneauSuperieur.add(transPanel);
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
	}
	private void panneauFractale(JPanel panneauInferieur, JPanel panneauSuperieur){
		// Partie supérieure droite, contenant l'affichage de la fractale
		JPanel fracPanel = new JPanel();
		panneauSuperieur.add(fracPanel);
		fracPanel.setLayout(new BorderLayout());
		Border fracBorder = BorderFactory.createTitledBorder("Fractale");
		fracPanel.setBorder(fracBorder);
		final FlameBuilderPreviewComponent fbpc = new FlameBuilderPreviewComponent(this.flameBuilder, this.background, this.palette, this.frame, this.density);
		fracPanel.add(fbpc,BorderLayout.CENTER);
		flameBuilder.addObserver(new ObservableFlameBuilder.Observer() {
			@Override
			public void update() {
				fbpc.repaint();
			}
		});
	}
	private void panneauListeFlameTransformations(JPanel panneauInferieur){
		// Partie gauche inférieure, contenant l'affichage textuel des transformations
		JPanel transEditPanel = new JPanel();
		Border transEditBorder = BorderFactory.createTitledBorder("Liste des transformations");
		transEditPanel.setBorder(transEditBorder);
		panneauInferieur.add(transEditPanel);
		transEditPanel.setLayout(new BorderLayout());
		
		final TransformationsListModel tlm = new TransformationsListModel();
		final JList<String> jListe = new JList<String>(tlm);
		jListe.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setSelectedTransformationIndex(jListe.getSelectedIndex());
			}
		});
		jListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListe.setVisibleRowCount(3);
		jListe.setSelectedIndex(0);
		
		// Liste des transformations
		JScrollPane listPane = new JScrollPane(jListe);
		transEditPanel.add(listPane, BorderLayout.CENTER);
		
		// Panneau des deux boutons (ajouter & supprimer)
		JPanel buttonsPanel = new JPanel();
		transEditPanel.add(buttonsPanel, BorderLayout.PAGE_END);
		buttonsPanel.setLayout(new GridLayout(1, 2));
		
		//Bouton d'ajout et de supression de transformation
		JButton addButton = new JButton("Ajouter");
		final JButton removeButton = new JButton("Supprimer");
		addButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (! removeButton.isEnabled()){
					removeButton.setEnabled(true);
				}
				notifyObservers();
				tlm.addTransformation();
				setSelectedTransformationIndex(tlm.getSize()-1);
				jListe.setSelectedIndex(selectedTransformationIndex);
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int j = jListe.getSelectedIndex();
				if (selectedTransformationIndex == tlm.getSize()-1) {
					jListe.setSelectedIndex(tlm.getSize()-2); //Sélectionne l'avant-dernier élément de la liste
					tlm.removeTransformation(j);
				}
				else{
					jListe.setSelectedIndex(j+1);
					tlm.removeTransformation(j);
				}
				removeButton.setEnabled(tlm.getSize() != 1);
				
				notifyObservers();
			}
		});
		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);
	}
	private void panneauTransformationSelect(JPanel panneauInferieur){
		// Partie inférieure droite, contenant l'édition de la transformation sélectionnée
		JPanel selectedTransfEditPanel = new JPanel();
		Border selectedTransfEditBorder = BorderFactory.createTitledBorder("Transformation courante");
		selectedTransfEditPanel.setBorder(selectedTransfEditBorder);
		panneauInferieur.add(selectedTransfEditPanel);
		selectedTransfEditPanel.setLayout(new BoxLayout(selectedTransfEditPanel, BoxLayout.PAGE_AXIS));
		
		// Panneau d'édition de la partie affine
		JPanel affineEditPanel = new JPanel();
		GroupLayout groupLayTr = new GroupLayout(affineEditPanel);
		
		JLabel translationLabel = new JLabel("Translation");
		JLabel rotationLabel = new JLabel("Rotation");
		JLabel dilatationLabel = new JLabel("Dilatation");
		JLabel transvectionLabel = new JLabel("Transvection");
		
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
				boolean returnValue = true;
				AbstractFormatter f = dilatationTextF.getFormatter();
				try {
					if (Double.parseDouble(dilatationTextF.getText()) == 0){
						dilatationTextF.setText(f.valueToString(dilatationTextF.getValue()));
					}
				} catch (ParseException e) {
					dilatationTextF.setText("1");
					returnValue = false;
				}
				return returnValue;
			}
		});
		dilatationTextF.setValue((double)1.05);
		dilatationTextF.setHorizontalAlignment(SwingConstants.RIGHT);
		final JFormattedTextField transvectionTextF = new JFormattedTextField(new DecimalFormat("#0.##"));
		transvectionTextF.setValue(0.1);
		transvectionTextF.setHorizontalAlignment(SwingConstants.RIGHT);

		
		JButton dilatationPlusH = new JButton("+↔");
		dilatationPlusH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(val, 1);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(scale);
				flameBuilder.setAffineTransformation(i,newT);
				notifyObservers();
			}
		});
		JButton dilatationMinusH = new JButton("-↔");
		dilatationMinusH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(1/val, 1);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(scale);
				flameBuilder.setAffineTransformation(i,newT);
				notifyObservers();
			}
		});
		JButton dilatationPlusV = new JButton("+↕");
		dilatationPlusV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(1, val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(scale);
				flameBuilder.setAffineTransformation(i,newT);
				notifyObservers();
			}
		});
		JButton dilatationMinusV = new JButton("-↕");
		dilatationMinusV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(dilatationTextF.getValue().toString());
				AffineTransformation scale = AffineTransformation.newScaling(1, 1/val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(scale);
				flameBuilder.setAffineTransformation(i,newT);
				notifyObservers();
			}
		});
		
		JButton translationLeft = new JButton("←");
		translationLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(-val, 0);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(i));
				flameBuilder.setAffineTransformation(i,newT);
				notifyObservers();
			}
		});
		JButton translationRight = new JButton("→");
		translationRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(val, 0);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(i));
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		JButton translationUp = new JButton("↑");
		translationUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(0, val);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(i));
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		JButton translationDown = new JButton("↓");
		translationDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(translationTextF.getValue().toString());
				AffineTransformation transl = AffineTransformation.newTranslation(0, -val);
				AffineTransformation newT = transl.composeWith(flameBuilder.affineTransformation(i));
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		
		JButton rotationAntiC = new JButton("↺");
		rotationAntiC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(rotationTextF.getValue().toString());
				AffineTransformation rot = AffineTransformation.newRotation(val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(rot);
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		JButton rotationC = new JButton("↻");
		rotationC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(rotationTextF.getValue().toString());
				AffineTransformation rot = AffineTransformation.newRotation(-val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(rot);
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		
		JButton transvectionLeft = new JButton("←");
		transvectionLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearX(-val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(transv);
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		JButton transvectionRight = new JButton("→");
		transvectionRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearX(val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(transv);
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		JButton transvectionUp = new JButton("↑");
		transvectionUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearY(val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(transv);
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		JButton transvectionDown = new JButton("↓");
		transvectionDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = selectedTransformationIndex;
				double val = Double.parseDouble(transvectionTextF.getValue().toString());
				AffineTransformation transv = AffineTransformation.newShearY(-val);
				AffineTransformation newT = flameBuilder.affineTransformation(i).composeWith(transv);
				flameBuilder.setAffineTransformation(i, newT);
				notifyObservers();
			}
		});
		
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
		
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationLeft, rotationAntiC, dilatationPlusH, transvectionLeft);
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationRight, rotationC, dilatationMinusH, transvectionRight);
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationUp, dilatationPlusV, transvectionUp);
		groupLayTr.linkSize(SwingConstants.HORIZONTAL, translationDown, dilatationMinusV, transvectionDown);
		
		selectedTransfEditPanel.add(affineEditPanel);
		affineEditPanel.setLayout(groupLayTr);
		
		selectedTransfEditPanel.add(new JSeparator());
		panneauVariations(selectedTransfEditPanel);
	}
	private void panneauVariations(JPanel selectedTransfEditPanel){
		JPanel variationsPanel = new JPanel();
		GroupLayout groupLayVar = new GroupLayout(variationsPanel);
		
		JLabel linearLabel = new JLabel("Linear");
		JLabel sinusoidalLabel = new JLabel("Sinusoidal");
		JLabel sphericalLabel = new JLabel("Spherical");
		JLabel swirlLabel = new JLabel("Swirl");
		JLabel horseshoeLabel = new JLabel("Horseshoe");
		JLabel bubbleLabel = new JLabel("Bubble");
		
		final JFormattedTextField[] textFieldArray = new JFormattedTextField[Variation.ALL_VARIATIONS.size()];
		for (int i = 0; i < textFieldArray.length; i++) {
			final int j = i;
			textFieldArray[i] = new JFormattedTextField(new DecimalFormat("#0.##"));
			textFieldArray[i].setHorizontalAlignment(SwingConstants.RIGHT);
			textFieldArray[i].setValue(flameBuilder.variationWeight(selectedTransformationIndex, Variation.ALL_VARIATIONS.get(j)));
			addObserver(new Observer() {
				@Override
				public void update() {
					textFieldArray[j].setValue(flameBuilder.variationWeight(selectedTransformationIndex, Variation.ALL_VARIATIONS.get(j)));
				}
			});
			textFieldArray[i].addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
				public void propertyChange(PropertyChangeEvent evt) {
					AbstractFormatter f = textFieldArray[j].getFormatter();
					Number fieldValue;
					try {
						fieldValue = (Number) f.stringToValue(textFieldArray[j].getText());
						textFieldArray[j].setText(textFieldArray[j].getValue().toString());
						
					} catch (ParseException e) {
						fieldValue = (Number) textFieldArray[j].getValue();
					}
					flameBuilder.setVariationWeight(selectedTransformationIndex, Variation.ALL_VARIATIONS.get(j), fieldValue.doubleValue());
					notifyObservers();
				}
			});
		}
		
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
		selectedTransfEditPanel.add(variationsPanel);
	}
	
	public void start(){
		// Ouverture de la fenêtre principale
		JFrame jframe = new JFrame("Flame Maker");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		    //Met le thème par défaut
		}
		// Partie inférieure de la fenêtre
		JPanel superPanneauInferieur = new JPanel();
		jframe.getContentPane().add(superPanneauInferieur, BorderLayout.PAGE_END);
		superPanneauInferieur.setLayout(new BorderLayout());
		
		JPanel panneauInferieur = new JPanel();
		superPanneauInferieur.add(panneauInferieur);
		panneauInferieur.setLayout(new BoxLayout(panneauInferieur, BoxLayout.LINE_AXIS));
		
		// Partie supérieure, contenant une représentation graphiques des transformations et l'affichage de la fractale
		JPanel panneauSuperieur = new JPanel();
		jframe.getContentPane().add(panneauSuperieur, BorderLayout.CENTER);
		panneauSuperieur.setLayout(new GridLayout(1,2));
		panneauSuperieur.setOpaque(true);
		
		
		panneauGraphiqueTransformationsAffines(panneauSuperieur);
		panneauFractale(superPanneauInferieur, panneauSuperieur);
		panneauListeFlameTransformations(panneauInferieur);
		panneauTransformationSelect(panneauInferieur);
		
		
		jframe.pack();
		jframe.setVisible(true);
	}
	
	public int getSelectedTransformationIndex() {
		return selectedTransformationIndex;
	}
	public void setSelectedTransformationIndex(int selectedTransformationIndex) {
		this.selectedTransformationIndex = selectedTransformationIndex;
		notifyObservers();
	}
	private void notifyObservers(){
		for (Observer o : observers) {
			o.update();
		}
	}
	public void addObserver(Observer o){
		this.observers.add(o);
	}
	public void removeObserver(Observer o){
		this.observers.remove(o);
	}
	
	class TransformationsListModel extends AbstractListModel<String>{
		private static final long serialVersionUID = 1L;
		@Override
		public int getSize() {
			return flameBuilder.transformationCount();
		}
		@Override
		public String getElementAt(int index) {
			return "Transformation n°" + (index + 1);
		}
		public void addTransformation(){
			AffineTransformation at = new AffineTransformation(1, 0, 0, 0, 1, 0);
			double[] vw = {1,0,0,0,0,0};
			FlameTransformation ft = new FlameTransformation(at, vw);
			flameBuilder.addTransformation(ft);
			fireIntervalAdded(this, getSize(), getSize());
		}
		public void removeTransformation(int index){
			flameBuilder.removeTransformation(index);
			fireIntervalRemoved(this, index, index);
		}
	}
	
	interface Observer{
		public void update();
	}
}