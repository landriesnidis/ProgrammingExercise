package pers.landriesnidis.calculate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * 逆波兰表达式(Reverse Polish Expression，简称：RPN)转换工具类
 * 将中缀表达式转换成逆波兰表达式(后缀表达式)
 * @author Landriesnidis
 * @date 2018年9月30日02:23:30
 */
public class RPN {
	
	// 运算符
	private final static char[] OP = new char[]{'+','-','*','/','(',')'};
	
	/**
	 * 判断字符是否是运算符
	 * @param op 运算符
	 * @return 是运算符返回true，不是则返回false
	 */
	public static boolean isOperator(char op){
		for(int i=0;i<OP.length;++i){
			if(op == OP[i])
				return true;
		}
		return false;
	}
	
	/**
	 * 获取运算符优先等级
	 * @param op 运算符
	 * @return 根据OP数组中运算符的顺序计算出运算符的优先等级：+ -是0级，* /是1级，( )是2级
	 */
	public static int getOperatorPriorityValue(char op){
		return (String.copyValueOf(OP).indexOf(op))/2;
	}
	
	/**
	 * 解析表达式字符串，将中缀表达式转换成后缀表达式(逆波兰表达式)
	 * 算法描述：
	 * 1、正序遍历表达式中的每一个字符c。
	 * 3、判断字符c是否为运算符。  
	 * 		(1) 若运算符c之前有可保存的信息则将其作为一个整体保存至output链表。
	 * 		(2) 若运算符c为左括号"("，则直接存入运算符栈。
	 * 		(3) 若运算符c为右括号")"，则依次从运算符栈中弹出运算符并保存至output链表，直到遇到左括号为止。   
	 * 		(4) 若运算符c为非括号运算符（即：四则运算符号）。
	 * 			(a) 若运算符栈为空则直接将c压栈至运算符栈。
	 * 			(b) 若运算符栈栈顶的运算符为左括号，则将c直接压栈至运算符栈。
	 * 			(c) 若运算符c的优先级高于运算符栈栈顶的运算符优先级，则将c压栈至运算符栈。
	 * 			(d) 若运算符c的优先级小于或等于运算符栈栈顶的运算符优先级，则依次从运算符栈中弹出运算符并保存至output链表，直到遇到左括号或c的优先级高于栈顶运算符优先级的为止。再将c压栈至运算符栈。
	 * 4、当表达式遍历完成后，将尚未保存的非运算符信息作为整体保存至output链表。若运算符栈中尚有运算符时，则依序弹出运算符到output链表。
	 * @param expression 表达式
	 * @return 后缀式链表
	 */
	public static LinkedList<String> parse(String expression){
		// 结果输出栈
		LinkedList<String> output = new LinkedList<String>();
		// 运算符栈
		Stack<Character> operators = new Stack<Character>();
		
		// 字符串截取起始位置
		int startPos = 0;
		// 字符串截取末尾位置
		int endPos = 0;
		
		// 正序遍历表达式中的每一个字符c
		for(char c:expression.toCharArray()){
			
			// 字符串截取的结束位置+1
			++endPos;
			
			// 判断字符c是否为运算符。
			if(isOperator(c)){
				
				// 若运算符c之前有可保存的信息则将其作为一个整体保存至output链表。
				if(startPos<endPos-1)
					output.add(expression.substring(startPos, endPos-1));
				
				// 更新字符串截取的起始位置
				startPos=endPos;
				
				// 若运算符c为左括号"("，则直接存入运算符栈。
				if(c=='('){
					operators.push(c);
				
				// 若运算符c为右括号")"，则依次从运算符栈中弹出运算符并保存至output链表，直到遇到左括号为止。
				}else if(c==')'){
					char op;
					while(!operators.isEmpty() && (op=operators.pop())!='('){
						output.add(String.valueOf(op));
					}
					
				// 若运算符c为非括号运算符（即：四则运算符号）
				}else{
					
					// 若运算符栈为空则直接将c压栈至运算符栈。
					if(operators.isEmpty()){
						operators.push(c);
					
					// 若运算符栈栈顶的运算符为左括号，则将c直接压栈至运算符栈。
					}else if(operators.peek()=='('){
						operators.push(c);
						
					// 若运算符c的优先级高于运算符栈栈顶的运算符优先级，则将c压栈至运算符栈。
					}else if(getOperatorPriorityValue(c)>getOperatorPriorityValue(operators.peek())){
						operators.push(c);
						
					// 若运算符c的优先级小于或等于运算符栈栈顶的运算符优先级，则依次从运算符栈中弹出运算符并保存至output链表，直到遇到左括号或c的优先级高于栈顶运算符优先级的为止。再将c压栈至运算符栈。
					}else{
						while(!operators.isEmpty() && getOperatorPriorityValue(c)<=getOperatorPriorityValue(operators.peek()) && operators.peek()!='('){
							output.add(String.valueOf(operators.pop()));
						}
						operators.push(c);
					}
				}
			}
		}
		
		// 当表达式遍历完成后，将尚未保存的非运算符信息作为整体保存至output链表。若运算符栈中尚有运算符时，则依序弹出运算符到output链表。
		if(startPos<expression.length())output.add(expression.substring(startPos));
		while(!operators.isEmpty()){
			output.add(String.valueOf(operators.pop()));
		}
		
		return output;
	}
	
	/**
	 * <!>该方法仅做测试使用
	 * 提供多个测试用例，通过比对解析结果和用例标准结果以检测逆波兰表达式转换算法的正确性。
	 * @param args 无效
	 */
	public static void main(String[] args) {
		
		// 测试用例 - HashMap<key:中缀表达式 , value:后缀表达式>
		HashMap<String , String> testMap = new HashMap<String, String>();
		
		testMap.put(	"a*(b-c*d)+e"				,"abcd*-*e+"			);
		testMap.put(	"1+2*3-4*5-6+7*8-9"			,"123*+45*-6-78*+9-"	);
		testMap.put(	"a*(b-c*d)+e-f/g*(h+i*j-k)"	,"abcd*-*e+fg/hij*+k-*-");
		testMap.put(	"6*(5+(2+3)*8+3)"			,"6523+8*+3+*"			);
		testMap.put(	"a+b*c+(d*e+f)*g"			,"abc*+de*f+g*+"		);
		testMap.put(	"6-8+1.5*(1*5+3)"			,"68-1.515*3+*+"		);
		
		for(Entry<String, String> entry:testMap.entrySet()){
			System.out.println(String.format("原式：%s", entry.getKey()));
			System.out.println(String.format("标准：%s", entry.getValue()));
			System.out.println(String.format("结果：%s", list2String(RPN.parse(entry.getKey()))));
			System.out.println("--------------------------------------------------------");
		}
	}
	
	/**
	 * 将泛型为String的列表转换为连续的String
	 * @param list 包含多个String片段的栈
	 * @return 连续的String对象
	 */
	private static String list2String(List<String> list){
		StringBuilder sb = new StringBuilder();
		for(String s:list) sb.append(s);
		return sb.toString();
	}
}
