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
	 * 表达式预处理
	 * 由于“+”、“-”可作为单目表达式使用（如：-3+2），因此通过预处理为表达式在不影响结果的前提下添加0，使其保证能有二元进行运算
	 * 示例：
	 * 8*-1.25+12 => 8*(0-1.25)+12
	 * @param expression 待处理的表达式
	 * @return 处理后的表达式
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
	 * 进行计算
	 * 算法需要优化(类型转换消耗频繁)
	 * @param expression 表达式
	 * @return 运算结果
	 * @throws NumberFormatException 数值非浮点型
	 * @throws InvalidExpressionException 表达式无效
	 * @throws UnknownOperatorException	未知的运算符
	 */
	public static double operation(String expression) throws NumberFormatException, InvalidExpressionException, UnknownOperatorException{
		
		// 预处理
		expression = expressionPretreatment(expression);
		
		LinkedList<String> rpnList = RPN.parse(expression);
		System.out.println(rpnList);
		
		// 若表达式第一位为运算符，则表达式无效
		if(RPN.isOperator(rpnList.get(0).charAt(0))) throw new InvalidExpressionException(expression);
		
		for(int i=1;i<rpnList.size();++i){
			String elem = rpnList.get(i);
			
			// 若是运算符
			if(RPN.isOperator(elem.charAt(0))){
				String value1,value2;
				
				if(i-2<0) throw new InvalidExpressionException(expression);
				
				value1 = rpnList.get(i-2);
				value2 = rpnList.get(i-1);
				
				// 检查数值有效性
				if(!RegexUtils.isValue(value1)) throw new NumberFormatException(value1);
				if(!RegexUtils.isValue(value2)) throw new NumberFormatException(value2);
				
				// 获得运算结果
				String result = String.valueOf(binaryOperation(elem.charAt(0),Double.parseDouble(value1),Double.parseDouble(value2)));
				
				// 将消耗掉的运算符和数值删除替换成运算结果
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
	 * 二元运算
	 * @param operator 运算符
	 * @param value1 值1
	 * @param value2 值2
	 * @return 运算结果
	 * @throws UnknownOperatorException 传入未知的运算符
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
