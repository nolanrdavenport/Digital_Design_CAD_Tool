/*
 * Component that acts as inputs and outputs that can be used in running functional logic simulations and producing timing diagrams.
 * 
 * This is an optional method for testing the circuit. Alternatively, the user can put a binary switch, function generator, or likewise
 * to test the circuit live.
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

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

// TODO: make ioports savable
public class IOPort extends Component{
	// Options are: "in", "out", or "bi", for input, output and bidirectional.
	public String direction = null;
	public String name = null;
	
	public IOPort(double xPos, double yPos, int rotation, int ID, String direction, String name) {
		super(30, 20, xPos, yPos, rotation, 1, ID);
		this.direction = direction;
		this.name = name;
		this.id = "IO_"+direction;
		
		@SuppressWarnings("rawtypes")
		Class cls;
		try {
			cls = Class.forName("Main");
			for(int i = 0; i < this.images.length; i++) {
				images[i] = new Image(cls.getResourceAsStream("Images/IOPort/"+direction+"/"+"IO_"+direction.toUpperCase()+"_"+i+".png"), 60,60,true,false);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.currImage = images[0];
	}
	
	public SerializableIOPort getSerializableIOPort() {
		return new SerializableIOPort(super.getX(), super.getY(), rotation, this.ID, direction, name);
	}
}
