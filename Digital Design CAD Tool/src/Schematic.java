import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Schematic extends Canvas {
	public int width, height;
	public int scale = 1;
	public GraphicsContext gc;
	public TabPane tabPane;
	public Main main;

	private double mouseX;
	private double mouseY;

	public Schematic(Main main, TabPane tabPane, int width, int height) {
		super(width, height);
		this.tabPane = tabPane;
		this.height = height;
		this.width = width;
		this.main = main;
		GraphicsContext gc = this.getGraphicsContext2D();
		this.gc = gc;

		gc.fillRect(0, 0, width, height);

		this.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				Image andImage = new Image("Images/" + main.selectedItem + ".png", 60, 40, false, false);
				gc.drawImage(andImage, (event.getX() - (event.getX() % 10)), (event.getY() - (event.getY() % 10)));
			}
		});

		tabPane.setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				mouseX = event.getX();
				mouseY = event.getY();
			}
		});

		tabPane.setOnMouseDragged(event -> {
			if (event.getButton() == MouseButton.MIDDLE) {
				middleMouseDragged(event);
			}
		});
		
		createGridLines();

	}

	public void middleMouseDragged(MouseEvent event) {		
		double deltaX = event.getX() - mouseX;
		double deltaY = event.getY() - mouseY;
		// this.relocate(getLayoutX() + deltaX, getLayoutY() + deltaY);
		this.setTranslateX(this.getTranslateX() + deltaX);
		this.setTranslateY(this.getTranslateY() + deltaY);
		mouseX = event.getX();
		mouseY = event.getY();
	}
	
	public void createGridLines() {
		gc.setFill(Color.GREEN);
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
		this.setScaleX(this.getScaleX() * zoomFactor);
		this.setScaleY(this.getScaleY() * zoomFactor);
	}
}
