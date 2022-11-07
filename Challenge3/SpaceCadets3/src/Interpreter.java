import java.io.*;
import java.util.ArrayList;

public class Interpreter {

    ArrayList<Variable> programVariables = new ArrayList<Variable>(); //Arraylist of Variable class where all the variables will be stored.

    public Interpreter(File sourceCode) throws IOException { //Where the file is converted to a buffered reader so it can be read line by line.
        BufferedReader br = new BufferedReader(new FileReader(sourceCode));
        interpret(br);
    }

    public void interpret(BufferedReader br) throws IOException {//Main interpreting function
        String currentLine = br.readLine();//Used to iterate through the lines of the buffered reader
        int count = 1;
        while(currentLine != null){//checks if the interpreter has not reached the end

            //Splits the line into instructions and then variables (and condition if it's a while loop or a special case when it is an end command)
            String instruction = currentLine.split(" ")[0];
            String variable = "";
            Integer condition = 0;
            if(instruction.equals("while")){
                variable = currentLine.split(" ")[1];
                condition = Integer.valueOf(currentLine.split(" ")[3]);
            }
            else if(instruction.equals("end;")){
                break;
            }
            else {
                try {//Added a try catch to handle when an invalid command is entered. When there is an error the code will highlight in which instruction the error is (an entire while loop counts as an instruction)
                    variable = currentLine.split(" ")[1].split(";")[0];
                }catch(Exception e){
                    System.err.println("Error in instruction "+count);
                }
            }
            switch (instruction) {//runs a certain method from the Variable class depending on which instruction is received except in the case of a while loop.
                case "while": //When a while instruction is reached the program will save all the following lines into an arraylist until it reaches an end; instruction on the same indentation level as it.
                    variable = variable.split(" ")[0];
                    ArrayList<String> whileLoop = new ArrayList<String>();
                    int whileLoopCount = 1;
                    do{
                        currentLine= br.readLine();
                        currentLine = currentLine.trim();
                        whileLoop.add(currentLine);
                        if(currentLine.contains("while")){
                            whileLoopCount++;
                        }
                        if(currentLine.contains("end")){
                            whileLoopCount--;
                        }

                    }while(!(currentLine.contains("end") && whileLoopCount==0));
                    //Once the arraylist is fully made, the arraylist is converted into a buffered reader and the loop alone will interpret.
                    // In the case of nested while loops the program will recursively interpret the while loops until the base cases/stopping conditions of each while loop is reached.
                    while(!(checkVariableExists(variable).getVarValue().equals(condition))){
                        BufferedReader wlwb = convertArrayToBR(whileLoop);
                        interpret(wlwb);
                    }
                    break;
                case "clear":
                    checkVariableExists(variable).clear();
                    break;
                case "incr":
                    checkVariableExists(variable).increment();
                    break;
                case "decr":
                    checkVariableExists(variable).decrement();
                    break;

                default://Any non standard instructions are dealt with here
                    if (variable.equals("=")) {//For all maths operations there will be a variable followed by an equals sign. This section will split the simple operation into its operators and operands and sets the variable before the = to the calculated value.
                        String operation = currentLine.split("=")[1].trim();
                        String operand1 = operation.split("-|/|\\+|\\*")[0].trim();
                        String operand2 = operation.split("-|/|\\+|\\*")[1].trim();
                        operation = operation.split(operand1)[1].split(operand2)[0].trim();
                        int op1 = 0;
                        int op2 = 0;

                        if (operand1.matches("^[0-9 ]+$")) {
                            op1 = Integer.parseInt(operand1);
                        } else {
                            op1 = checkVariableExists(operand1).getVarValue();
                        }

                        if (operand2.matches("^[0-9 ]+$")){
                            op2 = Integer.parseInt(operand2);
                        } else {
                            op2 = checkVariableExists(operand2).getVarValue();
                        }

                        switch (operation) {
                            case "+" -> checkVariableExists(instruction).setVarValue(op1 + op2);
                            case "-" -> checkVariableExists(instruction).setVarValue(op1 - op2);
                            case "/" -> checkVariableExists(instruction).setVarValue(op1 / op2);
                            case "*" -> checkVariableExists(instruction).setVarValue(op1 * op2);
                        }
                        break;
                    }
                    //For comments the line will always start with a double / . Comments can only be on a unique line and cannot follow code (like in java).
                    if(instruction.startsWith("//")){
                    //Ignore line as it is a comment
                    break;
                    }
                    else {//Anything else that is not defined simply outputs unrecognised command word.
                        System.out.println("Unrecognised Command Word in Instruction "+count);
                        break;
                    }
            }
            currentLine = br.readLine();
            count++;
        }
    }

    public Variable checkVariableExists(String variable){//Checks that a variable with a certain name already exists in the programVariables variable arraylist.
        // If it doesn't already exist then it creates a new one with the name specified.
        for(Variable v: programVariables){
            if(v.getVarName().equals(variable)){
                return v;
            }
        }
        programVariables.add(new Variable(variable));
        return programVariables.get(programVariables.size()-1);
    }

    public BufferedReader convertArrayToBR(ArrayList<String> arrayList){ //When iterating through a while loop all the indented operations of the while loops are saved to an arraylist rather than to a bufferedreader directly
        // so need to be converted to a buffered reader object in order to be used in the interpret method (recursion)
        StringBuilder buffer = new StringBuilder();
        for(String current : arrayList) {
            buffer.append(current).append("\n");
        }
        return new BufferedReader(new StringReader(buffer.toString()));
    }

}
