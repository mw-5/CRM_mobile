package com.w.m.crmmobile.controllers.activities;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.controllers.Mode;
import com.w.m.crmmobile.model.ContactPerson;
import com.w.m.crmmobile.model.Model;


public class EditContactPersonActivity extends AppCompatActivity
{
    private int id;
    private Mode mode;
    Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_person);

        mode = (Mode)getIntent().getSerializableExtra("mode");
        initFields();

        if (mode == Mode.NEW)
        {
            txtCid.setText(Integer.toString(getIntent().getExtras().getInt("cid")));
            id = 0;
        }
        else if(mode == Mode.EDIT)
        {
            id = getIntent().getExtras().getInt("id");
            loadData();
        }
    }

    TextView txtId;
    TextView txtCid;
    EditText txtForename;
    EditText txtSurname;
    CheckBox cbMale;
    CheckBox cbFemale;
    EditText txtEmail;
    EditText txtPhone;
    CheckBox cbMainContact;

    private void initFields()
    {
        txtId = (TextView)findViewById(R.id.id);
        txtCid = (TextView)findViewById(R.id.cid);
        txtForename = (EditText)findViewById(R.id.forename);
        txtSurname = (EditText)findViewById(R.id.surname);
        cbMale = (CheckBox)findViewById(R.id.male);
        cbFemale = (CheckBox)findViewById(R.id.female);
        txtEmail = (EditText)findViewById(R.id.email);
        txtPhone = (EditText)findViewById(R.id.phone);
        cbMainContact = (CheckBox)findViewById(R.id.mainContact);
    }

    private void loadData()
    {
        new AsyncTask<Integer, Void, ContactPerson>() {
            @Override
            protected ContactPerson doInBackground(Integer... params)
            {
                int id = params[0];
                try
                {
                    return Model.getModel().getContactPerson(id);
                }
                catch (Exception e)
                {
                    return new ContactPerson();
                }
            }

            @Override
            protected void onPostExecute(ContactPerson c)
            {
                txtId.setText(Integer.toString(id));
                txtCid.setText(Integer.toString(c.getCid()));
                txtForename.setText(c.getForename());
                txtSurname.setText(c.getSurename());
                if (c.getGender().equals("m"))
                {
                    cbMale.setChecked(true);
                }
                else if (c.getGender().equals("f"))
                {
                    cbFemale.setChecked(true);
                }
                txtEmail.setText(c.getEmail());
                txtPhone.setText(c.getPhone());
                cbMainContact.setChecked(c.isMainContact());
            }
        }.execute(id);
    }


    public void setGender(View v)
    {
        CheckBox cbCurrent = (CheckBox)v;

        if (cbMale != cbCurrent)
        {
            cbMale.setChecked(false);
        }
        else if(cbFemale != cbCurrent)
        {
            cbFemale.setChecked(false);
        }
    }

    public void cancel(View v)
    {
        finish();
    }

    public void submit(View v)
    {
        ContactPerson c = new ContactPerson();

        c.setId(id);
        c.setCid(Integer.valueOf(txtCid.getText().toString()));
        c.setForename(txtForename.getText().toString());
        c.setSurename(txtSurname.getText().toString());
        if (cbMale.isChecked())
        {
            c.setGender("m");
        }
        else if (cbFemale.isChecked())
        {
            c.setGender("f");
        }
        c.setEmail(txtEmail.getText().toString());
        c.setPhone(txtPhone.getText().toString());
        c.setMainContact(cbMainContact.isChecked());

        try
        {
            switch (mode)
            {
                case NEW:
                    model.createContactPerson(c);
                    break;
                case EDIT:
                    model.updateContactPerson(c);
                    break;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, getString(R.string.noConnection), Toast.LENGTH_LONG).show();
        }

        finish();
    }


}
