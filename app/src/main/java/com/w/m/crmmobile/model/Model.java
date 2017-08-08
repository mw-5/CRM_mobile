package com.w.m.crmmobile.model;


import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class Model
{
    private static Model model;

    private Model()
    {}

    public static Model getModel()
    {
        if (model == null)
        {
            model = new Model();
        }
        return model;
    }

    private static boolean mock;
    public boolean isMock()
    {
        return mock;
    }
    public void setMock(boolean mock)
    {
        this.mock = mock;
    }

    //region web service connection

    public String getAddressWebService()
    {
        return restClient.getAddressWebService();
    }

    public String getIp()
    {
        return restClient.getIp();
    }
    public void setIp(String ip)
    {
        restClient.setIp(ip);
    }

    public String getPort()
    {
        return restClient.getPort();
    }
    public void setPort(String port)
    {
        restClient.setPort(port);
    }

    public String getBasePath()
    {
        return restClient.getBasePath();
    }
    public void setBasePath(String basePath)
    {
        restClient.setBasePath(basePath);
    }

    //endregion

    private RestClient restClient = new RestClient();
    private DbHelper dbHelper = new DbHelper();
    private List<Customer> customers = null;

    //region refresh data views and lists

    private void refresh(HashMap<Class, Runnable> map)
    {
        for (Runnable r : map.values())
        {
            try
            {
                r.run();
            }
            catch (Exception e)
            {}
        }
    }

    private HashMap<Class, Runnable> refreshCallbacksCustomers = new HashMap<>();
    public HashMap<Class, Runnable> getRefreshCallbacksCustomers()
    {
        return refreshCallbacksCustomers;
    }
    public void updateCustomersViews()
    {
        refresh(refreshCallbacksCustomers);
    }

    private HashMap<Class, Runnable> refreshCallbacksContactPersons = new HashMap<>();
    public HashMap<Class, Runnable> getRefreshCallbacksContactPersons()
    {
        return refreshCallbacksContactPersons;
    }
    public void updateContactPersonsViews()
    {
        refresh(refreshCallbacksContactPersons);
    }

    private HashMap<Class, Runnable> refreshCallbacksNotes = new HashMap<>();
    public HashMap<Class, Runnable> getRefreshCallbacksNotes()
    {
        return refreshCallbacksNotes;
    }
    public void updateNotesViews()
    {
        refresh(refreshCallbacksNotes);
    }


    public void updateListOfCustomers()
    {
        new AsyncTask<Void, Void, List<Customer>>() {
            @Override
            protected List<Customer> doInBackground(Void... params)
            {
                try
                {
                    if(!isMock())
                    {
                        return restClient.getCustomers();
                    }
                    else
                    {
                        return Mock.createCustomers();
                    }
                }
                catch (Exception e)
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Customer> list)
            {
                if (list != null && list.size() > 0)
                {
                    customers = list;
                    updateCustomersViews();

                    new AsyncTask<List<Customer>, Void, Void>(){
                        @Override
                        protected Void doInBackground(List<Customer>... params)
                        {
                            dbHelper.deleteCustomers();
                            dbHelper.insertCustomers(params[0]);
                            return null;
                        }
                    }.execute(customers);
                }
            }
        }.execute();
    }

    //endregion

    //region get data

    public List<Customer> getCustomers()
    {
        if (!isMock())
        {
            if (customers == null)
            {
                customers = dbHelper.getCustomers();
            }
            return customers;
        }
        else
        {
            return Mock.createCustomers();
        }
    }

    public Customer getCustomer(int cid) throws Exception
    {
        System.out.println("Mock: " + isMock());
        if (!isMock())
        {
            return restClient.getCustomer(cid);
        }
        else
        {
            return Mock.createCustomer(cid);
        }
    }

    public List<Note> getNotes(int cid) throws Exception
    {
        if (!isMock())
        {
            return restClient.getNotes(cid);
        }
        else
        {
            return Mock.createNotesList(cid);
        }
    }

    public Note getNote(int id) throws Exception
    {
        if (!isMock())
        {
            return restClient.getNote(id);
        }
        else
        {
            return Mock.createNote(id);
        }
    }

    public List<ContactPerson> getContactPersons(int cid) throws Exception
    {
        if (!isMock())
        {
            return restClient.getContactPersons(cid);
        }
        else
        {
            return Mock.createContactPersons(cid);
        }
    }

    public ContactPerson getContactPerson(int id) throws Exception
    {
        if (!isMock())
        {
            return restClient.getContactPerson(id);
        }
        else
        {
            return Mock.createContactPerson(id);
        }
    }

    //endregion

    //region create and update data

    public void createCustomer(Customer customer)
    {
        if (!isMock())
        {
            new AsyncTask<Customer, Void, Void>() {
                @Override
                protected Void doInBackground(Customer... params) {
                    try
                    {
                        restClient.createCustomer(params[0]);
                    }
                    catch (Exception e)
                    {}
                    return null;
                }

                @Override
                protected void onPostExecute(Void v)
                {
                    updateListOfCustomers();
                }
            }.execute(customer);
        }
    }

    public void updateCustomer(Customer customer)
    {
        if (!isMock())
        {
            new AsyncTask<Customer, Void, Void>() {
                @Override
                protected Void doInBackground(Customer... params) {
                    try
                    {
                        restClient.updateCustomer(params[0]);
                    }
                    catch (Exception e)
                    {}
                    return null;
                }

                @Override
                protected void onPostExecute(Void v)
                {
                    updateListOfCustomers();
                }
            }.execute(customer);
        }
    }

    public void createContactPerson(ContactPerson cp)
    {
        if (!isMock())
        {
            new AsyncTask<ContactPerson, Void, Void>() {
                @Override
                protected Void doInBackground(ContactPerson... params)
                {
                    try
                    {
                        restClient.createContactPerson(params[0]);
                    }
                    catch (Exception e)
                    {}
                    return null;
                }

                @Override
                protected void onPostExecute(Void v)
                {
                    updateContactPersonsViews();
                }
            }.execute(cp);
        }
    }

    public void updateContactPerson(ContactPerson cp)
    {
        if (!isMock())
        {
            new AsyncTask<ContactPerson, Void, Void>() {
                @Override
                protected Void doInBackground(ContactPerson... params)
                {
                    try
                    {
                        restClient.updateContactPerson(params[0]);
                    }
                    catch (Exception e)
                    {}
                    return null;
                }

                @Override
                protected void onPostExecute(Void v)
                {
                    updateContactPersonsViews();
                }
            }.execute(cp);
        }
    }

    public void createNote(Note note)
    {
        if (!isMock())
        {
            new AsyncTask<Note, Void, Void>() {
                @Override
                protected Void doInBackground(Note... params)
                {
                    try
                    {
                        restClient.createNote(params[0]);
                    }
                    catch (Exception e)
                    {}
                    return null;
                }

                @Override
                protected void onPostExecute(Void v)
                {
                    updateNotesViews();
                }
            }.execute(note);
        }
    }

    public void updateNote(Note note)
    {
        if (!isMock())
        {
            new AsyncTask<Note, Void, Void>() {
                @Override
                protected Void doInBackground(Note... params)
                {
                    try
                    {
                        restClient.updateNote(params[0]);
                    }
                    catch (Exception e)
                    {}
                    return null;
                }

                @Override
                protected void onPostExecute(Void v)
                {
                    updateNotesViews();
                }
            }.execute(note);
        }
    }

    //endregion

    //region file transfer

    public void uploadFile(String pathAttachment, Integer cid)
    {
        if (!isMock())
        {
            String[] args = {pathAttachment, Integer.toString(cid)};

            new AsyncTask<String, Void, Void>()
            {
                @Override
                protected Void doInBackground(String... params)
                {
                    String pathAttachment = params[0];
                    int cid = Integer.parseInt(params[1]);
                    try
                    {
                        restClient.uploadFile(pathAttachment, cid);
                    }
                    catch (Exception e)
                    {
                    }
                    return null;
                }
            }.execute(args);
        }
    }

    public File downloadFile(int noteId) throws Exception
    {
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "crmMobileTemp");
        folder.mkdir();

        return restClient.downloadFile(noteId, folder.getAbsolutePath());
    }

    //endregion

}
