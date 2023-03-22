package polynomial_equation_solver;

import java.util.Arrays;

public class PolynomialFunction {

	private double[] coefficients;

	/*
	 * A polynomial "a_0 + a_1*X^1 + a_2*X^2 + ... + a_n*X^n" can be declare with
	 * coefficients = [a_0, a_1, a_2, ... , a_n].
	 */
	public PolynomialFunction(double... coefficients) {
		if (coefficients.length == 0) {
			this.coefficients = new double[] { 0 };
		} else {
			for (int i = coefficients.length - 1; i >= 0; i--) {
				if (coefficients[i] != 0 || i == 0) {
					this.coefficients = Arrays.copyOf(coefficients, i + 1);
					break;
				}
			}
		}
	}

	@Override
	public PolynomialFunction clone() {
		return new PolynomialFunction(coefficients);
	}

	public PolynomialFunction power(int exponent) throws Exception {
		if(exponent < 0) {
			throw new Exception("exponent can't be negative");
		}
		
		PolynomialFunction p = new PolynomialFunction(1);
		
		for(int i = 0; i < exponent; i++) {
			p = p.multiply(this);
		}
		
		return p;
	}

	public PolynomialFunction multiply(PolynomialFunction polynomialFunction) {
		double[] coefficients = new double[this.getDegree() + polynomialFunction.getDegree() + 1];

		for (int i = 0; i <= this.getDegree(); i++) {
			for (int j = 0; j <= polynomialFunction.getDegree(); j++) {
				coefficients[i + j] += this.getCoefficient(i) * polynomialFunction.getCoefficient(j);
			}
		}

		return new PolynomialFunction(coefficients);
	}

	public double getFunctionValue(double x) {
		double sum = 0;

		for (int i = 0; i < coefficients.length; i++) {
			sum += coefficients[i] * Math.pow(x, i);
		}

//		System.out.println("f(" + x + ")=" + sum);

		return sum;
	}

	public PolynomialFunction getDerivativeFunction() {
		double[] newCoefficients = new double[coefficients.length - 1];

		for (int i = 1; i < coefficients.length; i++) {
			newCoefficients[i - 1] = coefficients[i] * i;
		}

		return new PolynomialFunction(newCoefficients);
	}

	public int getDegree() {
		return coefficients.length - 1;
	}

	public double getCoefficient(int termDegree) {
		return coefficients[termDegree];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < coefficients.length; i++) {
			if (i > 0) {
				if (coefficients[i] >= 0) {
					sb.append(" + ");
				} else {
					sb.append(" - ");
				}
			}

			if (coefficients[i] >= 0 || i == 0) {
				sb.append(coefficients[i]);
			} else {
				sb.append(-coefficients[i]);
			}

			if (i > 0) {
				sb.append("X^").append(i);
			}
		}

		return sb.toString();
	}

}
