package components;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
	
	public void drawWire(GraphicsContext gc) {
		for(Line line : lines) {
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(2);
			
			gc.strokeLine(line.x1, line.y1 - 1, line.x2, line.y2 - 1);
		}
	}
}
