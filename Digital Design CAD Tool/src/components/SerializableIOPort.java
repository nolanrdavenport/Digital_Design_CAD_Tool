/* This class creates a serializable object that holds all necessary information to instantiate a component. 
 * 
 * 		This is the Digital Design CAD Tool. This tool is used to design digital circuits.
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

public class SerializableIOPort implements Serializable{
	private static final long serialVersionUID = 3025588903980672847L;
	public double xPos;
	public double yPos;
	public int rotation; // options: "right = 0", "down = 1", "left = 2", "up = 3
	public int ID;
	public String direction;
	public String name;
	
	// Constructor
	public SerializableIOPort(double xPos, double yPos, int rotation, int ID, String direction, String name) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
		this.ID = ID;
		this.direction = direction;
		this.name = name;
	}
	
	/*
	 * Creates a component instance based on the data stored in this object. This is used for instantiating objects that were saved onto a file.
	 */
	public Component getDeserializedIOPort() {
		return new IOPort(xPos, yPos, rotation, ID, direction, name);
	}
	
}