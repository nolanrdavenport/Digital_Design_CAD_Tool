package components;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wire implements Cloneable{
	
	public ArrayList<Line> lines;
	
	public Component valueDeterminingComponent = null;
	public boolean value;
	public ArrayList<Component> connections;
	
	public Wire() {
		lines = new ArrayList<Line>();
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return (Wire)super.clone();  
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
			gc.setFill(Color.WHITE);
			gc.setLineWidth(2);
			
			gc.strokeLine(line.x1, line.y1 - 1, line.x2, line.y2 - 1);
			if(line.endingPointClosed) {
				gc.fillOval(line.x2 - 5, line.y2 - 6, 10, 10);
			}
		}
	}
	
	public void setValueDeterminingComponent(Component comp) {
		valueDeterminingComponent = comp;
	}
	
	public void setValue() {
		if(valueDeterminingComponent != null) {
			value = valueDeterminingComponent.output;
		}
	}
}
