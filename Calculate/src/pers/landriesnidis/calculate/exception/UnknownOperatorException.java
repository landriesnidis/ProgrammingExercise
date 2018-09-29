package pers.landriesnidis.calculate.exception;

/**
 * 未知的运算符异常
 * @author Landriesnidis
 */
public class UnknownOperatorException extends Exception{
	private static final long serialVersionUID = 1L;

	public UnknownOperatorException(char operator) {
		super(String.format("'%c' is not a valid operator.", operator));
	}
}
