package ray.math;

import ray.scene.Surface;

public class RayTouchInfo {
	public Point3 point = new Point3();
	public Surface surface = null;
	public double t = 0;

	public RayTouchInfo (Point3 p, Surface s, double t){
		this.point = p;
		this.surface = s;
		this.t = t;
	}
	
	public RayTouchInfo (){
		this.point = new Point3();
		this.surface = null;
		this.t = 0;
	}

}
