package com.example.madhurarora.treebo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME="treebo.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Notes, String> notesDao;
    private RuntimeExceptionDao<Notes, String> notesRuntimeDao;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables(connectionSource);
    }

    private void createAllTables(ConnectionSource connectionSource){
        createtable(connectionSource, Notes.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            dropAllTables(connectionSource);
            onCreate(database, connectionSource);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void dropAllTables(ConnectionSource connectionSource){
        dropTable(connectionSource, Notes.class);
    }

    public Dao<Notes, String> getNotesDao() throws SQLException {
        if(notesDao == null) {
            notesDao = getDao(Notes.class);
        }
        return notesDao;
    }

    public RuntimeExceptionDao<Notes, String> getNotesRuntimeDao() throws SQLException {
        if(notesRuntimeDao == null) {
            notesRuntimeDao = getRuntimeExceptionDao(Notes.class);
        }
        return notesRuntimeDao;
    }

    @Override
    public void close() {
        super.close();
        notesDao = null;
    }

    private void dropTable(ConnectionSource connectionSource, Class classType){
        try{
            TableUtils.dropTable(connectionSource, classType, true);
        }catch (SQLException e) {}
    }

    private void createtable(ConnectionSource connectionSource, Class classtype){
        try{
            TableUtils.createTable(connectionSource, classtype);
        }catch (SQLException e){}
    }
}
