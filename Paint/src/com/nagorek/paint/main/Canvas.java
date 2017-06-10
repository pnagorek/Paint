package com.nagorek.paint.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Canvas extends JPanel{

	private static final long serialVersionUID = 1L;

	public int width;
	public int height;
	private BufferedImage image;
	private Graphics2D g2d;
	private int currentX, currentY, previousX, previousY;
	private GlassPanel glass;
	//private MessageBar message;
	
	private float dash[] = {3.0f};
	private BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	private Rectangle selection;
	public boolean rectangleDrawn = false;
	
	private BufferedImage tempImage, eraseImage;
	private Graphics2D tg2d, eg2d;

	public Canvas(int width, int height, MessageBar message){		
		this.width = width;
		this.height = height;
		//this.message = message;
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		clear();

		setSize(width, height);
		setPreferredSize(new Dimension(width, height));
		//setMaximumSize(new Dimension(width, height));
		//setMinimumSize(new Dimension(width, height));
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				previousX = e.getX();
				previousY = e.getY();
				if(Paint.isSelected){
					selection = null;
					rectangleDrawn = false;
				}
			}
			
			public void mouseReleased(MouseEvent e){
				if(Paint.isSelected && !rectangleDrawn){
					if(currentX - previousX < 0 || currentY - previousY < 0){
						return;
					}
					selection = new Rectangle(previousX, previousY, currentX - previousX, currentY - previousY);
					tempImage = new BufferedImage(selection.width, selection.height, BufferedImage.TYPE_INT_RGB);
					tg2d = tempImage.createGraphics();
					tg2d.setPaint(Color.WHITE);
					eraseImage = image.getSubimage(selection.x, selection.y, selection.width, selection.height);
				    eg2d = eraseImage.createGraphics();
				    eg2d.setPaint(Color.WHITE);
					tg2d.drawImage(eraseImage, 0, 0, null);
					eg2d.fillRect(0, 0, selection.width, selection.height);
					rectangleDrawn = true;
				}
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
				if(Paint.isSelected){
					if(rectangleDrawn){
						if(selection.contains(currentX, currentY)){			
							//g2d.drawImage(tempImage, currentX - selection.x, currentY - selection.y, null);
							repaint();
							return;
						} else {
							selection = null;
							rectangleDrawn = false;
							message.clearSelectionText();
						}
					}
					if(currentX - previousX <= 0 || currentY - previousY <= 0){
						message.clearSelectionText();
					} else {
						message.setSelectionText(currentX - previousX, currentY - previousY);
					}
					repaint();
					return;
				} else {
					message.clearSelectionText();
					g2d.setStroke(new BasicStroke(10));
					g2d.drawLine(previousX, previousY, currentX, currentY);
					repaint();
					previousX = currentX;
					previousY = currentY;
				}
			}

			public void mouseMoved(MouseEvent e){
				currentX = e.getX();
				currentY = e.getY();
				if(Paint.isSelected && rectangleDrawn){
					if(selection.contains(currentX, currentY)){
						glass.moveSelection(selection.x, selection.y, tempImage, new Cursor(Cursor.MOVE_CURSOR));
						repaint();
					} else {
						setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					}
				} else {
					message.setPositionText(e.getX(), e.getY());
				}
			}
		});
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2d.setPaint(Color.black);
		g2.drawImage(image, 0, 0, null);
		if(Paint.isSelected && selection == null){
			g2.setStroke(dashed);
			g2.drawRect(previousX, previousY, currentX - previousX, currentY - previousY);
		}
	}

	public void clear(){
		g2d.setPaint(Color.PINK);
		g2d.fillRect(0, 0, width, height);
		repaint();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public void addGlassPanel(GlassPanel glass){
		this.glass = glass;
	}
	
	public void addSelection(int x, int y, BufferedImage image){
		rectangleDrawn = false;
		g2d.drawImage(image, x, y, null);
		repaint();		
	}
}

