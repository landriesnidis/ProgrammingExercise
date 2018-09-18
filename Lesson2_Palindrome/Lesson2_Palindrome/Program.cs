using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lesson2_Palindrome
{
    class Program
    {
        static Boolean isPalindrome(string s)
        {
            Boolean ret = true;
            char[] array = s.ToArray();
            for (int i = 0; i<array.Length;i++)
            {
                if (array[i] != array[array.Length - i - 1])
                {
                    ret = false;
                    break;
                }
            }
            return ret;
        }
        static void Main(string[] args)
        {
            Console.WriteLine("请输入数值：");
            String str = Console.ReadLine();
            Console.WriteLine(isPalindrome(str));

            Console.ReadLine();

        }
    }
}
