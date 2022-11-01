import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
	public int[] pixels;
	private String locationOfTex;
	public final int SIZE;
	
	public static Texture wall = new Texture("res/wall.png", 256);
	public static Texture vent_wall = new Texture("res/vent_wall.png", 256);
	public static Texture wire = new Texture("res/wire.png", 256);
	public static Texture elew_door = new Texture("res/edoor.png", 256);
	public static Texture wall_inf = new Texture("res/wall_inf.png", 256);
	public static Texture door = new Texture("res/door.png", 256);
	public static Texture TreeDfx_wall = new Texture("res/3dfx_wall.png", 256);
	public static Texture p5_wall = new Texture("res/pentium_wall.png", 256);
	public static Texture p5closed_wall = new Texture("res/pentium_closed_wall.png", 256);
	
	public Texture(String location, int size) {
		locationOfTex = location;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}
	
	private void load() {
		try {
			BufferedImage image = ImageIO.read(new File(locationOfTex));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}