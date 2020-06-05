package components;

import java.util.ArrayList;

public class Wire {
	
	public ArrayList<Line> lines;
	
	public Wire() {
		lines = new ArrayList<Line>();
	}
	
	public void addLine(Line line) {
		lines.add(line);
	}
	
	public void removeLine(Line line) {
		lines.remove(line);
	}
	
	public ArrayList<Line> getLines(){
		return lines;
	}
}
