package components;
import components.Component;
import javafx.scene.image.Image;
public class NandGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public NandGate(double xPos, double yPos, int rotation, int numInputs) {
		super(xPos, yPos, rotation);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NAND";
		this.image = new Image("Images/NAND.png", 60, 40, false, false);
		this.width = 60;
		this.height = 40;
	}
	public void setOutput(){
		outputs[0] = true;
		for(boolean input : inputs) {
			if(input == false) { 
				outputs[0] = false;
			}
		}
		
		outputs[0] = !outputs[0];
	}
}
