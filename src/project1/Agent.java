package project1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


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
	// AI can determine tolerance levels to arrive at the best answer
	public HashMap<RavensFigure, Double> choiceWeights;
	public HashMap<String, RavensFigure> choices;
	public HashMap<String, RavensFigure> problems;
	public HashMap<String, Releationship> relationships;

	public Agent() {

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
		System.out.println("------------------------------------");
		problems = getProblems(problem); // A,B,C
		choices = getChoices(problem); // 1,2,3,4,5,6
		choiceWeights = new HashMap<RavensFigure, Double>(); // 1(5pts),2(3pts)
		relationships = genertateMatrixMap(problem); // AB,C1,C2.....



		for (int i = 1; i <= 6; i++) {
			choiceWeights.put(choices.get(String.valueOf(i)),Releationship.getRelationshipStrength(relationships.get("AB"), relationships.get("C"+i)));
		}
		
		return orderByStrength(choiceWeights).get(0);
	}

	private HashMap<String, RavensFigure> eliminateBadFrames(
			HashMap<String, RavensFigure> choices) {

		HashMap<String, RavensFigure> realChoices = new HashMap<String, RavensFigure>();
		Iterator<Entry<String, RavensFigure>> it = choices.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			realChoices.put(pairs.getKey(), (RavensFigure) pairs.getValue());
		}
		return realChoices;
	}

	public HashMap<String, RavensFigure> getProblems(RavensProblem problem) {

		HashMap<String, RavensFigure> problems = new HashMap<String, RavensFigure>();
		Iterator<Entry<String, RavensFigure>> it = problem.getFigures()
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			if (!pairs.getKey().toString().matches("-?\\d+(\\.\\d+)?")) {
				// eliminate bad frames here instead of calling eliminateBadFrames()
				problems.put(pairs.getKey().toString(),
						(RavensFigure) pairs.getValue());
			}
		}
		return problems;
	}

	public HashMap<String, RavensFigure> getChoices(RavensProblem problem) {

		HashMap<String, RavensFigure> choices = new HashMap<String, RavensFigure>();
		Iterator<Entry<String, RavensFigure>> it = problem.getFigures()
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			if (pairs.getKey().toString().matches("-?\\d+(\\.\\d+)?")) {
				choices.put(pairs.getKey().toString(),
						(RavensFigure) pairs.getValue());
			}
		}
		return eliminateBadFrames(choices);
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

	public List<String> orderByStrength(HashMap<RavensFigure, Double> choices) {

		List<String> choiceList = new ArrayList<String>();
		List<Entry<RavensFigure, Double>> list = new ArrayList<Entry<RavensFigure, Double>>(
				choices.entrySet());
		Collections.sort(list,
				new Comparator<Map.Entry<RavensFigure, Double>>() {
					public int compare(Map.Entry<RavensFigure, Double> o1,
							Map.Entry<RavensFigure, Double> o2) {
						return (o2.getValue()).compareTo(o1.getValue());
					}
				});
		for (Map.Entry<RavensFigure, Double> entry : list) {
			choiceList.add(entry.getKey().getName());
		}
		return choiceList;
	}

	public HashMap<String, Releationship> genertateMatrixMap(RavensProblem problem) {
		HashMap<String, Releationship> relationships = new HashMap<String, Releationship>();
		
		switch (problem.getProblemType()) {
		case "2x1":
			relationships.put("AB", new Releationship(problems.get("A"),problems.get("B")));
			Iterator<Entry<String, RavensFigure>> it = choices.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it.next();
				relationships.put("C" + pairs.getValue().getName(), new Releationship(
						problems.get("C"), pairs.getValue()));
			}
			break;
		case "3x1":
			relationships.put("ABC", null);
			break;
		case "3x2":
			relationships.put("ABC", null);
			relationships.put("DEF", null);
			relationships.put("AD", null);
			relationships.put("BE", null);
			relationships.put("CF", null);
			break;

		default:
			break;
		}
		return relationships;
	}

	public void compareReleationships() {

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

	// public boolean isRotation() {
	// return false;
	// }
	//
	// public boolean isDeletion() {
	// return false;
	// }
	//
	// public boolean isReflection() {
	// return false;
	// }
	//
	// public boolean isUnchanged() {
	// return false;
	// }
	//
	// public boolean isScaled() {
	// return false;
	// }
	//
	// public boolean isShapeChanged() {
	// return false;
	// }

}
