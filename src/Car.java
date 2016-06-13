
public class Car {
	
	int x, y, size;
	String orientation;
	
	public Car(int x, int y, String orientation, int size) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
		this.orientation = orientation;
	}
	
	public void moveDown() {
		this.x ++;
	}

	public void moveUp() {
		this.x --;
	}

	public void moveRight() {
		this.y ++;
	}

	public void moveLeft() {
		this.y --;
	}

	public boolean isHorizontal() {
		return orientation.equals("h");
	}

	public boolean isVertical() {
		return orientation.equals("v");
	}
	
	public Car clone(){
		return new Car(this.x, this.y, this.orientation, this.size);
	}
}
