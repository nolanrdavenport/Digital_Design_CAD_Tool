/* This class creates a serializable object that holds all necessary information to instantiate a wire. 
 * 
 *    	This is the Digital Design CAD Tool. This tool is used to design digital circuits.
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

import javafx.application.Application;

public class SerializableWire implements Serializable{
	private static final long serialVersionUID = 644962249459735287L;
	
	public ArrayList<Line> lines;
	public SerializableComponent valueDeterminingComponent;
	public SerializableWire(ArrayList<Line> lines, Component valueDeterminingComponent) {
		this.lines = lines;
		this.valueDeterminingComponent = valueDeterminingComponent.getSerializableComponent();
	}
	
	/*
	 * Creates a wire instance based on the data stored in this object. This is used for instantiating objects that were saved onto a file.
	 */
	public Wire getDeserializedWire(ArrayList<Component> components) {
		Wire wire = new Wire();
		wire.lines = lines;
		
		for(Component comp : components) {
			if(comp.ID == valueDeterminingComponent.ID) {
				wire.setValueDeterminingComponent(comp);
			}
		}
		
		return wire;
	}
}