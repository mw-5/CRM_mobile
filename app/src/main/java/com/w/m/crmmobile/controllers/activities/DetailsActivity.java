package com.w.m.crmmobile.controllers.activities;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.w.m.crmmobile.BuildConfig;
import com.w.m.crmmobile.R;
import com.w.m.crmmobile.controllers.Mode;
import com.w.m.crmmobile.model.Model;

import java.io.File;


public class DetailsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{
    Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        restoreState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        int tglId = tglCurrent == null ? 0 : tglCurrent.getId();
        outState.putInt("tglId", tglId);
    }

    private void restoreState(Bundle savedInstanceState)
    {
        if (savedInstanceState == null)
        {
            return;
        }

        int tglId = savedInstanceState.getInt("tglId", 0);
        if (tglId != 0)
        {
            tglCurrent = (ToggleButton) findViewById(tglId);
            setVisibility(tglCurrent);
        }
    }

    ToggleButton tglCurrent = null;

    public void setVisibility(View v)
    {
        tglCurrent = (ToggleButton)v;
        ToggleButton tglMasterData = (ToggleButton)findViewById(R.id.tglMasterData);
        ToggleButton tglContactPersons = (ToggleButton)findViewById(R.id.tglContactPersons);
        ToggleButton tglNotes = (ToggleButton)findViewById(R.id.tglNotes);

        tglMasterData.setChecked(false);
        tglContactPersons.setChecked(false);
        tglNotes.setChecked(false);
        tglCurrent.setChecked(true);

        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;

        if (tglMasterData.isChecked())
        {
            resizeFragment(R.id.masterDataFragment, matchParent, matchParent);
        }
        else
        {
            resizeFragment(R.id.masterDataFragment, 0, 0);
        }

        if (tglContactPersons.isChecked())
        {
            resizeFragment(R.id.contactPersonsFragment, matchParent, matchParent);
        }
        else
        {
            resizeFragment(R.id.contactPersonsFragment,0 ,0);
        }

        if (tglNotes.isChecked())
        {
            resizeFragment(R.id.notesFragment, matchParent, matchParent);
        }
        else
        {
            resizeFragment(R.id.notesFragment, 0, 0);
        }
    }

    private void resizeFragment(int fragmentId, int width, int height)
    {
        View v = findViewById(fragmentId);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, height);
        v.setLayoutParams(p);
        v.requestLayout();
    }

    //region phone call

    private static final int PERMISSIONS_CALL_PHONE = 5;

    public void callPhone(View v)
    {
        // get phone number
        View parent = (View)v.getParent();
        TextView tvPhoneNumber =  (TextView)parent.findViewById(R.id.phoneNumber);
        phoneNumber = tvPhoneNumber.getText().toString();

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_CALL_PHONE);
                return;
            }
        }
        makeCall();
    }

    private String phoneNumber;

    private void makeCall()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.call) + ": " + phoneNumber, Toast.LENGTH_LONG).show();
            Intent phoneIntent =  new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(phoneIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSIONS_CALL_PHONE)
        {
            for (int i = 0; i < permissions.length; i++)
            {
                if (permissions[i].equals(Manifest.permission.CALL_PHONE))
                {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        makeCall();
                    }
                    break;
                }
            }
        }
    }

    //endregion

    public void sendMail(View v)
    {
        // get email address
        View parent = (View)v.getParent();
        TextView tvEmail = (TextView)parent.findViewById(R.id.email);
        String email = tvEmail.getText().toString();

        Toast.makeText(getApplicationContext(), getString(R.string.sendMailTo) +  ": " + email, Toast.LENGTH_SHORT).show();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "my subject");
        //emailIntent.putExtra(Intent.EXTRA_TEXT, "my text body");

        try
        {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.sendMailWith)));
        }
        catch(ActivityNotFoundException e)
        {
            Toast.makeText(this, getString(R.string.noEmailClient), Toast.LENGTH_SHORT).show();
        }
    }

    // region edit & new buttons

    public void editMasterData(View v)
    {
        Intent i = new Intent(this, EditMasterDataActivity.class);
        i.putExtra("mode", Mode.EDIT);
        i.putExtra("cid", getIntent().getExtras().getInt("cid"));
        startActivity(i);
    }

    public void editContactPerson(View v)
    {
        View parent = (View)v.getParent();
        TextView txtId = (TextView)parent.findViewById(R.id.id);
        int id = Integer.valueOf(txtId.getText().toString());

        Intent i = new Intent(DetailsActivity.this, EditContactPersonActivity.class);
        i.putExtra("id", id);
        i.putExtra("mode", Mode.EDIT);
        startActivity(i);
    }

    public void newContactPerson(View v)
    {
        Intent i = new Intent(DetailsActivity.this, EditContactPersonActivity.class);
        i.putExtra("cid", getIntent().getExtras().getInt("cid"));
        i.putExtra("mode", Mode.NEW);
        startActivity(i);
    }

    public void newNote(View v)
    {
        Intent i = new Intent(DetailsActivity.this, EditNoteActivity.class);
        i.putExtra("cid", getIntent().getExtras().getInt("cid"));
        i.putExtra("mode", Mode.NEW);
        startActivity(i);
    }

    public void editNote(View v)
    {
        View parent = (View)v.getParent();
        TextView txtId = (TextView)parent.findViewById(R.id.id);
        int id = Integer.valueOf(txtId.getText().toString());

        Intent i = new Intent(DetailsActivity.this, EditNoteActivity.class);
        i.putExtra("id", id);
        i.putExtra("mode", Mode.EDIT);
        startActivity(i);
    }

    //endregion

    // region open attachment

    public void openAttachment(View v)
    {
        askForPermissions();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
            return;
        }

        View parent = (View)v.getParent();
        TextView txtId = (TextView)parent.findViewById(R.id.id);
        int id = Integer.valueOf(txtId.getText().toString());

        new AsyncTask<Integer, Void, File>(){
            @Override
            protected File doInBackground(Integer... params)
            {
                File file = null;
                try
                {
                    file = model.downloadFile(params[0]);
                }
                catch (Exception e)
                {}
                return file;
            }

            @Override
            protected void onPostExecute(File file)
            {
                viewFile(file);
            }
        }.execute(id);
    }

    private void viewFile(File file)
    {
        Uri path;
        if (Build.VERSION.SDK_INT >= 24)
        {
            path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        }
        else
        {
            path = Uri.fromFile(file);
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        //i.setDataAndType(path, "*/*");
        i.setData(path);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try
        {
            Toast.makeText(this, file.getName(), Toast.LENGTH_SHORT).show();
            //startActivity(Intent.createChooser(i, "open with")); // let user choose app to open document with
            startActivity(i);
        }
        catch (Exception e)
        {
            Toast.makeText(this, getString(R.string.noAppAvailable), Toast.LENGTH_SHORT).show();
        }
    }

    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 2;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE = 1;
    private void askForPermissions()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        }
    }

    //endregion

}
