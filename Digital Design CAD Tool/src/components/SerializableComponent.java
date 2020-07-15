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

public class SerializableComponent implements Serializable{
	private static final long serialVersionUID = 3025588903980672847L;
	public double xPos;
	public double yPos;
	public int rotation; // options: "right = 0", "down = 1", "left = 2", "up = 3"
	public String id;
	public int numInputs;
	public boolean[] inputs;
	public boolean output;
	public int width, height;
	public int ID;
	
	// Constructor
	public SerializableComponent(double xPos, double yPos, int rotation, String id, int numInputs, boolean[] inputs, boolean output, int width, int height, int ID) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
		this.id = id;
		this.numInputs = numInputs;
		this.inputs = inputs;
		this.output = output;
		this.width = width;
		this.height = height;
		this.ID = ID;
	}
	
	/*
	 * Creates a component instance based on the data stored in this object. This is used for instantiating objects that were saved onto a file.
	 */
	public Component getDeserializedComponent() {
		//return new Component(xPos, yPos, rotation, id, numInputs, inputs, output, width, height, ID);
		switch (id) {
		case "AND":
			return new AndGate(width, height, xPos, yPos, rotation, numInputs, ID);
		case "OR":
			return new OrGate(width, height, xPos, yPos, rotation, numInputs, ID);
		case "NAND":
			return new NandGate(width, height, xPos, yPos, rotation, numInputs, ID);
		case "NOR":
			return new NorGate(width, height, xPos, yPos, rotation, numInputs, ID);
		case "XOR":
			return new XorGate(width, height, xPos, yPos, rotation, numInputs, ID);
		case "NOT":
			return new NotGate(width, height, xPos, yPos, rotation, numInputs, ID);
		case "IO_IN":
			//TODO: CHANGE THIS TO ALLOW USER TO CHANGE NAME
			//currState.components.add(new IOPort((mouseEventX - (mouseEventX % 10)),
			//		(mouseEventY - (mouseEventY % 10)), 0, "in", "temp"));
			break;
		case "IO_OUT":
			//TODO: CHANGE THIS TO ALLOW USER TO CHANGE NAME
			//currState.components.add(new IOPort((mouseEventX - (mouseEventX % 10)),
			//		(mouseEventY - (mouseEventY % 10)), 0, "out", "temp"));
			break;
		case "IO_BI":
			//TODO: CHANGE THIS TO ALLOW USER TO CHANGE NAME
			//currState.components.add(new IOPort((mouseEventX - (mouseEventX % 10)),
			//		(mouseEventY - (mouseEventY % 10)), 0, "bi", "temp"));
			break;
		default:
			System.err.println("Invalid component ID. There is something wrong with the shematic file.");
		}
		return new AndGate(0,0,0,0,0,0,0);
	}
	
}