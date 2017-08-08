package com.w.m.crmmobile.controllers.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.model.Model;


public class SettingsActivity extends AppCompatActivity
{
    Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swt = (Switch)findViewById(R.id.swtDataSourceMock);
        txtIp = (EditText)findViewById(R.id.ip);
        txtPort = (EditText)findViewById(R.id.port);
        txtBasePath = (EditText)findViewById(R.id.basePath);

        swt.setChecked(!model.isMock());
        model.getAddressWebService(); // load address if it hasn't been initialized yet
        txtIp.setText(model.getIp());
        txtPort.setText(model.getPort());
        txtBasePath.setText(model.getBasePath());
    }

    Switch swt;
    EditText txtIp;
    EditText txtPort;
    EditText txtBasePath;

    public void cancel(View v)
    {
        finish();
    }

    public void submit(View v)
    {
        model.setMock(!swt.isChecked());

        model.setIp(txtIp.getText().toString());
        model.setPort(txtPort.getText().toString());
        model.setBasePath(txtBasePath.getText().toString());

        finish();
    }
}
