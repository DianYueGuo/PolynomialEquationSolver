package polynomial_equation_solver;

import java.util.ArrayList;

public class PolynomialEquationSolver {

	/*
	 * This method finds all the real roots of the equation "polynomialFunction = 0"
	 * and returns them as a double array, ordered from smallest to largest.
	 */
	public static double[] getRoots(PolynomialFunction polynomialFunction) throws Exception {
//		System.out.println("getRoots: " + polynomialFunction + " = 0");

		if (polynomialFunction.getDegree() == 0) {
			if (polynomialFunction.getFunctionValue(0) == 0) {
				throw new Exception("The equation \"0.0 = 0\" has infinitely many roots.");
			} else {
				return new double[0];
			}
		}

		if (polynomialFunction.getDegree() == 1) {
			return new double[] { -polynomialFunction.getCoefficient(0) / polynomialFunction.getCoefficient(1) };
		}

		PolynomialFunction d1f = polynomialFunction.getDerivativeFunction();

		double[] d1f_roots = getRoots(d1f);

//		System.out.println(Arrays.toString(d1f_roots));

		if (d1f_roots.length > 0) {
			ArrayList<Double> roots = new ArrayList<>();

			double[] leftRoot = searchRootLeftward(polynomialFunction, d1f_roots[0]);
			if (leftRoot.length != 0) {
				roots.add(leftRoot[0]);
			}

			for (int i = 0; i < d1f_roots.length - 1; i++) {
				double[] foundRoot = binarySearchRoot(polynomialFunction, d1f_roots[i], d1f_roots[i + 1]);

				if (foundRoot.length != 0) {
					roots.add(foundRoot[0]);
				}
			}

			double[] rightRoot = searchRootRightward(polynomialFunction, d1f_roots[d1f_roots.length - 1]);
			if (rightRoot.length != 0) {
				roots.add(rightRoot[0]);
			}

			double[] returnArray = new double[roots.size()];
			for (int i = 0; i < roots.size(); i++) {
				returnArray[i] = roots.get(i);
			}

			return returnArray;
		} else {
			return searchRoot(polynomialFunction);
		}
	}

	/*
	 * Using Newton's method to find left and right bound and then finding the root
	 * with binary search. f'(x) should be for all x either positive or negative.
	 * Search in (-infinite, infinite).
	 */
	private static double[] searchRoot(PolynomialFunction polynomialFunction) throws Exception {
//		System.out.println("searchRoot");

		double startingPoint = 0;
		double startingPointValue = polynomialFunction.getFunctionValue(startingPoint);

		if (startingPointValue == 0) {
			return new double[] { 0 };
		}

		PolynomialFunction d1f = polynomialFunction.getDerivativeFunction();

		while (true) {
			double nextPoint = startingPoint - startingPointValue / d1f.getFunctionValue(startingPoint); // d1f.getFunctionValue(startingPoint)
																											// shouldn't
																											// be zero
			double nextPointValue = polynomialFunction.getFunctionValue(nextPoint);

			if (nextPointValue == 0) {
				return new double[] { nextPoint };
			} else if (startingPointValue * nextPointValue < 0) {
				if (startingPoint < nextPoint) {
					return binarySearchRoot(polynomialFunction, startingPoint, nextPoint);
				} else {
					return binarySearchRoot(polynomialFunction, nextPoint, startingPoint);
				}
			} else {
				startingPoint = nextPoint;
				startingPointValue = nextPointValue;
			}
		}
	}

	/*
	 * search in interval [leftBound, rightBound]
	 */
	private static double[] binarySearchRoot(PolynomialFunction polynomialFunction, double leftBound, double rightBound)
			throws Exception {
//		System.out.println("binarySearchRoot");

		if (leftBound > rightBound) {
			throw new Exception("leftBound can't be larger than rightBound");
		}

		double leftValue = polynomialFunction.getFunctionValue(leftBound);

		if (leftValue == 0) {
			return new double[] { leftBound };
		}

		double rightValue = polynomialFunction.getFunctionValue(rightBound);

		if (rightValue == 0) {
			return new double[] { rightBound };
		}

		if (leftValue * rightValue > 0) {
			return new double[0];
		}

		double midPoint = leftBound;
		while (true) {
//			System.out.println("loop");
//			System.out.println("leftBound: " + leftBound);
//			System.out.println("rightBound: " + rightBound);

			if ((leftBound + rightBound) / 2 == midPoint) {
				return new double[] { midPoint };
			}

			midPoint = (leftBound + rightBound) / 2;
			double midValue = polynomialFunction.getFunctionValue(midPoint);

			leftValue = polynomialFunction.getFunctionValue(leftBound);

			if (midValue == 0) {
				return new double[] { midPoint };
			} else if (midValue * leftValue > 0) {
				leftBound = midPoint;
			} else {
				rightBound = midPoint;
			}
		}

	}

	/*
	 * Using Newton's method to find left and right bound and then find the root
	 * with binary search. Search in interval [startingPoint, double_max). f'(x)
	 * should be (for all x in the interval) either positive or negative.
	 */
	private static double[] searchRootRightward(PolynomialFunction polynomialFunction, double startingPoint)
			throws Exception {
//		System.out.println("searchRootRightward");

		double startingPointValue = polynomialFunction.getFunctionValue(startingPoint);

		if (startingPointValue == 0) {
			return new double[] { startingPoint };
		}

		PolynomialFunction d1f = polynomialFunction.getDerivativeFunction();

		if (startingPointValue * polynomialFunction.getCoefficient(polynomialFunction.getDegree()) > 0) {
			return new double[0];
		}

		double nextPoint = startingPoint + 1;

		do {
			double nextPointValue = polynomialFunction.getFunctionValue(nextPoint);

			if (nextPointValue == 0) {
				return new double[] { nextPoint };
			} else if (startingPointValue * nextPointValue < 0) {
				return binarySearchRoot(polynomialFunction, startingPoint, nextPoint);
			} else {
				startingPoint = nextPoint;
				startingPointValue = nextPointValue;
			}

			nextPoint = startingPoint - startingPointValue / d1f.getFunctionValue(startingPoint); // d1f.getFunctionValue(startingPoint)
																									// shouldn't be zero
		} while (true);
	}

	/*
	 * Using Newton's method to find left and right bound and then finding the root
	 * with binary search. Search in interval (-double_max, startingPoint]. f'(x)
	 * should be (for all x in the interval) either positive or negative.
	 */
	private static double[] searchRootLeftward(PolynomialFunction polynomialFunction, double startingPoint)
			throws Exception {
//		System.out.println("searchRootLeftward");

		double startingPointValue = polynomialFunction.getFunctionValue(startingPoint);

		if (startingPointValue == 0) {
			return new double[] { startingPoint };
		}

		PolynomialFunction d1f = polynomialFunction.getDerivativeFunction();

		if (startingPointValue * polynomialFunction.getCoefficient(polynomialFunction.getDegree()) < 0) {
			if (polynomialFunction.getDegree() % 2 == 1) {
				return new double[0];
			}
		} else {
			if (polynomialFunction.getDegree() % 2 == 0) {
				return new double[0];
			}
		}

		double nextPoint = startingPoint - 1;

		do {
			double nextPointValue = polynomialFunction.getFunctionValue(nextPoint);

			if (nextPointValue == 0) {
				return new double[] { nextPoint };
			} else if (startingPointValue * nextPointValue < 0) {
				return binarySearchRoot(polynomialFunction, nextPoint, startingPoint);
			} else {
				startingPoint = nextPoint;
				startingPointValue = nextPointValue;
			}

			nextPoint = startingPoint - startingPointValue / d1f.getFunctionValue(startingPoint); // d1f.getFunctionValue(startingPoint)
																									// shouldn't be zero
		} while (true);
	}

}
