package com.example.kalkulator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView outputDisplay;
    private StringBuilder input = new StringBuilder();  // StringBuilder untuk menyimpan input pengguna

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputDisplay = findViewById(R.id.output_display);

        // Menghubungkan setiap tombol di layout XML ke backend
        assignButtonClickListener(R.id._button_0, "0");
        assignButtonClickListener(R.id._button_1, "1");
        assignButtonClickListener(R.id._button_2, "2");
        assignButtonClickListener(R.id._button_3, "3");
        assignButtonClickListener(R.id._button_4, "4");
        assignButtonClickListener(R.id._button_5, "5");
        assignButtonClickListener(R.id._button_6, "6");
        assignButtonClickListener(R.id._button_7, "7");
        assignButtonClickListener(R.id._button_8, "8");
        assignButtonClickListener(R.id._button_9, "9");
        assignButtonClickListener(R.id._button_add, "+");
        assignButtonClickListener(R.id._button_subtract, "-");
        assignButtonClickListener(R.id._button_multiply, "*");
        assignButtonClickListener(R.id._button_divide, "/");
        assignButtonClickListener(R.id._button_dot, ".");
        assignButtonClickListener(R.id._button_openbracket, "(");
        assignButtonClickListener(R.id._button_closedbracket, ")");

        // Tombol untuk menghapus satu karakter
        findViewById(R.id._button_c).setOnClickListener(v -> {
            if (input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
                outputDisplay.setText(input.toString());
            }
        });

        // Tombol untuk menghapus seluruh input
        findViewById(R.id._button_ac).setOnClickListener(v -> {
            input.setLength(0);
            outputDisplay.setText("0");
        });

        // Tombol untuk menghitung hasil perhitungan
        findViewById(R.id._button_equal).setOnClickListener(v -> {
            try {
                String result = evaluateExpression(input.toString());
                outputDisplay.setText(result);
            } catch (Exception e) {
                outputDisplay.setText("Error");
            }
        });
    }

    // Fungsi untuk menghubungkan tombol ke listener
    private void assignButtonClickListener(int buttonId, String value) {
        findViewById(buttonId).setOnClickListener(v -> {
            input.append(value);
            outputDisplay.setText(input.toString());
        });
    }

    // Fungsi untuk mengevaluasi ekspresi matematika
    private String evaluateExpression(String expression) {
        return String.valueOf(evaluate(expression));
    }

    // Fungsi evaluasi ekspresi matematika menggunakan Shunting Yard Algorithm
    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        // Stack untuk angka
        Stack<Double> values = new Stack<>();

        // Stack untuk operator
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            // Jika karakter saat ini adalah angka (termasuk angka desimal)
            if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.')) {
                    sbuf.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            }

            // Jika karakter saat ini adalah '('
            else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            }

            // Jika karakter saat ini adalah ')'
            else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            }

            // Jika karakter saat ini adalah operator
            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        // Menyelesaikan sisa operator
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    // Mengembalikan true jika 'op2' memiliki preseden lebih tinggi atau sama dengan 'op1'
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    // Fungsi untuk menerapkan operator pada dua angka
    public static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
}