package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.Flame.Builder;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameMakerGUI {
	private Flame.Builder flameBuilder = new Builder(new Flame(Arrays.asList(new FlameTransformation[] {
			new FlameTransformation(new AffineTransformation(-0.4113504,
					-0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
					new double[] { 1, 0.1, 0, 0, 0, 0 }),
			new FlameTransformation(new AffineTransformation(-0.3957339, 0,
					-1.6, 0, -0.3957337, 0.2), 
					new double[] { 0, 0, 0, 0, 0.8,	1 }),
			new FlameTransformation(new AffineTransformation(0.4810169, 0, 1,
					0, 0.4810169, 0.9), 
					new double[] { 1, 0, 0, 0, 0, 0 })}
			)));	
	private Color background = Color.BLACK;
	private Palette palette = new InterpolatedPalette(
			Arrays.asList(new Color[] { Color.RED, Color.GREEN, Color.BLUE }));
	private Rectangle frame = new Rectangle(new Point(-0.25, 0), 5, 4);
	private int density = 50;

	private int selectedTransformationIndex;
	private Set<Observer> observers = new HashSet<Observer>();
	public void start() {
		JFrame frame = new JFrame("Flame Maker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panneauInferieur = new JPanel();
		frame.getContentPane().add(panneauInferieur, BorderLayout.PAGE_END);
		panneauInferieur.setLayout(new BoxLayout(panneauInferieur, BoxLayout.LINE_AXIS));
		
		JPanel transEditPanel = new JPanel();
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
		JScrollPane listPane = new JScrollPane(jListe);
		transEditPanel.add(listPane, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Ajouter");
		addButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tlm.addTransformation();
			}
		});
		JButton removeButton = new JButton("Supprimer");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tlm.removeTransformation(selectedTransformationIndex);
				if (selectedTransformationIndex == tlm.getSize()) {
					jListe.setSelectedIndex(tlm.getSize()-1);
				}
				else{
					jListe.setSelectedIndex(selectedTransformationIndex);
				}
			}
		});
		JPanel buttonsPanel = new JPanel();
		transEditPanel.add(buttonsPanel, BorderLayout.PAGE_END);
		buttonsPanel.setLayout(new GridLayout(1, 2));
		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);
		
		JPanel panneauSuperieur = new JPanel();
		frame.getContentPane().add(panneauSuperieur, BorderLayout.CENTER);
		panneauSuperieur.setLayout(new GridLayout(1,2));
		
		JPanel transPanel = new JPanel();
		panneauSuperieur.add(transPanel);
		transPanel.setLayout(new BorderLayout());
		Border transBorder = BorderFactory.createTitledBorder("Transformations affines");
		transPanel.setBorder(transBorder);
		final AffineTransformationsComponent atc = new AffineTransformationsComponent(this.flameBuilder, this.frame);
		addObserver(new Observer() {		
			@Override
			public void update() {
				atc.setHighlightedTransformationIndex(selectedTransformationIndex);
			}
		});
		transPanel.add(atc);
		
		JPanel fracPanel = new JPanel();
		panneauSuperieur.add(fracPanel);
		fracPanel.setLayout(new BorderLayout());
		Border fracBorder = BorderFactory.createTitledBorder("Fractale");
		fracPanel.setBorder(fracBorder);
		FlameBuilderPreviewComponent fbpc = new FlameBuilderPreviewComponent(this.flameBuilder, this.background, this.palette, this.frame, this.density);
		fracPanel.add(fbpc,BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
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
			return "Transformation n°" + index;
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