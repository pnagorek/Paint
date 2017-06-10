package com.nagorek.paint.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class MessageBar extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel position, selection, size;
	
	public MessageBar(int width, int height){
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		int column = width / 4;
		gridbag.columnWidths = new int[]{column, column, column, column / 5, column * 3 / 5};
		c.fill = GridBagConstraints.BOTH;

		setPreferredSize(new Dimension(width, height));
		
		c.weightx = 1;
		c.gridx = 0;
		position = addLabel("Position", c);
		c.gridx = 1;
		selection = addLabel("Selection", c);
		c.gridx = 2;
		size = addLabel("Size", c);
		
		c.weightx = 0;	
		c.gridx = 4;
		JSlider zoom = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
		add(zoom, c);
		
		c.gridx = 3;
		JLabel label = new JLabel(String.valueOf(zoom.getValue()) + "%", SwingConstants.CENTER);
		add(label, c);

	}
	
	private JLabel addLabel(String name, GridBagConstraints c){
		JLabel label = new JLabel(" " + name + ": ");
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		add(label, c);
		return label;
	}
	
	public void setSizeText(int width, int height){
		String text = width + " x " + height  + "px";
		text = " Size: " + text;
		size.setText(text);
	}
	
	public void setPositionText(int x, int y){
		String text = x + ", " + y + "px";
		text = " Position: " + text;
		position.setText(text);
	}
	
	public void clearPositionText(){
		position.setText(" Position: ");
	}
	
	public void setSelectionText(int x, int y){
		String text = x + ", " + y + "px";
		text = " Selection: " + text;
		selection.setText(text);
	}
	public void clearSelectionText(){
		selection.setText(" Selection: ");
	}


}
