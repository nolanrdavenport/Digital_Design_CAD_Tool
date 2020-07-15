/*
 * Holds the information for an entire wire. 
 * 
 *  	This is the Digital Design CAD Tool. This tool is used to design digital circuits.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wire implements Cloneable{
	// All lines that make up this wire.
	public ArrayList<Line> lines;

	// Component that determines the binary value of this line.
	public Component valueDeterminingComponent = null;
	
	// Other general variables.
	public boolean selected = false;
	
	// Constructor
	public Wire() {
		lines = new ArrayList<Line>();
	}

	// Allows a shallow copy of this wire.
	public Object clone() throws CloneNotSupportedException {
		return (Wire) super.clone();
	}

	/*
	 * Adds a line to the wire.
	 * 
	 * @param line The line to be added.
	 */
	public void addLine(Line line) {
		lines.add(line);
	}

	/*
	 * Removes a line from the wire.
	 * 
	 * @param line Line to be removed.
	 */
	public void removeLine(Line line) {
		lines.remove(line);
	}

	/*
	 * @return The lines ArrayList.
	 */
	public ArrayList<Line> getLines() {
		return lines;
	}

	/*
	 * Draws the wire onto a canvas.
	 * 
	 * @param gc The graphics context of the canvas that is to be drawn on.
	 */
	public void drawWire(GraphicsContext gc) {
		Iterator<Line> itr = lines.iterator();
		while(itr.hasNext()) {
			Line line = itr.next();
			if (selected) {
				gc.setStroke(Color.RED);
				gc.setFill(Color.RED);
			} else {
				gc.setStroke(Color.WHITE);
				gc.setFill(Color.WHITE);
			}

			gc.setLineWidth(2);

			gc.strokeLine(line.x1, line.y1 - 1, line.x2, line.y2 - 1);
			if(line.x1 == line.x2 && line.y1 == line.y2) {
				itr.remove();
			}
		}
		
		for (Line line1 : lines) {
			int numSame1 = 1;
			int numSame2 = 1;
			for (Line line2 : lines) {
				if (line1 != line2 && ((line1.x1 == line2.x1 && line1.y1 == line2.y1)
						|| (line1.x1 == line2.x2 && line1.y1 == line2.y2))) {
					numSame1++;
					if (numSame1 >= 3) {
						gc.fillOval(line1.x1 - 5, line1.y1 - 6, 10, 10);
						numSame1 = 0;
					}
				}else if(line1 != line2 && ((line1.x2 == line2.x1 && line1.y2 == line2.y1)
						|| (line1.x2 == line2.x2 && line1.y2 == line2.y2))) {
					numSame2++;
					if (numSame2 >= 3) {
						gc.fillOval(line1.x2 - 5, line1.y2 - 6, 10, 10);
						numSame2 = 0;
					}
				}
				// Vertically in between.
				if(line1.x1 == line1.x2 && line1.x1 == line2.x2
						&& ((line2.y2 < line1.y1 && line2.y2 > line1.y2) || (line2.y2 > line1.y1 && line2.y2 < line1.y2))) {
					gc.fillOval(line2.x2 - 5, line2.y2 - 6, 10, 10);
				}
				
				// Horizontally in between.
				if(line1.y1 == line1.y2 && line1.y1 == line2.y2
						&& ((line2.x2 < line1.x1 && line2.x2 > line1.x2) || (line2.x2 > line1.x1 && line2.x2 < line1.x2))) {
					gc.fillOval(line2.x2 - 5, line2.y2 - 6, 10, 10);
				}
			}
		}
	}

	/*
	 * Sets the value determining component.
	 * 
	 * @param comp The component that is to be the new value determining component.
	 */
	public void setValueDeterminingComponent(Component comp) {
		valueDeterminingComponent = comp;
	}

	/*
	 * @return Whether or not the x and y location sent in as parameter is a proper
	 * click onto this wire.
	 * 
	 * @param x The x value of the click.
	 * 
	 * @param y The y value of the click.
	 */
	public boolean clickedOnALine(double x, double y) {
		float temp1 = Math.round((float) x / 10) * 10 - 1;
		float temp2 = Math.round((float) y / 10) * 10;
		for (Line line : lines) {
			// vertically
			if (line.x1 == line.x2 && line.x1 == temp1
					&& ((line.y1 <= temp2 && line.y2 >= temp2) || (line.y1 >= temp2 && line.y2 <= temp2))) {
				return true;
			} else if (line.y1 == line.y2 && line.y1 == temp2
					&& ((line.x1 <= temp1 && line.x2 >= temp1) || (line.x1 >= temp1 && line.x2 <= temp1))) {
				return true;
			}
		}
		return false; // TODO: make it work
	}

	/*
	 * Cleans up false lines that have no length.
	 */
	public void cleanUp() {
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).x1 == lines.get(i).x2 && lines.get(i).y1 == lines.get(i).y2) {
				lines.remove(lines.get(i));
				i--;
			}
		}
	}
	
	public SerializableWire getSerializableWire() {
		return new SerializableWire(lines, valueDeterminingComponent);
	}

}
