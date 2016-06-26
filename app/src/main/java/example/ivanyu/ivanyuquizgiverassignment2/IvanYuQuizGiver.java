package example.ivanyu.ivanyuquizgiverassignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import android.widget.ScrollView;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import org.json.JSONObject;
import org.json.JSONException;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v7.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.util.*;
/**
 *  The IvanYuQuizGiver class generates questions and answer choices for the user
 *  and records the user's overall score: the number of correct answers given over the
 *  number of attempts the user has taken, as well as the percentage of correct answers.
 *  The user is provided buttons to generate a new question, exit the program, and select answer choices.
 *
 *  @author Ivan Yu
 *  @version 1.0
 *  @since 2016-06-22
 */

public class IvanYuQuizGiver extends AppCompatActivity {

    // These strings are respectively set to the token, question and the specified correct answer contained in the JSONObject retrieved from the server.
    static String token = "";
    static String question = "";
    static String correct;
    // The score integer represents the number of correct answer given by the user. The attempt integer is the number of questions answered by the user.
    static int score = 0;
    static int attempts = 0;
    // buttonA to buttonF will be answer choices for a given question. An answer is given by the user by clicking any of these buttons.
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    Button buttonE;
    Button buttonF;
    // nextQuestionButton will be set to the button for generating a new question. The exitButton will allow the user to exit the program.
    static Button nextQuestionButton;
    Button exitButton;
    // The question generated will be displayed in this TextView, and singleton will provide the single requestqueue to be used by the program.
    TextView questionView;
    static Singleton singleton;

    /**
     * This method is used to set the initial screen by initializing the button to generate a question,
     * the button to exit the program, and the initial text view that records the score and attempts of the user.
     *
     * @param savedInstanceState This is the only parameter to the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivan_yu_quiz_giver);
        // The static singleton object is instantiated, and the nextQuestionButton is set to the button from the xml file.
        singleton = Singleton.getInstance(this);
        nextQuestion();
        nextQuestionButton = (Button) findViewById(R.id.nextQButton);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method calls the nextQuestion method and then disables the functionality of the
             * nextQuestionButton.
             * @param view - This parameter is the view that was clicked to execute the method,
             *                which in this case is nextQuestionButton.
             */
            @Override
            public void onClick(View view) {
                nextQuestion();
                view.setEnabled(false);
            }
        });
        // exitButton is set to the button made in the xml file.
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method terminates the program.
             * @param view - This parameter is exitButton when it is clicked.
             */
            @Override
                public void onClick(View view) {
                    System.exit(0);
                }
        });
    }




    /**
     *  This method makes json object requests to the server and uses the Singleton class's methods
     *  to add these requests to a RequestQueue. Once the requests are successfully made, a question, a display
     *  of the token retrieved from the server, and a set of answers are generated and placed on the screen.
     */
    private void nextQuestion() {
        // To send the username and password to the server, the url argument for the json object request is written as such.
        String url = "http://sfsuswe.com/413/get_token?username=ivnyu&password=912425129";
        JsonObjectRequest jsonRequestToken = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            /**
             * This method is called when the json object is successfully retrieved from the server.
             * @param response - This parameter is the JSONObject returned by the server.
             *                  The token can be retrieved from this parameter.
             */
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // getString takes in a string argument corresponding to the key, and returns the value corresponding to that key.
                        token = response.getString("token");
                    } catch (JSONException e) {
                        // If a JSONException is caught, the exception's message will be displayed on the screen.
                        questionView.setText(e.getMessage());
                    } catch (Exception e) {
                        // If an Exception other than a JSONException is caught, the exception message will be displayed as well.
                        questionView.setText(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
            /**
             * This method is called if the attempt to request the json object has failed.
             * questionView will display the error message if this method is called.
             * @param error - This parameter is the error associated with the failure in requesting the json object.
             */
                @Override
                public void onErrorResponse(VolleyError error) {
                    questionView.setText(error.getMessage());
                }
            });
        /**
         * To complete the json object request to the server, the request is added to the Singleton class's RequestQueue.
         * The previousQuestion variable will be used to check if the new question is the same as the last.
         * The next json object request will be used to get the question, answers and the correct answer from the server.
         */
            singleton.addRequest(jsonRequestToken);
            final String previousQuestion = question;
            JsonObjectRequest jsonQuestionRequest = new JsonObjectRequest(Request.Method.GET, "http://sfsuswe.com/413/get_question?token=" + token, new Response.Listener<JSONObject>() {
                /**
                 * This method is executed upon successful request of the json object.
                 * In such case, new contents - the token, question and answer sets represented as buttons - will be
                 * displayed on the screen, above the previous contents.
                 * @param response - This parameter is the json object returned by the server upon successful request.
                 *                   From this parameter, the question, answers and the correct answer will be retrieved.
                 */
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        /**
                         * TextViews for questions and the token, and buttons for the answers, will be added to this LinearLayout.
                         * newLineView will be used as a margin to separated a set of a question and answers from another.
                         * */
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linLayout);
                        TextView newLineView = new TextView(getApplicationContext());
                        // The height set for newLineView is in scale independent pixels (sp).
                        newLineView.setHeight((int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                        questionView = new TextView(getApplicationContext());
                        // The minimum height set for the question view is given in scale independent pixels.
                        questionView.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
                        //The questionView's width spans he entire width of the layout it was placed in, and it's texts are centered.
                        questionView.setWidth(LayoutParams.FILL_PARENT);
                        questionView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        // the questionView's text size is given in scale independent pixels, and the texts' color is set to black.
                        questionView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                        questionView.setTextColor(Color.parseColor("black"));
                        // The question variable is set to the "question" key's value contained in the JSONObject's map.
                        question = response.getString("question");
                        // This TextView is for displaying the token that was retrieved and used to generate the question.
                        TextView tokenView = new TextView(getApplicationContext());
                        tokenView.setHeight(48);
                        // the tokenView's text size is given in scale independent pixels and its width spans the width of the layout it's placed in.
                        tokenView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        tokenView.setWidth(LayoutParams.FILL_PARENT);
                        // The text for the tokenView (the token retrieved) is aligned at the center and its text is colored as black.
                        tokenView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tokenView.setText("(token: "+token +")");
                        tokenView.setTextColor(Color.parseColor("black"));
                        /**
                         * If the question is not the same as the last question, a new token, question and set of answers will be displayed
                         * on top of the previous question.
                         */
                        if(!question.equals(previousQuestion)) {
                            // the question is set as the questionView's text and buttonA to buttonF are buttons for the answer choices.
                            questionView.setText(question);
                            buttonA = new Button(getApplicationContext());
                            buttonB = new Button(getApplicationContext());
                            buttonC = new Button(getApplicationContext());
                            buttonD = new Button(getApplicationContext());
                            buttonE = new Button(getApplicationContext());
                            buttonF = new Button(getApplicationContext());
                            // This set of codes allows all the buttons to have its text centered.
                            buttonA.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            buttonB.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            buttonC.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            buttonD.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            buttonE.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            buttonF.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            // This set of codes allows all the buttons to have a grey background.
                            buttonA.setBackgroundColor(Color.parseColor("#d3d3d3"));
                            buttonB.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            buttonC.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            buttonD.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            buttonE.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            buttonF.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            // This set of codes makes the texts in the button black
                            buttonA.setTextColor(Color.parseColor("black"));
                            buttonB.setTextColor(Color.parseColor("black"));
                            buttonC.setTextColor(Color.parseColor("black"));
                            buttonD.setTextColor(Color.parseColor("black"));
                            buttonE.setTextColor(Color.parseColor("black"));
                            buttonF.setTextColor(Color.parseColor("black"));
                            //This set of codes specifies the minimum height the buttons can have. The height is specified in scale independent pixels.
                            buttonA.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
                            buttonB.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
                            buttonC.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
                            buttonD.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
                            buttonE.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
                            buttonF.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
                            // The static String data field "correct" is assigned to the value contained in the key "correct" in the JSONObject.
                            correct = response.getString("correct");

                            /**
                             *  The following set of if-else statements checks if answer choices a, b, c, d, e and f are provided
                             *  for the question generated. No buttons will be displayed for answer choices that do not exist for the
                             *  given question. The onClick overridden method for all buttons enables the nextQuestionButton,
                             *  increments the score (if the button clicked is the right answer) and the number of attempts,
                             *  informs the user if the correct or wrong answer is clicked, and disables all of the answer buttons
                             *  so that the user may not give another answer for an answered question. In each if statements,
                             *  the JSONObject's get method is used to get the value (the actual answer) corresponding the the JSONObject's keys
                             *  representing the answer choices (a, b, c, d, e or f). If this value is not null, a button will be displayed for it,
                             *  but if the value is null, no button will be displayed for it.
                             */
                            if (!(response.get("answer_a").equals(null))) {
                                // Same as every answer choices encoded in the JSONObject from the server, if the answer choice is not null, a button will be displayed for it.
                                buttonA.setText("A. " + response.getString("answer_a"));
                                buttonA.setOnClickListener(new View.OnClickListener() {
                                    /**
                                     * This method increments the score and attempts appropriately, notifies the user of their correct or incorrect
                                     * answer, and disable all answer buttons. The onClick method for answer choices a, b, c, d, e and f
                                     * executes the same task.
                                     * @param view - This parameter is the view that was clicked, which in this case, will be buttonA.
                                     */
                                    @Override
                                    public void onClick(View view) {
                                        // The nextQuestionButton is enabled so the user can get another question after answering the current question.
                                        nextQuestionButton.setEnabled(true);
                                        // If buttonA is selected and if it's text is the correct answer, score is incremented by 1 and the user is notified that the correct answer was chosen.
                                        if (correct.equals("a")) {
                                            score++;
                                            buttonA.setText(buttonA.getText().toString() + " --CORRECT");
                                            buttonA.setTextColor(Color.parseColor("#458B00"));
                                            // Else if the user clicked on buttonA and it was the wrong answer, the user is made aware that the wrong answer was selected.
                                        } else {
                                            buttonA.setText(buttonA.getText().toString() + " --WRONG");
                                            buttonA.setTextColor(Color.parseColor("red"));
                                        }
                                        // The number of attempts is incremented when clicking any of the answer buttons, and then the updated score is displayed.
                                        attempts++;
                                        TextView scoreView = (TextView) findViewById(R.id.score);
                                        scoreView.setText("Score: " + score + " / " + attempts + "\nCorrect%: " + String.format("%.2f", (((double) score)/((double)attempts)*100)) + "%");
                                        // The answer buttons are disabled so the user won't be able to answer the same question after having answered it the first time.
                                        disableAnswerButtons();
                                    }
                                });
                            } else {
                                // if the value corresponding to the answer_a key was null, no button will be displayed for this answer choice.
                                buttonA.setVisibility(View.INVISIBLE);
                            }
                            /**
                             * The explanations for the remaining if-else statements for answer choices b to f are the same
                             * as that of the comments explaining the first if-else statement for the answer choice "a".
                             */
                            if (!(response.get("answer_b").equals(null))) {
                                buttonB.setText("B. " + response.getString("answer_b"));
                                buttonB.setOnClickListener(new View.OnClickListener() {
                                    /**
                                     * This method executes when buttonB is clicked. The scores and attempts are updated appropriately,
                                     * the answer buttons are disabled and the user is notified for having answered correctly or incorrectly
                                     * when this method executes.
                                     * @param view - This parameter is the View that was clicked to execute the method, which is buttonB.
                                     */
                                    @Override
                                    public void onClick(View view) {
                                        nextQuestionButton.setEnabled(true);
                                        if (correct.equals("b")) {
                                            score++;
                                            buttonB.setText(buttonB.getText().toString() + " --CORRECT");
                                            buttonB.setTextColor(Color.parseColor("#458B00"));
                                        } else {
                                            buttonB.setText(buttonB.getText().toString() + " --WRONG");
                                            buttonB.setTextColor(Color.parseColor("red"));
                                        }
                                        attempts++;
                                        TextView scoreView = (TextView) findViewById(R.id.score);
                                        scoreView.setText("Score: " + score + " / " + attempts + "\nCorrect%: " + String.format("%.2f", (((double) score)/((double)attempts)*100)) + "%");
                                        disableAnswerButtons();
                                    }
                                });
                            } else {
                                // buttonB will not be displayed if an answer choice "B" isn't provided.
                                buttonB.setVisibility(View.INVISIBLE);
                            }
                            if (!(response.get("answer_c").equals(null))) {
                                buttonC.setText("C. " + response.getString("answer_c"));
                                buttonC.setOnClickListener(new View.OnClickListener() {
                                    /**
                                     * This method executes when buttonC is clicked. The function of this method
                                     * is the same as that of buttonA, buttonB and the remaining answer buttons.
                                     * @param view - This parameter is buttonC once clicked.
                                     */
                                    @Override
                                    public void onClick(View view) {
                                        nextQuestionButton.setEnabled(true);
                                        if (correct.equals("c")) {
                                            score++;
                                            buttonC.setText(buttonC.getText().toString() + " --CORRECT");
                                            buttonC.setTextColor(Color.parseColor("#458B00"));
                                        } else {
                                            buttonC.setText(buttonC.getText().toString() + " --WRONG");
                                            buttonC.setTextColor(Color.parseColor("red"));
                                        }
                                        attempts++;
                                        TextView scoreView = (TextView) findViewById(R.id.score);
                                        scoreView.setText("Score: " + score + " / " + attempts + "\nCorrect%: " + String.format("%.2f", (((double) score)/((double)attempts)*100)) + "%");
                                        disableAnswerButtons();
                                    }
                                });
                            } else {
                                // buttonC will not be visible if an answer choice for "C" is not provided.
                                buttonC.setVisibility(View.INVISIBLE);
                            }
                            if (!(response.get("answer_d").equals(null))) {
                                buttonD.setText("D. " + response.getString("answer_d"));
                                buttonD.setOnClickListener(new View.OnClickListener() {
                                    /**
                                     * This method executes once buttonD is clicked.
                                     * @param view - This parameter is buttonD.
                                     */
                                    @Override
                                    public void onClick(View view) {
                                        nextQuestionButton.setEnabled(true);
                                        if (correct.equals("d")) {
                                            score++;
                                            buttonD.setText(buttonD.getText().toString() + " --CORRECT");
                                            buttonD.setTextColor(Color.parseColor("#458B00"));
                                        } else {
                                            buttonD.setText(buttonD.getText().toString() + " --WRONG");
                                            buttonD.setTextColor(Color.parseColor("red"));
                                        }
                                        attempts++;
                                        TextView scoreView = (TextView) findViewById(R.id.score);
                                        scoreView.setText("Score: " + score + " / " + attempts + "\nCorrect%: " + String.format("%.2f", (((double) score)/((double)attempts)*100)) + "%");
                                        disableAnswerButtons();
                                    }
                                });
                            } else {
                            buttonD.setVisibility(View.INVISIBLE);
                            }
                            if (!(response.get("answer_e").equals(null))) {
                                buttonE.setText("E. " + response.getString("answer_e"));
                                buttonE.setOnClickListener(new View.OnClickListener() {
                                    /**
                                     * This method executes once buttonE is clicked.
                                     * @param view - This parameter will be buttonE when clicked.
                                     */
                                    @Override
                                    public void onClick(View view) {
                                        nextQuestionButton.setEnabled(true);
                                        if (correct.equals("e")) {
                                            score++;
                                            buttonE.setText(buttonE.getText().toString() + " --CORRECT");
                                            buttonE.setTextColor(Color.parseColor("#458B00"));
                                        } else {
                                            buttonE.setText(buttonE.getText().toString() + " --WRONG");
                                            buttonE.setTextColor(Color.parseColor("red"));
                                        }
                                        attempts++;
                                        TextView scoreView = (TextView) findViewById(R.id.score);
                                        scoreView.setText("Score: " + score + " / " + attempts + "\nCorrect%: " + String.format("%.2f", (((double) score)/((double)attempts)*100)) + "%");
                                        disableAnswerButtons();
                                    }
                                });
                            } else {
                                // buttonE will not be visible if the answer choice "E" isn't provided for the question.
                                buttonE.setVisibility(View.INVISIBLE);
                            }
                            if (!(response.get("answer_f").equals(null))) {
                                buttonF.setText("F. " + response.getString("answer_f"));
                                buttonF.setOnClickListener(new View.OnClickListener() {
                                    /**
                                     * This method executes when buttonF is clicked. Again, all of the functions of the onClick methods
                                     * for buttonA, buttonB, buttonC, buttonD, buttonE and buttonF are the same.
                                     * @param view - This parameter is buttonF when it is clicked.
                                     */
                                    @Override
                                    public void onClick(View view) {
                                        nextQuestionButton.setEnabled(true);
                                        if (correct.equals("f")) {
                                            score++;
                                            buttonF.setText(buttonF.getText().toString() + " --CORRECT");
                                            buttonF.setTextColor(Color.parseColor("#458B00"));
                                        } else {
                                            buttonF.setText(buttonF.getText().toString() + " --WRONG");
                                            buttonF.setTextColor(Color.parseColor("red"));
                                        }
                                        attempts++;
                                        TextView scoreView = (TextView) findViewById(R.id.score);
                                        scoreView.setText("Score: " + score + " / " + attempts + "\nCorrect%: " + String.format("%.2f", (((double) score)/((double)attempts)*100)) + "%");
                                        disableAnswerButtons();
                                    }
                                });
                            } else {
                                // buttonF will not be displayed on the screen if the answer choice for "F" isn't provided for the question.
                                buttonF.setVisibility(View.INVISIBLE);
                            }
                            // This set of TextViews (line1-line5) will be used to generate spaces between the buttons.
                            TextView line1 = new TextView(getApplicationContext());
                            TextView line2 = new TextView(getApplicationContext());
                            TextView line3 = new TextView(getApplicationContext());
                            TextView line4 = new TextView(getApplicationContext());
                            TextView line5 = new TextView(getApplicationContext());
                            // Each of these TextViews will have a height of 10 pixels.
                            line1.setHeight(10);
                            line2.setHeight(10);
                            line3.setHeight(10);
                            line4.setHeight(10);
                            line5.setHeight(10);
                            // Each buttons and TextViews are placed on the screen such that the token and the question is above the answer choices.
                            linearLayout.addView(buttonF, 0);
                            linearLayout.addView(line3, 0);
                            linearLayout.addView(buttonE, 0);
                            linearLayout.addView(line2, 0);
                            linearLayout.addView(buttonD, 0);
                            linearLayout.addView(line1, 0);
                            linearLayout.addView(buttonC, 0);
                            linearLayout.addView(line4, 0);
                            linearLayout.addView(buttonB, 0);
                            linearLayout.addView(line5, 0);
                            linearLayout.addView(buttonA, 0);
                            linearLayout.addView(questionView, 0);
                            linearLayout.addView(tokenView, 0);
                            // newLineView acts as a margin from the top for the entire set of token, question and answer choices.
                            linearLayout.addView(newLineView, 0);
                        }else{
                            /**
                             * If the next question generated is the same as the last question, nextQuestion is recursively called,
                             * and a new question will be generated until the question retrieved is different from the previous one.
                             * Therefore, questions that are the same as the last answered question will not be displayed on the screen.
                             */
                            nextQuestion();
                        }
                    } catch (JSONException e) {
                        // If a JSONException is thrown, its message is displayed on the screen.
                        questionView.setText(e.getMessage());
                    } catch (Exception e) {
                        // Any Exception thrown other than JSONException will result in the error message being displayed on the screen.
                        questionView.setText(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                /**
                 * This method gets called when there is an error in requesting the json object.
                 * @param error - This parameter is the error associated in failure to request the json object.
                 */
                @Override
                public void onErrorResponse(VolleyError error) {
                    // The error message is displayed on the screen upon request failure.
                    questionView = new TextView(getApplicationContext());
                    questionView.setText(error.getMessage());
                }
            });
            // The JSONObject is added to the RequestQueue of the Singleton class, completing the attempt to request a JSONObject from the server.
            singleton.addRequest(jsonQuestionRequest);
        }

    /**
     * This method disables all the answer buttons.
     */
    private void disableAnswerButtons(){
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
        buttonE.setEnabled(false);
        buttonF.setEnabled(false);
    }

}
