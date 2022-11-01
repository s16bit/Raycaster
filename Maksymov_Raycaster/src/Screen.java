import java.util.ArrayList;
import java.awt.Color;

public class Screen {
	public int[][] map;
	public int mapWidth, mapHeight, width, height;
	public ArrayList<Texture> textures;
	
	private int textureExp;
	
	public Screen(int[][] mp, int mapW, int mapH, ArrayList<Texture> txs, int w, int h) {
		map = mp;
		mapWidth = mapW;
		mapHeight = mapH;
		textures = txs;
		width = w;
		height = h;
		textureExp = log2 (textures.get(0).SIZE);
	}
	
	public int[] update(Viewer camera, int[] pixels) {
		for(int n=0; n < pixels.length >> 1 ; n++) {
			if(pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
		}
		for(int i=pixels.length >> 1; i<pixels.length; i++){
			if(pixels[i] != Color.gray.getRGB()) pixels[i] = Color.gray.getRGB();
		}
	    
	    for(int x=0; x<width; x=x+1) {
			float cameraX = (x << 1) / (float)(width) - 1;
		    float rayDirX = camera.xDir + camera.xPlane * cameraX;
		    float rayDirY = camera.yDir + camera.yPlane * cameraX;
		    int mapX = (int)camera.xPos;
		    int mapY = (int)camera.yPos;
		    //Довжина променя з поточної позиції до наступної x або y сторони
		    double sideDistX;
		    double sideDistY;
		    //Довжина променя з однієї сторони до іншої на карті
		    //double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
		    //double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
		    double deltaDistX = Math.abs(1 / rayDirX);
		    double deltaDistY = Math.abs(1 / rayDirY);
		    //debug("deltaDistY " + deltaDistY);
		    //double deltaDistX = 1/invSqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
		    //double deltaDistY = 1/invSqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
		    
		    double perpWallDist;

		    int stepX, stepY;
		    boolean hit = false;//зіткн  зі стіною
		    int side=0;// вертик хоризонт нп стіни
		    
		    if (rayDirX < 0)
		    {
		    	stepX = -1;
		    	sideDistX = (camera.xPos - mapX) * deltaDistX;
		    }
		    else
		    {
		    	stepX = 1;
		    	sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
		    }
		    
		    if (rayDirY < 0)
		    {
		    	stepY = -1;
		        sideDistY = (camera.yPos - mapY) * deltaDistY;
		    }
		    else
		    {
		    	stepY = 1;
		        sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
		        
		    }
		    //Де промінь перетинає стіну
		    while(!hit) {
		    	//наступна точка на карті
		    	if (sideDistX < sideDistY)
		        {
		    		sideDistX += deltaDistX;
		    		//sideDistX += 1/deltaDistX;
		    		mapX += stepX;
		    		side = 0;
		        }
		        else
		        {
		        	sideDistY += deltaDistY;
		        	//sideDistY += 1/deltaDistY;
		        	mapY += stepY;
		        	side = 1;
		        }
		    	
		    	//System.out.println(mapX + ", " + mapY + ", " + map[mapX][mapY]);
		    	if(map[mapX][mapY] > 0) hit = true;
		    }
		    //обчислюємо дистанцію зіткнення променя
		    if(side==0)
		    	perpWallDist = Math.abs((mapX - camera.xPos + ( (1 - stepX) >> 1) ) / rayDirX);
		    else
		    	perpWallDist = Math.abs((mapY - camera.yPos + ( (1 - stepY) >> 1) ) / rayDirY);	
		    //debug("yPos" + camera.yPos);
		    //Тепер обчислюємо висоту стіни знаючи дистанцію від камери
		    int lineHeight;
		    if(perpWallDist > 0) lineHeight = Math.abs((int)(height / perpWallDist));//////////////---
		    else lineHeight = height;
		    // стовпець = ниж и верхній піксель для заповнення стовпця
		    int drawStart = -(lineHeight >> 1) + (height >> 1);
		    if(drawStart < 0)
		    	drawStart = 0;
		    int drawEnd = (lineHeight >> 1) + (height >> 1);
		    if(drawEnd >= height) 
		    	drawEnd = height - 1;
		    //додаємо текстуру
		    int texNum = map[mapX][mapY] - 1;
		    double wallX;
		    if(side==1) {
		    	wallX = (camera.xPos + ((mapY - camera.yPos + ((1 - stepY) >> 1) ) / rayDirY) * rayDirX);
		    } else {
		    	wallX = (camera.yPos + ((mapX - camera.xPos + ((1 - stepX) >> 1) ) / rayDirX) * rayDirY);
		    }
		    
		    wallX-=Math.floor(wallX);
		    //x коорд на текстурі
		    int texX = (int)(wallX * (textures.get(texNum).SIZE));
	    	//System.out.println(texX);
		    if(side == 0 && rayDirX > 0) texX = textures.get(texNum).SIZE - texX - 1;
		    if(side == 1 && rayDirY < 0) texX = textures.get(texNum).SIZE - texX - 1;
		    //обчислюємо y коорд на текстурі
		    for(int y=drawStart; y<drawEnd; y++) {
		    	//int texY = (((y*2 - height + lineHeight) << 4) / lineHeight) / 2;
		    	int texY = ((( (y << 1) - height + lineHeight) << textureExp) / lineHeight) >> 1;
		    	int color;
		    	/*
		    	if(side==0) color = textures.get(texNum).pixels[texX + ( texY * textures.get(texNum).SIZE)];
		    	else color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)]>>1) & 8355711;
		    	*/
		    	if(side==0) color = textures.get(texNum).pixels[texX + ( texY << textureExp)];
		    	else color = (textures.get(texNum).pixels[texX + (texY << textureExp)]>>1) & 8355711;
		    	
		    	pixels[x + y * (width)] = color;
		    }
		}
		return pixels;
	}
	
	private void debug(String inf) {
		System.out.println(inf);
	}
	
	public int log2(int N)
    {
  
        // calculate log2 N indirectly
        // using log() method
        int result = (int)(Math.log(N) / Math.log(2));
  
        return result;
    }
	//John Carmak fast invSqrt
	public static float invSqrt(float x) {
	    float xhalf = 0.5f * x;
	    int i = Float.floatToIntBits(x);
	    i = 0x5f3759df - (i >> 1);
	    x = Float.intBitsToFloat(i);
	    x *= (1.5f - xhalf * x * x);
	    return x;
	}
	
}