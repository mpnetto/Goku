package Sayajin;
/**
 * This class represnts the classes belonging to an input variable.
 * @author Francesco Giorgio & Andrea Toscano
 *
 */
public class InputClass extends Class{
	
	/**
	 * fitness is the belonging grade of the Fuzzy Variable to this set
	 */
	private  double fitness = 0.0;

	public InputClass( double a, double b, double c, double d, String n) {
		
		super( a,  b,  c,  d,  n);
		
	}
	
	/**
	 * This method calculates the fitness grade associated to this set, given
	 * a certain crisp value
	 * @param value The input variable crisp value
	 */
	public void setFitness(double value) {
		this.fitness = Math.max(Math.min(Math.min((value - a)/(b-a), 1), (d - value)/(d - c)), 0);
		System.out.println("fitness: " + fitness);
	}	

	/**
	 * 
	 * @return
	 */
	public double getFitness() {
		return fitness;
	}
}
