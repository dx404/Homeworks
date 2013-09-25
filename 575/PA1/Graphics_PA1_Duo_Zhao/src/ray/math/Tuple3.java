package ray.math;

public class Tuple3 {
	
	public double x, y, z;
	
	public Tuple3() {
		this(0, 0, 0);
	}
	
	public Tuple3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Tuple3 (Tuple3 t) {
		this (t.x, t.y, t.z);
	}

	public Tuple3 set (Tuple3 t) {
		this.x = t.x;
		this.y = t.y;
		this.z = t.z;
		return this;
	}
	
	public Tuple3 set (double inX, double inY, double inZ) {
		this.x = inX;
		this.y = inY;
		this.z = inZ;
		return this;
	}
	
	public Tuple3 scale (double factor) {
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
		return this;
	}
	
	public Tuple3 add (Tuple3 t) {
		this.x += t.x;
		this.y += t.y;
		this.z += t.z;
		return this;
	}
	
	public Tuple3 scaleAdd(double scale, Tuple3 t) {
		this.x += scale * t.x;
		this.y += scale * t.y;
		this.z += scale * t.z;
		return this;
	}

	public double dot (Tuple3 t) {
		return this.x * t.x + this.y * t.y + this.z * t.z;
	}
	
	public Tuple3 thruProduct(Tuple3 t){
		this.x *= t.x;
		this.y *= t.y;
		this.z *= t.z;
		return this;
	}

	public double squareSum(){
		return x * x + y * y + z * z;
	}

	public String toString() {
		return "[" + x + "," + y + "," + z+"]";
	}
}

