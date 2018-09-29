package pers.landriesnidis.calculate;

import java.util.LinkedList;

import pers.landriesnidis.calculate.exception.InvalidExpressionException;
import pers.landriesnidis.calculate.exception.NumberFormatException;
import pers.landriesnidis.calculate.exception.UnknownOperatorException;

public class Calculate {
	
	public static void main(String[] args) throws NumberFormatException, InvalidExpressionException, UnknownOperatorException {
		String expression = "5/0+8";
		System.out.println(expression);
		System.out.println(expressionPretreatment(expression));
		System.out.println(operation(expression));
	}
	
	/**
	 * ���ʽԤ����
	 * ���ڡ�+������-������Ϊ��Ŀ���ʽʹ�ã��磺-3+2�������ͨ��Ԥ����Ϊ���ʽ�ڲ�Ӱ������ǰ�������0��ʹ�䱣֤���ж�Ԫ��������
	 * ʾ����
	 * 8*-1.25+12 => 8*(0-1.25)+12
	 * @param expression ������ı��ʽ
	 * @return �����ı��ʽ
	 */
	private static String expressionPretreatment(String expression){
		StringBuilder sb = new StringBuilder(expression);
		int zero = 0;
		int len = sb.length();
		boolean b = false;
		for(int i=0;i<len+zero;++i){
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
					zero+=2;
					for(int j=i+4;i<len+zero;++j){
						if(RPN.isOperator(sb.charAt(j))){
							sb.insert(j, ')');
							++zero;
							break;
						}
					}
					
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * ���м���
	 * �㷨��Ҫ�Ż�(����ת������Ƶ��)
	 * @param expression ���ʽ
	 * @return ������
	 * @throws NumberFormatException ��ֵ�Ǹ�����
	 * @throws InvalidExpressionException ���ʽ��Ч
	 * @throws UnknownOperatorException	δ֪�������
	 */
	public static double operation(String expression) throws NumberFormatException, InvalidExpressionException, UnknownOperatorException{
		
		// Ԥ����
		expression = expressionPretreatment(expression);
		
		LinkedList<String> rpnList = RPN.parse(expression);
		System.out.println(rpnList);
		
		// �����ʽ��һλΪ�����������ʽ��Ч
		if(RPN.isOperator(rpnList.get(0).charAt(0))) throw new InvalidExpressionException(expression);
		
		for(int i=1;i<rpnList.size();++i){
			String elem = rpnList.get(i);
			
			// ���������
			if(RPN.isOperator(elem.charAt(0))){
				String value1,value2;
				
				if(i-2<0) throw new InvalidExpressionException(expression);
				
				value1 = rpnList.get(i-2);
				value2 = rpnList.get(i-1);
				
				// �����ֵ��Ч��
				if(!RegexUtils.isValue(value1)) throw new NumberFormatException(value1);
				if(!RegexUtils.isValue(value2)) throw new NumberFormatException(value2);
				
				// ���������
				String result = String.valueOf(binaryOperation(elem.charAt(0),Double.parseDouble(value1),Double.parseDouble(value2)));
				
				// �����ĵ������������ֵɾ���滻��������
				rpnList.remove(i);
				rpnList.remove(i-1);
					rpnList.remove(i-2);
					rpnList.add(i-2,result);
					i-=2;
				
				System.out.println(rpnList);
				continue;
			}
		}
		
		return Double.parseDouble(rpnList.get(0));
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
