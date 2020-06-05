package components;
import components.Component;
public class NorGate extends Component{
	public boolean[] inputs;
	public boolean[] outputs = new boolean[1];
	public int numInputs;
	public String id;
	public NorGate(double xPos, double yPos, String rotation, int numInputs) {
		super(xPos, yPos, rotation);
		inputs = new boolean[numInputs];
		this.numInputs = numInputs;
		this.id = "NOR";
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