import preprocessor.Preprocessor;

import java.util.List;
import java.io.IOException;

import parser.Parser;

import lexer.Token;
import lexer.Lexer;

import ast.ProgramNode;

public class Main {

    public static void main(String[] args) {
        String inputFile = "../test/Test.cs";
        String cleanedFile = "../test/TestCleaned.cs";

        try {
            String cleanedCode = Preprocessor.process(
                    inputFile,
                    cleanedFile
            );

            Lexer lexer = new Lexer();

            List<Token> tokens = lexer.analyze(cleanedCode);
            lexer.printResults();

            Parser parser = new Parser(tokens);

            ProgramNode ast = parser.parse();

            System.out.println("\nAST:\n");
            System.out.println(ast);

            System.out.println(
                    "\nСинтаксический анализ завершён успешно."
            );
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {

            System.out.println(
                    "\nСинтаксическая ошибка:"
            );

            System.out.println(
                    e.getMessage()
            );
        }
    }
}