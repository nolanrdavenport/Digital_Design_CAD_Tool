package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class OrGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public OrGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(xPos, yPos, rotation, canvas);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "OR";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/OR/OR_"+i+".png");
		}
		this.currImage = images[0];
	}
	public void setOutput(){
		outputs[0] = false;
		for(boolean input : inputs) {
			if(input == true) { 
				outputs[0] = true;
			}
		}
	}
}