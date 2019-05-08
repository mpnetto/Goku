package Sayajin;

/**
* Abstract class representing the Fuzzy Set in which a certain Fuzzy Variable can
* has value.
* The class includes the common features for both input and output Fuzzy Set.
* @author Francesco Giorgio & Andrea Toscano
*/

public abstract class Class {
	
	/**
	 * Each Fuzzy Set is represented as a trapezoid where "a" and "d" are the "foots"
	 * of the trapezoid and "b" and "c" are the points within which the variable
	 * assumes value 1.0
	 */
	double a;
	double b;
	double c;
	double d;
	private  String name;
	
	/**
	 * Constructor
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param n
	 */
	public Class(double a, double b, double c, double d, String n) {
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.name = n;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public double getA() {
		return a;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getB() {
		return b;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getC() {
		return c;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getD() {
		return d;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

//	public boolean isActivaded() {
//		if (fitness != 0) 
//			return true;
//		else 
//			return false;
//	}
	
}

