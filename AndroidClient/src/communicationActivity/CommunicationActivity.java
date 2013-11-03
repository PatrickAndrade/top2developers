package communicationActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.client.R;

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
        
        String request = requestView.getText().toString();
        
        if (request.equals("") || request == null) {
            return;
        }
        
        new SendingRequestTask() {
            
            @Override
            protected void onPostExecute(String answer) {
                answerView.setText(answer);
            }
        }.execute(request, progressDialog);
        
    }
}
