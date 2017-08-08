package com.w.m.crmmobile.controllers.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.model.Note;

import java.util.Calendar;
import java.util.List;
import java.text.*;


public class NoteListItemAdapter extends ArrayAdapter<Note>
{
    private Context context;
    private int layoutIdNoteListItem;

    public NoteListItemAdapter(Context context, int layoutIdNoteListItem, List<Note> list)
    {
        super(context, layoutIdNoteListItem, list);
        this.context = context;
        this.layoutIdNoteListItem = layoutIdNoteListItem;
    }

    public static class ViewHolder
    {
        TextView id;
        TextView createdBy;
        TextView entryDate;
        TextView category;
        ImageButton attachment;
        TextView memo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        View row;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutIdNoteListItem, parent, false);
            viewHolder.id = (TextView)row.findViewById(R.id.id);
            viewHolder.createdBy = (TextView)row.findViewById(R.id.created_by);
            viewHolder.entryDate = (TextView)row.findViewById(R.id.entry_date);
            viewHolder.category = (TextView)row.findViewById(R.id.category);
            viewHolder.attachment = (ImageButton)row.findViewById(R.id.attachment);
            viewHolder.memo = (TextView)row.findViewById(R.id.memo);
            row.setTag(viewHolder);
        }
        else
        {
            row = convertView;
            viewHolder = (ViewHolder)row.getTag();
        }
        viewHolder.id.setText(Integer.toString(getItem(position).getId()));
        viewHolder.createdBy.setText(getItem(position).getCreatedBy());
        Calendar c = getItem(position).getEntryDate();
        Format format = new SimpleDateFormat(getContext().getResources().getString(R.string.dateFormat));
        String date = format.format(c.getTime());
        viewHolder.entryDate.setText(date);
        viewHolder.category.setText(getItem(position).getCategory());
        String attachment = getItem(position).getAttachment();
        if (attachment == null || attachment.equals(""))
        {
            viewHolder.attachment.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.attachment.setVisibility(View.VISIBLE);
        }
        viewHolder.memo.setText(getItem(position).getMemo());

        return row;
    }
}