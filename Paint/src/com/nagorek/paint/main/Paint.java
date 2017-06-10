package com.nagorek.paint.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Paint extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private final int WIDTH = 1024;
	private final int HEIGHT = WIDTH * 9 / 16;
	private int defaultCanvasSize = 512;
	
	private Screen screen;
	private JPanel tools;
	private MessageBar message;
	private GlassPanel glass;
	private Canvas canvas;
		
	private JMenuBar menuBar;
	private JMenu file, edit, settings;	
	
	private JButton select;
	public static boolean isSelected = false;
	
	public Paint(){
		
		initUI();
	}
	
	private void initUI(){
		setTitle("My Paint");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setUpMenu();
		
		message = new MessageBar(WIDTH, 30);
		message.setSizeText(defaultCanvasSize, defaultCanvasSize);
		add(message, BorderLayout.SOUTH);
		
		canvas = new Canvas(defaultCanvasSize, defaultCanvasSize, message);
		
		glass = new GlassPanel(defaultCanvasSize, canvas, message);
		setGlassPane(glass);
		canvas.addGlassPanel(glass);
		
		screen  = new Screen(canvas, message, glass);		
		JScrollPane scroll = new JScrollPane(screen);
		add(scroll, BorderLayout.CENTER);
		
		glass.setX(screen.getX() + screen.getPadding() + 1);
		glass.setY(screen.getY() + screen.getPadding() + 24);
		glass.addScreen(screen);
		
		tools = new JPanel();
		tools.setPreferredSize(new Dimension(150, HEIGHT));
		tools.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		add(tools, BorderLayout.EAST);
		
		select = new JButton("Select");
		select.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!isSelected){
					isSelected = true;
					select.setBackground(Color.YELLOW);
				} else {
					isSelected = false;
					select.setBackground(null);
				}
			}			
		});
		tools.add(select);
			
		pack();
		setLocationRelativeTo(null);
	}
	
	private void setUpMenu(){
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		file.setMnemonic('f');
		menuBar.add(file);
		
		edit = new JMenu("Edit");
		edit.setMnemonic('e');
		menuBar.add(edit);
		
		settings = new JMenu("Settings");
		settings.setMnemonic('s');
		menuBar.add(settings);
		
		addJMenuItem(file, "New", 'n', new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
			}			
		});
		
		file.addSeparator();
		
		
		addJMenuItem(file, "Open", 'o', new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
			}			
		});
		
		addJMenuItem(file, "Save", 's', new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
			}			
		});
		
		addJMenuItem(file, "Save As", 'a', new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
			}			
		});
		
		file.addSeparator();
		
		addJMenuItem(file, "Exit", 'x', new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});
		
		setJMenuBar(menuBar);
	}
	
	private void addJMenuItem(JMenu menu, String name, char mnemonic, ActionListener listener){
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setMnemonic(mnemonic);
		menuItem.addActionListener(listener);
		menu.add(menuItem);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				Paint p = new Paint();
				p.setVisible(true);
			}
		});
	}

}
