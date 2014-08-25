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
	// AI can determine tolerance levels to arrive at the best answer
	public HashMap<RavensFigure, Integer> choiceWeights;
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

		problems = new HashMap<String, RavensFigure>(); // A,B,C
		choices = new HashMap<String, RavensFigure>(); // 1,2,3,4,5,6
		choiceWeights = new HashMap<RavensFigure, Integer>(); // 1(5pts),2(3pts)
		relationships = new HashMap<String, Releationship>(); // AB,C1,C2.....

		System.out.println(problem.getName());
		splitProblemsAndChoices(problem.getFigures());
		buildStrengthMap();
		genertateMatrixMap(problem.getProblemType());
		System.out
				.println("---------------------------------------------------------");

		return orderByStrength(choiceWeights).get(0);
	}



	public void splitProblemsAndChoices(HashMap<String, RavensFigure> figures) {
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

//	public int getTranformStrength(ArrayList<RavensObject> a,
//			ArrayList<RavensObject> b) {
//		for (RavensObject obj : a) {
//			for (RavensObject object : b) {
//				if (object.getName().equals(obj.getName())) {
//				}
//			}
//		}
//		return 0;
//	}

	public void buildStrengthMap() {
		Iterator<Entry<String, RavensFigure>> it = choices.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			choiceWeights.put((RavensFigure) pairs.getValue(), 0);
		}
	}


	public void incrementStrength(String choice) {
		choiceWeights.put(choices.get(choice),
				choiceWeights.get(choices.get(choice)) + 1);
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

	public Releationship getReleationship(String frameA, String frameB) {

		if (frameA.length() == 1 && frameB.length() == 1) {
			return relationships.get(frameA + frameB);
		}
		return null;

	}

	public String getAttributeValue(RavensObject a, String key) {
		for (RavensAttribute obj : a.getAttributes()) {
			if (obj.getName().equals(key.toLowerCase())) {
				return obj.getValue();
			}
		}
		return null;
	}

	public List<String> orderByStrength(HashMap<RavensFigure, Integer> choices) {
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

	public void genertateMatrixMap(String problemType) {

		switch (problemType) {
		case "2x1":
			relationships.put("AB", new Releationship(problems.get("A"),
					problems.get("B")));
			for (int i = 1; i <= 6; i++) {
				relationships.put("C" + String.valueOf(i), new Releationship(
						problems.get("C"), choices.get(String.valueOf(i))));
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
