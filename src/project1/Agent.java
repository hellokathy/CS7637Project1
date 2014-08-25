package project1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.security.sasl.RealmCallback;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures: public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
	/**
	 * The default constructor for your Agent. Make sure to execute any
	 * processing necessary before your Agent starts solving problems here.
	 * 
	 * Do not add any variables to this signature; they will not be used by
	 * main().
	 * 
	 */
	// used to test an assumptions strength
	// i.e. if a transformation has a strong similarity weight than the
	// AI can make tolerance levels to arrive at the best answer
	public HashMap<RavensFigure, Integer> choiceWeights;
	public HashMap<String, RavensFigure> choices;
	public HashMap<String, RavensFigure> problems;
	public HashMap<String, Releationship> relationships;

	public Agent() {
		// Weights = new HashMap<String, Integer>();
		// Weights.put("Unchanged", 5);
		// Weights.put("Reflected", 4);
		// Weights.put("Scaled", 2);
		// Weights.put("Deleted", 1);
		// Weights.put("ShapeChanged", 0);
	}

	/**
	 * The primary method for solving incoming Raven's Progressive Matrices. For
	 * each problem, your Agent's Solve() method will be called. At the
	 * conclusion of Solve(), your Agent should return a String representing its
	 * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
	 * are also the Names of the individual RavensFigures, obtained through
	 * RavensFigure.getName().
	 * 
	 * In addition to returning your answer at the end of the method, your Agent
	 * may also call problem.checkAnswer(String givenAnswer). The parameter
	 * passed to checkAnswer should be your Agent's current guess for the
	 * problem; checkAnswer will return the correct answer to the problem. This
	 * allows your Agent to check its answer. Note, however, that after your
	 * agent has called checkAnswer, it will *not* be able to change its answer.
	 * checkAnswer is used to allow your Agent to learn from its incorrect
	 * answers; however, your Agent cannot change the answer to a question it
	 * has already answered.
	 * 
	 * If your Agent calls checkAnswer during execution of Solve, the answer it
	 * returns will be ignored; otherwise, the answer returned at the end of
	 * Solve will be taken as your Agent's answer to this problem.
	 * 
	 * @param problem
	 *            the RavensProblem your agent should solve
	 * @return your Agent's answer to this problem
	 */
	public String Solve(RavensProblem problem) {

		// Separate problems from possible choices
		System.out.println(problem.getName());

		problems = new HashMap<String, RavensFigure>();
		choices = new HashMap<String, RavensFigure>();
		choiceWeights = new HashMap<RavensFigure, Integer>();
		relationships = new HashMap<String, Releationship>();

		seperateProblemsAndChoices(problem.getFigures());
		buildStrengthMap();
		genertateMatrixMap(problem.getProblemType());

	
		relationships.put("AB", buildReleationship(problems.get("A"), problems.get("B")));

	
		getTranformStrength(problems.get("A").getObjects(), problems.get("B")
				.getObjects());
		System.out
				.println("---------------------------------------------------------");

		return orderByStrength(choiceWeights).get(0);
	}

	public void generateProblemRelationships() {

	}

	public Releationship getReleationship(String frameA, String frameB) {

		if (frameA.length() == 1 && frameB.length() == 1) {
			return relationships.get(frameA + frameB);
		}
		return null;

	}

	public void buildStrengthMap() {
		Iterator<Entry<String, RavensFigure>> it = choices.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			choiceWeights.put((RavensFigure) pairs.getValue(), 0);
		}
	}

	public Releationship buildReleationship(RavensFigure objA, RavensFigure objB) {
		Releationship releationship = getReleationship(objA.getName(), objB.getName());
		
		for (RavensObject A: objA.getObjects()) {
			for (RavensObject B: objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					for (RavensAttribute aAttribute : A.getAttributes()) {
						for (RavensAttribute bAttribute : B.getAttributes()) {
							Map<String, RavensAttribute> map = new HashMap<String, RavensAttribute>();
							if (aAttribute.getName().equals(bAttribute.getName()) &&
									aAttribute.getValue().equals(bAttribute.getValue())) {
									map.put(A.getName(), aAttribute);
									releationship.addSimilarity(map);								
							}
							else {
								if (aAttribute.getName().equals(bAttribute.getName())) {
									map.put(A.getName(), bAttribute);
									releationship.differencesList.add(map);		
								}
							}
						}
					}
				}
			}
		}	
		
		//counts side in fig
		for (RavensObject A: objA.getObjects()) {
			for (RavensAttribute aAttribute : A.getAttributes()) {
				if (aAttribute.getName().equals("shape")) {
					releationship.totalSidesA += getSides(aAttribute.getValue()) ;
				}	
			}	
		}
		
		for (RavensObject B: objB.getObjects()) {
			for (RavensAttribute bAttribute : B.getAttributes()) {
				if (bAttribute.getName().equals("shape")) {
					releationship.totalSidesB += getSides(bAttribute.getValue()) ;
				}			
			}	
		}
		
		//get shapes deleted between x and y
		for (RavensObject A: objA.getObjects()) {
			releationship.shapesDeleted.add(A);
			for (RavensObject B: objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					releationship.shapesDeleted.remove(A);
					break;
				}
			}	
		}
		//get shapes added between x and y
		for (RavensObject A: objB.getObjects()) {
			releationship.shapesAdded.add(A);
			for (RavensObject B: objA.getObjects()) {
				if (A.getName().equals(B.getName())) {
					releationship.shapesAdded.remove(A);
					break;
				}
			}	
		}
		
		logRelationship(releationship);
		return releationship;	
	}

	private void logRelationship(Releationship releationship) {
		System.out.println("Total Sides fig. A : " + releationship.totalSidesA);
		System.out.println("Total Sides fig. B : " + releationship.totalSidesB);
		System.out.println();
		System.out.println("Similarities:");
		for (Map<String, RavensAttribute>  sim : releationship.similaritesList) {
			Iterator<Entry<String, RavensAttribute>> it = sim.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, RavensAttribute> pairs = (Map.Entry<String, RavensAttribute>) it
						.next();
				System.out.println((pairs.getKey() + " " + pairs.getValue().getName() + " " + pairs.getValue().getValue()));
			}
			
		}
	
		System.out.println();
		System.out.println("Differences:");
		for (Map<String, RavensAttribute>  diff : releationship.differencesList) {
			Iterator<Entry<String, RavensAttribute>> it = diff.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, RavensAttribute> pairs = (Map.Entry<String, RavensAttribute>) it
						.next();
				System.out.println((pairs.getKey() + " " + pairs.getValue().getName() + " " + pairs.getValue().getValue()));
			}
		}
		System.out.println();
		System.out.println("ShapesAdded:");
		for (RavensObject obj : releationship.shapesAdded) {
			System.out.println(obj.getName());
		}
		System.out.println();
		System.out.println("ShapesDeleted:");
		for (RavensObject obj : releationship.shapesDeleted) {
			System.out.println(obj.getName());
		}
		System.out.println();
	
	}
	public void compareReleationships() {

		Releationship releationship = getReleationship("A", "B");
		RavensFigure figure;

		for (int i = 1; i <= choices.size(); i++) {
			switch (i) {
			case 1:
				figure = choices.get("1");
				break;
			case 2:
				figure = choices.get("2");
				break;
			case 3:
				figure = choices.get("3");
				break;
			case 4:
				figure = choices.get("4");
				break;
			case 5:
				figure = choices.get("5");
				break;
			case 6:
				figure = choices.get("6");
				break;
			default:
				break;
			}
		}
	}

	public void incrementStrength(String choice) {
		choiceWeights.put(choices.get(choice),
				choiceWeights.get(choices.get(choice)) + 1);
	}

	public void seperateProblemsAndChoices(HashMap<String, RavensFigure> figures) {
		Iterator<Entry<String, RavensFigure>> it = figures.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			if (!pairs.getKey().toString().matches("-?\\d+(\\.\\d+)?")) {
				problems.put(pairs.getKey().toString(),
						(RavensFigure) pairs.getValue());
			} else {
				choices.put(pairs.getKey().toString(),
						(RavensFigure) pairs.getValue());
			}
		}
	}

	public int getTranformStrength(ArrayList<RavensObject> a,
			ArrayList<RavensObject> b) {
		for (RavensObject obj : a) {
			for (RavensObject object : b) {
				if (object.getName().equals(obj.getName())) {
				}
			}
		}
		return 0;
	}

	public int compareAttributes(ArrayList<RavensAttribute> a,
			ArrayList<RavensAttribute> b) {

		for (RavensAttribute obj : a) {
			for (RavensAttribute object : b) {
				if (object.getName().equals(obj.getName())) {
				}
			}
		}
		return 0;
	}

	public String getAttributeValue(RavensObject a, String key) {
		for (RavensAttribute obj : a.getAttributes()) {
			if (obj.getName().equals(key.toLowerCase())) {
				return obj.getValue();
			}
		}
		return null;
	}

	public List<String> orderByStrength(
			HashMap<RavensFigure, Integer> choices) {
		List<String> choiceList = new ArrayList<String>();
		List<Entry<RavensFigure, Integer>> list = new ArrayList<Entry<RavensFigure, Integer>>(
				choices.entrySet());
		Collections.sort(list,
				new Comparator<Map.Entry<RavensFigure, Integer>>() {
					public int compare(Map.Entry<RavensFigure, Integer> o1,
							Map.Entry<RavensFigure, Integer> o2) {
						return (o2.getValue()).compareTo(o1.getValue());
					}
				});
		for (Map.Entry<RavensFigure, Integer> entry : list) {
			choiceList.add(entry.getKey().getName());
		}
		return choiceList;
	}

	public static int randInt(int min, int max) {

		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;

	}

	public void genertateMatrixMap(String problemType) {

		switch (problemType) {
		case "2x1":
			relationships.put("AB", new Releationship());
			break;
		case "3x1":
			relationships.put("ABC", new Releationship());
			break;
		case "3x2":
			relationships.put("ABC", new Releationship());
			relationships.put("DEF", new Releationship());
			relationships.put("AD", new Releationship());
			relationships.put("BE", new Releationship());
			relationships.put("CF", new Releationship());
			break;

		default:
			break;
		}
	}

	public boolean isRotation() {
		return false;
	}

	public boolean isDeletion() {
		return false;
	}

	public boolean isReflection() {
		return false;
	}

	public boolean isUnchanged() {
		return false;
	}

	public boolean isScaled() {
		return false;
	}

	public boolean isShapeChanged() {
		return false;
	}

	public int getSides(String shape) {

		switch (shape) {
		case "triangle":
			return 3;
		case "square":
			return 4;
		case "diamond":
			return 4;
		case "pentagon":
			return 5;
		case "half-arrow":
			return 5;
		case "hexagon":
			return 6;
		case "heptagon":
			return 7;
		case "arrow":
			return 7;
		case "octagon":
			return 8;
		case "nonagon":
			return 9;
		case "decagon":
			return 10;
		default:
			break;
		}
		return 0;

	}

}
