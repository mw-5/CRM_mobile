package com.w.m.crmmobile.controllers.activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.w.m.crmmobile.controllers.adapters.CustomerListItemAdapter;
import com.w.m.crmmobile.R;
import com.w.m.crmmobile.model.Customer;
import com.w.m.crmmobile.model.Model;

import java.util.Comparator;
import java.util.List;


public class CustomerListActivity extends AppCompatActivity
{
    private ListView listView;
    private List<Customer> list;
    private CustomerListItemAdapter adapter;
    Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        setListeners();
        loadList();

        model.getRefreshCallbacksCustomers().put(getClass(), refreshCallback);

        if (!isInitiallyRefreshed)
        {
            model.updateListOfCustomers(); // Only called first time activity is created - expensive operation
            isInitiallyRefreshed = true;
        }
    }

    @Override
    protected void onDestroy()
    {
        model.getRefreshCallbacksCustomers().remove(getClass());
        super.onDestroy();
    }

    Runnable refreshCallback = new Runnable() {
        @Override
        public void run()
        {
            loadList();
        }
    };

    private static boolean isInitiallyRefreshed = false;

    public void loadList()
    {
        list = model.getCustomers();
        adapter = new CustomerListItemAdapter(this, R.layout.customer_list_item, list);      // use simpleCursorAdapter for cursor in case of database data
        listView = (ListView)findViewById(R.id.customer_list);
        listView.setAdapter(adapter);
    }

    public void navDetailsFromItem(View v)
    {
        int cid = Integer.valueOf(((Button)v).getText().toString());
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra("cid", cid);
        startActivity(i);
    }

    public void orderByIdAsc(View v)
    {
        adapter.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {return Integer.valueOf(o1.getCid()).compareTo(o2.getCid()); }
        });
    }

    public void orderByIdDesc(View v)
    {
        adapter.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {return Integer.valueOf(o2.getCid()).compareTo(o1.getCid()); }
        });
    }

    public void orderByCompanyAsc(View v)
    {
        adapter.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {return o1.getCompany().compareToIgnoreCase(o2.getCompany());}
        });
    }

    public void orderByCompanyDesc(View v)
    {
        adapter.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {return o2.getCompany().compareToIgnoreCase(o1.getCompany());}
        });
    }

    private void setListeners()
    {
        ((EditText)findViewById(R.id.filterList)).addTextChangedListener(filterListener);
    }

    private TextWatcher filterListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            adapter.filter(s);
        }

        @Override
        public void afterTextChanged(Editable s)
        {}
    };
}
