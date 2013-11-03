package communicationActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import app.entry.ConnectionActivity;
import app.entry.R;


public class CommunicationActivity extends Activity {

    private EditText requestView;
    private ListView itemsView;
    private ArrayAdapter<ArrayAdapterItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        requestView = (EditText) findViewById(R.id.request_edit_text);
        itemsView = (ListView) findViewById(R.id.items_list_view);
        
        adapter = new ArrayAdapter<ArrayAdapterItem>(this, R.layout.item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ItemTextView itemTextView;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item,
                            parent, false);

                    itemTextView = new ItemTextView();
                    itemTextView.setItem((TextView) convertView
                            .findViewById(R.id.answer_text_view));

                    convertView.setTag(itemTextView);
                } else {
                    itemTextView = (ItemTextView) convertView.getTag();
                }

                ArrayAdapterItem answer = adapter.getItem(position);
                itemTextView.getItem().setText(answer.getAnswer());

                return convertView;
            }
        };

        itemsView.setAdapter(adapter);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        ConnectionActivity.sClient.disconnect();
    }

    public void sendRequest(View view) {
        
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait",
                "Currently sending a request to the server...", true);
        
        String request = requestView.getText().toString();
        
        adapter.add(new ArrayAdapterItem(request));
        
        if (request.equals("") || request == null) {
            return;
        }
        
        new SendingRequestTask() {
            
            @Override
            protected void onPostExecute(String answer) {
                
                adapter.add(new ArrayAdapterItem(answer));
                adapter.notifyDataSetChanged();
                
                // Ensures that the list view always show the bottom
                itemsView.setSelection(adapter.getCount() - 1); 
            }
        }.execute(request, progressDialog);
        
    }
    
    public void clearConsole(View view) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        
        requestView.setText("");
    }

}
