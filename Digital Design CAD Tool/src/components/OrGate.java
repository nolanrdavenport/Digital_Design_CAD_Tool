package components;
import components.Component;
import javafx.scene.image.Image;
public class OrGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public OrGate(double xPos, double yPos, int rotation, int numInputs) {
		super(xPos, yPos, rotation);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "OR";
		this.image = new Image("Images/OR.png", 60, 40, false, false);
		this.width = 60;
		this.height = 40;
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