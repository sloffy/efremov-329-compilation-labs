package semantic;

import java.util.List;

public class SemanticResult {

    private final List<SymbolInfo> symbols;
    private final List<Triad> triads;

    public SemanticResult(List<SymbolInfo> symbols, List<Triad> triads) {
        this.symbols = symbols;
        this.triads = triads;
    }

    public void printSymbolTable() {
        System.out.println();
        System.out.println("Таблица символов");
        System.out.printf("%-15s | %-10s | %-9s | %-14s | %-10s%n",
                "Имя", "Тип", "Объявлена", "Инициализирована", "Область");
        System.out.println("---------------+------------+-----------+----------------+-----------");

        for (SymbolInfo s : symbols) {
            System.out.printf("%-15s | %-10s | %-9s | %-14s | %-10s%n",
                    s.getName(),
                    s.getType(),
                    s.isDeclared() ? "true" : "false",
                    s.isInitialized() ? "true" : "false",
                    s.getScope());
        }
    }

    public void printTriads() {
        System.out.println();
        System.out.println("Триады");
        for (Triad triad : triads) {
            System.out.println(triad);
        }
    }

    public List<SymbolInfo> getSymbols() {
        return symbols;
    }

    public List<Triad> getTriads() {
        return triads;
    }
}