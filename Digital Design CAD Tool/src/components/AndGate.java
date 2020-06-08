package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class AndGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public AndGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(xPos, yPos, rotation, canvas);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "AND";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/AND/AND_"+i+".png", 120,120,true,false);
		}
		this.currImage = images[0];
	}
	public void setOutput(){
		outputs[0] = true;
		for(boolean input : inputs) {
			if(input == false) { 
				outputs[0] = false;
			}
		}
	}
	
}
