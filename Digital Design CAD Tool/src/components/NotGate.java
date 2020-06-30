package components;
import components.Component;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
public class NotGate extends Component{
	public boolean[] inputs = new boolean[1];
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public NotGate(double xPos, double yPos, int rotation, Canvas canvas, int numInputs) {
		super(xPos, yPos, rotation, canvas);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NOR";
		for(int i = 0; i < this.images.length; i++) {
			images[i] = new Image("Images/NOT/NOT_"+i+".png",60,60,true,false);
		}
		this.currImage = images[0];
	}
	public void setOutput(){
		outputs[0] = !inputs[0];
	}
}