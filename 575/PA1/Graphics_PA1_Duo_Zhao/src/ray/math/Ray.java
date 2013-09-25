package ray.math;

import java.util.ArrayList;

import ray.scene.Surface;

public class Ray {

	public static final double EPSILON = 1e-6;

	public final Point3 origin = new Point3();

	public final Vector3 direction = new Vector3();

	public double start, end;

	public Ray(Point3 newOrigin, Vector3 newDirection) {
		origin.set(newOrigin);
		direction.set(newDirection);
	}

	public void set(Point3 newOrigin, Vector3 newDirection) {
		origin.set(newOrigin);
		direction.set(newDirection);
	}

	public void evaluate(Point3 outPoint, double t) {

		outPoint.set(origin);
		outPoint.scaleAdd(t, direction);

	}
	
	public boolean isIntersectAny(ArrayList<Surface> sfs){
		for (Surface sf : sfs){
			if (sf.isIntersect(this)){
				return true;
			}
		}
		return false;
	}
	
}