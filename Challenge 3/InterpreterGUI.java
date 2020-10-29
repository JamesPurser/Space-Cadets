import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

import java.util.HashMap;
import java.util.Map;

class InterpreterGUI extends JFrame implements ActionListener {

    static JTextArea textArea;

    InterpreterGUI() {
        textArea = new JTextArea();
        JFrame window = new JFrame("Interpreter");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(400, 400);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu run = new JMenu("Run");

        JMenuItem fileOpen = new JMenuItem("Open");
        JMenuItem fileSave = new JMenuItem("Save");
        JMenuItem fileQuit = new JMenuItem("Quit");

        JMenuItem runRun = new JMenuItem("Run");

        menuBar.add(file);
        file.add(fileOpen);
        file.add(fileSave);
        file.add(fileQuit);

        menuBar.add(run);
        run.add(runRun);

        fileOpen.addActionListener(this);
        fileSave.addActionListener(this);
        fileQuit.addActionListener(this);
        runRun.addActionListener(this);

        window.getContentPane().add(BorderLayout.NORTH, menuBar);
        window.getContentPane().add(BorderLayout.CENTER, textArea);
        window.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "Open":
                JFileChooser fileOpenDialogue = new JFileChooser("f:");

                int fileOpenResponse = fileOpenDialogue.showOpenDialog(null);
                File fileOpenPath;

                if (fileOpenResponse == JFileChooser.APPROVE_OPTION) {
                    fileOpenPath = new File(fileOpenDialogue.getSelectedFile().getAbsolutePath());

                    try {
                        BufferedReader input = new BufferedReader(new FileReader(fileOpenPath));
                        StringBuffer code = new StringBuffer(); 
                        String line;
                
                        while( (line = input.readLine()) != null) {
                            code.append(line + "\n");
                        }
                    
                        input.close();
                        
                        textArea.setText(code.toString());
                        } catch (IOException ioe) {
                            throw new RuntimeException(ioe);
                    }
                }
                
            break;
            case "Save":
                JFileChooser fileSaveDialogue = new JFileChooser("f:");

                int fileSaveResponse = fileSaveDialogue.showSaveDialog(null);
                File fileSavePath;

                if (fileSaveResponse == JFileChooser.APPROVE_OPTION) {
                    fileSavePath = new File(fileSaveDialogue.getSelectedFile().getAbsolutePath());

                    try {
                        BufferedWriter output = new BufferedWriter(new FileWriter(fileSavePath));
                        String text = textArea.getText();
                        
                        output.write(text);
                        output.close();
                        
                        } catch (IOException ioe) {
                            throw new RuntimeException(ioe);
                    }
                }
            break;
            case "Quit":
                System.exit(0);
            break;
            case "Run":
                Map<String, String> strVars = new HashMap<String, String>();
                Map<String, Integer> intVars = new HashMap<String, Integer>();

                String[] commands = readText();
                interCmd(commands, intVars, strVars);
    
                for (Map.Entry<String, Integer> var : intVars.entrySet()) {
                    System.out.print(var.getKey() + ": " + var.getValue() + "\n");
                }

            break;
        }
    }

    public static void main(String args[]) throws IOException {
        new InterpreterGUI();
    }

    public static String [] readText() {
        StringBuffer text = new StringBuffer();
        for (String line : textArea.getText().split("\n")) {
            line = line.trim();
            text.append(line);
        }
        return text.toString().split(";");
    }

    public static void interCmd(String[] commands, Map<String, Integer> intVars, Map<String, String> strVars) {

        int i;
        for (i = 0; i < commands.length; i++) {
            switch (commands[i].split(" ")[0]) {
                case "clear":
                        clear(commands[i], intVars);
                break;
                case "incr":
                    incr(commands[i], intVars);
                break;
                case "decr":
                    decr(commands[i], intVars);
                break;
                case "while":
                    i = loop(commands, intVars, i, strVars);
                break;
                case "if":
                break;
                case "add":
                    add(commands[i], intVars);
                break;
                case "sub":
                    sub(commands[i], intVars);
                break;
                case "div":
                    div(commands[i], intVars);
                break;
                case "mul":
                    mul(commands[i], intVars);
                break;
                case "asg":
                    asg(commands[i], intVars, strVars);
                break;
            }
        }
    }

    public static void clear(String command, Map<String, Integer> intVars) {
        intVars.put(command.split(" ")[1], Integer.valueOf(0));
    }
    
    public static void incr(String command, Map<String, Integer> intVars) {
        /*
        Boolean incrFlag = false;
        for (Map.Entry<String, Integer> var : intVars.entrySet()) {
            if (command.split(" ")[1].equals(var.getKey())) {
                incrFlag = true;
            }
        }
        if (incrFlag) {
            intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[1]) + 1);
        } else {
            intVars.put(command.split(" ")[1], Integer.valueOf(1));
        }
        */
        intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[1]) + 1);
    }

    public static void decr(String command, Map<String, Integer> intVars) {
        /*
        Boolean decrFlag = false;
        for (Map.Entry<String, Integer> var : intVars.entrySet()) {
            if (command.split(" ")[1].equals(var.getKey())) {
                decrFlag = true;
            }
        }
        if (decrFlag) {
            intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[1]) - 1);
        } else {
            intVars.put(command.split(" ")[1], Integer.valueOf(-1));
        }
        if (intVars.get(command.split(" ")[1]) < 0) {
            System.out.println("Error; variables cannot take a negative value.");
            System.exit(-1);
        }
        */
        intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[1]) - 1);
    }

    public static Integer loop(String[] commands, Map<String, Integer> intVars, Integer i, Map<String, String> strVars) {
        if (intVars.get(commands[i].split(" ")[1]) != Integer.valueOf(commands[i].split(" ")[3])) {
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
            interCmd(loop.toString().split(";"), intVars, strVars);
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

    public static void asg(String command, Map<String, Integer> intVars, Map<String, String> strVars) {
        switch (command.split(" ")[1]) {
            case "str":
                strVars.put(command.split(" ")[2], command.split(" ")[3]);
            break;
            case "int":
                intVars.put(command.split(" ")[2], Integer.valueOf(command.split(" ")[3]));
            break;
        }

    }

    public static void add(String command, Map<String, Integer> intVars) {
        intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[2]) + intVars.get(command.split(" ")[3]));
    }

    public static void sub(String command, Map<String, Integer> intVars) {
        intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[2]) - intVars.get(command.split(" ")[3]));
    }

    public static void mul(String command, Map<String, Integer> intVars) {
        intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[2]) * intVars.get(command.split(" ")[3]));
    }

    public static void div(String command, Map<String, Integer> intVars) {
        intVars.put(command.split(" ")[1], intVars.get(command.split(" ")[2]) / intVars.get(command.split(" ")[3]));
    }
}