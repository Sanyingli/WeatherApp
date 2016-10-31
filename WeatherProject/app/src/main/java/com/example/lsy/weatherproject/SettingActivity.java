package com.example.lsy.weatherproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {



    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText inputZipText;
    Switch unitSwitch;
    Switch gpsSwitch;

    TextView set;
    TextView days;
    TextView enterZip;
    TextView CorF;
    TextView GPS;

    Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //keep settings
        SharedPreferences settings = getSharedPreferences("PrefFile", 0);


        set = (TextView) findViewById(R.id.settingsText);
        set.setText("SETTING");

        days = (TextView) findViewById(R.id.daysText);
        days.setText(R.string.days);

        enterZip = (TextView) findViewById(R.id.zipText);
        enterZip.setText(R.string.useZipCode);

        CorF = (TextView) findViewById(R.id.unitText);
        CorF.setText(R.string.CorF);

        GPS = (TextView) findViewById(R.id.gText);
        GPS.setText(R.string.useGPS);

        inputZipText = (EditText) findViewById(R.id.inputZipText);
        inputZipText.setEnabled(settings.getBoolean("type",true));
        inputZipText.setText(settings.getString("ZipCode", "20006"));

        unitSwitch = (Switch) findViewById(R.id.switch1);
        unitSwitch.setChecked(settings.getBoolean("tempUnit", true));
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Cel), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Fah), Toast.LENGTH_SHORT).show();
                }

            }
        });

        gpsSwitch = (Switch) findViewById(R.id.gpsSwitch);
        gpsSwitch.setChecked(!settings.getBoolean("type",true));
        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputZipText.setEnabled(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.useGPS), Toast.LENGTH_SHORT).show();
                } else {
                    inputZipText.setEnabled(true);
                    Toast.makeText(getApplicationContext(), getString(R.string.useZipCode), Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.forecastDay, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String compareValue = "" + settings.getInt("forecastDate", 3);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + getString(R.string.selected), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmButton = (Button) findViewById(R.id.button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Alert:if EditText is empty;
                if(inputZipText.isEnabled() && inputZipText.getText().toString().matches("")){
                    AlertDialog.Builder zipAlertBuilder = new AlertDialog.Builder(SettingActivity.this);
                    zipAlertBuilder.setTitle("Error");
                    zipAlertBuilder.setMessage("Zip Code is empty");

                    zipAlertBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Toast.makeText(SettingActivity.this,"clicked the yes button",Toast.LENGTH_LONG).show();
                        }
                    });

                    AlertDialog zipAlertDialog = zipAlertBuilder.create();
                    zipAlertDialog.show();
                }

                else {
                    SharedPreferences settings = getSharedPreferences("PrefFile", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("tempUnit", unitSwitch.isChecked());
                    editor.putInt("forecastDate", spinner.getSelectedItemPosition() + 1);
                    editor.putBoolean("type",inputZipText.isEnabled());
                    if (inputZipText.isEnabled()) {
                        editor.putString("zipCode", inputZipText.getText().toString());
                    }
                    editor.commit();
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent);

                    ProgressDialog progressDialog = new ProgressDialog(SettingActivity.this);
                    progressDialog.setTitle(getString(R.string.diaTitle));
                    progressDialog.setMessage(getString(R.string.diaMessage));
                    progressDialog.show();}


            }
        });

    }
}
