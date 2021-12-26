import interpreter.command.Command;
import lexical.LexicalAnalysis;
import syntatic.SyntaticAnalysis;

public class mli {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java mli [miniLua file]");
            return;
        }

        try (LexicalAnalysis l = new LexicalAnalysis(args[0])) {
            
            SyntaticAnalysis s = new SyntaticAnalysis(l);
            Command c = s.start();
            c.execute();
            
        } catch (Exception e) {
            System.err.println("Internal error: " + e.getMessage());
        }
    }

}
