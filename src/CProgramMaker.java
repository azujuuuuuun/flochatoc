import java.net.URLDecoder;

public class CProgramMaker {
    static int numberOfIndent;

    static {
        numberOfIndent = 0;
    }

    public static String[] create(ActionState actionStates[]) {
        String actions[] = decideActionOrder(actionStates);
        int len = actions.length;
        String program[] = new String[len];
        for (int i = 0; i < len; i++) {
            program[i] = increaseIndent(putSemicolon(replace(urlDecode(actions[i]))));
        }
        return program;
    }

    public static String[] decideActionOrder(ActionState actionStates[]) {
        int numberOfAction = actionStates.length;
        String actions[] = new String[numberOfAction];
        int numberOfOrderd = 0;
        String outgoing = "";
        int i = 0;
        while (true) {
            if (!actionStates[i].isOrdered) {
                if (actionStates[i].action.equals("START")) {
                    actions[numberOfOrderd++] = actionStates[i].action;
                    outgoing = actionStates[i].outgoing[0];
                    actionStates[i].isOrdered = true;
                    i = 0;
                } else if (actionStates[i].incoming[0].equals(outgoing)) {
                    actions[numberOfOrderd++] = actionStates[i].action;
                    if (actionStates[i].action.equals("END")) {
                        break;
                    } else {
                        outgoing = actionStates[i].outgoing[0];
                        actionStates[i].isOrdered = true;
                        i = 0;
                    }
                }
            }
            i++;
        }
        return actions;
    }

    private static String urlDecode(String urlEncoded) {
        return URLDecoder.decode(urlEncoded);
    }

    private static String replace(String original) {
        String replaced = "";
        if (original.matches("^START$")) {
            replaced = "#include <stdio.h>\r\nvoid main() {";
        } else if (original.matches("^END$")) {
            replaced = "}";
        } else if (original.startsWith("input")) {
            String variable = original.substring(6, original.length() - 1);
            replaced = "scanf(\"%d\", &" + variable + ")";
        } else if (original.startsWith("output")) {
            String variable = original.substring(7, original.length() - 1);
            replaced = "printf(\"%d\", " + variable +  ")";
        }
        if (replaced.equals("")) {
            replaced = original;
        }
        return replaced;
    }

    private static String putSemicolon(String s) {
        if (s.endsWith(">") || s.endsWith("{") || s.endsWith("}") || s.endsWith(";")) {
            return s;
        } else {
            return s + ";";
        }
    }

    private static String increaseIndent(String s) {
        if (s.endsWith("}")) {
            numberOfIndent -= 4;
        }

        String indented = s;
        for (int i = 0; i < numberOfIndent; i++) {
            indented = " " + indented;
        }
        
        if (s.endsWith("{")) {
            numberOfIndent += 4;
        }
        return indented;
    }
}