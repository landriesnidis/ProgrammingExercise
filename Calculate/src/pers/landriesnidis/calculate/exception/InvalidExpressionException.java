package pers.landriesnidis.calculate.exception;

/**
 * 无效表达式异常
 * @author Landriesnidis
 */
public class InvalidExpressionException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidExpressionException(String expression) {
		super(String.format("Invalid expression: '%s'.", expression));
	}
}
