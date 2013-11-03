package com.example.client;

import java.net.InetSocketAddress;

import android.os.AsyncTask;

public abstract class ConnectingClientTask extends AsyncTask<String, Client, Client> {

    @Override
    protected Client doInBackground(String... addresses) {
        Client client = new Client(new InetSocketAddress(addresses[0], 22122));
        if (client.connect()){
            return client;
        }
        return null;
    }

    protected abstract void onPostExecute(Client client);
}
