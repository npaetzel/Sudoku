package com.github.npaetzel.Sudoku;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ViewGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JCheckBoxMenuItem cbMenuItem;
	JLabel[][] lbl = new JLabel[9][9];
	DrawPanel panel;
	ActionListener actionListener;
	
	
	public ViewGUI(ControllerGUI controller) {
	menuBar = new JMenuBar();
	
	menu = new JMenu("Game");
	menu.setMnemonic(KeyEvent.VK_G);
	menuBar.add(menu);
	
	submenu = new JMenu("New Game");
	submenu.setMnemonic(KeyEvent.VK_W);
	menu.add(submenu);
	
	menuItem = new JMenuItem("Easy");
	menuItem.setMnemonic(KeyEvent.VK_E);
	menuItem.addActionListener(controller.getActionListener());
	submenu.add(menuItem);
	
	menuItem = new JMenuItem("Normal");
	menuItem.setMnemonic(KeyEvent.VK_N);
	menuItem.addActionListener(controller.getActionListener());
	submenu.add(menuItem);
	
	menuItem = new JMenuItem("Hard");
	menuItem.setMnemonic(KeyEvent.VK_H);
	menuItem.addActionListener(controller.getActionListener());
	submenu.add(menuItem);
	
	menu.addSeparator();
	menuItem = new JMenuItem("Load Game");
	menuItem.setMnemonic(KeyEvent.VK_L);
	menuItem.addActionListener(controller.getActionListener());
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Save Game");
	menuItem.setMnemonic(KeyEvent.VK_S);
	menuItem.addActionListener(controller.getActionListener());
	menu.add(menuItem);
	
	menu.addSeparator();
	menuItem = new JMenuItem("Exit");
	menuItem.setMnemonic(KeyEvent.VK_E);
	menuItem.addActionListener(controller.getActionListener());
	menu.add(menuItem);
	
	menu = new JMenu("Help");
	menu.setMnemonic(KeyEvent.VK_H);
	menuBar.add(menu);
	
	cbMenuItem = new JCheckBoxMenuItem("Mark mistakes");
	cbMenuItem.setSelected(true);
	cbMenuItem.setMnemonic(KeyEvent.VK_M);
	cbMenuItem.addActionListener(controller.getActionListener());
	menu.add(cbMenuItem);
	
	menu.addSeparator();
	menuItem = new JMenuItem("About");
	menuItem.setMnemonic(KeyEvent.VK_A);
	menuItem.addActionListener(controller.getActionListener());
	menu.add(menuItem);
	
	for(int y=0; y<9; y++) {
		for(int x=0; x<9; x++) {
			lbl[y][x] = new JLabel();
			Font font = lbl[y][x].getFont().deriveFont(Font.BOLD, 30);
			lbl[y][x].setFont(font);
			lbl[y][x].setBounds(x*50, y*50, 50, 50);
			lbl[y][x].setHorizontalAlignment(JLabel.CENTER);
			add(lbl[y][x]);
		}
	}
	
	panel = new DrawPanel();
	add(panel);
	
	addMouseListener(controller.getMouseListener());
	setJMenuBar(menuBar);
	setSize(456, 502);
	setResizable(false);
	setLocationRelativeTo(null);
	setTitle("Sudoku");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	class DrawPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private boolean[] wrongRows = new boolean[9];
		private boolean[] wrongColumns = new boolean[9];
		private boolean[] wrongFields = new boolean[9];
		private boolean helpMarker = true;
		
		public boolean getWrongRow(int index) {
			return wrongRows[index];
		}
		public void setWrongRow(int index, boolean wrong) {
			wrongRows[index] = wrong;
		}
		public boolean getWrongColumn(int index) {
			return wrongColumns[index];
		}
		public void setWrongColumn(int index, boolean wrong) {
			wrongColumns[index] = wrong;
		}
		public boolean getWrongField(int index) {
			return wrongFields[index];
		}
		public void setWrongField(int index, boolean wrong) {
			wrongFields[index] = wrong;
		}
		public boolean getHelpMarker() {
			return helpMarker;
		}
		public void setHelpMarker(boolean helpMarker) {
			this.helpMarker = helpMarker;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.black);
			for(int i=1; i<9; i++) {
				g.drawLine(i*50, 0, i*50, 450);
				g.drawLine(0, i*50, 450, i*50);
			}
			for(int i=1;i<3; i++) {
				g.fillRect(150*i-1, 0, 2, 450);
				g.fillRect(0, 150*i-1, 450, 2);
			}	
			if(getHelpMarker()) {
				g.setColor(Color.red);
				for(int i=0; i<9; i++) {
					if(getWrongRow(i)) {
						RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(10, i*50+25-15, 430, 30, 20, 20);
						((Graphics2D) g).draw(roundedRectangle);
					}
					if(getWrongColumn(i)) {
						RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(i*50+25-15, 10, 30, 430, 20, 20);
						((Graphics2D) g).draw(roundedRectangle);
					}
					if(getWrongField(i)) {
						int y = Field.values()[i].getFirstRow();
						int x = Field.values()[i].getFirstColumn();
						RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x*50+10, y*50+10, 130, 130, 20, 20);
						((Graphics2D) g).draw(roundedRectangle);
					} else {
						
					}
				}
			}
		}
		public void paint() {
			
		}
	}
	
}
