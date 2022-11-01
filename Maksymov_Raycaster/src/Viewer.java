import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Viewer implements KeyListener {
	public int fieldOfView;
	public float xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean turnLeft, turnRight, forward, back, strafeLeft, strafeRight;
	public final float MoveSpeed = 0.07f;
	public final float RotationSpeed = 0.04f;
	
	public Viewer(float x, float y, char direction, int fov) {
		fieldOfView = fov;
		
		xPos = x;
		yPos = y;
		
		switch(direction) {
		case 'N':
		case 'n':
			xDir = 0;
			yDir = -1;
			
			xPlane = (float)Math.tan( degreeToRad(-fov / 2.0) );
			yPlane = 0;
			break;
		case 'S':
		case 's':
			xDir = 0;
			yDir = 1;
			
			xPlane = (float)Math.tan( degreeToRad(fov / 2.0) );
			yPlane = 0;
			break;
		case 'W':
		case 'w':
			xDir = -1;
			yDir = 0;
			
			xPlane = 0;
			yPlane = (float)Math.tan( degreeToRad(fov / 2.0) );
			break;
		case 'E':
		case 'e':
			xDir = 1;
			yDir = 0;
			
			xPlane = 0;
			yPlane = (float)Math.tan( degreeToRad(-fov / 2.0) );
		}
		
	}
	/*
	public Camera(float x, float y, float xd, float yd, float xp, float yp) {
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
	}
	*/
	private float RadToDegree(float rad) {
		
		return rad * 180 / (float)Math.PI;
	}
	
	private double degreeToRad(double degree) {
		
		return degree * Math.PI / 180;
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		
		switch(key.getKeyCode()) {
		
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			forward = true;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			back = true;
			break;
		case KeyEvent.VK_A:
			strafeLeft = true;
			break;
		case KeyEvent.VK_D:
			strafeRight = true;
			break;
		case KeyEvent.VK_LEFT:
			turnLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			turnRight = true;
		}
		
		
	}
	
	@Override
	public void keyReleased(KeyEvent key) {
		
		switch(key.getKeyCode()) {
		
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			forward = false;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			back = false;
			break;
		case KeyEvent.VK_A:
			strafeLeft = false;
			break;
		case KeyEvent.VK_D:
			strafeRight = false;
			break;
		case KeyEvent.VK_LEFT:
			turnLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			turnRight = false;
		}
		
	}
	public void update(int[][] map) {
		if(forward) {
			if(map[(int)(xPos + xDir * MoveSpeed)][(int)yPos] == 0) {
				xPos+=xDir*MoveSpeed;
			}
			if(map[(int)xPos][(int)(yPos + yDir * MoveSpeed)] == 0)
				yPos+=yDir*MoveSpeed;
		}
		if(back) {
			if(map[(int)(xPos - xDir * MoveSpeed)][(int)yPos] == 0)
				xPos-=xDir*MoveSpeed;
			if(map[(int)xPos][(int)(yPos - yDir * MoveSpeed)] == 0)
				yPos-=yDir*MoveSpeed;
		}
		
		if(strafeLeft) {
			if(map[(int)(xPos - yDir * MoveSpeed)][(int)yPos] == 0)
				xPos+=-yDir*MoveSpeed;
			if(map[(int)xPos][(int)(yPos + xDir * MoveSpeed)] == 0)
				yPos+=xDir*MoveSpeed;
		}
		
		if(strafeRight) {
			if(map[(int)(xPos + yDir * MoveSpeed)][(int)yPos] == 0)
				xPos+=yDir*MoveSpeed;
			if(map[(int)xPos][(int)(yPos - xDir * MoveSpeed)] == 0)
				yPos+=-xDir*MoveSpeed;
		}
		
		if(turnRight) {
			
			float oldxDir=xDir;
			xDir=xDir*(float)Math.cos(-RotationSpeed) - yDir*(float)Math.sin(-RotationSpeed);
			yDir=oldxDir*(float)Math.sin(-RotationSpeed) + yDir*(float)Math.cos(-RotationSpeed);
			float oldxPlane = xPlane;
			xPlane=xPlane*(float)Math.cos(-RotationSpeed) - yPlane*(float)Math.sin(-RotationSpeed);
			yPlane=oldxPlane*(float)Math.sin(-RotationSpeed) + yPlane*(float)Math.cos(-RotationSpeed);
			
		}
		if(turnLeft) {
			
			float oldxDir=xDir;
			xDir=xDir*(float)Math.cos(RotationSpeed) - yDir*(float)Math.sin(RotationSpeed);
			yDir=oldxDir*(float)Math.sin(RotationSpeed) + yDir*(float)Math.cos(RotationSpeed);
			float oldxPlane = xPlane;
			xPlane=xPlane*(float)Math.cos(RotationSpeed) - yPlane*(float)Math.sin(RotationSpeed);
			yPlane=oldxPlane*(float)Math.sin(RotationSpeed) + yPlane*(float)Math.cos(RotationSpeed);
			
		}
		//System.out.println(xDir + "; " + yDir);
	}
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}