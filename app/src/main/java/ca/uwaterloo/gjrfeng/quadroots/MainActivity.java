package ca.uwaterloo.gjrfeng.quadroots;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Locks app in portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // Method for the calculate button
    public void displayRoots(View view)
    {
        // Hide the keyboard; taken from: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard/46557572
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // Initialize EditTexts that hold user input for coefficients and round amount
        EditText aEditText = (EditText) findViewById(R.id.aEditText);
        EditText bEditText = (EditText) findViewById(R.id.bEditText);
        EditText cEditText = (EditText) findViewById(R.id.cEditText);
        EditText roundEditText = (EditText) findViewById(R.id.roundEditText);

        // Get user input

        double a = getDoubleInput(aEditText, 1.0);
        double b = getDoubleInput(bEditText, 0.0);
        double c = getDoubleInput(cEditText, 0.0);
        int round = Integer.parseInt(roundEditText.getText().toString());

        // Convert equation to an equivalent one working with "ints"
        double aFracPart = ((int) a) - a;
        double bFracPart = ((int) b) - b;
        double cFracPart = ((int) c) - c;

        // Multiply values by 10 until fractional part is 0
        while(aFracPart != 0 && bFracPart != 0 && cFracPart != 0)
        {
            a *= 10;
            b *= 10;
            c *= 10;

            aFracPart = ((int) a) - a;
            bFracPart = ((int) b) - b;
            cFracPart = ((int) c) - c;
        }



        // Initialize TextViews
        TextView rootCountTextView = (TextView) findViewById(R.id.rootCountTextView);
        TextView rootValueTextView = (TextView) findViewById(R.id.rootValueTextView);

        // Find how many roots there are via discriminant

        double root1, root2;

        // 2 real roots
        if(b * b >= 4 * a * c)
        {
            // Show result for number of roots in rootCountTextView
            rootCountTextView.setText("This equation has 2 real roots.\n\nThey are:");

            root1 = quadFormula(a, b, c, 1);
            root2 = quadFormula(a, b, c, -1);

            // Make the smaller root first
            if (root1 > root2)
            {
                double temp = root1;
                root1 = root2;
                root2 = temp;
            }

            // Round the roots
            root1 = roundTo(round, root1);
            root2 = roundTo(round, root2);

            // Show result for value of roots in rootValueTextView
            rootValueTextView.setText("x = " + root1 + "\nx = " + root2);
        }
        // no real roots
        else
        {
            rootCountTextView.setText("This equation has 2 imaginary roots.\n\nThey are:");

            String imRoot1 = imaginaryRoots(a, b, c, 1);
            String imRoot2 = imaginaryRoots(a, b, c, -1);

            rootValueTextView.setText("x = " + imRoot1 + "\nx = " + imRoot2);
        }
    }

    // Quadratic Formula Method: sign =  1 -> +
    //                           sign = -1 -> -
    // Requires: b^2 >= 4ac
    private static double quadFormula(double a, double b, double c, int sign)
    {
        return (-b + sign * sqrt(b * b - 4 * a * c))/(2 * a);
    }

    // Method that finds imaginary roots: sign =  1 -> +
    //                                    sign = -1 -> -
    private static String imaginaryRoots(double a, double b, double c, int sign)
    {
        String result = "";

        // ignore first term if it's 0, otherwise print it out
        if(b != 0)
        {
            result += (-b / (2 * a));
        }

        // print the root indicated by sign
        if(sign == 1)
        {
            result += " + ";
        }
        else
        {
            result += " - ";
        }
        result += (sqrt(- (b * b - 4 * a * c))/(2 * a)) + "i";

        return result;
    }

    // Method that gets user input as a Double in the EditTexts
    // Default values are: a -> 1.0
    //                     b -> 0.0
    //                     c -> 0.0
    private static double getDoubleInput(EditText editText, double defaultValue)
    {
        // When editText is empty, returns a set default value
        if(isEmpty(editText))
        {
            return defaultValue;
        }

        // Otherwise returns user's input as a Double
        return Double.parseDouble(editText.getText().toString());

    }

    // Method that checks if input in an EditText is empty
    // Produces true if empty, false otherwise
    private static boolean isEmpty(EditText editText)
    {
        return editText.getText().toString().trim().length() == 0;
    }

    // Method that rounds number to x decimal spots
    private static double roundTo(int x, double number)
    {
        double roundedNumber = Math.round((number * Math.pow(10.0, x))) / Math.pow(10.0, x);
        return roundedNumber;
    }

}
