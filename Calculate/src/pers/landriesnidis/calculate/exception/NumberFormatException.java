package pers.landriesnidis.calculate.exception;

/**
 * 数值格式异常
 * @author Landriesnidis
 */
public class NumberFormatException extends Exception{
	private static final long serialVersionUID = 1L;

	public NumberFormatException(String numberString) {
		super(String.format("'%s' is not a valid floating-point number.", numberString));
	}
}
