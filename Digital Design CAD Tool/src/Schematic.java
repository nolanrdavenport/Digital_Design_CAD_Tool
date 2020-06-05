import java.util.ArrayList;

import components.*;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Schematic extends Canvas {
	public int width, height;
	public int scale = 1;
	public GraphicsContext gc;
	public TabPane tabPane;
	public Main main;

	private double mouseX;
	private double mouseY;
	
	public ArrayList<Component> components;
	
	public Schematic(Main main, TabPane tabPane, int width, int height) {
		super(width, height);
		components = new ArrayList<Component>();
		this.tabPane = tabPane;
		this.height = height;
		this.width = width;
		this.main = main;
		GraphicsContext gc = this.getGraphicsContext2D();
		this.gc = gc;

		gc.fillRect(0, 0, width, height);

		this.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && main.selectedItem != "NONE" && !main.selectedItem.contains("~")) {
				placeItem(main.selectedItem, event);
			}
		});

		tabPane.setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				mouseX = event.getScreenX();
				mouseY = event.getScreenY();
				main.scene.setCursor(Cursor.OPEN_HAND);
			}
		});

		this.setOnMouseDragged(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				middleMouseDragged(event);
			}
		});
		
		tabPane.setOnMouseReleased(event -> {
			if(event.getButton() == MouseButton.MIDDLE) {
				if(!main.selectedItem.contains("~") && main.selectedItem != "NONE") {
					main.scene.setCursor(Cursor.CROSSHAIR);
				}else {
					main.scene.setCursor(Cursor.DEFAULT);
				}
			}
		});
		createGridLines();

	}

	public void middleMouseDragged(MouseEvent event) {		
		double deltaX = event.getScreenX() - mouseX;
		double deltaY = event.getScreenY() - mouseY;
		// this.relocate(getLayoutX() + deltaX, getLayoutY() + deltaY);
		this.setTranslateX(this.getTranslateX() + deltaX);
		this.setTranslateY(this.getTranslateY() + deltaY);
		mouseX = event.getScreenX();
		mouseY = event.getScreenY();
	}
	
	public void createGridLines() {
        gc.setStroke(Color.rgb(50,50,50,0.5));
        gc.setLineWidth(1);
        for(int i = 10; i < height; i += 10) {
        	gc.strokeLine(0,i,width,i);
        }
        for(int i = 10; i < width; i += 10) {
        	gc.strokeLine(i,0,i,height);
        }
	}

	public void zoom(int direction) {
		double zoomFactor = 1.1;
		if (direction == 1) {
			zoomFactor = 2.0 - zoomFactor;
		}
		if((zoomFactor > 1 && this.getScaleY() < 2.5) || (zoomFactor < 1 && this.getScaleY() > 0.5)) {
			this.setScaleX(this.getScaleX() * zoomFactor);
			this.setScaleY(this.getScaleY() * zoomFactor);
		}
	}
	
	public void refresh() {
		gc.clearRect(0, 0, width, height);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
		createGridLines();
	}
	
	public void placeItem(String selectedItem, MouseEvent event) {
		switch(selectedItem) {
			case "AND":	
				components.add(new AndGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), "right", 2));
				break;
			case "OR":
				components.add(new OrGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), "right", 2));
				break;
			case "NAND":
				components.add(new NandGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), "right", 2));
				break;
			case "NOR":
				components.add(new NorGate((event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)), "right", 2));
				break;
			default :
				System.err.println("This is not a valid component ID");
		}
		
		System.out.println("Components:");
		for(Component comp : components) {
			System.out.println(comp + " " + comp.xPos + " " + comp.yPos);
		}
		
		Image image = new Image("Images/" + selectedItem + ".png", 60, 40, false, false);
		gc.drawImage(image, (event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)));
	}
}
