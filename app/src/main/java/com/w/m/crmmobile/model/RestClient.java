package com.w.m.crmmobile.model;


import android.net.http.HttpResponseCache;
import android.util.JsonReader;
import android.util.JsonWriter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RestClient
{
    public RestClient()
    {
        if (cache == null)
        {
            cache = (HttpResponseCache) HttpResponseCache.getDefault();
        }
    }

    private static HttpResponseCache cache;


    //region address web service

    DbHelper dbHelper = new DbHelper();

    private static String ip = null;
    public String getIp()
    {
        if (ip == null)
        {
            ip = dbHelper.getIp();
        }
        return ip;
    }
    public void setIp(String value)
    {
        ip = value;
        dbHelper.setIp(value);
        setAddressWebService();
    }

    private static String port = null;
    public String getPort()
    {
        if (port == null)
        {
            port = dbHelper.getPort();
        }
        return port;
    }
    public void setPort(String value)
    {
        port = value;
        dbHelper.setPort(value);
        setAddressWebService();
    }

    private static String basePath = null;
    public String getBasePath()
    {
        if (basePath == null)
        {
            basePath = dbHelper.getBasePath();
        }
        return basePath;
    }
    public void setBasePath(String value)
    {
        if (basePath.endsWith("/"))
        {
            value = value.substring(0, value.length()-1);
        }
        basePath = value;
        dbHelper.setColBasePath(value);
        setAddressWebService();
    }

    private static String addressWebService = "";
    public String getAddressWebService()
    {
        if (addressWebService.equals(""))
        {
            addressWebService = getAddressWebServiceFromDatabase();
        }

        return addressWebService;
    }
    private void setAddressWebService()
    {
        addressWebService = "http://" +  ip + ":" + port + "/" + basePath + "/ws/";
    }

    private String getAddressWebServiceFromDatabase()
    {
        ip = dbHelper.getIp();
        port = dbHelper.getPort();
        basePath = dbHelper.getBasePath();
        setAddressWebService();

        return addressWebService;
    }

    //endregion address web service


    /**
     *
     * @param pathParam variable part of uri of web service
     * @return HttpURLConnection
     */
    private HttpURLConnection getConnection(String pathParam) throws IOException
    {
        String wsAddress = getAddressWebService();
        if (!wsAddress.endsWith("/"))
        {
            wsAddress += "/";
        }
        if (pathParam.startsWith("/"))
        {
            pathParam = pathParam.substring(1);
        }
        wsAddress = wsAddress + pathParam;

        URL url = new URL(wsAddress);
        return (HttpURLConnection) url.openConnection();
    }

    private JsonReader getJsonReader(HttpURLConnection connection) throws IOException
    {
        connection.setRequestProperty("Accept", "application/json");
        JsonReader jsonReader = null;
        if (connection.getResponseCode() == 200)
        {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody);
            jsonReader = new JsonReader(responseBodyReader);
        }
        return jsonReader;
    }

    //region conversion Calendar - JSON

    private Calendar getCalendarFromJson(JsonReader jsonReader)
    {
        String dateTime = "";
        try
        {
            dateTime = jsonReader.nextString();
        }
        catch (IOException e)
        {
            dateTime = "";
        }
        Calendar cal =  Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        if (!dateTime.equals("")  && dateTime.length() >= 19)
        {
            int year = Integer.parseInt(dateTime.substring(0, 4));
            int month = Integer.parseInt((dateTime.substring(5, 7)));
            int day = Integer.parseInt(dateTime.substring(8, 10));
            int hour = Integer.parseInt(dateTime.substring(11, 13));
            int minute = Integer.parseInt(dateTime.substring(14, 16));
            int second = Integer.parseInt(dateTime.substring(17, 19));
            cal.set(year, month-1, day, hour, minute, second);
        }
        else
        {
            cal = null;
        }
        return cal;
    }

    private String getJsonFromCalendar(Calendar cal)
    {
        String json;
        if (cal != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            json = sdf.format(cal.getTime());
        }
        else
        {
            json = null;
        }
        return json;
    }

    //endregion

    //region get data

    //region retrieval customers

    public Customer getCustomer(int cid) throws Exception
    {
        Customer customer = null;
        HttpURLConnection con = null;
        JsonReader jsonReader = null;
        try
        {
            con = getConnection("customer/" + Integer.toString(cid));
            jsonReader = getJsonReader(con);
            customer = getCustomerFromJson(jsonReader);
        }
        finally
        {
            jsonReader.close();
            con.disconnect();
        }
        return customer;
    }

    private Customer getCustomerFromJson(JsonReader jsonReader) throws Exception
    {
        Customer customer = new Customer();
        String key = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            key = jsonReader.nextName();
            switch (key)
            {
                case "cid":
                    customer.setCid(jsonReader.nextInt());
                    break;
                case "company":
                    customer.setCompany(jsonReader.nextString());
                    break;
                case "address":
                    customer.setAddress(jsonReader.nextString());
                    break;
                case "zip":
                    customer.setZip(jsonReader.nextString());
                    break;
                case "city":
                    customer.setCity(jsonReader.nextString());
                    break;
                case "country":
                    customer.setCountry(jsonReader.nextString());
                    break;
                case "contractId":
                    customer.setContractId(jsonReader.nextString());
                    break;
                case "contractDate":
                    customer.setContractDate(getCalendarFromJson(jsonReader));
                    break;
                default:
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return customer;
    }

    public List<Customer> getCustomers() throws Exception
    {
        List<Customer> list = new ArrayList<>();
        HttpURLConnection con = null;
        JsonReader jsonReader = null;
        try
        {
            con = getConnection("customer");
            jsonReader = getJsonReader(con);
            jsonReader.beginArray();
            while (jsonReader.hasNext())
            {
                list.add(getCustomerFromJson(jsonReader));
            }
            jsonReader.endArray();
        }
        finally
        {
            jsonReader.close();
            con.disconnect();
        }
        return list;
    }

    //endregion

    //region retrieval contact persons

    public ContactPerson getContactPerson(int id) throws Exception
    {
        HttpURLConnection con = null;
        JsonReader jsonReader = null;
        ContactPerson cp = null;
        try
        {
            con = getConnection("contactPerson/" + Integer.toString(id));
            jsonReader = getJsonReader(con);
            cp = getContactPersonFromJsonReader(jsonReader);
        }
        finally
        {
            jsonReader.close();
            con.disconnect();
        }
        return cp;
    }

    private ContactPerson getContactPersonFromJsonReader(JsonReader jsonReader) throws Exception
    {
        ContactPerson cp =  new ContactPerson();
        String key = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext())
        {
            key = jsonReader.nextName();
            switch(key)
            {
                case "id":
                    cp.setId(jsonReader.nextInt());
                    break;
                case "cid":
                    jsonReader.beginObject();
                    while (jsonReader.hasNext())
                    {
                        if (jsonReader.nextName().equals("cid"))
                        {
                            cp.setCid(jsonReader.nextInt());
                        }
                        else
                        {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    break;
                case "forename":
                    cp.setForename(jsonReader.nextString());
                    break;
                case "surname":
                    cp.setSurename(jsonReader.nextString());
                    break;
                case "gender":
                    cp.setGender(jsonReader.nextString());
                    break;
                case "email":
                    cp.setEmail(jsonReader.nextString());
                    break;
                case "phone":
                    cp.setPhone(jsonReader.nextString());
                    break;
                case "mainContact":
                    cp.setMainContact(jsonReader.nextBoolean());
                    break;
                default:
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return cp;
    }

    public List<ContactPerson> getContactPersons(int cid) throws Exception
    {
        ArrayList<ContactPerson> list = new ArrayList<>();
        HttpURLConnection con = null;
        JsonReader jsonReader = null;
        try
        {
            con = getConnection("contactPerson/cid/" + Integer.toString(cid));
            jsonReader = getJsonReader(con);
            jsonReader.beginArray();
            while (jsonReader.hasNext())
            {
                list.add(getContactPersonFromJsonReader(jsonReader));
            }
            jsonReader.endArray();
        }
        finally
        {
            jsonReader.close();
            con.disconnect();
        }
        return list;
    }

    //endregion

    //region retrieval notes

    public Note getNote(int id) throws Exception
    {
        HttpURLConnection con = null;
        JsonReader jsonReader = null;
        Note note;
        try
        {
            con = getConnection("note/" + Integer.toString(id));
            jsonReader = getJsonReader(con);
            note = getNoteFromJsonReader(jsonReader);
        }
        finally
        {
            jsonReader.close();
            con.disconnect();
        }
        return note;
    }

    private Note getNoteFromJsonReader(JsonReader jsonReader) throws Exception
    {
        Note note = new Note();
        String key = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext())
        {
            key = jsonReader.nextName();
            switch(key)
            {
                case "id":
                    note.setId(jsonReader.nextInt());
                    break;
                case "cid":
                    jsonReader.beginObject();
                    while (jsonReader.hasNext())
                    {
                        if (jsonReader.nextName().equals("cid"))
                        {
                            note.setCid(jsonReader.nextInt());
                        }
                        else
                        {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    break;
                case "createdBy":
                    note.setCreatedBy(jsonReader.nextString());
                    break;
                case "entryDate":
                    note.setEntryDate(getCalendarFromJson(jsonReader));
                    break;
                case "memo":
                    note.setMemo(jsonReader.nextString());
                    break;
                case "category":
                    note.setCategory(jsonReader.nextString());
                    break;
                case "attachment":
                    note.setAttachment(jsonReader.nextString());
                    break;
                default:
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return note;
    }

    public List<Note> getNotes(int cid) throws Exception
    {
        List<Note> list = new ArrayList<>();
        HttpURLConnection con = null;
        JsonReader jsonReader = null;
        try
        {
            con = getConnection("note/cid/" + cid);
            jsonReader = getJsonReader(con);
            jsonReader.beginArray();
            while(jsonReader.hasNext())
            {
                list.add(getNoteFromJsonReader(jsonReader));
            }
            jsonReader.endArray();
        }
        finally
        {
            jsonReader.close();
            con.disconnect();
        }
        return list;
    }

    //endregion

    //endregion

    //region send data

    private void sendJson(JSONObject json, String path, String httpMethod) throws Exception
    {
        HttpURLConnection con = null;
        try
        {
            con = getConnection(path);
            con.setRequestMethod(httpMethod);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(json.toString().getBytes("UTF-8"));
            int code = con.getResponseCode(); // sends request and data
            System.out.println("response: " + code);
        }
        finally
        {
            con.disconnect();
        }
    }

    //region create and update customer

    // unused alternative approach
    private String getJsonStrFromCustomer(Customer customer) throws Exception
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(os));
        jsonWriter.beginObject()
                .name("cid").value(customer.getCid())
                .name("company").value(customer.getCompany())
                .name("address").value(customer.getAddress())
                .name("zip").value(customer.getZip())
                .name("city").value(customer.getCity())
                .name("country").value(customer.getCountry())
                .name("contractId").value(customer.getContractId())
                .name("contractDate").value(getJsonFromCalendar(customer.getContractDate()))
                .endObject();

        jsonWriter.close();
        return os.toString("UTF-8");
    }

    private JSONObject getJsonObjFromCustomer(Customer customer) throws Exception
    {
        JSONObject json = new JSONObject();

        json.put("cid", customer.getCid())
                .put("company",customer.getCompany())
                .put("address",customer.getAddress())
                .put("zip",customer.getZip())
                .put("city",customer.getCity())
                .put("country", customer.getCountry())
                .put("contractId",customer.getContractId())
                .put("contractDate",getJsonFromCalendar(customer.getContractDate()));

        return json;
    }

    private void sendCustomer(Customer customer, String httpMethod) throws Exception
    {
        JSONObject json = getJsonObjFromCustomer(customer);
        sendJson(json, "customer", httpMethod);
    }

    public void createCustomer(Customer customer) throws Exception
    {
        sendCustomer(customer, "POST");
    }

    public void updateCustomer(Customer customer) throws Exception
    {
        sendCustomer(customer, "PUT");
    }

    //endregion

    //region create and update contact person

    private JSONObject getJsonFromContactPerson(ContactPerson cp) throws Exception
    {
        JSONObject fKey = new JSONObject();
        fKey.put("cid", cp.getCid());

        JSONObject json = new JSONObject();

        json.put("id", cp.getId())
                .put("cid", fKey)  // needs object instead of int because cid in contact person model of web service is of typ Customer, not int
                .put("forename", cp.getForename())
                .put("surname", cp.getSurename())
                .put("gender", cp.getGender())
                .put("email", cp.getEmail())
                .put("phone", cp.getPhone())
                .put("mainContact", cp.isMainContact());

        return json;
    }

    private void sendContactPerson(ContactPerson cp, String httpMethod) throws Exception
    {
        JSONObject json = getJsonFromContactPerson(cp);
        sendJson(json, "contactPerson", httpMethod);
    }

    public void createContactPerson(ContactPerson cp) throws Exception
    {
        sendContactPerson(cp, "POST");
    }

    public void updateContactPerson(ContactPerson cp) throws Exception
    {
        sendContactPerson(cp, "PUT");
    }

    //endregion

    //region create and update note

    private JSONObject getJsonFromNote(Note note) throws Exception
    {
        JSONObject fKey = new JSONObject();
        fKey.put("cid", note.getCid());

        JSONObject json = new JSONObject();

        json.put("id", note.getId())
                .put("cid", fKey)  // needs object instead of int because cid in note model of web service is of type Customer, not int
                .put("createdBy", note.getCreatedBy())
                .put("entryDate", getJsonFromCalendar(note.getEntryDate()))
                .put("memo", note.getMemo())
                .put("category", note.getCategory())
                .put("attachment", note.getAttachment());

        return json;
    }

    private void sendNote(Note note, String httpMethod) throws Exception
    {
        JSONObject json = getJsonFromNote(note);
        sendJson(json, "note", httpMethod);
    }

    public void createNote(Note note) throws Exception
    {
        sendNote(note, "POST");
    }

    public void updateNote(Note note) throws Exception
    {
        sendNote(note, "PUT");
    }

    //endregion

    //endregion

    //region file transfer

    public void uploadFile(String pathFile, int cid) throws Exception
    {
        File file = new File(pathFile);
        HttpURLConnection con = null;
        FileInputStream is = null;
        try
        {
            con = getConnection("note/file?fileName=" + file.getName() + "&cid=" + cid);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            is = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while((bytesRead = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            int code = con.getResponseCode();
            System.out.println(code);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            con.disconnect();
            is.close();
        }
    }

    public File downloadFile(int noteId, String dstPath) throws Exception
    {
        HttpURLConnection con = null;
        FileOutputStream os = null;
        File file = null;
        try
        {
            con = getConnection("note/file/" + noteId);
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/octet-stream");
            int code = con.getResponseCode();
            System.out.println(code);
            String fileName = con.getHeaderField("fileName");
            int posPoint = fileName.lastIndexOf(".");
            String prefix = fileName.substring(0, posPoint);
            String suffix = fileName.substring(posPoint);
            InputStream is = con.getInputStream();
            file = File.createTempFile(prefix , suffix, new File(dstPath));
            file.deleteOnExit();
            //file = new File(dstPath, fileName);
            if (file.exists())
            {
                file.delete();
            }
            os = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
        }
        finally
        {
            con.disconnect();
            os.close();
        }
        return file;
    }

    //endregion

}
