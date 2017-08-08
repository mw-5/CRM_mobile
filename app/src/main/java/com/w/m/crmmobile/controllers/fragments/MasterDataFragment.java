package com.w.m.crmmobile.controllers.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.w.m.crmmobile.R;
import com.w.m.crmmobile.model.Customer;
import com.w.m.crmmobile.model.Model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;


public class MasterDataFragment extends Fragment
{
    View view;
    Model model = Model.getModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_master_data, container, false);
        loadData();
        model.getRefreshCallbacksCustomers().put(getClass(), refreshCallback);
        return view;
    }

    @Override
    public void onDestroyView()
    {
        model.getRefreshCallbacksCustomers().remove(getClass());
        super.onDestroyView();
    }

    private Runnable refreshCallback = new Runnable() {
        @Override
        public void run()
        {
            loadData();
        }
    };

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
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.noId), Toast.LENGTH_LONG).show();
            return;
        }

        // set master data
        try
        {
            new AsyncTask<Integer, Void, Customer>() {
                @Override
                protected Customer doInBackground(Integer... params)
                {
                    int cid = params[0];
                    try
                    {
                        return model.getCustomer(cid);
                    }
                    catch (Exception e)
                    {
                        List<Customer> list = model.getCustomers();
                        for(Customer c : list)
                        {
                            if (c.getCid() == cid)
                            {
                                return c;
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Customer cli)
                {
                    if (getActivity() != null)
                    {
                        ((TextView) view.findViewById(R.id.company)).setText(cli.getCompany());
                        ((TextView) view.findViewById(R.id.cid)).setText(Integer.toString(cli.getCid()));
                        ((TextView) view.findViewById(R.id.address)).setText(cli.getAddress());
                        ((TextView) view.findViewById(R.id.zip)).setText(cli.getZip());
                        ((TextView) view.findViewById(R.id.city)).setText(cli.getCity());
                        ((TextView) view.findViewById(R.id.country)).setText(cli.getCountry());
                        ((TextView) view.findViewById(R.id.contractId)).setText(cli.getContractId());
                        Format format = new SimpleDateFormat(getString(R.string.dateFormat));
                        ((TextView) view.findViewById(R.id.contractDate)).setText(cli.getContractDate() == null ? null : format.format(cli.getContractDate().getTime()));
                    }
                }
            }.execute(cid);
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.noMasterData), Toast.LENGTH_LONG).show();
        }
    }
}
