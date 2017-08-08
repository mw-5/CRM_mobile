package com.w.m.crmmobile.model;


import java.util.Calendar;


public class Note
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

    private String createdBy;
    public String getCreatedBy()
    {
        return createdBy;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private Calendar entryDate;
    public Calendar getEntryDate()
    {
        return entryDate;
    }
    public void setEntryDate(Calendar entryDate)
    {
        this.entryDate = entryDate;
    }

    private String memo;
    public String getMemo()
    {
        return memo;
    }
    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    private String category;
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    private String attachment;
    public String getAttachment()
    {
        return attachment;
    }
    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }
}
