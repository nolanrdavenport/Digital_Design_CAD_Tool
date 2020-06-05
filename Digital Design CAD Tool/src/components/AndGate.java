package components;
import components.Component;
public class AndGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public String id;
	public int numInputs;
	public AndGate(double xPos, double yPos, String rotation, int numInputs) {
		super(xPos, yPos, rotation);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "AND";
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
