package com.csclab.hc.socketsample;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity implements View.OnClickListener {
    /**
     * Init Variable for IP page
     **/
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";

    /**
     * Init Variable for Page 1
     **/
    Button btnAdd;
    Button btnSub;
    Button btnMul;
    Button btnDiv;

    EditText inputNumTxt1;
    EditText inputNumTxt2;

    /** Init Variable for Page 2 **/
    TextView textResult;

    Button return_button;

    /** Init Variable **/
    String oper = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_page);
        inputIP = (EditText) findViewById(R.id.edIP);
        ipSend = (Button) findViewById(R.id.ipButton);

        ipSend.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                /** Func() for setup page 1 **/
                ipAdd = inputIP.getText().toString();
                jumpToMainLayout();
            }
        });
    }

    /**
     * Function for page 1 setup
     */
    public void jumpToMainLayout() {
        //TODO: Change layout to activity_main
        setContentView(R.layout.activity_main);
        inputNumTxt1 = (EditText)findViewById(R.id.inputNum1) ;
        inputNumTxt2 = (EditText)findViewById(R.id.inputNum2) ;

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMul = (Button) findViewById(R.id.btnMul);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

    /**
     * Function for onclick() implement
     */
    @Override
    public void onClick(View v) {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        // defines the button that has been clicked and performs the corresponding operation
        // write operation into oper, we will use it later for output
        //TODO: caculate result
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2 ;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2 ;
                break;
            case R.id.btnMul:
                oper = "*";
                result = num1 * num2 ;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2 ;
                break;
            default:
                break;
        }
        // HINT:Using log.d to check your answer is correct before implement page turning
        Log.d("The result from APP is",num1 + oper +num2 + "=" + result);
        //TODO: Pass the result String to jumpToResultLayout() and show the result at Result view
        jumpToResultLayout(new String(num1 + " " + oper + " " + num2 + " = " + result));
    }
    /*
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.csclab.hc.socketsample/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    */
    /** Function for page 2 (result page) setup */
    public void jumpToResultLayout(String resultStr){
        setContentView(R.layout.result_page);

        //TODO: Bind return_button and textResult form result view
        // HINT: findViewById()
        // HINT: Remember to change type
        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);

        if (textResult != null) {
            //TODO: Set the result text
            thread th = new thread(resultStr) ;
            th.start() ;
            textResult.setText(resultStr);
        }

        if (return_button != null) {
            //TODO: prepare button listener for return button
            // HINT:
            // mybutton.setOnClickListener(new View.OnClickListener(){
            //      public void onClick(View v) {
            //          // Something to do..
            //      }
            // }
            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    jumpToMainLayout();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.csclab.hc.socketsample/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class thread extends Thread{
        private String stringToSend;
        thread(String input) {
            stringToSend = input;
        }
        public void run(){
            try{
                int serverPort = 2000;
                Socket socket = new Socket(ipAdd, serverPort);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println(stringToSend);
                writer.flush();
            }catch (Exception e){
                System.out.println("Error" + e.getMessage());
            }
        }
    }
}
