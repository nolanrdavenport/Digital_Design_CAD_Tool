package components;
import components.Component;
public class OrGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public String id;
	public OrGate(double xPos, double yPos, String rotation, int numInputs) {
		super(xPos, yPos, rotation);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "OR";
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