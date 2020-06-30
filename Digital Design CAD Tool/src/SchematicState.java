import java.util.ArrayList;

import components.Component;
import components.Wire;

public class SchematicState {
	private ArrayList<Component> components;
	private ArrayList<Wire> wires;
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}
	public ArrayList<Wire> getWires() {
		return wires;
	}
	public void setWires(ArrayList<Wire> wires) {
		this.wires = wires;
	}
	
	public SchematicState(ArrayList<Component> c, ArrayList<Wire> w) {
		components = c;
		wires = w;
	}
}
