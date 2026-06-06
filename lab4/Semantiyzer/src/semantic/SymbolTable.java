package semantic;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    private final Map<String, SymbolInfo> symbols = new LinkedHashMap<>();

    public SymbolInfo declare(String name, String type, String scope, boolean initialized) {
        if (symbols.containsKey(name)) {
            throw new SemanticException(
                    "Семантическая ошибка: повторное объявление переменной '" + name + "'"
            );
        }

        SymbolInfo info = new SymbolInfo(name, type, scope, initialized);
        symbols.put(name, info);
        return info;
    }

    public SymbolInfo resolve(String name) {
        return symbols.get(name);
    }

    public Collection<SymbolInfo> all() {
        return symbols.values();
    }
}