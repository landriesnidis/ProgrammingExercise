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
	 * 表达式补全
	 * 由于“+”、“-”可作为单目表达式使用（如：-3+2），因此通过预处理为表达式在不影响结果的前提下添加0，使其保证能有二元进行运算
	 * 示例：
	 * 8*-1.25+12 => 8*(0-1.25)+12
	 * @param expression 待处理的表达式
	 * @return 处理后的表达式
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
	 * 表达式计算
	 * 先将中缀表达式转换为后缀表达式，再计算表达式的结果
	 * @param expression 表达式
	 * @return 运算结果
	 * @throws NumberFormatException 数值非浮点型
	 * @throws InvalidExpressionException 表达式无效
	 * @throws UnknownOperatorException	未知的运算符
	 */
	public static double operation(String expression) throws NumberFormatException, InvalidExpressionException, UnknownOperatorException{
		
		// 将表达式补充完整
		expression = expressionCompletion(expression);
		
		// 使用逆波兰算法处理
		LinkedList<String> rpnList = RPN.parse(expression);
		
		// 保存每一步运算结果的操作数栈
		Stack<Double> operands = new Stack<Double>();
		
		// 若表达式第一位为运算符，则表达式无效
		if(RPN.isOperator(rpnList.get(0).charAt(0))) throw new InvalidExpressionException(expression);
		
		// 遍历逆波兰表达式中每一项元素
		for(String elem:rpnList){
			
			// 若是运算符
			if(RPN.isOperator(elem.charAt(0))){
				
				// 不满足二元运算抛出异常
				if(operands.size()<2) throw new InvalidExpressionException(expression);
				
				// 从操作数栈取出栈顶的两个操作数
				double value2 = operands.pop();
				double value1 = operands.pop();
				
				// 获得运算结果
				double result = binaryOperation(elem.charAt(0),value1,value2);
				
				// 将计算结果压栈
				operands.push(result);
				
			// 如果是数值
			}else{
				// 检查数值有效性
				if(!RegexUtils.isValue(elem)) throw new NumberFormatException(elem);
				operands.push(Double.parseDouble(elem));
			}
		}
		// 如果操作数栈中元素个数不唯一则说明表达式不正确
		if(operands.size()!=1) throw new InvalidExpressionException(expression);
		
		// 返回操作数栈中唯一的元素
		return operands.pop();
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
