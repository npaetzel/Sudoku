package com.github.npaetzel.Sudoku;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControllerGUI {

	private ViewGUI view;
	private Grid model;
	private Timer timer = new Timer();
	long lastClick;
	
	public ControllerGUI() {
		view = new ViewGUI(this);
		newGame(20);
		view.setVisible(true);
	}
	public void newGame(int toClear) {
		model = new Grid(toClear);
		
		for(int y=0; y<9; y++) {
			for(int x=0; x<9; x++) {
				numberToLabel(x, y);
			}
		}
		checkNumbers();
		view.repaint();
	}
	public void numberToLabel(int x, int y) {
		Block gridBlock = model.getRow(y).getBlock(x);
		Block guessedBlock = model.getGuessedBlock(x, y);
		if(gridBlock != null) {
			view.lbl[y][x].setText(Integer.toString(gridBlock.getNumber()));
			view.lbl[y][x].setForeground(Color.BLACK);
		} else if(guessedBlock != null && guessedBlock.getNumber() > 0) {
			view.lbl[y][x].setText(Integer.toString(guessedBlock.getNumber()));
			view.lbl[y][x].setForeground(Color.BLUE);
		} else {
			view.lbl[y][x].setText("");
		}
	}
	public MouseListener getMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if(mouseEvent.getY() > 50 && !model.getWin()) {
					int x = mouseEvent.getX()/50;
					int y = (mouseEvent.getY()-50)/50;
					Block block = model.getGuessedBlock(x, y);
					if(block != null) {
						if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
							if(block.getNumber() == 9) {
								block.setNumber(0);
							} else {
								block.setNumber(block.getNumber()+1);
							}
						} else if(mouseEvent.getButton() == MouseEvent.BUTTON3) {
							if(block.getNumber() == 0) {
								block.setNumber(9);
							} else {
								block.setNumber(block.getNumber()-1);
							}
						}
						numberToLabel(x, y);
						
					}
					lastClick = System.currentTimeMillis();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							if(System.currentTimeMillis()-lastClick > 900) {
								checkNumbers();
							}
						}	
					}, 1000);		
				}	
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		};
	}
	public ActionListener getActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println(actionEvent.getActionCommand());
				switch (actionEvent.getActionCommand()) {
				case "Easy":
					newGame(20);
					break;
				case "Normal":
					newGame(35);
					break;
				case "Hard":
					newGame(55);
					break;
				case "Load Game":
					FileFilter filter2 = new FileNameExtensionFilter("Savegame file", "sud");
					JFileChooser fileChooser2 = new JFileChooser(System.getProperty("user.dir","."));
					fileChooser2.setAcceptAllFileFilterUsed(false);
					fileChooser2.addChoosableFileFilter(filter2);
					fileChooser2.setDialogTitle("Load game");
					if(fileChooser2.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
						File file2 = fileChooser2.getSelectedFile();
						System.out.println(file2.getAbsolutePath());
						InputStream fis = null;
						ObjectInputStream ois = null;
						try {
							fis = new FileInputStream(file2);
							ois = new ObjectInputStream(fis);
							model = (Grid) ois.readObject();
						} catch(IOException e) {
							System.err.println(e);
						} catch(ClassNotFoundException f) {
							System.err.println(f);
						} finally {
							try {
								fis.close();
							} catch (Exception e) {
								
							}
						}
						for(int y=0; y<9; y++) {
							for(int x=0; x<9; x++) {
								Block block = model.getRow(y).getBlock(x);
								if(block != null) {
									block.setField();
								} else {
									Field.getField(x, y).setBlock(Field.getIndex(x, y), null);
								}
								
								numberToLabel(x, y);
							}
						}
						checkNumbers();
						view.repaint();
					}
					break;
				case "Save Game":
					FileFilter filter = new FileNameExtensionFilter("Savegame file", "sud");
					JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir","."));
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.addChoosableFileFilter(filter);
					fileChooser.setDialogTitle("Save game");
					if(fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						System.out.println(file.getAbsolutePath());
						if(!file.getAbsolutePath().endsWith("sud")) {
							file = new File(file.getAbsolutePath() + ".sud");
						}
						OutputStream fos = null;
						ObjectOutputStream ous = null;
						try {
							fos = new FileOutputStream(file);
							ous = new ObjectOutputStream(fos);
							ous.writeObject(model);		
						} catch(IOException e) {
							System.err.println(e);
						} finally {
							try {
								ous.close();
								fos.close();
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}				
					break;
				case "Exit":
					view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
					break;
				case "Mark mistakes":
					view.panel.setHelpMarker(view.panel.getHelpMarker() ? false : true);
					view.repaint();
					break;
				case "About":
					JOptionPane.showMessageDialog(view, 
							"Sudoku programmiert von Nico Pätzel", 
							"About", 
							JOptionPane.INFORMATION_MESSAGE);
					break;
				}
			}
		};
	}
	public void checkNumbers() {
		for(int i=0; i<9; i++) {
			view.panel.setWrongRow(i, false);
			view.panel.setWrongColumn(i, false);
			view.panel.setWrongField(i, false);
		}
		for(int i=0; i<model.getToClear(); i++) {
			Block block = model.getGuessedBlock(i);
			Line row = model.getRow(block.getY());
			Line column = model.getColumn(block.getX());
			Field field = block.getField();
			if(row.hasDoubles(block.getX(), block.getNumber())) {
				view.panel.setWrongRow(block.getY(), true);
			}
			if(column.hasDoubles(block.getY(), block.getNumber())) {
				view.panel.setWrongColumn(block.getX(), true);
			}
			if(field.hasDoubles(block)) {
				view.panel.setWrongField(field.ordinal(),true);
			}
			
		}
		
		model.checkWin();
		if(model.getWin()) {
			for(int y=0; y<9; y++) {
				for(int x=0; x<9; x++) {
					view.lbl[y][x].setForeground(Color.GREEN);
				}
			}
		}
		view.repaint();
	}
}
