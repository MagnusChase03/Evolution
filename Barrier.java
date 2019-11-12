package evolution;

public class Barrier {

	private int x;
	private int y;
	private int height;
	
	Barrier() {
		
		x = 1024;
		height = (int) (Math.random() * 51 + 10);
		y = 290 - height;
		
	}
	
	void update() {
		
		x -= 3;
		
	}

	int getX() {
		return x;
	}

	void setX(int x) {
		this.x = x;
	}

	int getY() {
		return y;
	}

	void setY(int y) {
		this.y = y;
	}

	int getHeight() {
		return height;
	}

	void setHeight(int height) {
		this.height = height;
	}
	
	
	
}
