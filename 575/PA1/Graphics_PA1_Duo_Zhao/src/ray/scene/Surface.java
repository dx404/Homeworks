package ray.scene;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;

public abstract class Surface {

	public abstract boolean isIntersect(Ray ray);
	
	public abstract Point3 getIntersect(Ray rayIn);
	
	public abstract Vector3 getUnitNormal(Point3 p);

	/**
	 * part 2 added
	 */
	public Vector3 ka, kd, ks;
	public double sp; //specular power
}
