import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import javafx.util.Pair;

public class GamePanel extends JPanel implements Runnable, MouseInputListener{
	
	public static final int PIXELWIDTH = 1001, PIXELHEIGHT = 501; // Pixel size of game board
	public static final int WIDTH = 50, HEIGHT = 25; // Total size of game board (in graph)
	public static final int PIXELSIZE = 20; // Total size of a pixel (in pixels)
	
	private int totalUnvisited;
	private Thread thread;
	
	public boolean running;
	
	volatile private boolean isRunning = false;
	
	public boolean pickedStartPixel = false, pickedEndPixel = false; // Whether the user has picked the first and last pixel
	
	public Pixel[][] graph = new Pixel[WIDTH][HEIGHT]; // Represents the full graph
	Pixel start;
	Pixel end;
	
	/**
	 * 
	 */
	public GamePanel() {
		setFocusable(true);
		setPreferredSize(new Dimension(PIXELWIDTH, PIXELHEIGHT));
		addMouseListener(this);
		
		JButton button = new JButton("Sign In");   
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		this.add(button);
	    
		totalUnvisited = WIDTH*HEIGHT;
		start();
	}
	
	public void start() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Pixel pixel = new Pixel(x,y);
				pixel.setColor("WHITE");
				graph[x][y] = pixel;
			}
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param g
	 */
	public void paint(Graphics g) {
		// Graph lines
		g.clearRect(0, 0, PIXELWIDTH, PIXELHEIGHT);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, PIXELWIDTH, PIXELHEIGHT);

		// Filling all pixels with default white
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Pixel pixel = graph[x][y];
				pixel.colorize(g, pixel.getColor());
			}
		}
	}
	
	public void run() {
		while (running) {
			step();
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!pickedStartPixel) {
			Pixel pixel = graph[e.getX() / PIXELSIZE][e.getY() / PIXELSIZE];
			start = pixel;
			pixel.setStart();
			pixel.setColor("GREEN");
			graph[e.getX() / PIXELSIZE][e.getY() / PIXELSIZE] = pixel;
			pickedStartPixel = true;
			totalUnvisited--;
		}
		else if (!pickedEndPixel) {
			Pixel pixel = graph[e.getX() / PIXELSIZE][e.getY() / PIXELSIZE];
			end = pixel;
			pixel.setEnd();
			pixel.setColor("RED");
			graph[e.getX() / PIXELSIZE][e.getY() / PIXELSIZE] = pixel;
			pickedEndPixel = true;
			totalUnvisited--;
		}
		else {
			Pixel pixel = graph[e.getX() / PIXELSIZE][e.getY() / PIXELSIZE];
			if (!pixel.isStart() && !pixel.isEnd()) {
				pixel.setWall();
				pixel.setColor("BLACK");
				graph[e.getX() / PIXELSIZE][e.getY() / PIXELSIZE] = pixel;
				totalUnvisited--;
			}
		}
		
	}

	private void visitNeighbor(Pixel neighbor, Pixel currPixel) {
		// If neighbor is a wall
		if (neighbor.isWall()) return;

		// If not visited yet
		if (neighbor.getCost() < 0) {
			neighbor.setCost(currPixel.getCost()+1);
			neighbor.setPrev(currPixel);
			if (neighbor != end) { 
				//neighbor.setColor("CYAN");
				totalUnvisited--;
			}
		}
		// If the neighbor's cost is higher than our current cost (we found a better path)
		else if (neighbor.getCost() > currPixel.getCost()+1) {
			neighbor.setCost(currPixel.getCost()+1);
			neighbor.setPrev(currPixel);
		}
	}

	private void visitNeighbors(Pixel currPixel) {
		if (currPixel.visited() || currPixel.isWall()) return; 
		currPixel.setVisited();
		int x = currPixel.getXCoor();
		int y = currPixel.getYCoor();

		// Visiting surrounding pixels
		checkNeighbor(currPixel, x-1, y);
		checkNeighbor(currPixel, x+1, y);
		checkNeighbor(currPixel, x, y-1);
		checkNeighbor(currPixel, x, y+1);
		
		if (x-1 >= 0) visitNeighbors(graph[x-1][y]); // Left
		if (y+1 < HEIGHT) visitNeighbors(graph[x][y+1]); // Up
		if (y-1 >= 0) visitNeighbors(graph[x][y-1]); // Down
		if (x+1 < WIDTH) visitNeighbors(graph[x+1][y]); // Right

		if (totalUnvisited <= 0) finish();	
	}

	private void checkNeighbor(Pixel currPixel, int x, int y) {
		// Making sure we're not out of the dimensions
		if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT)
                        visitNeighbor(graph[x][y], currPixel);
	}
	private void finish() {
		Pixel curr = end;
		while ( curr != start ) {
			if (curr != end) curr.setColor("ORANGE");
			curr = curr.getPrev();
		}
	}

	public void Dijkstra() {
		if (!pickedStartPixel || !pickedEndPixel) {
			System.out.println("Did not pick start/end yet\n");
			return;
		}
		visitNeighbors(start);	
	}
}
