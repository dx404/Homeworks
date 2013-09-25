package ray.math;

/**
 * For computation of the a x^2 + b x + c = 0 form of equations
 * for real numbers and read solutions.
 * @author Duo Zhao;
 *
 */
public class Quadratic {
	private Double a, b, c;
	
	private Double rootMax;
	private Double rootMin;
	private boolean rootStatus = false;
	
	public Quadratic (double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
		solveIt();
	}
	
	public boolean set (double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
		solveIt();
		return rootStatus;
	}
	
	public Double getRootMax(){
		return rootMax;
	}
	
	public Double getRootMin(){
		return rootMin;
	}
	
	public boolean getStatus(){
		return rootStatus;
	}
	
	private void solveIt(){
		double delta = b * b - 4 * a * c;
		if (a == 0 || delta < 0){
			this.rootStatus = false;
			this.rootMax = Double.NaN;
			this.rootMin = Double.NaN;
		}
		else{
			this.rootStatus = true;
			this.rootMax = (-b + Math.sqrt(delta))/(2*a);
			this.rootMin = (-b - Math.sqrt(delta))/(2*a);
		}
	}
}
