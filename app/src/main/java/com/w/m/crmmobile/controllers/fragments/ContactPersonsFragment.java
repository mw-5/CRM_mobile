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
import com.w.m.crmmobile.controllers.adapters.ContactPersonListItemAdapter;
import com.w.m.crmmobile.model.ContactPerson;
import com.w.m.crmmobile.model.Model;

import java.util.ArrayList;
import java.util.List;


public class ContactPersonsFragment extends Fragment
{
    View view;
    Model model = Model.getModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_contact_persons, container, false);
        loadData();
        model.getRefreshCallbacksContactPersons().put(getClass(), refreshCallback);
        return view;
    }

    @Override
    public void onDestroyView()
    {
        model.getRefreshCallbacksContactPersons().remove(getClass());
        super.onDestroyView();
    }

    private Runnable refreshCallback = new Runnable() {
        @Override
        public void run()
        {
            loadData();
        }
    };

    private ContactPersonListItemAdapter adapterCP;
    private ListView lvCPs;

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

        // set contact persons
        try
        {
            new AsyncTask<Integer, Void, List<ContactPerson>>() {
                @Override
                protected List<ContactPerson> doInBackground(Integer... params)
                {
                    int cid = params[0];
                    try
                    {
                        return Model.getModel().getContactPersons(cid);
                    }
                    catch (Exception e)
                    {
                        return new ArrayList<ContactPerson>();
                    }
                }

                @Override
                protected void onPostExecute(List<ContactPerson> listCP)
                {
                    if (getActivity() != null)
                    {
                        adapterCP = new ContactPersonListItemAdapter(getContext(), R.layout.contact_person_list_item, listCP);
                        lvCPs = (ListView) view.findViewById(R.id.contactPersonsListView);
                        lvCPs.setAdapter(adapterCP);
                    }
                }
            }.execute(cid);
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.loadCpFailed), Toast.LENGTH_LONG);
        }
    }
}
