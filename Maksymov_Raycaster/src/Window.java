import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Window extends JFrame implements Runnable{
	

	private Thread renderer_thread;
	private boolean running;
	private BufferedImage image;
	public int[] pixels;
	public ArrayList<Texture> textures;
	
	public final int projectionPlaneWidth = 640, projectionPlaneHeight = 480;

	
	public Viewer camera;
	public Screen screen;
	
	public short mapWidth = 16;
	public short mapHeight = 8;
	
	public static int[][] map;
	/*
	public static int[][] map = 
		{
			{1,3,9,8,9,3,1},
			{1,3,0,0,0,3,1},
			{1,7,0,0,0,7,1},
			{1,3,0,0,0,3,1},
			{1,3,5,0,5,3,1},
			{1,0,0,0,0,0,1},
			{1,8,1,6,1,8,1}
			   
		};
		
	*/
	
	public Window() {
		renderer_thread = new Thread(this);
		image = new BufferedImage(projectionPlaneWidth, projectionPlaneHeight, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		Map map = new Map(mapWidth, mapHeight);
		
		map.fillField();
		
		this.map = map.maze;
		
		//printMatrix(this.map);
		
		textures = new ArrayList<Texture>();
		textures.add(Texture.wall);
		textures.add(Texture.vent_wall);
		textures.add(Texture.wire);
		textures.add(Texture.elew_door);
		textures.add(Texture.wall_inf);
		textures.add(Texture.door);
		textures.add(Texture.TreeDfx_wall);
		textures.add(Texture.p5_wall);
		textures.add(Texture.p5closed_wall);
		
		camera = new Viewer(1.5f, 1.5f, 'E', 67);
		
		//camera = new Camera(1.5f, 1.5f, 1f, 0f, 0f, -0.66f);
		
		screen = new Screen(this.map, mapWidth, mapHeight, textures, projectionPlaneWidth, projectionPlaneHeight);
		addKeyListener(camera);
		setSize(projectionPlaneWidth, projectionPlaneHeight);
		setResizable(false);
		setTitle("Maksymov Raycaster testing...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}
	private synchronized void start() {
		running = true;
		renderer_thread.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			renderer_thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;// = 60 fps
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//оновлюємо логіку 60 разів за секунду
			{
				
				screen.update(camera, pixels);
				camera.update(map);
				delta--;
			}
			render();
		}
	}
	public static void main(String [] args) {
		Window game = new Window();
	}
	
	private static void printMatrix(int[][] matrix) {
		//double[][] matrix = paralelAdd.getResult();
		for (int column = 0; column < matrix[0].length; column++){
			for (int row = 0; row < matrix.length; row++){
				System.out.print((int)matrix[row][column] + " ");
			}
			System.out.println();
		}
		System.out.println(matrix[7][2]);
	}
}