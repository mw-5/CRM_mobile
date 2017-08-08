package com.w.m.crmmobile.controllers.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.controllers.adapters.NoteListItemAdapter;
import com.w.m.crmmobile.model.Model;
import com.w.m.crmmobile.model.Note;

import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends Fragment
{
    View view;
    Model model = Model.getModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_notes, container, false);
        loadData();
        model.getRefreshCallbacksNotes().put(getClass(), refreshCallback);
        return view;
    }

    @Override
    public void onDestroyView()
    {
        model.getRefreshCallbacksNotes().remove(getClass());
        super.onDestroyView();
    }

    private Runnable refreshCallback = new Runnable() {
        @Override
        public void run()
        {
            loadData();
        }
    };

    private List<Note> listNotes;
    private NoteListItemAdapter adapterNotes;
    private ListView lvNotes;

    public void loadData()
    {
        int cid  = 0;
        try
        {
            cid = getActivity().getIntent().getExtras().getInt("cid");
        }
        catch(Exception e)
        {
            cid = 0;
            return;
        }

        // set notes
        try
        {
            new AsyncTask<Integer, Void, List<Note>>() {
                @Override
                protected List<Note> doInBackground(Integer... params)
                {
                    int cid = params[0];
                    try
                    {
                        return model.getNotes(cid);
                    }
                    catch (Exception e)
                    {
                        return new ArrayList<Note>();
                    }
                }

                @Override
                protected void onPostExecute(List<Note> n)
                {
                    if (getActivity() != null)
                    {
                        listNotes = n;
                        adapterNotes = new NoteListItemAdapter(getActivity(), R.layout.note_list_item, listNotes);
                        lvNotes = (ListView) view.findViewById(R.id.notesListView);
                        lvNotes.setAdapter(adapterNotes);
                    }
                }
            }.execute(cid);
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.noNotes), Toast.LENGTH_LONG).show();
        }
    }

}
