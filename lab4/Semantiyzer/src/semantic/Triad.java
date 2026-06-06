package semantic;

public class Triad {

    private final int number;
    private final String operation;
    private final String arg1;
    private final String arg2;

    public Triad(int number, String operation, String arg1, String arg2) {
        this.number = number;
        this.operation = operation;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        if (arg2 == null) {
            return number + ") (" + operation + ", " + arg1 + ")";
        }
        return number + ") (" + operation + ", " + arg1 + ", " + arg2 + ")";
    }
}