package pers.landriesnidis.calculate.exception;

/**
 * ��Ч���ʽ�쳣
 * @author Landriesnidis
 */
public class InvalidExpressionException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidExpressionException(String expression) {
		super(String.format("Invalid expression: '%s'.", expression));
	}
}
