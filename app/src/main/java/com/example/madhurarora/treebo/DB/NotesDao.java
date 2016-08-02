package com.example.madhurarora.treebo.DB;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class NotesDao {

    Dao<Notes, String> notesDao;

    public NotesDao(Context context) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            DatabaseHelper db = dbManager.getHelper(context);
            notesDao = db.getNotesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(Notes notes) {
        try {
            Dao.CreateOrUpdateStatus status = notesDao.createOrUpdate(notes);
            return status.getNumLinesChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Notes notes) {
        try {
            return notesDao.update(notes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Notes notes) {
        try {
            return notesDao.delete(notes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Notes getShipmentbyID(String ID) {
        try {
            return notesDao.queryForId(ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Notes> getAll() {
        try {
            return notesDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAll() {
        try {
            notesDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws SQLException {
                    List<Notes> shipmentEntries = getAll();
                    if (shipmentEntries != null) {
                        for (int i = 0; i < shipmentEntries.size(); i++) {
                            delete(shipmentEntries.get(i));
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
