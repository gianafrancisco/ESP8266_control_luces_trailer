package fransis.homecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleButton = null;
    private AndroidHttpClient client = null;
    private Context mContext = null;
    private String s = "http://192.168.43.209/";
    private EditText espAddress = null;
    private Button saveButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();

        SharedPreferences settings = getSharedPreferences("config",MODE_APPEND);
        s = settings.getString("esp_address","http://192.168.43.209/");

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    new DoRequest().execute(s,"on");
                } else {
                    // The toggle is disabled
                    new DoRequest().execute(s,"off");
                }
            }
        });

        ToggleButton toggleBalizas = (ToggleButton) findViewById(R.id.toggleButtonBalizas);
        toggleBalizas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    new DoRequest().execute(s,"balizas/on");
                } else {
                    // The toggle is disabled
                    new DoRequest().execute(s,"balizas/off");
                }
            }
        });

        espAddress = (EditText) findViewById(R.id.esp_address);
        espAddress.setText(s);

        saveButton = (Button)findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = espAddress.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_APPEND).edit();
                editor.putString("esp_address", s);
                editor.commit();
            }
        });


    }

    private class DoRequest extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            doRequest(strings[0],strings[1]);
            return null;
        }

        private void doRequest(String url, String action) {
            client = AndroidHttpClient.newInstance("android");
            HttpGet request = new HttpGet(url+action);
            try {
                HttpResponse response = client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
