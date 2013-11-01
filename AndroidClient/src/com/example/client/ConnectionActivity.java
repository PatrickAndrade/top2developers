package com.example.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectionActivity extends Activity {

    public static Client sClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    public void connectToClient(View view) {

        EditText ipAddress = (EditText) findViewById(R.id.edit_ip_address);
        EditText port = (EditText) findViewById(R.id.edit_port);
        
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait",
                "Currently connecting to the server...", true);

        new StartingClient() {

            @Override
            protected void onPostExecute(Client client) {
                progressDialog.dismiss();
                if (client != null) {
                    sClient = client;
                    Intent intent = new Intent(ConnectionActivity.this, CommunicationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConnectionActivity.this, "Could not connect!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute("128.179.144.218");

    }
}
