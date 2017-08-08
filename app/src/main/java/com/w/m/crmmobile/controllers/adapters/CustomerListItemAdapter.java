package com.w.m.crmmobile.controllers.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.model.Customer;

import java.util.Comparator;
import java.util.List;


public class CustomerListItemAdapter extends ArrayAdapter<Customer>
{
    private Context context;
    private int layoutIdCustomerListItem;
    private CharSequence constraint = "";

    public CustomerListItemAdapter(Context context, int layoutIdCustomerListItem, List<Customer> list)
    {
        super(context, layoutIdCustomerListItem, list);
        this.context = context;
        this.layoutIdCustomerListItem = layoutIdCustomerListItem;
    }

    private static class ViewHolder
    {
        TextView cid;
        TextView company;
        TextView km;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutIdCustomerListItem, parent, false);
            viewHolder.cid = (TextView)row.findViewById(R.id.cid);
            viewHolder.company = (TextView)row.findViewById(R.id.company);
            row.setTag(viewHolder);
        }
        else
        {
            row = convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.cid.setText(Integer.toString((getItem(position).getCid())));
        viewHolder.company.setText(getItem(position).getCompany());

        return row;
    }

    @Override
    public void sort(Comparator<? super Customer> c)
    {
        // Implemented because after first time filtering no changes are shown anymore. Problem of mObject and mOriginalValues
        super.sort(c);
        updateUI();
    }

    private void updateUI()
    {
        getFilter().filter(constraint);
    }

    public void filter(CharSequence constraint)
    {
        this.constraint = constraint;  // cache constraint for updateUI()
        getFilter().filter(constraint);
    }
}
