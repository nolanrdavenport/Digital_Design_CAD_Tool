package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class NandGate extends Component{
	public NandGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(60, 40, xPos, yPos, rotation, canvas, numInputs);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NAND";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/NAND/NAND_"+i+".png",120,120,true,false);
		}
		this.currImage = images[0];
	}
	public void setOutput(){
		output = true;
		for(boolean input : inputs) {
			if(input == false) { 
				output = false;
			}
		}
		
		output = !output;
	}
}
