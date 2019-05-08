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
	private boolean memoryFlag;
	
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
		this.memoryFlag = true;
		this.Centroid = (c+b)/2;
		// SLIDE: ele pega o centro de cada trapezio pra fazer o calculo
	}

	public OutputClass (double a, double b, double c, double d, String n, boolean memory) {
		super( a,  b,  c,  d,  n);
		this.memoryFlag = memory;
		this.Centroid = (c+b)/2;
		// SLIDE: ele pega o centro de cada trapezio pra fazer o calculo
	}
	
	/**
	 * Adds a fit grade whenever a rule activates the class
	 * @param fit
	 */
	public void setFitness(double fit) {
		
		this.fitness.add(fit);
	}

	public void clearFitness() {
		fitness = new ArrayList <Double>();
	}
	
	/**
	 * @return The weighted sum of all fitness for the centroid value
	 */
	public double getWeigth() {
		
		double sum = 0;
		
		for (double fit : fitness) {
			
			sum += fit*Centroid;
			//System.out.println("sum: " + fit + " * " + Centroid + " = " + fit*Centroid);
		}

		//System.out.println("peso: " + sum + " - qtd: " + fitness.size());
		if (!memoryFlag) {
			clearFitness();
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
