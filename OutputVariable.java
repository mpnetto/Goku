package Sayajin;

import java.util.Map;

/**
 * This class represents the output Fuzzy Variable of a Fuzzy System
 * @author Francesco Giorgio & Andrea Toscano
 *
 */
public class OutputVariable extends FuzzyVariable {

	/**
	 * Construct
	 * @param classes
	 * @param name
	 */
	public OutputVariable( Map classes, String name) {
		
       super(classes, name);
		
	}
	
	/**
	 * Returns the Crisp Value obtained with the "Centroid Method"
	 * @param den The sum of all the fit grade activated for the variable
	 * @return Crisp Value
	 */
	public double getOutput(double den) {
		
		double sum = 0;
		
		for(Class cl : classes.values()) {
			
			sum += ((OutputClass)cl).getWeigth();
			
		}
		
		return sum / den;
	}

}

