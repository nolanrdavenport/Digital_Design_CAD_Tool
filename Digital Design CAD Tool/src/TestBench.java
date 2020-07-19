import components.*;

/*	This is a test bench that I can use to test features without having to test them inside of important classes. I can do whatever I want in this bad boy!
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
public class TestBench {
	XorGate test;
	public static void main(String args[]) {
		new TestBench();
	}
	public TestBench() {
		test = new XorGate(60, 40, 0, 0, 0, 2, 123123); // test and gate
				
		test.calculateOutput();
	}
}
