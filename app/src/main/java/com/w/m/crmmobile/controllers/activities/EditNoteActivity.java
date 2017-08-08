package com.w.m.crmmobile.controllers.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.controllers.Mode;
import com.w.m.crmmobile.model.Model;
import com.w.m.crmmobile.model.Note;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditNoteActivity extends AppCompatActivity
{
    Mode mode;
    int id;
    String pathAttachment;
    Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mode = (Mode)getIntent().getSerializableExtra("mode");
        initFields();
        if (mode == Mode.NEW)
        {
            txtCid.setText(Integer.toString(getIntent().getExtras().getInt("cid")));
            id = 0;
            btnDeleteAttachment.setVisibility(View.INVISIBLE);
        }
        else if (mode == Mode.EDIT)
        {
            id = getIntent().getExtras().getInt("id");
            loadData();
        }
    }

    TextView txtId;
    TextView txtCid;
    TextView txtCreatedBy;
    TextView txtEntryDate;
    EditText txtMemo;
    EditText txtCategory;
    TextView txtAttachment;
    SimpleDateFormat format;
    ImageButton btnDeleteAttachment;

    private void initFields()
    {
        txtId = (TextView)findViewById(R.id.id);
        txtCid = (TextView)findViewById(R.id.cid);
        txtCreatedBy = (TextView)findViewById(R.id.createdBy);
        txtEntryDate = (TextView)findViewById(R.id.entryDate);
        txtMemo = (EditText)findViewById(R.id.memo);
        txtCategory = (EditText)findViewById(R.id.category);
        txtAttachment = (TextView)findViewById(R.id.attachment);
        format = new SimpleDateFormat(getString(R.string.dateTimeFormat));
        btnDeleteAttachment = (ImageButton) findViewById(R.id.deleteAttachment);
    }

    private void loadData()
    {
        new AsyncTask<Integer, Void, Note>() {
            @Override
            protected Note doInBackground(Integer... params)
            {
                int id = params[0];
                Note note;
                try
                {
                    note = model.getNote(id);
                }
                catch (Exception e)
                {
                    note = new Note();
                }
                return note;
            }

            @Override
            protected void onPostExecute(Note n)
            {
                txtId.setText(Integer.toString(id));
                txtCid.setText(Integer.toString(n.getCid()));
                txtCreatedBy.setText(n.getCreatedBy());
                txtEntryDate.setText(format.format(n.getEntryDate().getTime()));
                txtMemo.setText(n.getMemo());
                txtCategory.setText(n.getCategory());
                txtAttachment.setText(n.getAttachment());
                if (n.getAttachment() == null || n.getAttachment().equals(""))
                {
                    btnDeleteAttachment.setVisibility(View.INVISIBLE);
                }
            }
        }.execute(id);
    }


    public void cancel(View v)
    {
        finish();
    }

    public void submit(View v)
    {
        Note n = new Note();

        n.setId(id);
        n.setCid(Integer.valueOf(txtCid.getText().toString()));
        if (mode == Mode.NEW)
        {
            String user = System.getenv("username");
            if (user == null || user.equals(""))
            {
                user = "anonymous";
            }
            n.setCreatedBy(user);
            n.setEntryDate(Calendar.getInstance());
        }
        else if (mode == Mode.EDIT)
        {
            n.setCreatedBy(txtCreatedBy.getText().toString());
            try
            {
                Calendar entryDate = Calendar.getInstance();
                entryDate.setTime(format.parse(txtEntryDate.getText().toString()));
                n.setEntryDate(entryDate);
            }
            catch(Exception e)
            {
                Toast.makeText(this, getString(R.string.unableParseDate), Toast.LENGTH_SHORT).show();
            }
        }
        n.setMemo(txtMemo.getText().toString());
        n.setCategory(txtCategory.getText().toString());
        n.setAttachment(txtAttachment.getText().toString());

        try
        {
            switch (mode)
            {
                case NEW:
                    model.createNote(n);
                    break;
                case EDIT:
                    model.updateNote(n);
                    break;
            }

            if(pathAttachment != null && pathAttachment != "")
            {
                model.uploadFile(pathAttachment, n.getCid());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, getResources().getString(R.string.noConnection), Toast.LENGTH_LONG).show();
        }

        finish();
    }

    // region attachment

    private static final int FILE_SELECT_CODE = 0;

    public void setPathAttachment(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        askForPermission();

        try
        {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.selectFileToUpload)), FILE_SELECT_CODE);
        }
        catch (android.content.ActivityNotFoundException e)
        {
            Toast.makeText(this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {
            if (requestCode == FILE_SELECT_CODE)
            {
                if (resultCode == RESULT_OK)
                {
                    Uri uri = data.getData(); // get the Uri of the selected file
                    pathAttachment = getPath(this, uri); // get the path of selected file
                    String fileName = new File(pathAttachment).getName();
                    txtAttachment.setText(fileName);
                    btnDeleteAttachment.setVisibility(View.VISIBLE);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        catch(Exception e)
        {
            Toast.makeText(this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
        }
    }

    private String getPath(Context context, Uri uri) throws URISyntaxException
    {
        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try
            {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }

        return null;
    }

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private void askForPermission()
    {
        if (Build.VERSION.SDK_INT >= 23) // previous versions rely on permission in Manifest, granted at installation
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
//                // check if explanation should be shown to user - not needed here
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
//                {
//                    // show message to user
//                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    public void removeAttachment(View v)
    {
        pathAttachment = null;
        txtAttachment.setText(null);
        btnDeleteAttachment.setVisibility(View.INVISIBLE);
    }

    //endregion
}
