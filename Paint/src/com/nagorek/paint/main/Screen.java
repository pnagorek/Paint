package com.nagorek.paint.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel{

	private static final long serialVersionUID = 1L;

	private Canvas canvas;
	private GlassPanel glass;
	//private MessageBar message;
	private Rectangle bottom, side, edge;
	private GridBagConstraints c;

	private int padding = 6;
	private int squareSize = 5;

	private int mouseX, mouseY;
	private Cursor cursor = Cursor.getDefaultCursor();
	private int resize = 0;
	private Rectangle rectangle;
	

	public Screen(Canvas canvas, MessageBar message, GlassPanel glass){

		this.canvas = canvas;
		this.glass = glass;
		//this.message = message;		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(padding, padding, 0, 0);
		c.weighty = 1.0;  
		c.weightx = 1.0;
		add(canvas, c);		
		
		setRectangles(canvas.width, canvas.height);

		addMouseMotionListener(new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				mouseX = e.getX();
				mouseY = e.getY();
				if(bottom.contains(mouseX, mouseY)){
					cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
					resize = GlassPanel.RESIZE_BOTTOM;
					rectangle = bottom;
				} else if(side.contains(mouseX, mouseY)){
					cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
					resize = GlassPanel.RESIZE_SIDE;
					rectangle = side;
				} else if(edge.contains(mouseX, mouseY)){
					cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
					resize = GlassPanel.RESIZE_BOTH;
					rectangle = edge;
				} 
				else {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					resize = GlassPanel.RESIZE_NONE;
					rectangle = null;
					return;
				}
				setCursor(cursor);
				repaint();
				glass.triggerSelection(cursor, resize, rectangle);					
			}
		});
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
			}
		});
	}
	
	private void setRectangles(int width, int height){
		bottom = new Rectangle((width)/2 - squareSize/2 + padding, height + padding, squareSize, squareSize);
		side = new Rectangle(width + padding, (height)/2 - squareSize/2 + padding, squareSize, squareSize);
		edge = new Rectangle(width + padding, height + padding, squareSize, squareSize);	
	}
	
	public void resizeCanvas(Canvas canvas){
		canvas.addGlassPanel(glass);
		BufferedImage image = this.canvas.getImage();		
		if(canvas.width < this.canvas.width && canvas.height < this.canvas.height){
			image = image.getSubimage(0, 0, canvas.width, canvas.height);
		}
		BufferedImage newImage = new BufferedImage(canvas.width, canvas.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = newImage.createGraphics();
		g2d.setPaint(Color.white);
		g2d.fillRect(0, 0, canvas.width, canvas.height);
		g2d.drawImage(image, 0, 0, null);
		canvas.setImage(newImage);
		
		removeAll();
		this.canvas = canvas;
		add(canvas, c);
		canvas.repaint();
		
		setRectangles(canvas.width, canvas.height);
		setPreferredSize(new Dimension(canvas.width + padding + squareSize, canvas.height + padding + squareSize));
		repaint();
		validate();
	}

	@Override
	public void paintComponent(Graphics g){
		GradientPaint gp = new GradientPaint(0, 0, new Color(179, 200, 209), 0, getHeight(), new Color(227, 232, 239));
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.WHITE);
		g2d.fill(bottom);
		g2d.fill(side);
		g2d.fill(edge);		
		g2d.setColor(Color.GRAY);
		g2d.draw(bottom);
		g2d.draw(side);
		g2d.draw(edge);
	}
	
	public int getPadding(){
		return padding;
	}

	/*private class Canvass extends JPanel{

		private static final long serialVersionUID = 1L;

		private int width;
		private int height;
		private BufferedImage image;
		private Graphics2D g2d;
		private int currentX, currentY, previousX, previousY;

		public Canvass(int width, int height){		
			this.width = width;
			this.height = height;

			setPreferredSize(new Dimension(width, height));
			setMaximumSize(new Dimension(width, height));
			setMinimumSize(new Dimension(width, height));
			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

			addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					previousX = e.getX();
					previousY = e.getY();
				}

				@Override
				public void mouseExited(MouseEvent e){
					message.clearPositionText();
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

				@Override
				public void mouseEntered(MouseEvent e){
					setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
			});

			addMouseMotionListener(new MouseMotionAdapter(){
				public void mouseDragged(MouseEvent e){
					currentX = e.getX();
					currentY = e.getY();
					if(g2d != null)
						g2d.setStroke(new BasicStroke(10));
					g2d.drawLine(previousX, previousY, currentX, currentY);
					repaint();
					previousX = currentX;
					previousY = currentY;
				}

				public void mouseMoved(MouseEvent e){
					message.setPositionText(e.getX(), e.getY());
				}

			});
			
		}

		@Override
		public void paintComponent(Graphics g){
			if(image == null){
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				g2d = image.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				clear();
			}
			g.drawImage(image, 0, 0, null);
		}

		public void clear(){
			g2d.setPaint(Color.white);
			g2d.fillRect(0, 0, width, height);
			g2d.setPaint(Color.black);
			repaint();
		}
	}*/

}

