import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Schematic extends Canvas{
	public int length, width;
	public GraphicsContext gc;
	public Schematic(int length, int width) {
		super(length, width);
		this.length = length;
		this.width = width;
		GraphicsContext gc = this.getGraphicsContext2D();
		this.gc = gc;
		
		gc.fillRect(0,0,length,width);
		
		this.setOnMouseClicked(event -> {
			gc.setFill(Color.WHITE);
		      int tempx = (int)event.getX();
		      int tempy = (int)event.getY();
		      
		      gc.fillRect(tempx, tempy, 20, 20);
		      
		});
	}
	
	
}
