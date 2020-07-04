import components.Component;
import components.Wire;

import java.util.ArrayList;
public class SchematicState {
	public ArrayList<Component> components;
	public ArrayList<Wire> wires;
	
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
	
	public SchematicState() {
		components = new ArrayList<Component>();
		wires = new ArrayList<Wire>();
	}
	
	public void addComponent(Component comp) {
		components.add(comp);
	}
	
	public void addWire(Wire wire) {
		wires.add(wire);
	}
}
