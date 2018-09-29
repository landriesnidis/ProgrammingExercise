package pers.landriesnidis.calculate.exception;

/**
 * 未知的运算符异常
 * @author Landriesnidis
 */
public class InvalidExpressionException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidExpressionException(String expression) {
		super(String.format("Invalid expression: '%s'.", expression));
	}
}
