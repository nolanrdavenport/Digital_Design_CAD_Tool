package components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Component implements Cloneable{
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
	
	public Object clone()throws CloneNotSupportedException{  
		return (Component)super.clone();  
	}

	public boolean insideBounds(double mouseX, double mouseY) {
		if (mouseX > xPos && mouseX < xPos + currImage.getWidth() && mouseY > yPos && mouseY < yPos + currImage.getHeight()) {
			selected = true;
		}

		return selected;
	}

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

	public void drawComponent(GraphicsContext gc) {
		gc.drawImage(images[rotation], (xPos - (xPos % 10)), (yPos - (yPos % 10)));
		if (this.selected == true) {
			gc.setStroke(Color.WHITE);
			gc.strokeRect((xPos - (xPos % 10)), (yPos - (yPos % 10)), currImage.getWidth(), currImage.getHeight());
		}
	}

	public void rotate() {
		if (rotation < 3) {
			rotation++;
		} else if (rotation >= 3) {
			rotation = 0;
		}
		
		currImage = images[rotation];
	}

	public void square() {
		xPos = xPos - (xPos % 10);
		yPos = yPos - (yPos % 10);
	}

}
