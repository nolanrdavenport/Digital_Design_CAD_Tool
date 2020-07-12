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

public class IOPort extends Component{
	// Options are: "in", "out", or "bi", for input, output and bidirectional.
	public String direction = null;
	public String name = null;
	
	public IOPort(double xPos, double yPos, int rotation, Canvas canvas, String direction, String name) {
		super(30, 20, xPos, yPos, rotation, canvas, 1);
		this.direction = direction;
		this.name = name;
		this.id = "IO_"+direction;
		
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/IOPort/"+direction+"/"+"IO_"+direction.toUpperCase()+"_"+i+".png", 60,60,true,false);
		}
		this.currImage = images[0];
	}
}
