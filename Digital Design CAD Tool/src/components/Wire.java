/*
 * Holds the information for an entire wire. 
 */
package components;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wire implements Cloneable{
	// All lines that make up this wire.
	public ArrayList<Line> lines;
	
	// Component that determines the binary value of this line.
	public Component valueDeterminingComponent = null;
	public boolean value;
	public ArrayList<Component> connections;
	
	public boolean selected = false;
	
	// Constructor
	public Wire() {
		lines = new ArrayList<Line>();
	}
	
	// Allows a shallow copy of this wire.
	public Object clone()throws CloneNotSupportedException{  
		return (Wire)super.clone();  
	}
	
	/*
	 * Adds a line to the wire.
	 * @param line The line to be added.
	 */
	public void addLine(Line line) {
		lines.add(line);
	}
	
	/*
	 * Removes a line from the wire.
	 * @param line Line to be removed.
	 */
	public void removeLine(Line line) {
		lines.remove(line);
	}
	
	/*
	 * @return The lines ArrayList.
	 */
	public ArrayList<Line> getLines(){
		return lines;
	}
	
	/*
	 * Draws the wire onto a canvas.
	 * @param gc The graphics context of the canvas that is to be drawn on.
	 */
	public void drawWire(GraphicsContext gc) {
		for(Line line : lines) {
			if(selected) {
				gc.setStroke(Color.RED);
				gc.setFill(Color.RED);
			}else {
				gc.setStroke(Color.WHITE);
				gc.setFill(Color.WHITE);
			}
			
			gc.setLineWidth(2);
			
			gc.strokeLine(line.x1, line.y1 - 1, line.x2, line.y2 - 1);
			if(line.endingPointClosed) {
				gc.fillOval(line.x2 - 5, line.y2 - 6, 10, 10);
			}
		}
	}
	
	/*
	 * Sets the value determining component.
	 * @param comp The component that is to be the new value determining component.
	 */
	public void setValueDeterminingComponent(Component comp) {
		valueDeterminingComponent = comp;
	}
	
	/*
	 * Sets the binary value of the wire.
	 */
	public void setValue() {
		if(valueDeterminingComponent != null) {
			value = valueDeterminingComponent.output;
		}
	}
	
	/*
	 * @return Whether or not the x and y location sent in as parameter is a proper click onto this wire.
	 * @param x The x value of the click.
	 * @param y The y value of the click.
	 */
	public boolean clickedOnALine(double x, double y) {
		float temp1 = Math.round((float) x / 10) * 10 - 1;
		float temp2 = Math.round((float) y / 10) * 10;
		for(Line line : lines) {
			// vertically
			if(line.x1 == line.x2 && line.x1 == temp1
					&& ((line.y1 <= temp2 && line.y2 >= temp2) || (line.y1 >= temp2 && line.y2 <= temp2))) {
				return true;
			}else if(line.y1 == line.y2 && line.y1 == temp2 && ((line.x1 <= temp1 && line.x2 >= temp1) || (line.x1 >= temp1 && line.x2 <= temp1))) {
				return true;
			}
		}
		return false; //TODO: make it work
	}
}
