/*   	This is the Digital Design CAD Tool. This tool is used to design digital circuits.
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
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class OrGate extends Component{
	public OrGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(60, 40, xPos, yPos, rotation, canvas, numInputs);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "OR";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/OR/OR_"+i+".png",120,120,true,false);
		}
		this.currImage = images[0];
	}
	public void setOutput(){
		output = false;
		for(boolean input : inputs) {
			if(input == true) { 
				output = true;
			}
		}
	}
}