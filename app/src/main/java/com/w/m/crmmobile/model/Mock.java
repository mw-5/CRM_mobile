package com.w.m.crmmobile.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Mock
{
    public static List<Customer> createCustomers()
    {
        ArrayList<Customer> list = new ArrayList<>();
        Customer cli;
        Calendar c;

        cli = new Customer();
        cli.setCid(1);
        cli.setCompany("Smoothies Gmbh");
        cli.setAddress("Torstr. 35");
        cli.setZip("10119");
        cli.setCity("Berlin");
        cli.setCountry("Germany");
        cli.setContractId("g1");
        c = Calendar.getInstance();
        c.set(2017,2,1);
        cli.setContractDate(c);
        list.add(cli);

        cli = new Customer();
        cli.setCid(2);
        cli.setCompany("StartUp Coffee, Inc.");
        cli.setAddress("2675 Middlefield Rd A");
        cli.setZip("CA 94306");
        cli.setCity("Palo Alto");
        cli.setCountry("USA");
        cli.setContractId("u1");
        c = Calendar.getInstance();
        c.set(2017, 3, 4);
        cli.setContractDate(c);
        list.add(cli);

        cli = new Customer();
        cli.setCid(3);
        cli.setCompany("El Sabor del Cacao S.L.");
        cli.setAddress("Plaza Puerta del Sol 6");
        cli.setZip("28013");
        cli.setCity("Madrid");
        cli.setCountry("Spain");
        cli.setContractId("s1");
        c = Calendar.getInstance();
        c.set(2017,4,2);
        cli.setContractDate(c);
        list.add(cli);

        return list;
    }

    public static Customer createCustomer(int cid)
    {
        for (Customer c : createCustomers())
        {
            if (c.getCid() == cid)
            {
                return c;
            }
        }
        return new Customer();
    }

    public static List<ContactPerson> createContactPersons(int cid)
    {
        List<ContactPerson> list = new ArrayList<>();
        ContactPerson cp;
        switch(cid)
        {
            case 1:
                cp = new ContactPerson();
                cp.setId(1);
                cp.setCid(1);
                cp.setForename("Chantal");
                cp.setSurename("Obst");
                cp.setGender("f");
                cp.setEmail("chantal.obst@smoothies.de");
                cp.setPhone("+49 30 12345678");
                cp.setMainContact(true);
                list.add(cp);
                break;
            case 2:
                cp = new ContactPerson();
                cp.setId(2);
                cp.setCid(2);
                cp.setForename("Mike");
                cp.setSurename("Miller");
                cp.setGender("m");
                cp.setEmail("mike.miller@startupcafe.com");
                cp.setPhone("+1 650 1234567");
                cp.setMainContact(true);
                list.add(cp);
                break;
            case 3:
                cp = new ContactPerson();
                cp.setId(3);
                cp.setCid(3);
                cp.setForename("Carlos");
                cp.setSurename("Sanchez");
                cp.setGender("m");
                cp.setEmail("carlos.sanchez@sabordelcacao.es");
                cp.setPhone("+34 915 123456");
                cp.setMainContact(true);
                list.add(cp);

                cp = new ContactPerson();
                cp.setId(4);
                cp.setCid(3);
                cp.setForename("Carla");
                cp.setSurename("Sanchez");
                cp.setGender("f");
                cp.setEmail("carla.sanchez@sabordelcacao.es");
                cp.setPhone("+34 915 123456");
                cp.setMainContact(false);
                list.add(cp);
                break;
        }
        return list;
   }

   public static ContactPerson createContactPerson(int id)
   {
       for (Customer customer : createCustomers())
       {
           for (ContactPerson cp : createContactPersons(customer.getCid()))
           {
               if (cp.getId() == id)
               {
                   return cp;
               }
           }
       }
       // if id is not found
       ContactPerson cp = new ContactPerson();
       cp.setId(id);
       return cp;
   }

   public static List<Note> createNotesList(int cid)
   {
       List<Note> list = new ArrayList<>();
       Note n;
       Calendar c;
       switch(cid)
       {
           case 1:
               n = new Note();
               n.setId(1);
               n.setCid(1);
               n.setCreatedBy("user1");
               c = Calendar.getInstance();
               c.set(2017, 3, 4);
               n.setEntryDate(c);
               n.setCategory("category1");
               n.setAttachment("test.pdf");
               n.setMemo("text\ntext");
               list.add(n);

               n = new Note();
               n.setId(2);
               n.setCid(1);
               n.setCreatedBy("user2");
               c = Calendar.getInstance();
               c.set(2017, 4, 5);
               n.setEntryDate(c);
               n.setMemo("text2");
               n.setCategory("category2");
               list.add(n);
               break;
           case 2:
               n = new Note();
               n.setId(3);
               n.setCid(2);
               n.setCreatedBy("user3");
               c = Calendar.getInstance();
               c.set(2017, 7, 8);
               n.setEntryDate(c);
               n.setMemo("text3");
               list.add(n);

               n = new Note();
               n.setId(4);
               n.setCid(2);
               n.setCreatedBy("user4");
               c = Calendar.getInstance();
               c.set(2017, 8, 6);
               n.setEntryDate(c);
               n.setMemo("text4");
               list.add(n);
               break;
           case 3:
               n = new Note();
               n.setId(5);
               n.setCid(3);
               n.setCreatedBy("user5");
               c = Calendar.getInstance();
               c.set(2017, 9, 10);
               n.setEntryDate(c);
               n.setMemo("text5 sdalökfjsaldökfjsdölfkjsadölfjksdölfjkdölfjasölsfdkjsaöljsfdalkj sdjkölasfdjö sdal öjfsaldfj asdjökl \ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5\ntesxt5");
               list.add(n);

               n = new Note();
               n.setId(6);
               n.setCid(3);
               n.setCreatedBy("user6");
               c = Calendar.getInstance();
               c.set(2017, 10, 11);
               n.setEntryDate(c);
               n.setMemo("text6");
               list.add(n);
               break;
       }
       return list;
   }

   public static Note createNote(int id)
   {
       for (Customer c : createCustomers())
       {
           for (Note n : createNotesList(c.getCid()))
           {
               if (n.getId() == id)
               {
                   return n;
               }
           }
       }
       // if note isn't found
       Note n = new Note();
       n.setId(id);
       return n;
   }




}
