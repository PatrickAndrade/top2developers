package entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.client.Client;
import com.example.client.R;
import com.example.client.ConnectingClientTask;
import communicationActivity.CommunicationActivity;

public class ConnectionActivity extends Activity {

    public static Client sClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    public void connectToClient(View view) {

        //EditText ipAddress = (EditText) findViewById(R.id.edit_ip_address);
        //EditText port = (EditText) findViewById(R.id.edit_port);
        
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait",
                "Currently connecting to the server...", true);

        new ConnectingClientTask() {

            @Override
            protected void onPostExecute(Client client) {
                progressDialog.dismiss();
                if (client != null) {
                    sClient = client;
                    Intent intent = new Intent(ConnectionActivity.this, CommunicationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConnectionActivity.this, "Could not connect to the server specified!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute("192.168.0.18");

    }
}
