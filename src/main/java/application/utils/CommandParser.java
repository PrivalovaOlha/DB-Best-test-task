package application.utils;

import java.util.Arrays;

public final class CommandParser {
    private CommandParser() {
        throw new UnsupportedOperationException("private constructor");
    }

    public static int[] parseCsv(String command) {
        String[] stringResult = command.trim().split(";");
        return Arrays.asList(stringResult).stream().mapToInt(Integer::parseInt).toArray();

    }

}
