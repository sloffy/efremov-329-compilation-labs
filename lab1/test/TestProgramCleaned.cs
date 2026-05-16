namespace test;
using System;
class TestProgramCleaned
{
static int a = 5;
static int b = 3;
static int result;
static void Main(string[] args)
{
Console.WriteLine("Начало программы");
result = Add(a , b);
Console.WriteLine("Результат: " + result);
if (result > 5)
{
Console.WriteLine("Результат больше 5");
}
else
{
Console.WriteLine("Результат меньше или равен 5");
}
for (int i = 0; i < 5; i++)
{
Console.WriteLine("i = " + i);
}
int counter = 0;
while (counter < 3)
{
Console.WriteLine("counter = " + counter);
counter++;
}
Console.WriteLine("Конец программы");
}
static int Add(int x, int y)
{
int sum = x + y;
return sum;
}
}
