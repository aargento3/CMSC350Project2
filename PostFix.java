/* import all required java libraries */
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.*;

/**
 *
 * @author AArgento
 * @date 9 April 2017
 * @class CMSC 350
 * @purpose Accept an arithmetic expression of unsigned integers in postfix expression and build an
 *          arithmetic expression tree that represents that expression. From that tree populate a parenthesized
 *          infix expression and generate a file that contains three address format instructions.
 *
 */

 class PostFix {

    private JOptionPane frame = new JOptionPane();

    private static final String FILENAME = "AddressOutput.txt";

    BufferedWriter bw;

    /* PostFix constructor */
    PostFix(){
        try {
            bw = new BufferedWriter(new FileWriter(FILENAME, true));
        } catch(Exception e ) {
            out.println("Output file cannot be opened.");
            exit(0);
        }
    }

    private Node root;

    /* class to define tree nodes */
    private abstract class Node {
        String Data;
        Node leftNode;
        Node rightNode;
        int processed = 0;
        String registerName = "";
        abstract public void setNodeData(String data, Node leftNode, Node rightNode);
        abstract public String getData();
    }//end Node

    /* OperatorNode class derived from Node */
    private class OperatorNode extends Node {
        public void setNodeData(String operator, Node leftNode, Node rightNode) {
            this.Data = operator;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        public String getData() {
            return this.Data;
        }
    }//end OperatorNode

    /* OperandNode class derived from Node */
    private class OperandNode extends Node {
        public void setNodeData(String operator, Node leftNode, Node rightNode){
            this.Data=operator;
        }

        public String getData() {
            return this.Data;
        }
    }//end OperandNode

    /* build arithmetic exression tree that represents postfix expression */
    boolean expressionTree(String expression) throws InvalidToken{

        String[] splitExpression = expression.split("");
        int length = splitExpression.length;
        Node[] nodeList = new Node[length];

        int z = 0;

        for (String k : splitExpression) {
            try {
                int j = Integer.parseInt(k);
                nodeList[z] = new OperandNode();
                nodeList[z].setNodeData(k, null, null);

            } catch (RuntimeException e) {
                if (!(k.equals("+") || k.equals("-") || k.equals("*") || k.equals("/"))) {
                    JOptionPane.showMessageDialog(frame, "Invalid token: " + k, "Token Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new InvalidToken();
                } else {
                    nodeList[z] = new OperatorNode();
                    nodeList[z].setNodeData(k, null, null);
                }
            }
            z ++;
        }

        int x = length;
        int y = 0;

        while(x > 1) {
            for(int i = 0; i < length; i ++) {
                if(nodeList[i] != null) {
                    if (nodeList[i].processed != 1) {
                        try {
                            int j = Integer.parseInt(nodeList[i].getData());
                        } catch(Exception e) {
                            Node temp = new OperatorNode();
                            temp.setNodeData(nodeList[i].getData(), nodeList[i - 2], nodeList[i - 1]);
                            temp.processed = 1;
                            temp.registerName = "R"+ y ++;
                            nodeList[i-2] = temp;
                            arraycopy(nodeList, i - 1 + 2, nodeList, i - 1, length - 2 - (i - 1));
                            nodeList[length - 1] = null;
                            nodeList[length - 2] = null;
                            x = x - 2;
                            i = - 1;
                        }
                    }
                } else {
                    break;
                }
            }
        }
        root = nodeList[0];

        return true;
    }

    String getExpressionString() throws IOException {
        return generateInfix(root);
    }

    /* generates Infix expression to be populated in GUI text box. Also populates three address instructions */
    private String generateInfix(Node root) throws IOException {

        String inFix;
        inFix = "";

        if(root != null) {
            if(root.processed == 1)
                inFix ="(";
            if(root.leftNode != null && root.rightNode != null) {
                inFix = inFix + generateInfix(root.leftNode);
            }
            inFix = inFix + root.Data;

            if(root.leftNode != null && root.rightNode != null) {
                inFix = inFix + generateInfix(root.rightNode);
            }

            if(root.processed == 1)
                inFix = inFix + ")";

            if(root.leftNode != null && root.rightNode != null) {
                String left;
                if(root.leftNode.processed == 1) {
                    left = root.leftNode.registerName;
                } else {
                    left = root.leftNode.Data;
                }

                String right;
                if(root.rightNode.processed == 1) {
                    right = root.rightNode.registerName;
                } else right = root.rightNode.Data;

                switch (root.Data) {
                    case "+":
                        bw.write("Add " + root.registerName + " " + left + " " + right);
                        break;
                    case "-":
                        bw.write("Sub " + root.registerName + " " + left + " " + right);
                        break;
                    case "*":
                        bw.write("Mul " + root.registerName + " " + left + " " + right);
                        break;
                    case "/":
                        bw.write("Div " + root.registerName + " " + left + " " + right);
                        break;
                }

                bw.newLine();

            }
        }

        return inFix;

    }//end generateInfix

}//end PostFix