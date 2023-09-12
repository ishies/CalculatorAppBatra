package com.example.calculatorappbatra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TypedPage extends AppCompatActivity {

    public void returnHome (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

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

    String operator;

    // Function adapted from Geeks for Geeks:
    // https://www.geeksforgeeks.org/how-to-programmatically-hide-android-soft-keyboard/
    public void closeKeyboard(View v)
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void resetButtonStyles() {
        ViewCompat.setBackgroundTintList(
                findViewById(R.id.key_plus),
                ColorStateList.valueOf(R.color.TextAndButtons));
        Button b = (Button) this.findViewById(R.id.key_plus);
        b.setTextColor(R.color.selectedButton);

        ViewCompat.setBackgroundTintList(
                findViewById(R.id.key_minus),
                ColorStateList.valueOf(R.color.TextAndButtons));
        b = (Button) this.findViewById(R.id.key_minus);
        b.setTextColor(R.color.selectedButton);

        ViewCompat.setBackgroundTintList(
                findViewById(R.id.key_times),
                ColorStateList.valueOf(R.color.TextAndButtons));
        b = (Button) this.findViewById(R.id.key_times);
        b.setTextColor(R.color.selectedButton);

        ViewCompat.setBackgroundTintList(
                findViewById(R.id.key_over),
                ColorStateList.valueOf(R.color.TextAndButtons));
        b = (Button) this.findViewById(R.id.key_over);
        b.setTextColor(R.color.selectedButton);

    }

    @SuppressLint("ResourceAsColor")
    public void setOperator(View v) {
        resetButtonStyles();
        ViewCompat.setBackgroundTintList(
                v,
                ColorStateList.valueOf(R.color.selectedButton));
        Button b = (Button) v;
        b.setTextColor(R.color.selectedButton);

        TextView op = findViewById(v.getId());
        operator = op.getText().toString();
    }

    public void calculate (View v) {
        EditText num1 = findViewById(R.id.number1);
        String n1 = num1.getText().toString();

        EditText num2 = findViewById(R.id.number2);
        String n2 = num2.getText().toString();

        String evaluate = n1 + operator + n2;
        Log.i("ishaan", evaluate);
        String answer = "";
        if (!operator.equals("")) {
            try {
                double roundedNum = (double)(Math.round(eval(evaluate) * 1000)) / 1000;
                answer = "" + roundedNum;
                if (answer.contains("Infinity")) {
                    answer = "CANNOT COMPUTE";
                }
            } catch (Exception e) {
                answer = "CANNOT COMPUTE";
            }
            evaluate = "";
            num1.setText("");
            num2.setText("");
            // resetButtonStyles();
        }
        else {
            answer = "please select an operator";
        }

        TextView ans = (TextView) this.findViewById(R.id.answer);
        ans.setText(answer);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typed_page);
    }
}