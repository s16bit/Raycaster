import java.io.*;

public class Map {

	private static short WIDTH, HEIGHT;

	private static final char
			HOLLOW = '.',
			WALL = '#',
			VENT = '+',
			WIRE = '-',
			EDOOR = 'x',
			WALLINF = 'i',
			DOOR = '|',
			c3DFX = '^',
			P5 = 'c',
			P5C = 'p';
			
	//public static char maze[][] = new char[ROWS][COLUMNS];
	public static int maze[][];

	public Map(short width, short height) {
		// for(byte i = 2; i < COLUMNS - 2; i++){
		// field[2][i] = '#';
		// }
		WIDTH = width;
		HEIGHT = height;
		maze = new int[WIDTH][HEIGHT];
	}

	public void fillField() {
		/*
		 * for(short row = 0; row < ROWS; row++){ for(short column = 0; column <
		 * COLUMNS; column++){ field[row][column] = ' '; } }
		 */
		try {
			// File file = new File("Map.txt");
			InputStream is = getClass().getResourceAsStream("maps/map.txt");
			// System.out.println(file.getParent());
			// System.out.println(is.);
			
			//System.out.println("inf: ");
			// BufferedReader map = new BufferedReader( new FileReader(file));

			BufferedReader map = new BufferedReader(new InputStreamReader(is));

			// File file = new File(ref);

			// BufferedReader map = new BufferedReader( new FileReader(file));

			int c = ' ';

			for (short y = 0; y < HEIGHT; y++) {
				for (short x = 0; x < WIDTH; x++) {

					c = map.read();
					/*
					 * if( (c == '\n') || (c == '\r') || (c == ' ')) { column--;
					 * continue; }
					 */
					/*
					switch (c) {
					case '\n':
					case '\r':
					case ' ':
						column--;
						continue;
					}
					*/
					// if(c != -1)
					//System.out.println("x:" + x + " y:" + y);
					switch (c) {
					case '\n':
					case '\r':
					case ' ':
						x--;
						continue;
					case HOLLOW:
						maze[x][y] = 0;
						break;
					case WALL:
						//System.out.println("x:" + row + " y:" + column);
						maze[x][y] = 1;
						break;
					case VENT:
						maze[x][y] = 2;
						break;
					case WIRE:
						maze[x][y] = 3;
						break;
					case EDOOR:
						maze[x][y] = 4;
						break;
					case WALLINF:
						maze[x][y] = 5;
						break;
					case DOOR:
						maze[x][y] = 6;
						break;
					case c3DFX:
						maze[x][y] = 7;
						break;
					case P5:
						maze[x][y] = 8;
						break;
					case P5C:
						maze[x][y] = 9;
						break;
					default:
						maze[x][y] = 0;
					}
					
					//maze[row][column] = (char) c;

				}
			}

			map.close();

		}

		catch (FileNotFoundException e) {
			System.out.println("!exist");
		}

		catch (IOException e) {
			System.out.println("!!!!");
		}

	}

}