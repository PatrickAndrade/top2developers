package com.example.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CommunicationActivity extends Activity {

    private TextView answerView;
    private EditText requestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        answerView = (TextView) findViewById(R.id.answer_text_view);
        requestView = (EditText) findViewById(R.id.request_edit_text);
    }

    public void sendRequest(View view) {
        
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait",
                "Currently sending a request to the server...", true);
        
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... params) {
                progressDialog.dismiss();
                return ConnectionActivity.sClient.fakeCommand(params[0]);
            };

            protected void onPostExecute(String result) {
                answerView.setText(result);
            };
        }.execute(requestView.getText().toString());
        
    }
}
