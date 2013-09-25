package ray.math;

public class Point3 extends Tuple3 {

	public Point3() {
		super(0, 0, 0);
	}

	public Point3(Point3 p) {
		super(p.x, p.y, p.z);
	}

	public Point3(double newX, double newY, double newZ) {
		super(newX, newY, newZ);
	}

	public double distance(Point3 other) {
		double dx = (this.x - other.x);
		double dy = (this.y - other.y);
		double dz = (this.z - other.z);
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public Point3 add(Vector3 vector) {
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;
		return this;
	}

	public Point3 scaleAdd(double scale, Vector3 vector) {
		this.x += scale * vector.x;
		this.y += scale * vector.y;
		this.z += scale * vector.z;
		return this;
	}
	
	public Point3 getScaleAdd(double scale, Vector3 vector) {
		return new Point3 (
				this.x + scale * vector.x,
				this.y + scale * vector.y,
				this.z + scale * vector.z
			);
	}
	
	public static Vector3 getVector3(Point3 start, Point3 end){
		return new Vector3 (end.x - start.x, end.y - start.y, end.z - start.z);
	}
	
	public Ray getRayTo(Point3 dest){
		Vector3 dir = Point3.getVector3(this, dest);
		return new Ray(this, dir);
	}
}

