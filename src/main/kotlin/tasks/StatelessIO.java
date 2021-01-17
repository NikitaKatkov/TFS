package tasks;

import java.io.PrintWriter;
import java.util.Scanner;

public class StatelessIO implements IOActor {
    private static final PrintWriter outputWriter = new PrintWriter(System.out);
    private static final Scanner inputReader = new Scanner(System.in);

    @Override
    public String readLine() {
        return inputReader.nextLine();
    }

    @Override
    public void writeLine(String line) {
        outputWriter.write(line);
    }
}
