package tasks;

import java.io.PrintWriter;

public class StatefulIO implements IOActor {
    private static final PrintWriter outputWriter = new PrintWriter(System.out);

    int currentIndex;
    String[] lines;

    public StatefulIO(String input) {
        currentIndex = 0;
        lines = input.split("\n");
    }

    @Override
    public String readLine() {
        String line = lines[currentIndex];
        currentIndex += 1;
        return line;
    }

    @Override
    public void writeLine(String line) {
        outputWriter.write(line);
    }
}
