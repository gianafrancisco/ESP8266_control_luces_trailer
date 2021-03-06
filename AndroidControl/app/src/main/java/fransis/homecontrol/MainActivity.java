package fransis.homecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {

    private ToggleButton toggle = null;
    private ToggleButton toggleBalizas = null;
    private Context mContext = null;
    private String s = "http://192.168.43.209/";
    private EditText espAddress = null;
    private Button saveButton = null;
    private ArduinoService service = null;
    private Retrofit retrofit = null;
    private DoRequest hiloSecundario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();


        SharedPreferences settings = getSharedPreferences("config",MODE_APPEND);
        s = settings.getString("esp_address","http://192.168.43.209/");

        retrofit = new Retrofit.Builder()
                .baseUrl(s).addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = retrofit.create(ArduinoService.class);

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hiloSecundario = new DoRequest();
                if (isChecked) {
                    // The toggle is enabled
                    hiloSecundario.execute((service.frenosOn()));

                } else {
                    // The toggle is disabled
                    hiloSecundario.execute((service.frenosOff()));
                }
            }
        });

        toggleBalizas = (ToggleButton) findViewById(R.id.toggleButtonBalizas);
        toggleBalizas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hiloSecundario = new DoRequest();
                if (isChecked) {
                    // The toggle is enabled

                    hiloSecundario.execute((service.balizasOn()));
                } else {
                    // The toggle is disabled
                    hiloSecundario.execute((service.balizasOff()));
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
                retrofit = new Retrofit.Builder()
                        .baseUrl(s).addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                service = retrofit.create(ArduinoService.class);
            }
        });


    }

    private class DoRequest extends AsyncTask<Call<String>, Void, String>{

        @Override
        protected String doInBackground(Call<String>... calls) {
            String res = "";
            try {
                calls[0].execute();
            } catch (IOException e) {
                res = e.getMessage();
                Log.e("Retrofit", e.getMessage());
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
        }
    }

}
