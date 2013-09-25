package ray.scene;

import ray.math.Point3;

/**
 * For now just for white light
 */
public class Light {
	
	public Point3 position;
	public void setPosition(Point3 position) { this.position.set(position); }
	
	/** How bright the light is. */
	public double intensity;
	
	public Light(double x, double y, double z, double intensity) {
		this.position = new Point3 (x, y, z);
		this.intensity = intensity;
	}
	
}