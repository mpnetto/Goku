package Sayajin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a typical FAM rule
 * @author Francesco Giorgio & Andrea Toscano
 *
 */

public class Rule {

	/**
	 * input represents activation condition, output the output variables classes
	 * to active.
	 * 
	 * In each map the key is the variable name, while the value is the activated
	 * class name
	 */
	private Map<String, String> in= new HashMap();
	private Map<String, String> out = new HashMap();
	private double fitnessActivated = 0.0;

	/**
	 * Constructor
	 * @param input
	 * @param output
	 */
	public Rule(Map input, Map output) {
		this.in = input;
		this.out = output;
	}
	
	/**
	 * Computes the fitness grade with which the rule is activated
	 * @param variables The input variables
	 * @param outputVariables The output variables
	 * @return 0.0 if the rule is not activated, a number between 0.0 and 1.0 else 
	 */
	public double applyRule(Map<String, FuzzyVariable> variables, Map<String, FuzzyVariable> outputVariables ) {
	
		double min = 2.0;
		
		//For each class specified in the input, find the minimum fitness grade
		for (String var : in.keySet()) {
			
		    FuzzyVariable temp = variables.get(var);
		    
		    InputClass tempClass = (InputClass)temp.getClasses().get(in.get(var));
		    double fit = tempClass.getFitness();
		    if (fit < min) {
		    	min = fit;
		    }		
		}
		
		fitnessActivated = min;
		System.out.println("regola attivata con grado: " + min);
		
		//Set the activation grade of each output class as the founded grade
		for (String outVar : out.keySet()) {
			
			FuzzyVariable outTemp = outputVariables.get(outVar);
			OutputClass outTempClass = (OutputClass)outTemp.getClasses().get(out.get(outVar));
			outTempClass.setFitness(min);
		}
		
		return fitnessActivated;
	}
	
	
	
}