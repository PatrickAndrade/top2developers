package communicationActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import entry.ConnectionActivity;

public abstract class SendingRequestTask extends AsyncTask<Object, Void, String> {

    @Override
    protected String doInBackground(Object... args) {
        ((ProgressDialog)args[1]).dismiss();
        return ConnectionActivity.sClient.fakeCommand((String)args[0]);
    }

    protected abstract void onPostExecute(String answer);
}