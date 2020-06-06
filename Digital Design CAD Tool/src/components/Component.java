package components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public abstract class Component {
	public double xPos, yPos;
	public int rotation; // options: "right = 0", "down = 1", "left = 2", "up = 3"
	public String id;
	public Image image;
	public boolean selected;
	public int width, height;
	public Component(double xPos, double yPos, int rotation) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
		this.selected = false;
	}
	
	public boolean insideBounds(double mouseX, double mouseY) {
		if(mouseX > xPos && mouseX < xPos + width && mouseY > yPos && mouseY < yPos + height) {
			selected = true;
		}
		
		return selected;
	}
	
	public void setX(double x) {
		xPos = x;
	}
	public void setY(double y) {
		yPos = y;
	}
	public double getX() {
		return xPos;
	}
	public double getY() {
		return yPos;
	}
	
	public void drawComponent(GraphicsContext gc) {
		switch(rotation) {
			case 0:
				gc.drawImage(image, (xPos - (xPos % 10)), (yPos - (yPos % 10)));
				break;
			case 1:
				gc.save();
				rotate(gc, 90, xPos, yPos);
				gc.drawImage(image, (xPos - (xPos % 10)), (yPos - (yPos % 10)) - height);
				gc.restore();
				break;
			case 2:
				gc.save();
				rotate(gc, 180, xPos, yPos);
				gc.drawImage(image, (xPos - (xPos % 10)) - width, (yPos - (yPos % 10)) - height);
				gc.restore();
				break;
			case 3:
				gc.save();
				rotate(gc, 270, xPos, yPos);
				gc.drawImage(image, (xPos - (xPos % 10)) - width, (yPos - (yPos % 10)));
				gc.restore();
				break;
			default:
				System.err.println("Something went wrong with rotating this component!");
		}
	}
	
	public void rotate() {
		if(rotation < 3) {
			rotation++;
		}else if(rotation >= 3) {
			rotation = 0;
		}
		
		xPos = xPos - (xPos % 10);
		yPos = yPos - (yPos % 10);
		
		System.out.println("new rotation: " + rotation);
	}
	
	private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
	
}
