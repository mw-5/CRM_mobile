package com.w.m.crmmobile.controllers.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.model.ContactPerson;

import java.util.List;


public class ContactPersonListItemAdapter extends ArrayAdapter<ContactPerson>
{
    private int layoutIdContactPersonListItem;
    private Context context;

    public ContactPersonListItemAdapter(Context context, int layoutIdContactPersonListItem, List<ContactPerson> list)
    {
        super(context, layoutIdContactPersonListItem, list);
        this.context = context;
        this.layoutIdContactPersonListItem = layoutIdContactPersonListItem;
    }

    private static class ViewHolder
    {
        TextView id;
        TextView name;
        TextView phoneNumber;
        TextView email;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        View row;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutIdContactPersonListItem, parent, false);
            viewHolder.id = (TextView)row.findViewById(R.id.id);
            viewHolder.name = (TextView)row.findViewById(R.id.name);
            viewHolder.phoneNumber = (TextView)row.findViewById(R.id.phoneNumber);
            viewHolder.email = (TextView)row.findViewById(R.id.email);
            row.setTag(viewHolder);
        }
        else
        {
            row = convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.id.setText(Integer.toString(getItem(position).getId()));
        viewHolder.name.setText(getItem(position).getName());
        viewHolder.phoneNumber.setText(getItem(position).getPhone());
        viewHolder.email.setText(getItem(position).getEmail());
        if (getItem(position).isMainContact())
        {
            viewHolder.name.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else
        {
            viewHolder.name.setTypeface(Typeface.DEFAULT);
        }

        return row;
    }
}