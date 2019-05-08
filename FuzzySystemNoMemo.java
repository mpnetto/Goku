package Sayajin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class FuzzySystemNoMemo {
	
	final int  maxSpeed = 8;
	// getMaxDistance()

	double maxDistance;
	double den = 0.0;
	double crisp;
	
	FuzzyVariable distance;
	FuzzyVariable speed;
	OutputVariable firepower;
	Rule rule;
	LinkedList<Rule> rules;
	Map<String, FuzzyVariable> input;
	Map<String, FuzzyVariable> output;
	OutputVariable firePower;
	
	public FuzzySystemNoMemo(double w, double h) {
		
		setDistance( h,  w);

		Map<String, Class> distanceClasses = new HashMap<String, Class>();
		Class D_Baixa = new InputClass(0, 0, 0.3 * maxDistance, 0.45* maxDistance, "D_Baixa");
		Class D_Media = new InputClass(0.35*maxDistance, 0.55*maxDistance, 0.65*maxDistance, 0.75*maxDistance, "D_Media");
		Class D_Alta = new InputClass(0.65*maxDistance, 0.9*maxDistance, 5*maxDistance,  5*maxDistance, "D_Alta");
		
		distanceClasses.put(D_Baixa.getName(), D_Baixa); 
		distanceClasses.put(D_Media.getName(), D_Media); 
		distanceClasses.put(D_Alta.getName(), D_Alta);

		distance = new InputVariable(distanceClasses, "distance");

		Map<String, Class> speedClasses = new HashMap<String, Class>();
		Class V_Baixa = new InputClass(-0.01, 0.00*maxSpeed, 0.2*maxSpeed, 0.30*maxSpeed, "V_Baixa");
		Class V_MedioBaixa = new InputClass(0.21*maxSpeed, 0.31*maxSpeed, 0.4*maxSpeed, 0.6*maxSpeed, "V_MedioBaixa");
		Class V_Media = new InputClass(0.41*maxSpeed, 0.61*maxSpeed, 0.7*maxSpeed, 0.8*maxSpeed, "V_Media");
		Class V_Alta = new InputClass(0.71*maxSpeed, Math.abs(0.81*maxSpeed), 1.01*maxSpeed, 1.01*maxSpeed, "V_Alta");

		speedClasses.put(V_Baixa.getName(), V_Baixa);
		speedClasses.put(V_MedioBaixa.getName(), V_MedioBaixa);
		speedClasses.put(V_Media.getName(), V_Media);
		speedClasses.put(V_Alta.getName(), V_Alta);

		speed = new InputVariable(speedClasses, "speed");

		Map<String, Class> firePowerClasses = new HashMap<String, Class>();
		Class FBaixa = new OutputClass(0.1, 0.8, 1, 1.5, "F_Baixa", false);
		Class FMedioBaixa = new OutputClass(1.2, 1.5, 1.5, 2, "F_MedioBaixa", false); 
		Class FMedia = new OutputClass(1.7, 2, 2, 2.2, "F_Media", false);
		Class FAlta = new OutputClass(2, 2.2, 5, 5, "F_Alta", false);

		firePowerClasses.put(FBaixa.getName(), FBaixa);
		firePowerClasses.put(FMedioBaixa.getName(), FMedioBaixa);
		firePowerClasses.put(FMedia.getName(), FMedia);
		firePowerClasses.put(FAlta.getName(), FAlta);	

		firePower = new OutputVariable(firePowerClasses, "firePower");

		rules = new LinkedList<Rule>();

		// IF distancia IS baixa THEN firePower IS alta
		Map<String, String> inputHashmap = new HashMap<String, String>();
		Map<String, String> outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Baixa");
		outputHashmap.put("firePower", "F_Alta");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);


		// IF distancia IS media AND speed IS baixa THEN firePower IS alta
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_Baixa");
		outputHashmap.put("firePower", "F_Alta");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);


		// IF distancia IS media AND speed IS medioBaixa THEN firePower IS media
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_MedioBaixa");
		outputHashmap.put("firePower", "F_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		// IF distancia IS media AND speed IS media THEN firePower IS medioBaixa
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_Media");
		outputHashmap.put("firePower", "F_MedioBaixa");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		// IF distancia IS media AND speed IS alta THEN firePower IS medioBaixa
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_Alta");
		outputHashmap.put("firePower", "F_MedioBaixa");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		// IF distancia IS alta AND speed IS baixa THEN firePower IS medioBaixa
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_Baixa");
		outputHashmap.put("firePower", "F_MedioBaixa");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		// IF distancia IS alta AND speed IS medioBaixa THEN firePower IS medioBaixa
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_MedioBaixa");
		outputHashmap.put("firePower", "F_MedioBaixa");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		// IF distancia IS alta AND speed IS media THEN firePower IS baixa
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_Media");
		outputHashmap.put("firePower", "F_Baixa");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		// IF distancia IS alta AND speed IS alta THEN firePower IS baixa
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_Alta");
		outputHashmap.put("firePower", "F_Baixa");
	
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		

		input = new HashMap<String, FuzzyVariable>();
		input.put(distance.getName(), distance);
		input.put(speed.getName(), speed);
		
		output = new HashMap<String, FuzzyVariable>();
		output.put(firePower.getName(), firePower);
	
	}

	public double setFire(double dist, double sp) {
		boolean flag = false; // dist < 200;

		((InputVariable)distance).setValue(dist);
		((InputVariable)speed).setValue(Math.abs(sp));

		if (flag) {
			System.out.println("Dist:" + dist);		
			System.out.println("Sp:" + Math.abs(sp));		
			System.out.println("Regras:");
		}
		
		den = 0;
		for (Rule r : rules) {
			double increment = r.applyRule(input, output);
			den += increment;
			if (flag) {
				System.out.println("Valor: " + increment);
			}
		}
		
		crisp = firePower.getOutput(den);
		if (flag) {
			System.out.println("Den: " + den);
			System.out.println("Crisp: " + crisp);
		}
		//System.out.println("d1stancia maxima: " + maxDistance);
		//System.out.println("distancia do inimigo: " + dist);
		//System.out.println("velocidade do inimigo: " + sp);
		//System.out.println("valor do disparo: " + crisp);
		return crisp;
	}

	public void setDistance(double h, double w) {
		
		this.maxDistance = Math.sqrt(w*w + h*h)/2;
	}
	
	public double getDistance() {
		
		return this.maxDistance;
	}
}
