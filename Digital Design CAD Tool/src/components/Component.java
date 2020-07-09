/*
 * Abstract class that defines what a component should be and how it should work. 
 */
package components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Component implements Cloneable{
	// Component information.
	public double xPos, yPos;
	public int rotation; // options: "right = 0", "down = 1", "left = 2", "up = 3"
	public String id;
	public Image[] images = new Image[4];
	public Image currImage;
	public boolean selected;
	public Canvas canvas;
	public int numInputs;
	public boolean[] inputs;
	public boolean output;
	public int width, height;

	// Constructor.
	public Component(int width, int height, double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		this.width = width;
		this.height = height;
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
		this.selected = false;
		this.canvas = canvas;
		this.numInputs = numInputs;
		
		inputs = new boolean[numInputs];
	}
	
	// Allows for shallow copy of the component.
	public Object clone()throws CloneNotSupportedException{  
		return (Component)super.clone();  
	}

	// Checks if a mouse click is inside the bounds of the component.
	public boolean insideBounds(double mouseX, double mouseY) {
		if (mouseX > xPos && mouseX < xPos + currImage.getWidth() && mouseY > yPos && mouseY < yPos + currImage.getHeight()) {
			return true;
		}else {
			return false;
		}
	}

	
	// Getters and setters.
	public void setX(double x) {
		xPos = x;
	}
	public void setY(double y) {
		yPos = y;
	}
	public double getX() {
		return xPos;
	}
	public double getY() {
		return yPos;
	}
	
	/*
	 * Draws the component onto a canvas.
	 * @param gc The graphics context of the canvas that is to be drawn on.
	 */
	public void drawComponent(GraphicsContext gc) {
		gc.drawImage(images[rotation], (xPos - (xPos % 10)), (yPos - (yPos % 10)));
		if (this.selected == true) {
			gc.setStroke(Color.WHITE);
			gc.strokeRect((xPos - (xPos % 10)), (yPos - (yPos % 10)), currImage.getWidth(), currImage.getHeight());
		}
	}
	
	/*
	 * Rotates the component clockwise.
	 */
	public void rotate() {
		if (rotation < 3) {
			rotation++;
		} else if (rotation >= 3) {
			rotation = 0;
		}
		
		currImage = images[rotation];
	}
	
	/*
	 * Squares the component to the grid.
	 */
	public void square() {
		xPos = xPos - (xPos % 10);
		yPos = yPos - (yPos % 10);
	}

}
