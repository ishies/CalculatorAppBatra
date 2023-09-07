package com.example.calculatorappbatra;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class ButtonPage extends AppCompatActivity {

    // This function takes a string input, and parses it as a mathematical expression. It then returns the answer in double form.
    // Source: https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    String evaluate = "";
    public void addToCalc(View v) {
        int i = v.getId();

        if (i == R.id.key_0) {
            evaluate += 0;
        }
        if (i == R.id.key_1) {
            evaluate += 1;
        }
        if (i == R.id.key_2) {
            evaluate += 2;
        }
        if (i == R.id.key_3) {
            evaluate += 3;
        }
        if (i == R.id.key_4) {
            evaluate += 4;
        }
        if (i == R.id.key_5) {
            evaluate += 5;
        }
        if (i == R.id.key_6) {
            evaluate += 6;
        }
        if (i == R.id.key_7) {
            evaluate += 7;
        }
        if (i == R.id.key_8) {
            evaluate += 8;
        }
        if (i == R.id.key_9) {
            evaluate += 9;
        }
        if (i == R.id.key_plus) {
            evaluate += " + ";
        }
        if (i == R.id.key_minus) {
            evaluate += " - ";
        }
        if (i == R.id.key_times) {
            evaluate += " * ";
        }
        if (i == R.id.key_over) {
            evaluate += " / ";
        }

        Button b = (Button) this.findViewById(R.id.answer);
        b.setText(evaluate);
    }

    public void evaluate (View v) {
        Button b = (Button) this.findViewById(R.id.answer);
        String answer = "";
        try {
                answer = "" + eval(evaluate) + "";
                if (answer.contains("Infinity")) {
                    answer = "CANNOT COMPUTE";
                }
            }
        catch(Exception e){
                answer = "CANNOT COMPUTE";
            }

            b.setText(answer);
            evaluate = "";
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_page);
    }
}