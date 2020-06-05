package components;


public abstract class Component {
	public double xPos, yPos;
	public String rotation; // options: "right", "left", "up", "down"
	public String id;
	public Component(double xPos, double yPos, String rotation) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
	}
}
