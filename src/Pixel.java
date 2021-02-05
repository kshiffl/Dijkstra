import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Pixel {
	public static final int PIXELSIZE = 20;
	private int xCoor, yCoor, cost;	
	private Pixel prev, next;
	
	private String color;
	
	private boolean start, end, wall;
	private boolean visited;
	
	public Pixel(int xCoor, int yCoor) {
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		prev = null;
		next = null;
		visited = false;
		cost = -1;
	}
	
	// Gets the x-coordinate of the pixel
	public int getXCoor() {	return xCoor; }
	
	// Gets the y-coordinate of the pixel
	public int getYCoor() { return yCoor; }
	
	public void setPrev(Pixel pixel) { prev = pixel; }
	public Pixel getPrev() { return prev; }
	
	// Next pixel
	public void setNext(Pixel pixel) { next = pixel; }
	public Pixel getNext() { return next; }
	
	// Cost
	public int getCost() { return cost; }
	public void setCost(int cost) { this.cost = cost; }
	
	// Color
	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }
	
	public boolean isStart() { return start; }
	public void setStart() { 
		start = true; 
		end = false;
		wall = false;
		cost = 0;
	}
	
	public boolean isEnd() { return end; }
	public void setEnd() { 
		end = true; 
		start = false;
		wall = false;
		cost = -1;
	}
	
	public boolean isWall() { return wall; }
	public void setWall() { 
		wall = true; 
		start = false;
		end = false;
	}

	public boolean visited() { return visited; }
	public void setVisited() { visited = true; }
	public void resetVisited() { visited = false; }
	
	// Changes the color of the pixel
	public void colorize(Graphics g, String color) {
		switch (color) {
		case "GREEN":
			g.setColor(Color.GREEN); // Start point
			break;
		case "RED":
			g.setColor(Color.RED); // End point
			break;
		case "WHITE":
			g.setColor(Color.WHITE); // Blank (untouched) point
			break;
		case "ORANGE":
			g.setColor(Color.ORANGE); // Point in current shortest path
			break;
		case "CYAN":
			g.setColor(Color.CYAN); // Processing (or processed) point
			break;
		default:
			g.setColor(Color.BLACK); // Error color
			break;
		}
		g.fillRect(xCoor*PIXELSIZE+1, yCoor*PIXELSIZE+1, PIXELSIZE-1, PIXELSIZE-1);
	}

}
