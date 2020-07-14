/*
 * Stores information for the state of the schematic, such as componet and wire information.
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
import components.Component;
import components.Wire;

import java.io.Serializable;
import java.util.ArrayList;
public class SchematicState implements Serializable{
	private static final long serialVersionUID = 676096957550284174L;
	// Holds the component and wire data.
	public ArrayList<Component> components;
	public ArrayList<Wire> wires;
	
	// Copy constructor.
	public SchematicState(SchematicState copy) {
		components = new ArrayList<Component>();
		wires = new ArrayList<Wire>();
		
		for(Component comp : copy.components) {
			try {
				components.add((Component)comp.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Wire wire : copy.wires) {
			try {
				wires.add((Wire)wire.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Constructor.
	public SchematicState() {
		components = new ArrayList<Component>();
		wires = new ArrayList<Wire>();
	}
	
	/*
	 * Adds a component to the components ArrayList.
	 * @param comp The component to be added.
	 */
	public void addComponent(Component comp) {
		components.add(comp);
	}
	
	/*
	 * Adds a wire to the wires ArrayList.
	 * @param wire The wire to be added.
	 */
	public void addWire(Wire wire) {
		wires.add(wire);
	}
}
