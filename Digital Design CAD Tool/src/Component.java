
public abstract class Component {
	public int xPos, yPos;
	public String rotation; // options: "right", "left", "up", "down"
	public Component(int xPos, int yPos, String rotation) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
	}	
}
