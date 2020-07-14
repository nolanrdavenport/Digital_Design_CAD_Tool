/*
 * Holds information for a single line. 
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

import java.io.Serializable;

public class Line implements Serializable{
	private static final long serialVersionUID = 2250651531902867060L;
	// beginning point
	public double x1;
	public double y1;
	
	// ending point
	public double x2;
	public double y2;
	
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
}
