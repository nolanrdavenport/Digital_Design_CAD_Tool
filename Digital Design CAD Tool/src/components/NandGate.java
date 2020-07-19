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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import components.Component;
import javafx.scene.image.Image;
public class NandGate extends Component{
	public NandGate(int width, int height, double xPos, double yPos, int rotation, int numInputs, int ID) {
		super(60, 40, xPos, yPos, rotation, numInputs, ID);
		this.id = "NAND";
		for(int i = 0; i < this.images.length; i++) {
			try {
				Class cls = Class.forName("Main");
				images[i] = new Image(cls.getResourceAsStream("Images/NAND/NAND_"+i+".png"),120,120,true,false);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.currImage = images[0];
	}
}
