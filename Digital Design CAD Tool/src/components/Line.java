/*
 * Holds information for a single line. 
 */
package components;

public class Line {
	// beginning point
	public double x1;
	public double y1;
	public boolean beginningPointClosed;
	
	// ending point
	public double x2;
	public double y2;
	public boolean endingPointClosed;
	
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.beginningPointClosed = beginningPointClosed;
		this.endingPointClosed = endingPointClosed;
	}
}
