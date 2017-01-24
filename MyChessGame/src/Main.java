import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

public class Main {

	/**
	 * Main method
	 */final static JLabel clbl = new JLabel();
	 final static JLabel nlbl = new JLabel();
	 final static JButton neural = new JButton();
	 public static boolean toggleNeural = false;
	 public static void main(String[] args) {

		// Get the Alpha Beta Search Depth Panel
		JPanel depthPanel = getDepthPanel();
		
		// Get the Evaluation Panel
		JPanel evalPanel = getEvaluationPanel();
		
		// Create Menu Panel
		JPanel menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(200, 534));
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		// Add Depth Panel to Menu Panel
		menuPanel.add(depthPanel);
		// Add Evaluate Panel to Menu Panel
		menuPanel.add(evalPanel);
		
		// Add UI Panel to Main Panel
		UserInterface ui = new UserInterface();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(ui);
		
		// Add Menu Panel to Main Panel
		panel.add(menuPanel, BorderLayout.EAST);
		
		// Add Panel to Frame
		JFrame frame = new JFrame("Chess with AI");
		frame.add(panel);
		frame.setVisible(true);
		frame.setSize(512+200, 534);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}
	
	/**
	 * Create Alpha Beta Search Depth Interface
	 * @return depthPanel
	 */
	public static JPanel getDepthPanel() {
		
		// Create Depth Panel
		JPanel depthPanel = new JPanel();
		depthPanel.setBorder(new TitledBorder("Alpha Beta Search"));
		depthPanel.setPreferredSize(new Dimension(150, 50));
		final JTextField field = new JTextField(5);
		field.setText(String.valueOf(MoveGenerator.globalDepth));
		depthPanel.add(field);
		JButton dButton = new JButton("Set Depth");
		depthPanel.add(dButton);
		final JLabel diff = new JLabel();
		diff.setText(" Difficulties");
		depthPanel.add(diff);
		final JLabel easy = new JLabel();
		easy.setText("For easy enter Depth 2");
		depthPanel.add(easy);
		final JLabel med = new JLabel();
		med.setText("For medium enter Depth 3/4");
		depthPanel.add(med);
		final JLabel hard = new JLabel();
		hard.setText("For hard enter Depth 5/6");
		depthPanel.add(hard);
	
		// add an ActionListener to the button
		dButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                    	// Set Depth of Alpha Beta Search
                    	MoveGenerator.globalDepth = Integer.parseInt(field.getText());
                    	if (MoveGenerator.globalDepth > 6){
        					JOptionPane.showMessageDialog(null, "Please lower depth to 6 or lower");
        				} else if (MoveGenerator.globalDepth < 2){
        					JOptionPane.showMessageDialog(null, "Please increase depth to 2 or higher");
        				}
        				else
                    	JOptionPane.showMessageDialog(null, "Depth Set");
                        return null;
                    }
                }.execute();
            }
        });

		
		return depthPanel;
	}
	
	/**
	 * Create Evaluation Criteria Interface
	 * @return evalPanel.
	 */
	public static JPanel getEvaluationPanel() {
		
		// Create Evaluate Panel
		JPanel evalPanel = new JPanel();
		evalPanel.setPreferredSize(new Dimension(150, 125));
		evalPanel.setBorder(new TitledBorder("Overall Game Evaluations"));
		
		final JLabel albl = new JLabel();
		final JLabel blbl = new JLabel();
		final JLabel nnlbl = new JLabel();
		albl.setText("Naive Bayes predicts ");
		evalPanel.add(albl);
		evalPanel.add(clbl);
		nnlbl.setText("Neural Network predicts");
		evalPanel.add(nnlbl);
		evalPanel.add(nlbl);
		JButton neural = new JButton("Turn on Neural");
		evalPanel.add(neural);
		JButton neuraloff = new JButton("Turn off Neural");
		evalPanel.add(neuraloff);
		
		neural.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleNeural = true;
			} 
		});
		neuraloff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleNeural = false;
			} 
		});
		
		
		return evalPanel;
	}
	
	/**
	 * Create Piece Value Interface
	 * @return valuePanel.
	 */
	public static JPanel getValuePanel() {
		
		// Create Value Panel
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new GridLayout(0, 1));
		valuePanel.setBorder(new TitledBorder("Piece Values"));
		valuePanel.setPreferredSize(new Dimension(150, 200));
		final JTextField pawnField = new JTextField(5);				// Pawn
		pawnField.setText(String.valueOf(Evaluate.pawnValue));
		JLabel pawnLabel = new JLabel("Pawn");
		JPanel pawn = new JPanel();
		pawn.add(pawnLabel);
		pawn.add(pawnField);
		final JTextField rookField = new JTextField(5);				// Rook
		rookField.setText(String.valueOf(Evaluate.rookValue));
		JLabel rookLabel = new JLabel("Rook");
		JPanel rook = new JPanel();
		rook.add(rookLabel);
		rook.add(rookField);
		final JTextField knightField = new JTextField(5);			// Knight
		knightField.setText(String.valueOf(Evaluate.knightValue));
		JLabel knightLabel = new JLabel("Knight");
		JPanel knight = new JPanel();
		knight.add(knightLabel);
		knight.add(knightField);
		final JTextField bishopField = new JTextField(5);			// Bishop
		bishopField.setText(String.valueOf(Evaluate.bishopValue));
		JLabel bishopLabel = new JLabel("Bishop");
		JPanel bishop = new JPanel();
		bishop.add(bishopLabel);
		bishop.add(bishopField);
		final JTextField queenField = new JTextField(5);			// Queen
		queenField.setText(String.valueOf(Evaluate.queenValue));
		JLabel queenLabel = new JLabel("Queen");
		JPanel queen = new JPanel();
		queen.add(queenLabel);
		queen.add(queenField);
		valuePanel.add(pawn);		// Pawn
		valuePanel.add(rook);		// Rook
		valuePanel.add(knight);		// Knight
		valuePanel.add(bishop);		// Bishop
		valuePanel.add(queen);		// Queen
		JButton vButton = new JButton("Set Values");
		valuePanel.add(vButton);
		// add an ActionListener to the button
		vButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                    	// Set Values of Pieces
                    	Evaluate.pawnValue = Integer.parseInt(pawnField.getText());
                    	Evaluate.rookValue = Integer.parseInt(rookField.getText());
                    	Evaluate.knightValue = Integer.parseInt(knightField.getText());
                    	Evaluate.bishopValue = Integer.parseInt(bishopField.getText());
                    	Evaluate.queenValue = Integer.parseInt(queenField.getText());
                    	JOptionPane.showMessageDialog(null, "Values Set");
                        return null;
                    }
                }.execute();
            }
        });
		
		return valuePanel;
	}
}
