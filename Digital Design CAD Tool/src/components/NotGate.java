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
import javafx.scene.image.Image;
public class NotGate extends Component{
	public NotGate(int width, int height, double xPos, double yPos, int rotation, int numInputs, int ID) {
		super(30, 20, xPos, yPos, rotation, numInputs, ID);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NOR";
		for(int i = 0; i < this.images.length; i++) {
			try {
				Class cls = Class.forName("Main");
				images[i] = new Image(cls.getResourceAsStream("Images/NOT/NOT_"+i+".png"),60,60,true,false);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.currImage = images[0];
	}
}