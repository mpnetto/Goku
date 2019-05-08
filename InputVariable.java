package Sayajin;

import java.util.Map;

/**
 * This class represents the System input Fuzzy Variable
 * @author Francesco Giorgio & Andrea Toscano
 *
 */
public class InputVariable extends FuzzyVariable {
	
	/**
	 * This value is the crisp value of the respective crisp variable
	 */
	private double value;
	
	/**
	 * Constructor
	 * @param classes
	 * @param name
	 */
	public InputVariable (Map classes, String name) {
		
		super (classes, name);
		
	}
	
	/**
	 * 
	 * @param v
	 */
	public void setValue (double v) {
		
		this.value = v;
		computeFitness();
	}
	
	/**
	 * When a new value comes in input due to {@link #setValue(double)},
	 * this method calls, for each associated Fuzzy Set, the method that will
	 * calculate the fit grade for that Set
	 */
	private void computeFitness() {
		
		for (Class cl : classes.values()) {
			System.out.println("classe : " + cl.getName() + " della variabile " + getName());
			((InputClass)cl).setFitness(value);
			
		}
	}

}
