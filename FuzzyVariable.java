package Sayajin;

import java.util.*;

/**
 * Abstract class including common features of both input and output variables
 * in a Fuzzy System
 * @author Francesco Giorgio & Andrea Toscano 
 * 
 */
public abstract class FuzzyVariable {

	/**
	 * @classes A map where the objects are the sets whom the variable can belong
	 * and where the keys are the names of the sets
	 */
	protected Map<String, Class> classes = null;
	protected String name;

	/**
	 * Constructor
	 * @param classes
	 * @param name
	 */
	public FuzzyVariable(Map classes, String name) {
		this.classes = classes;
		this.name = name;
		
	}
	
	/**
	 * Return the map of the classes associated to the variable
	 * @return map of the classes associated to the variable
	 */
	public Map getClasses() {
		return classes;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

}
