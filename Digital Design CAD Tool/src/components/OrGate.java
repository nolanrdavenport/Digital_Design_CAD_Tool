package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class OrGate extends Component{
	public OrGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(60, 40, xPos, yPos, rotation, canvas, numInputs);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "OR";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/OR/OR_"+i+".png",120,120,true,false);
		}
		this.currImage = images[0];
	}
	public void setOutput(){
		output = false;
		for(boolean input : inputs) {
			if(input == true) { 
				output = true;
			}
		}
	}
}