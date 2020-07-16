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
	AndGate test;
	public static void main(String args[]) {
		new TestBench();
	}
	public TestBench() {
		test = new AndGate(60, 40, 0, 0, 0, 4, 123123); // test and gate
		
		System.out.println("Creating truth table using the test and gate as the logical component: ");
		test.inputs[0] = false;
		test.inputs[1] = false;
		test.inputs[2] = false;
		test.inputs[3] = false;
		test.calculateOutput();
		for(Boolean input : test.inputs) {
			System.out.print(input + " | ");
		}
		System.out.println(test.output + " |");
		
		test.inputs[0] = false;
		test.inputs[1] = true;
		test.inputs[2] = false;
		test.inputs[3] = false;
		test.calculateOutput();
		for(Boolean input : test.inputs) {
			System.out.print(input + " | ");
		}
		System.out.println(test.output + " |");
		
		test.inputs[0] = true;
		test.inputs[1] = false;
		test.inputs[2] = false;
		test.inputs[3] = false;
		test.calculateOutput();
		for(Boolean input : test.inputs) {
			System.out.print(input + " | ");
		}
		System.out.println(test.output + " |");
		
		test.inputs[0] = true;
		test.inputs[1] = true;
		test.inputs[2] = true;
		test.inputs[3] = true;
		test.calculateOutput();
		for(Boolean input : test.inputs) {
			System.out.print(input + " | ");
		}
		System.out.println(test.output + " |");
	}
}
