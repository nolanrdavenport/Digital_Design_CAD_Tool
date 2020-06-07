package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class NandGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public NandGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(xPos, yPos, rotation, canvas);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NAND";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/NAND/NAND_"+i+".png");
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
		
		outputs[0] = !outputs[0];
	}
}
