package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class NorGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public NorGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(xPos, yPos, rotation, canvas);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NOR";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/NOR/NOR_"+i+".png",120,120,true,false);
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
		
		outputs[0] = !outputs[0];
	}
}