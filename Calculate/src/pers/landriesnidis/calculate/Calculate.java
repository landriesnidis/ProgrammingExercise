package pers.landriesnidis.calculate;

import java.util.LinkedList;
import java.util.Stack;

import pers.landriesnidis.calculate.exception.InvalidExpressionException;
import pers.landriesnidis.calculate.exception.NumberFormatException;
import pers.landriesnidis.calculate.exception.UnknownOperatorException;

public class Calculate {
	
	public static void main(String[] args) throws NumberFormatException, InvalidExpressionException, UnknownOperatorException {
		
		if(args.length==0) args = new String[]{"2*3.14*(-2+10)"};
		
		if(args.length>0){
			System.out.println(operation(args[0]));
		}else{
			System.out.println("no expression.");
		}
	}
	
	/**
	 * ���ʽ��ȫ
	 * ���ڡ�+������-������Ϊ��Ŀ���ʽʹ�ã��磺-3+2�������ͨ��Ԥ����Ϊ���ʽ�ڲ�Ӱ������ǰ�������0��ʹ�䱣֤���ж�Ԫ��������
	 * ʾ����
	 * 8*-1.25+12 => 8*(0-1.25)+12
	 * @param expression ������ı��ʽ
	 * @return �����ı��ʽ
	 */
	private static String expressionCompletion(String expression){
		StringBuilder sb = new StringBuilder(expression);
		int len = sb.length();
		boolean b = false;
		for(int i=0;i<len;++i){
			char c = sb.charAt(i);
			b = false;
			
			if(c=='-' || c=='+'){
				if(i==0){
					sb.insert(0, "(0");
					b=true;
				}else{
					if(RPN.isOperator(sb.charAt(i-1)) && sb.charAt(i-1)!=')'){
						sb.insert(i, "(0");
						b=true;
					}
				}
				if(b){
					len+=2;
					for(int j=i+3;i<len;++j){
						if(RPN.isOperator(sb.charAt(j))){
							sb.insert(j, ')');
							++len;
							break;
						}
					}
					
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * ���ʽ����
	 * �Ƚ���׺���ʽת��Ϊ��׺���ʽ���ټ�����ʽ�Ľ��
	 * @param expression ���ʽ
	 * @return ������
	 * @throws NumberFormatException ��ֵ�Ǹ�����
	 * @throws InvalidExpressionException ���ʽ��Ч
	 * @throws UnknownOperatorException	δ֪�������
	 */
	public static double operation(String expression) throws NumberFormatException, InvalidExpressionException, UnknownOperatorException{
		
		// �����ʽ��������
		expression = expressionCompletion(expression);
		
		// ʹ���沨���㷨����
		LinkedList<String> rpnList = RPN.parse(expression);
		
		// ����ÿһ���������Ĳ�����ջ
		Stack<Double> operands = new Stack<Double>();
		
		// �����ʽ��һλΪ�����������ʽ��Ч
		if(RPN.isOperator(rpnList.get(0).charAt(0))) throw new InvalidExpressionException(expression);
		
		// �����沨�����ʽ��ÿһ��Ԫ��
		for(String elem:rpnList){
			
			// ���������
			if(RPN.isOperator(elem.charAt(0))){
				
				// �������Ԫ�����׳��쳣
				if(operands.size()<2) throw new InvalidExpressionException(expression);
				
				// �Ӳ�����ջȡ��ջ��������������
				double value2 = operands.pop();
				double value1 = operands.pop();
				
				// ���������
				double result = binaryOperation(elem.charAt(0),value1,value2);
				
				// ��������ѹջ
				operands.push(result);
				
			// �������ֵ
			}else{
				// �����ֵ��Ч��
				if(!RegexUtils.isValue(elem)) throw new NumberFormatException(elem);
				operands.push(Double.parseDouble(elem));
			}
		}
		// ���������ջ��Ԫ�ظ�����Ψһ��˵�����ʽ����ȷ
		if(operands.size()!=1) throw new InvalidExpressionException(expression);
		
		// ���ز�����ջ��Ψһ��Ԫ��
		return operands.pop();
	}
	
	/**
	 * ��Ԫ����
	 * @param operator �����
	 * @param value1 ֵ1
	 * @param value2 ֵ2
	 * @return ������
	 * @throws UnknownOperatorException ����δ֪�������
	 */
	private static double binaryOperation(char operator, double value1, double value2) throws UnknownOperatorException{
		switch(operator){
		case '+':
			return value1+value2;
		case '-':
			return value1-value2;
		case '*':
			return value1*value2;
		case '/':
			if(value2==0) throw new ArithmeticException("/ by zero.");
			return value1/value2;
		default:
			throw new UnknownOperatorException(operator);
		}
	}
}	
