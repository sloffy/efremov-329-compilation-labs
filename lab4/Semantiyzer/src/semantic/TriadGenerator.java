package semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TriadGenerator {

    private final List<Triad> triads = new ArrayList<>();
    private int labelCounter = 1;

    public String emit(String operation, String arg1, String arg2) {
        Triad triad = new Triad(triads.size() + 1, operation, arg1, arg2);
        triads.add(triad);
        return "^" + triad.getNumber();
    }

    public void emit(String operation, String arg1) {
        triads.add(new Triad(triads.size() + 1, operation, arg1, null));
    }

    public void emitLabel(String label) {
        triads.add(new Triad(triads.size() + 1, "label", label, null));
    }

    public String newLabel() {
        return "L" + labelCounter++;
    }

    public List<Triad> getTriads() {
        return Collections.unmodifiableList(triads);
    }
}