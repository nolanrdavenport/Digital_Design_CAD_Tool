/*
 * Abstract class that defines what a component should be and how it should work. 
 *
 *   	This is the Digital Design CAD Tool. This tool is used to design digital circuits.
 *      Copyright (C) 2020  Nolan Davenport
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package components;

import com.sun.javafx.geom.Vec2d;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Component implements Cloneable{
	// Component information.
	public Vec2d location;
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
	public Vec2d outputLocation;
	public Vec2d[] inputLocations;

	// Constructor.
	public Component(int width, int height, double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.selected = false;
		this.canvas = canvas;
		this.numInputs = numInputs;
		
		inputs = new boolean[numInputs];
		
		// Location vectors
		location = new Vec2d(xPos, yPos);
		outputLocation = new Vec2d();
		inputLocations = new Vec2d[numInputs];
		for(int i = 0; i < numInputs; i++) {
			inputLocations[i] = new Vec2d();
		}
		calculateLocations();
	}
	
	// Allows for shallow copy of the component.
	public Object clone()throws CloneNotSupportedException{  
		return (Component)super.clone();  
	}

	// Checks if a mouse click is inside the bounds of the component.
	public boolean insideBounds(double mouseX, double mouseY) {
		if (mouseX > location.x && mouseX < location.x + currImage.getWidth() && mouseY > location.y && mouseY < location.y + currImage.getHeight()) {
			return true;
		}else {
			return false;
		}
	}
	
	/*
	 * Draws the component onto a canvas.
	 * @param gc The graphics context of the canvas that is to be drawn on.
	 */
	public void drawComponent(GraphicsContext gc) {
		gc.drawImage(images[rotation], (location.x - (location.x % 10)), (location.y - (location.y % 10)));
		if (this.selected == true) {
			gc.setStroke(Color.WHITE);
			gc.strokeRect((location.x - (location.x % 10)), (location.y - (location.y % 10)), currImage.getWidth(), currImage.getHeight());
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
		
		calculateLocations();
	}
	
	/*
	 * Squares the component to the grid.
	 */
	public void square() {
		location.x = location.x - (location.x % 10);
		location.y = location.y - (location.y % 10);
		calculateLocations();
	}
	
	/*
	 * Uses the location vector to calculate the inputs and output location vectors.
	 */
	public void calculateLocations() {
		System.out.println("calculated locations... ");
		System.out.println("Location: " + location.x + ", " + location.y);
		System.out.println("Output Location: " + outputLocation.x + ", " + outputLocation.y);
		switch(rotation) {
			case 0:
				outputLocation.x = location.x + (2 * width);
				outputLocation.y = location.y + height;
				for(int i = 0, j = 20; i < numInputs; j += 40, i++) {
					inputLocations[i].x = location.x;
					inputLocations[i].y = location.y + j;
				}
				break;
			case 1:
				outputLocation.x = location.x + height;
				outputLocation.y = location.y + (2 * width);
				
				for(int i = 0, j = 20; i < numInputs; j += 40, i++) {
					inputLocations[i].x = location.x + j;
					inputLocations[i].y = location.y;
				}
				break;
			case 2:
				outputLocation.x = location.x;
				outputLocation.y = location.y + height;
				
				for(int i = 0, j = 20; i < numInputs; j += 40, i++) {
					inputLocations[i].x = location.x + (2 * width);
					inputLocations[i].y = location.y + j;
				}
				break;
			case 3:
				outputLocation.x = location.x + height;
				outputLocation.y = location.y;
				
				for(int i = 0, j = 20; i < numInputs; j += 40, i++) {
					inputLocations[i].x = location.x + j;
					inputLocations[i].y = location.y + (2 * width);
				}
				break;
			default:
				System.err.println("That is an invalid rotation value");
		}
	}
	
	public double getX() {
		return location.x;
	}
	public double getY() {
		return location.y;
	}
	public void setX(double x) {
		location.x = x;
		//calculateLocations();
	}
	public void setY(double y) {
		location.y = y;
		//calculateLocations();
	}

}
