package Sayajin;

/**
 * MyClass - a class by (your name here)
 */
public class Vector
{

	public double x,y;
	
	Vector(double x, double y)
	{
	    this.x=x;
	    this.y=y;
		
	}
	
	public double length()
	{
	    return Math.sqrt(x*x+y*y);
	}

	public	void normalizar(){
	    double length = this.length();
	
	    if(length != 0){
	        this.x = this.x/length;
	        this.y = this.y/length;
	    }
	}
	
	void add(Vector v)
	{
	    this.x+=v.x;
	    this.y+=v.y;
	}
	
	void add(double i)
	{
	    this.x+=i;
	    this.y+=i;
	}
	
	void mult(double i)
	{
	    this.x*=i;
	    this.y*=i;
	}
	
	void div(double i)
	{
	    this.x/=i;
	    this.y/=i;
	}
	
	public double getAngle()
	{
		
		return Math.atan2(this.y,this.x);
	}
	
//	Vector get()
//	{
//	    vetor v(this.x, this.y);
//	    return v;
//	}
}
