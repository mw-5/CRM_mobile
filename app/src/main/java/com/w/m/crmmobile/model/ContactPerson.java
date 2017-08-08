package com.w.m.crmmobile.model;


public class ContactPerson
{

    private int id;
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    private int cid;
    public int getCid()
    {
        return cid;
    }
    public void setCid(int cid)
    {
        this.cid = cid;
    }

    private String forename;
    public String getForename()
    {
        return forename;
    }
    public void setForename(String forename)
    {
        this.forename = forename;
    }

    private String surename;
    public String getSurename()
    {
        return surename;
    }
    public void setSurename(String surename)
    {
        this.surename = surename;
    }

    private String gender;
    public String getGender()
    {
        return gender;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    private String email;
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    private String phone;
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    private boolean mainContact;
    public boolean isMainContact()
    {
        return mainContact;
    }
    public void setMainContact(boolean mainContact)
    {
        this.mainContact = mainContact;
    }

    public String getName()
    {
        return getForename() + " " + getSurename();
    }

}