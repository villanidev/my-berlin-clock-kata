package villanidev.berlinclock.domain.calculator;

/** Shared helpers used by the row calculators. Not instantiable. */
public final class RowUtils {

    public static final char YELLOW = 'Y';
    public static final char RED    = 'R';
    public static final char OFF    = 'O';

    private RowUtils() { throw new UnsupportedOperationException("utility class"); }

    /**
     * Builds a row string of {@code total} lamps where the first {@code lit}
     * lamps are {@code onChar} and the rest are {@link #OFF}.
     */
    static String buildRow(int total, int lit, char onChar) {
        char[] row = new char[total];
        for (int i = 0; i < total; i++) {
            row[i] = i < lit ? onChar : OFF;
        }
        return new String(row);
    }

    /** Counts the number of lit (non-{@link #OFF}) lamps in a row string. */
    public static int countLit(String row) {
        int count = 0;
        for (char c : row.toCharArray()) {
            if (c != OFF) count++;
        }
        return count;
    }
}
