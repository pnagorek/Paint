package com.nagorek.paint.main;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class GlassPanel extends JComponent{

	private static final long serialVersionUID = 1L;
	
	private int x, y;
	private boolean isCleared = true;
	
	private int mouseX, mouseY;
	private int oldX, oldY;
	private int canvasSizeX;
	private int canvasSizeY;
	private Canvas canvas;
	
	public static final int RESIZE_NONE = 0;
	public static final int RESIZE_BOTTOM = 1;
	public static final int RESIZE_SIDE = 2;
	public static final int RESIZE_BOTH = 3;
	
	private int currentResize = 0;
	private Rectangle rectangle;
	
	private BufferedImage image;
	private int positionX, positionY;
	
	private float dash[] = {3.0f};
    private BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	
	//private MessageBar message;
	private Screen screen;
	
	public GlassPanel(int defaultCanvasSize, Canvas canvas, MessageBar message){
		//this.message = message;
		this.canvas = canvas;
		this.canvasSizeX = this.canvasSizeY = defaultCanvasSize;
		
		addMouseMotionListener(new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				mouseX = e.getX();
				mouseY = e.getY();
				if(Paint.isSelected && image != null){
					if(mouseX > positionX + image.getWidth() || mouseX < positionX || mouseY > positionY + image.getHeight() || mouseY < positionY){
						//dropMoveSelection();
						setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
						return;
					} else {
						setCursor(new Cursor(Cursor.MOVE_CURSOR));
						message.setPositionText(mouseX - x, mouseY - y);
						repaint();
						return;
					}
				}
				if(rectangle == null) return;
				Rectangle mouseCursor = new Rectangle(mouseX - x, mouseY - y, 20, 20);
				if(!rectangle.intersects(mouseCursor) && isCleared){
					dropSelection();
				}
			}
			public void mouseDragged(MouseEvent e){	
				mouseX = e.getX() - x;
				mouseY = e.getY() - y;
				if(Paint.isSelected && image != null){
					message.setPositionText(mouseX, mouseY);	
					if(oldX == 0) oldX = mouseX;
					if(oldY == 0) oldY = mouseY;
					positionX += mouseX - oldX;
					positionY += mouseY - oldY;
					repaint();
					oldX = mouseX;
					oldY = mouseY;
					return;
				}
				repaint();
				message.setSizeText(mouseX, mouseY);
			}
		});

		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(Paint.isSelected && image != null){
					if(mouseX > positionX + image.getWidth() || mouseX < positionX || mouseY > positionY + image.getHeight() || mouseY < positionY){
						dropMoveSelection();
						return;
					}
				}

				isCleared = false;
				mouseX = e.getX() - x;
				mouseY = e.getY() - y;
			}
			public void mouseReleased(MouseEvent e){
				if(Paint.isSelected && image != null){
					return;
				}
				Canvas canvas = new Canvas(canvasSizeX, canvasSizeY, message);
				setCanvas(canvas);
				screen.resizeCanvas(canvas);
				dropSelection();
			}
		});

	}
	
	private void setCanvas(Canvas canvas){
		this.canvas = canvas;
	}
	
	private void dropMoveSelection(){
		canvas.addSelection(positionX - x, positionY - y, image);
		positionX = 0;
		positionY = 0;
		oldX = 0;
		oldY = 0;
		image = null;
		setVisible(false);
	}
	
	public void moveSelection(int x, int y, BufferedImage image, Cursor cursor){
		positionX = x + this.x;
		positionY = y + this.y;
		this.image = image;
		setCursor(cursor);
		setVisible(true);
	}
	
	public void addScreen(Screen screen){
		this.screen = screen;
	}
	
	public void triggerSelection(Cursor cursor, int resize, Rectangle rectangle){
		this.rectangle = rectangle;
		setCursor(cursor);
		currentResize = resize;
		setVisible(true);
	}
	
	private void dropSelection(){
		currentResize = 0;
		isCleared = true;
		setVisible(false);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		if(Paint.isSelected && image != null){
			g2d.drawImage(image, positionX, positionY, null);
			g2d.setStroke(dashed);
			g2d.drawRect(positionX, positionY, image.getWidth(), image.getHeight());
			return;
		}
		if(!isCleared){
		    g2d.setStroke(dashed);
			if(currentResize == RESIZE_BOTTOM){
				g2d.drawRect(x, y, canvasSizeX, canvasSizeY + (mouseY - canvasSizeY));
				canvasSizeY = mouseY;
			} else if(currentResize == RESIZE_SIDE){
				g2d.drawRect(x, y, canvasSizeX + (mouseX - canvasSizeX), canvasSizeY);
				canvasSizeX = mouseX;
			} else if(currentResize == RESIZE_BOTH){
				g2d.drawRect(x, y, canvasSizeX + (mouseX - canvasSizeX), canvasSizeY + (mouseY - canvasSizeY));
				canvasSizeX = mouseX;
				canvasSizeY = mouseY;
			} else return;
		}
	}

	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

}
