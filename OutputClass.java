package Sayajin;
import java.util.*;

/**
 * Represents the Fuzzy Sets associated to an output Fuzzy Variable
 * @author Francesco Giorgio & Andrea Toscano
 *
 */
public class OutputClass extends Class {

	/**
	 * The centroid is the point where the class has value 1.0, while fitness
	 * contains all the fit grades activated for the class
	 */
	private double Centroid = 0.0;
	private ArrayList <Double> fitness = new ArrayList <Double>();
	
	/**
	 * Constructor
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param n
	 */
	public OutputClass (double a, double b, double c, double d, String n) {
		
		super( a,  b,  c,  d,  n);
		this.Centroid = (c+b)/2;
	}
	
	/**
	 * Adds a fit grade whenever a rule activates the class
	 * @param fit
	 */
	public void setFitness(double fit) {
		
		this.fitness.add(fit);
	}
	
	/**
	 * @return The weighted sum of all fitness for the centroid value
	 */
	public double getWeigth() {
		
		double sum = 0;
		
		for (double fit : fitness) {
			
			sum += fit*Centroid;
			
		}
		
		return sum;
		
	}
	
	/**
	 * @return
	 */
	public double getCentroid() {
		
		return this.Centroid;
	}
	
}
