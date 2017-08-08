package com.w.m.crmmobile.model;


import java.util.Calendar;


public class Customer
{
    private int cid;
    public int getCid()
    {
        return cid;
    }
    public void setCid(int cid)
    {
        this.cid = cid;
    }

    private String company;
    public String getCompany()
    {
        return company;
    }
    public void setCompany(String company)
    {
        this.company = company;
    }

    private String address;
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    private String zip;
    public String getZip()
    {
        return zip;
    }
    public void setZip(String zip)
    {
        this.zip = zip;
    }

    private String city;
    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    private String country;
    public String getCountry()
    {
        return country;
    }
    public void setCountry(String country)
    {
        this.country = country;
    }

    private String contractId;
    public String getContractId()
    {
        return contractId;
    }
    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    private Calendar contractDate;
    public Calendar getContractDate()
    {
        return contractDate;
    }
    public void setContractDate(Calendar contractDate)
    {
        this.contractDate = contractDate;
    }

    @Override
    public String toString()
    {
        // Implementation needed to use default filtering of array adapter.
        return getCompany() + " " + getCid();
    }
}
