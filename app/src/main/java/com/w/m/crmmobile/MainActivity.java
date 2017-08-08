package com.w.m.crmmobile;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.w.m.crmmobile.controllers.Mode;
import com.w.m.crmmobile.controllers.activities.CustomerListActivity;
import com.w.m.crmmobile.controllers.activities.EditMasterDataActivity;
import com.w.m.crmmobile.controllers.activities.SettingsActivity;
import com.w.m.crmmobile.model.DbHelper;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbHelper.setContext(getApplicationContext());
        animate();
    }

    //region navigation

    public void navList(View v)
    {
        Intent i = new Intent(MainActivity.this, CustomerListActivity.class);
        startActivity(i);
    }

    public void navNewCustomer(View v)
    {
        Intent i = new Intent(MainActivity.this, EditMasterDataActivity.class);
        Mode mode = Mode.NEW;
        i.putExtra("mode", mode);
        startActivity(i);
    }

    public void navSettings(View v)
    {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    //endregion

    //region animation

    private boolean isAnimated = true;

    private void animate()
    {
        if (isAnimated)
        {
            Button btnList = (Button) findViewById(R.id.btnList);
            Animation aniSlideBtnList = AnimationUtils.loadAnimation(this, R.anim.slidebtnlist);
            btnList.startAnimation(aniSlideBtnList);

            Button btnNewCustomer = (Button) findViewById(R.id.btnNewCustomer);
            Animation aniSlideBtnNewCustomer = AnimationUtils.loadAnimation(this, R.anim.slidebtnnewcustomer);
            btnNewCustomer.startAnimation(aniSlideBtnNewCustomer);

            Button btnSettings = (Button)findViewById(R.id.btnSettings);
            Animation aniSlideBtnSettings = AnimationUtils.loadAnimation(this, R.anim.slidebtnsettings);
            btnSettings.startAnimation(aniSlideBtnSettings);

            isAnimated = false;
        }
    }

    //endregion
}
