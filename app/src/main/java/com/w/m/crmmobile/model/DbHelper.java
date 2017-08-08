package com.w.m.crmmobile.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper
{
    //region names of database, tables and columns

    public static final String DB_NAME = "CRM_mobile.db";

    public static final String TBL_CUSTOMERS = "customers";
    public static final String COL_CID = "cid";
    public static final String COL_COMPANY = "company";
    public static final String COL_ADDRESS = "address";
    public static final String COL_ZIP = "zip";
    public static final String COL_CITY = "city";
    public static final String COL_COUNTRY = "country";
    public static final String COL_CONTRACT_ID = "contract_id";
    public static final String COL_CONTRACT_DATE = "contract_date";

    public static final String TBL_WS_ADDRESS = "ws_address";
    public static final String COL_ID = "id";
    public static final String COL_IP = "ip";
    public static final String COL_PORT = "port";
    public static final String COL_BASE_PATH = "base_path";

    //endregion

    private static Context context = null;
    public static void setContext(Context _context)
    {
        context = _context;
    }

    public DbHelper()
    {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + TBL_CUSTOMERS + " ("
                + COL_CID + " INTEGER PRIMARY KEY, "
                + COL_COMPANY + " NVARCHAR(255), "
                + COL_ADDRESS + " NVARCHAR(255), "
                + COL_ZIP + " NVARCHAR(255), "
                + COL_CITY + " NVARCHAR(255), "
                + COL_COUNTRY + " NVARCHAR(255), "
                + COL_CONTRACT_ID + " NVARCHAR(255), "
                + COL_CONTRACT_DATE + " TIMESTAMP)";

        db.execSQL(sql);

        sql = "CREATE TABLE " + TBL_WS_ADDRESS + " ("
                + COL_ID + " INTEGER PRIMARY KEY, "
                + COL_IP + " NVARCHAR(255), "
                + COL_PORT + " NVARCHAR(255), "
                + COL_BASE_PATH + " NVARCHAR(255))";

        db.execSQL(sql);

        ContentValues data = new ContentValues();
        data.put(COL_ID, 1);
        data.put(COL_IP, "");
        data.put(COL_PORT, "");
        data.put(COL_BASE_PATH, "");

        db.insert(TBL_WS_ADDRESS, null, data);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_WS_ADDRESS);
        onCreate(db);
    }


    //region web service connection data

    private String getWsAddressElement(String colName)
    {
        Cursor rs = getReadableDatabase().rawQuery("SELECT * FROM " + TBL_WS_ADDRESS + " WHERE " + COL_ID + " = 1", null);
        rs.moveToFirst();
        String element = rs.getString(rs.getColumnIndex(colName));
        rs.close();
        return element;
    }
    private void setWsAddressElement(String colName, String value)
    {
        getWritableDatabase().execSQL("UPDATE " + TBL_WS_ADDRESS + " SET " + colName + " = \"" + value + "\" WHERE " + COL_ID + " = 1");
    }
    public String getIp()
    {
        return getWsAddressElement(COL_IP);
    }
    public void setIp(String ip)
    {
        setWsAddressElement(COL_IP, ip);
    }
    public String getPort()
    {
        return getWsAddressElement(COL_PORT);
    }
    public void setPort(String port)
    {
        setWsAddressElement(COL_PORT, port);
    }
    public String getBasePath()
    {
        return getWsAddressElement(COL_BASE_PATH);
    }
    public void setColBasePath(String basePath)
    {
        setWsAddressElement(COL_BASE_PATH, basePath);
    }

    //endregion

    //region data retrieval

    private Customer getCustomerFromCursor(Cursor rs)
    {
        Customer c = new Customer();

        c.setCid(rs.getInt(rs.getColumnIndex(COL_CID)));
        c.setCompany(rs.getString(rs.getColumnIndex(COL_COMPANY)));
        c.setAddress(rs.getString(rs.getColumnIndex(COL_ADDRESS)));
        c.setZip(rs.getString(rs.getColumnIndex(COL_ZIP)));
        c.setCity(rs.getString(rs.getColumnIndex(COL_CITY)));
        c.setCountry(rs.getString(rs.getColumnIndex(COL_COUNTRY)));
        c.setContractId(rs.getString(rs.getColumnIndex(COL_CONTRACT_ID)));
        Calendar cal = parseCalendar(rs.getString(rs.getColumnIndex(COL_CONTRACT_DATE)));
        c.setContractDate(cal);

        return c;
    }

    public Customer getCustomer(int cid)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TBL_CUSTOMERS + " WHERE " + COL_CID + " = " + cid, null);
        rs.moveToFirst();
        Customer customer = getCustomerFromCursor(rs);
        rs.close();
        return customer;
    }

    public ArrayList<Customer> getCustomers()
    {
        ArrayList<Customer> list = new ArrayList<>();

        Cursor rs = getReadableDatabase().rawQuery("SELECT * FROM " + TBL_CUSTOMERS, null);
        rs.moveToFirst();
        while (!rs.isAfterLast())
        {
            list.add(getCustomerFromCursor(rs));
            rs.moveToNext();
        }
        rs.close();

        return list;
    }

    //endregion

    public long count()
    {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TBL_CUSTOMERS);
    }

    //region inserts

    public void insertCustomer(Customer c)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();

        data.put(COL_CID, c.getCid());
        data.put(COL_COMPANY, c.getCompany());
        data.put(COL_ADDRESS, c.getAddress());
        data.put(COL_ZIP, c.getZip());
        data.put(COL_CITY, c.getCity());
        data.put(COL_COUNTRY, c.getCountry());
        data.put(COL_CONTRACT_ID, c.getContractId());
        data.put(COL_CONTRACT_DATE, getStringFromCalendar(c.getContractDate()));

        db.insert(TBL_CUSTOMERS, null, data);
    }

    public void insertCustomers(List<Customer> customers)
    {
        for (Customer c : customers) {
            insertCustomer(c);
        }
    }

    //endregion

    //region updates

    public void updateCustomer(Customer c)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();

        data.put(COL_COMPANY, c.getCompany());
        data.put(COL_ADDRESS, c.getAddress());
        data.put(COL_ZIP, c.getZip());
        data.put(COL_CITY, c.getCity());
        data.put(COL_COUNTRY, c.getCountry());
        data.put(COL_CONTRACT_ID, c.getContractId());
        data.put(COL_CONTRACT_DATE, getStringFromCalendar(c.getContractDate()));

        db.update(TBL_CUSTOMERS, data, COL_CID + " = ?", new String[]{Integer.toString(c.getCid())});
    }

    public void updateCustomers(List<Customer> customers)
    {
        for (Customer c : customers)
        {
            updateCustomer(c);
        }
    }

    //endregion

    //region deletes

    public void deleteCustomer(int cid)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TBL_CUSTOMERS, COL_CID + " = ?", new String[]{Integer.toString(cid)});
    }

    public void deleteCustomers()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TBL_CUSTOMERS);
    }

    //endregion

    //region conversion Calendar - String

    private String getStringFromCalendar(Calendar cal)
    {
        if (cal == null)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(cal.getTime());
    }

    private Calendar parseCalendar(String dateTime)
    {
        if (dateTime != null)
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);

            int year = Integer.parseInt(dateTime.substring(0, 4));
            int month = Integer.parseInt((dateTime.substring(5, 7)));
            int day = Integer.parseInt(dateTime.substring(8, 10));
            int hour = Integer.parseInt(dateTime.substring(11, 13));
            int minute = Integer.parseInt(dateTime.substring(14, 16));
            int second = Integer.parseInt(dateTime.substring(17, 19));

            cal.set(year, month - 1, day, hour, minute, second);
            return cal;
        }
        else
        {
            return null;
        }
    }

    //endregion

}