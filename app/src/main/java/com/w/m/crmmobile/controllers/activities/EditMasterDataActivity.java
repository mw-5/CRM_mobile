package com.w.m.crmmobile.controllers.activities;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.controllers.Mode;
import com.w.m.crmmobile.model.Customer;
import com.w.m.crmmobile.model.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditMasterDataActivity extends AppCompatActivity
{
    private int cid;
    private Mode mode;
    Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_master_data);

        format =  new SimpleDateFormat(getString(R.string.dateFormat));
        initFields();

        mode = (Mode)getIntent().getSerializableExtra("mode");
        if (mode == Mode.EDIT)
        {
            cid = getIntent().getExtras().getInt("cid");
            loadData();
        }
        else
        {
            cid = 0;
        }
    }

    TextView txtCid;
    EditText txtCompany;
    EditText txtAddress;
    EditText txtZip;
    EditText txtCity;
    EditText txtCountry;
    EditText txtContractId;
    EditText txtContractDate;
    SimpleDateFormat format;

    private void initFields()
    {
        txtCid = ((TextView)findViewById(R.id.cid));
        txtCompany = ((EditText)findViewById(R.id.company));
        txtAddress = ((EditText)findViewById(R.id.address));
        txtZip = ((EditText)findViewById(R.id.zip));
        txtCity = ((EditText)findViewById(R.id.city));
        txtCountry = ((EditText)findViewById(R.id.country));
        txtContractId = ((EditText)findViewById(R.id.contractId));
        txtContractDate = ((EditText)findViewById(R.id.contractDate));
    }

    private void loadData()
    {
        try
        {
            new AsyncTask<Integer, Void, Customer>() {
                @Override
                protected Customer doInBackground(Integer... params)
                {
                    int cid = params[0];
                    try
                    {
                        return Model.getModel().getCustomer(cid);
                    }
                    catch (Exception e)
                    {
                        Customer c = new Customer();
                        c.setCid(cid);
                        return c;
                    }
                }

                @Override
                protected void onPostExecute(Customer c)
                {
                    txtCid.setText(Integer.toString(cid));
                    txtCompany.setText(c.getCompany());
                    txtAddress.setText(c.getAddress());
                    txtZip.setText(c.getZip());
                    txtCity.setText(c.getCity());
                    txtCountry.setText(c.getCountry());
                    txtContractId.setText(c.getContractId());
                    txtContractDate.setText(c.getContractDate() == null ? null : format.format(c.getContractDate().getTime()));
                }
            }.execute(cid);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View v)
    {
        finish();
    }

    public void submit(View v)
    {
        Customer c = new Customer();

        c.setCid(cid);
        c.setCompany(txtCompany.getText().toString());
        c.setAddress(txtAddress.getText().toString());
        c.setZip(txtZip.getText().toString());
        c.setCity(txtCity.getText().toString());
        c.setCountry(txtCountry.getText().toString());
        c.setContractId(txtContractId.getText().toString());
        try
        {
            Calendar date = Calendar.getInstance();
            date.setTime(format.parse(txtContractDate.getText().toString()));
            c.setContractDate(date);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.unableParseDate), Toast.LENGTH_LONG).show();
            c.setContractDate(null);
        }

        try
        {
            switch (mode)
            {
                case NEW:
                    model.createCustomer(c);
                    break;
                case EDIT:
                    model.updateCustomer(c);
                    break;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_LONG).show();
        }

        finish();
    }

    public void selectDate(View v)
    {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(EditMasterDataActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
            {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                txtContractDate.setText(format.format(selectedDate.getTime()));
            }
        }, year, month, day);
        datePicker.show();
        // overwrite white text color of crmButton Style
        datePicker.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        datePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
