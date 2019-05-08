package Sayajin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class FuzzySystem {
	
	final int  maxSpeed = 8;
	// getMaxDistance()

	double maxDistance;
	double den = 0.0;
	double crisp;
	
	FuzzyVariable distance;
	FuzzyVariable speed;
	OutputVariable guessFactor;
	OutputVariable firepower;
	Rule rule;
	LinkedList<Rule> rules;
	Map<String, FuzzyVariable> input;
	Map<String, FuzzyVariable> output;
	
	
	public FuzzySystem(double w, double h) {
		
		setDistance( h,  w);
		
		Map<String, Class> distanceClasses = new HashMap<String, Class>();
		Class D_Baixa = new InputClass(0.00*maxDistance, 0.00*maxDistance, 0.1 * maxDistance, 0.25* maxDistance, "D_Baixa");
		Class D_MediaBaixa = new InputClass(0.18*maxDistance, 0.28*maxDistance,0.35*maxDistance,0.4*maxDistance, "D_MediaBaixa");
		Class D_Media = new InputClass(0.36*maxDistance, 0.45*maxDistance, 0.51*maxDistance, 0.60*maxDistance, "D_Media");
		Class D_Alta = new InputClass(0.61*maxDistance, 0.81*maxDistance, 1*maxDistance,  1.01*maxDistance, "D_Alta");
		
		distanceClasses.put(D_Baixa.getName(), D_Baixa); 
		distanceClasses.put(D_MediaBaixa.getName(), D_MediaBaixa);
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

		Map<String, Class> guessFactorClass = new HashMap<String, Class>();
		Class G_Baixa = new OutputClass(1, 2, 3, 4, "G_Baixa");
		Class G_MedioBaixa = new OutputClass(3, 5, 7, 9, "G_MedioBaixa"); 
		Class G_Media = new OutputClass(7, 8, 10, 11, "G_Media");
		Class G_Alta = new OutputClass(12, 13, 15, 16, "G_Alta");

		guessFactorClass.put(G_Baixa.getName(), G_Baixa);
		guessFactorClass.put(G_MedioBaixa.getName(), G_MedioBaixa);
		guessFactorClass.put(G_Media.getName(), G_Media);
		guessFactorClass.put(G_Alta.getName(), G_Alta);	

		guessFactor = new OutputVariable(guessFactorClass, "guessFactor");

		rules = new LinkedList<Rule>();

		Map<String, String> inputHashmap = new HashMap<String, String>();
		Map<String, String> outputHashmap = new HashMap<String, String>();

		inputHashmap.put("distance", "D_Baixa");
		inputHashmap.put("speed", "V_Baixa");
		outputHashmap.put("guessFactor", "G_Baixa");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);

		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Baixa");
		inputHashmap.put("speed", "V_MedioBaixa");
		outputHashmap.put("guessFactor", "G_MedioBaixa");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);

		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Baixa");
		inputHashmap.put("speed", "V_Media");
		outputHashmap.put("guessFactor", "G_MedioBaixa");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);

		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Baixa");
		inputHashmap.put("speed", "V_Alta");
		outputHashmap.put("guessFactor", "G_Media");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);

		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_MediaBaixa");
		inputHashmap.put("speed", "V_Baixa");
		outputHashmap.put("guessFactor", "G_MedioBaixa");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);

		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_MediaBaixa");
		inputHashmap.put("speed", "V_MedioBaixa");
		outputHashmap.put("guessFactor", "G_MedioBaixa");

		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);

		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_MediaBaixa");
		inputHashmap.put("speed", "V_Media");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_MediaBaixa");
		inputHashmap.put("speed", "V_Alta");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_Baixa");
		outputHashmap.put("guessFactor", "G_MedioBaixa");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_MedioBaixa");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_Media");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Media");
		inputHashmap.put("speed", "V_Alta");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_Baixa");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_MedioBaixa");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_Media");
		outputHashmap.put("guessFactor", "G_Media");
		
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		inputHashmap = new HashMap<String, String>();
		outputHashmap = new HashMap<String, String>();
		inputHashmap.put("distance", "D_Alta");
		inputHashmap.put("speed", "V_Alta");
		outputHashmap.put("guessFactor", "G_Alta");
	
		rule = new Rule(inputHashmap, outputHashmap);
		rules.add(rule);
		
		input = new HashMap<String, FuzzyVariable>();
		input.put(distance.getName(), distance);
		input.put(speed.getName(), speed);
		
		output = new HashMap<String, FuzzyVariable>();
		output.put(guessFactor.getName(), guessFactor);
	
	}

	public double getGuessIndex(double dist, double sp) {
		
		((InputVariable)distance).setValue(dist);
		((InputVariable)speed).setValue(Math.abs(sp));
		
		for (Rule r : rules) {
			den += r.applyRule(input, output);
		}
		
		crisp = guessFactor.getOutput(den);
		System.out.println("distancia do inimigo: "+ dist);
		System.out.println("velocidade do inimigo: " + sp);
		System.out.println("valor do disparo: " + crisp);
		return crisp;
	}

	public void setDistance(double h, double w) {
		
		this.maxDistance = Math.sqrt(w*w + h*h)/50;
	}
	
	public double getDistance() {
		
		return this.maxDistance;
	}
}
