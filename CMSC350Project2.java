/* import all required java libraries */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author AArgento
 * @date 9 April 2017
 * @class CMSC 350
 * @purpose  Define all components required for GUI. Initialize GUI. Monitor GUI for user input.
 *
 */

public class CMSC350Project2 extends JFrame {

    //constructor
    private CMSC350Project2() {

        /* define JPanels */
        JPanel postfixExpression = new JPanel(new FlowLayout());
        JPanel constructTree = new JPanel(new FlowLayout());
        JPanel infixExpresson = new JPanel(new FlowLayout());

        /* add JPanels */
        add(postfixExpression);
        add(constructTree);
        add(infixExpresson);

        /* set layout and add JPanels */
        setLayout(new GridLayout(3,2));

        /* define components */
        JTextField textPostfixExpression = new JTextField(20);
        JTextField textInfixExpresssion = new JTextField(20);
        JLabel labelPostfixExpression = new JLabel("Enter Postfix Expression");
        JLabel labelInfixExpression = new JLabel("Infix Expression");
        JButton buttonConstructTree = new JButton("Construct Tree");

        /* add components to JPanels */
        postfixExpression.add(labelPostfixExpression);
        postfixExpression.add(textPostfixExpression);
        constructTree.add(buttonConstructTree);
        infixExpresson.add(labelInfixExpression);
        infixExpresson.add(textInfixExpresssion);

        /* listener for "Construct Tree" button action */
        buttonConstructTree.addActionListener((ActionEvent e) -> {
            PostFix PF = new PostFix();

            String expression = textPostfixExpression.getText();

            boolean valid = false;
            try {
                valid = PF.expressionTree(expression);
            } catch (InvalidToken invalidToken) {
                invalidToken.printStackTrace();
            }

            /* attempt to set infix expression of postfix expression */
            if (valid) try {
                textInfixExpresssion.setText(PF.getExpressionString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            /* attempt to close BufferedWriter */
            try {
                if (PF.bw != null)
                    PF.bw.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });

        /* set behavior and particulars of GUI */
        setTitle("Three Address Generator");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }//end constructor

    /* launch GUI */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new CMSC350Project2().setVisible(true));
    }//end main

}//end CMSC350Project2