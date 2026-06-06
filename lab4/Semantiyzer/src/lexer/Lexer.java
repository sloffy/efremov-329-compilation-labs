package lexer;

import java.util.*;

public class Lexer {

    private final Set<String> keywords = Set.of(
            "using", "namespace", "class", "static", "void",
            "int", "double", "string", "bool",
            "if", "else", "for", "while",
            "true", "false", "return", "new"
    );

    private final Set<String> operators = Set.of(
            "+", "-", "*", "/", "=",
            "==", "!=", "<", ">", "<=", ">=",
            "++", "--", "+=", "-=",
            "&&", "||", "!"
    );

    private final Set<Character> delimiters = Set.of(
            ';', ',', '(', ')', '{', '}', '[', ']', '.'
    );

    private final List<Token> tokens = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    public List<Token> analyze(String code) {
        int i = 0;

        while (i < code.length()) {

            char current = code.charAt(i);

            // Игнорирование BOM
            if (current == '\uFEFF') {
                i++;
                continue;
            }

            // Пробелы
            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            // Комментарии //
            if (current == '/' && i + 1 < code.length()
                    && code.charAt(i + 1) == '/') {

                while (i < code.length() && code.charAt(i) != '\n') {
                    i++;
                }
                continue;
            }

            // Строки
            if (current == '"') {

                StringBuilder sb = new StringBuilder();
                sb.append(current);
                i++;

                boolean closed = false;

                while (i < code.length()) {

                    char c = code.charAt(i);
                    sb.append(c);

                    if (c == '"') {
                        closed = true;
                        i++;
                        break;
                    }

                    i++;
                }

                if (closed) {
                    tokens.add(new Token(
                            TokenType.CONSTANT_STRING,
                            sb.toString()
                    ));
                } else {
                    errors.add("Ошибка: незакрытый строковый литерал");
                }

                continue;
            }

            // Числа
            if (Character.isDigit(current)) {

                StringBuilder sb = new StringBuilder();
                boolean hasDot = false;

                while (i < code.length()) {

                    char c = code.charAt(i);

                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                    else if (c == '.') {

                        if (hasDot) {
                            errors.add("Ошибка: некорректное число " + sb + ".");
                            break;
                        }

                        hasDot = true;
                        sb.append(c);
                    }
                    else if (Character.isLetter(c)) {

                        sb.append(c);

                        while (i + 1 < code.length()
                                && Character.isLetterOrDigit(code.charAt(i + 1))) {
                            i++;
                            sb.append(code.charAt(i));
                        }

                        errors.add("Ошибка: идентификатор начинается с цифры: " + sb);
                        break;
                    }
                    else {
                        break;
                    }

                    i++;
                }

                String number = sb.toString();

                if (!number.isEmpty()) {

                    if (number.matches("\\d+")) {
                        tokens.add(new Token(TokenType.CONSTANT_INT, number));
                    }
                    else if (number.matches("\\d+\\.\\d+")) {
                        tokens.add(new Token(TokenType.CONSTANT_DOUBLE, number));
                    }
                }

                continue;
            }

            // Идентификаторы и ключевые слова
            if (Character.isLetter(current) || current == '_') {

                StringBuilder sb = new StringBuilder();

                while (i < code.length()
                        && (Character.isLetterOrDigit(code.charAt(i))
                        || code.charAt(i) == '_')) {

                    sb.append(code.charAt(i));
                    i++;
                }

                String word = sb.toString();

                if (word.equals("true") || word.equals("false")) {
                    tokens.add(new Token(TokenType.CONSTANT_BOOL, word));
                }
                else if (keywords.contains(word)) {
                    tokens.add(new Token(TokenType.KEYWORD, word));
                }
                else {
                    tokens.add(new Token(TokenType.IDENTIFIER, word));
                }

                continue;
            }

            // Двухсимвольные операторы
            if (i + 1 < code.length()) {

                String twoChar = "" + current + code.charAt(i + 1);

                if (operators.contains(twoChar)) {
                    tokens.add(new Token(TokenType.OPERATOR, twoChar));
                    i += 2;
                    continue;
                }
            }

            // Односимвольные операторы
            if (operators.contains(String.valueOf(current))) {
                tokens.add(new Token(
                        TokenType.OPERATOR,
                        String.valueOf(current)
                ));
                i++;
                continue;
            }

            // Разделители
            if (delimiters.contains(current)) {
                tokens.add(new Token(
                        TokenType.DELIMITER,
                        String.valueOf(current)
                ));
                i++;
                continue;
            }

            // Неизвестный символ
            errors.add("Ошибка: недопустимый символ '" + current + "'");
            i++;
        }

        return tokens;
    }

    public void printResults() {

        System.out.println("Лексема\t\t|\tТип");
        System.out.println("----------------------------------------");

        for (Token token : tokens) {
            System.out.printf("%-15s |\t%s%n",
                    token.getLexeme(),
                    token.getType());
        }

        System.out.println("\nСписок токенов:");

        System.out.println(tokens);

        System.out.println();

        if (errors.isEmpty()) {
            System.out.println("Лексический анализ завершён успешно.");
            System.out.println("Обнаружено " + tokens.size() + " токенов.");
            System.out.println("Ошибок не найдено.");
        } else {

            System.out.println("Обнаружены ошибки:");

            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}