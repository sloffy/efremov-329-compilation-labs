namespace test;
// Это тестовая программа для проверки препроцессора

using System;      // подключаем библиотеку

/*
   Многострочный комментарий
   с описанием программы
*/

class TestProgram
{

    // Глобальные переменные
    static int a = 5;     
    static int b = 3;      
    static int result;     


    static void Main(string[] args)      // точка входа
    {

        Console.WriteLine("Начало программы");     // вывод сообщения
        
        result = Add(a , b);        // вызов функции сложения
        
        Console.WriteLine("Результат: " + result);     // печать результата


        // Проверка условия
        if (result > 5)       
        {
            Console.WriteLine("Результат больше 5");    
        }
        else
        {
            Console.WriteLine("Результат меньше или равен 5");
        }



        // Цикл for
        for (int i = 0; i < 5; i++)     
        {
            Console.WriteLine("i = " + i);    
        }



        // Цикл while
        int              counter = 0;     

        while (counter < 3)    
        {
            Console.WriteLine("counter = " + counter);   
            counter++;       
        }



        /* Комментарий
           внутри программы
           который должен быть удалён */
        
        /* НЕЗАКРЫТЫЙ КОММЕНТАРИЙ Этот комментарий намеренно не закрывается чтобы препроцессор обнаружил ошибку

        Console.WriteLine("Конец программы");
    }

    // Функция сложения
    static int Add(int x, int y)
    {
        int sum = x + y;        // арифметическое выражение
        return sum;
    }

}
