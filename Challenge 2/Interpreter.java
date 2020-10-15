import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;
import java.util.Map;

class Interpreter {

    public static void main(String[] args) throws IOException {
        Map<String, Integer> variables = new HashMap<String, Integer>();
        String[] commands = readFile();
        interCmd(commands, variables);
        for (Map.Entry<String, Integer> var : variables.entrySet()) {
            System.out.print(var.getKey() + ": " + var.getValue() + "\n");
        }
    }

    public static String[] readFile() throws IOException {  

        BufferedReader input = new BufferedReader(new FileReader("code"));
        StringBuffer code = new StringBuffer(); 
        String line;

        while( (line = input.readLine()) != null) {
            line = line.trim();
            code.append(line);
        }
    
        input.close();

        return code.toString().split(";");
    }

    public static void interCmd(String[] commands, Map<String, Integer> variables) {

        int i;
        for (i = 0; i < commands.length; i++) {
            switch (commands[i].split(" ")[0]) {
                case "clear":
                        clear(commands[i], variables);
                break;
                case "incr":
                    incr(commands[i], variables);
                break;
                case "decr":
                    decr(commands[i], variables);
                break;
                case "while":
                    i = loop(commands, variables, i);
                break;
            }
        }
    }

    public static void clear(String command, Map<String, Integer> variables) {
        variables.put(command.split(" ")[1], Integer.valueOf(0));
    }
    
    public static void incr(String command, Map<String, Integer> variables) {
        Boolean incrFlag = false;
        for (Map.Entry<String, Integer> var : variables.entrySet()) {
            if (command.split(" ")[1].equals(var.getKey())) {
                incrFlag = true;
            }
        }
        if (incrFlag) {
            variables.put(command.split(" ")[1], variables.get(command.split(" ")[1]) + 1);
        } else {
            variables.put(command.split(" ")[1], Integer.valueOf(1));
        }
    }

    public static void decr(String command, Map<String, Integer> variables) {
        Boolean decrFlag = false;
        for (Map.Entry<String, Integer> var : variables.entrySet()) {
            if (command.split(" ")[1].equals(var.getKey())) {
                decrFlag = true;
            }
        }
        if (decrFlag) {
            variables.put(command.split(" ")[1], variables.get(command.split(" ")[1]) - 1);
        } else {
            variables.put(command.split(" ")[1], Integer.valueOf(-1));
        }
        if (variables.get(command.split(" ")[1]) < 0) {
            System.out.println("Error; variables cannot take a negative value.");
            System.exit(-1);
        }
    }

    public static Integer loop(String[] commands, Map<String, Integer> variables, Integer i) {
        if (variables.get(commands[i].split(" ")[1]) != Integer.valueOf(commands[i].split(" ")[3])) {
            int j;
            int endCount = 0;
            StringBuffer loop = new StringBuffer();
            for (j = i + 1; j < commands.length; j++) {
                
                if (commands[j].split(" ")[0].equals("end")) {
                    if (endCount == 0) {
                        break;
                    } else {
                        endCount = endCount - 1;
                        loop.append(commands[j] + ";");
                    }
                } else {
                    if (commands[j].split(" ")[0].equals("while")) {
                        endCount = endCount + 1;
                    }
                    loop.append(commands[j] + ";");
                }
            }
            interCmd(loop.toString().split(";"), variables);
            i = i - 1;
        } else {
            int j;
            int endCount = 0;
            for (j = i + 1; j < commands.length; j++) {
                if (commands[j].split(" ")[0].equals("end")) {
                    if (endCount == 0) {
                        i = j;
                        break;
                    } else {
                        endCount = endCount - 1;
                    }
                } else if (commands[j].split(" ")[0].equals("while")) {
                    endCount = endCount + 1;
                }
            }
        }
        return i;
    }
}