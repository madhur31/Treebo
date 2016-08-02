package com.example.madhurarora.treebo.DB;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by madhur.arora on 02/08/16.
 */
@DatabaseTable(tableName = "notes")
public class Notes {

    @DatabaseField(generatedId = true, columnName = "test", allowGeneratedIdInsert = true)
    private int ID;

    @DatabaseField(columnName = "date")
    private String date;

    @DatabaseField(columnName = "title")
    private String title;

    @DatabaseField(columnName = "body")
    private String body;

    public Notes() { }

    public Notes(String date, String title, String body) {
        //setID(ID);
        setBody(body);
        setDate(date);
        setTitle(title);
    }

    public Notes(int ID, String date, String title, String body) {
        this.ID = ID;
        this.date = date;
        this.title = title;
        this.body = body;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
