package ray.scene;

import ray.math.Point3;
import ray.math.Quadratic;
import ray.math.Ray;
import ray.math.Vector3;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
	
	private Point3 center = new Point3(0, 0, 0);
	public double radius = 1.0;
	public void setCenter(Point3 center) {
		this.center.set(center);
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public Sphere(Point3 c, double r) {
		this.center.set(c);
		this.radius = r;
	}
	
	public Sphere(double x, double y, double z, double r){
		this.center = new Point3(x, y, z);
		this.radius = r;
	}
	
	public boolean isIntersect(Ray rayIn){
		double a = rayIn.direction.lengthsquared();
		double b = 2 * rayIn.origin.dot(rayIn.direction) 
				- 2 * center.dot(rayIn.direction);
		double c = center.squareSum() 
				+ rayIn.origin.squareSum() 
				- 2 * center.dot(rayIn.origin) 
				- radius * radius;
		Quadratic quad = new Quadratic(a, b, c); //solve for the value of t
		if (quad.getStatus() && quad.getRootMin() > Ray.EPSILON){
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "sphere " + center + " " + radius + " end";
	}
	
	/**
	 * Part 2 added
	 */
	public Vector3 ka, kd, ks;
	@Override
	public Point3 getIntersect(Ray rayIn) {
		double a = rayIn.direction.lengthsquared();
		double b = 2 * rayIn.origin.dot(rayIn.direction) 
				- 2 * center.dot(rayIn.direction);
		double c = center.squareSum() 
				+ rayIn.origin.squareSum() 
				- 2 * center.dot(rayIn.origin) 
				- radius * radius;
		Quadratic quad = new Quadratic(a, b, c);
		if (quad.getStatus()){
			double t_min = quad.getRootMin();
			return rayIn.origin.getScaleAdd(t_min, rayIn.direction);
		}
		return null;
	}
	
	/**
	 * @param p on the surface of a sphere
	 * @return normal vector at this point
	 */
	public Vector3 getUnitNormal(Point3 p){
		double nx = (p.x - center.x)/radius;
		double ny = (p.y - center.y)/radius;
		double nz = (p.z - center.z)/radius;
		return new Vector3 (nx, ny, nz);
	}

}