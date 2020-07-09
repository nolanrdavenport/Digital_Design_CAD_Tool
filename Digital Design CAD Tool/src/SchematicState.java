/*
 * Stores information for the state of the schematic, such as componet and wire information.
 */
import components.Component;
import components.Wire;

import java.util.ArrayList;
public class SchematicState {
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
