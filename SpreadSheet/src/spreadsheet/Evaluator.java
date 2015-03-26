/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spreadsheet;

import java.util.Stack;

/**
 *
 * @author 130594B
 */
public class Evaluator {
    
    public static String evaluateToString(String str)
    {
    
        str = String.valueOf(evaluate(str));
        if(str.length()<=2) return str;
        if(str.charAt(str.length()-2)=='.' && str.charAt(str.length()-1)=='0')  return str.substring(0, str.length()-2);
        return str;
    }
        
    public static Double evaluate(String str)
    {
        char arr[] = str.toCharArray();
        
        // take different stacks for both operations and values
        Stack<Double> vals = new Stack<Double>();
        Stack<Character> ops = new Stack<Character>();
        
        boolean lastIsOperator = false;
        
        //check element by element
        for(int i=0;i<arr.length;i++)
        {
            //whitespace neglect
            if(arr[i]==' ') 
            {
                continue;
            }
            
            //if it's a number collect the whole number and push it in to the 'vals'
            if( ((arr[i]>='0' && arr[i]<='9')||arr[i]=='.') || (lastIsOperator&&isSign(arr[i])))
            {
                //Assume it is positive there is no sign.
                String tmp = "+";
                //while there are signs calculate the correct sign of the value.
                while(lastIsOperator&&isSign(arr[i]))
                {
                    if(tmp.charAt(0)==arr[i])
                        tmp ="+";
                    else tmp = "-";
                    i++;
                }
                
                //Start making the value
                lastIsOperator = false;
                while(i<arr.length)
                {
                    if(((arr[i]>='0' && arr[i]<='9')||arr[i]=='.'))
                    {
                        tmp += arr[i]; 
                        
                    }
                    else if(!(arr[i]==' ')) break;
                    i++;
                }
                i--;
                vals.push(Double.parseDouble(tmp));
                
                continue;
                //System.out.println(tmp);
            }
            
            //for operation charactors
            //previous operations which are having higher priority or equal will be carried out. and result value will be added into vals
            //operation will be pushed into ops stack
            if(isOperator(arr[i]))
            {
                lastIsOperator = true;
                while((!ops.empty())&& (vals.size()>1)&&(canEvaluate(arr[i],ops.peek())))
                {
                    vals.push(calc(ops.pop(),vals.pop(),vals.pop()));
                }
                ops.push(arr[i]);
                continue;
            }
            
            // push into ops
            if(arr[i]=='(') 
            {
                lastIsOperator = true;
                ops.push(arr[i]);
                continue;
            }
            
            //all operations will be carried out untill find a '('
            if(arr[i]==')')
            {
                lastIsOperator = false;
                while((ops.peek()!='('))
                {
                    vals.push(calc(ops.pop(),vals.pop(),vals.pop()));
                }
                ops.pop();
            }
        }
        
        //  all remaining will be evaluated -- ;)from left side
        while((!ops.empty()) &&(vals.size()>1))
        {
            vals.push(calc(ops.pop(),vals.pop(),vals.pop()));
        }
        return vals.peek();
    }
    
    
    //Check whether can be evaluated by looking at coming operator in arr and peek operator in stak
    private static boolean canEvaluate(char ar , char peek)
    {
        
        //depends on associativity and priority
        if(ar=='^'&&peek=='^') return false;
        return getPriority(ar)<=getPriority(peek);
    }
    //returns the priority of operators
    private static int getPriority(char ch)
    {   
        if (ch=='^') return 4;
        if (ch=='*'|| ch=='/') return 3;
        if (ch=='+'|| ch=='-') return 2;
        else return 0;
    }
    //evaluate one operation
    private static Double calc(char op, Double val2, Double val1)
    {
        if(op=='+') return val1+val2;
        if(op=='-') return val1-val2;
        if(op=='*') return val1*val2;
        if(op=='/') return val1/val2;
        if(op=='^') return Math.pow(val1,val2);
        return 0.0;
    }
    
    private static boolean isOperator(char ch)
    {
        return (ch=='+')||(ch=='-')||(ch=='*')||(ch=='/')||(ch=='^');
    }
    // check whether it can be a sign of a number
    private static boolean isSign(char ch)
    {
        return ch=='+'||ch=='-';
    }
}
