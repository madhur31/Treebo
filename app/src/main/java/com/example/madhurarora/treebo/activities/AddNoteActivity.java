package com.example.madhurarora.treebo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madhurarora.treebo.DB.Notes;
import com.example.madhurarora.treebo.DB.NotesDao;
import com.example.madhurarora.treebo.R;
import com.example.madhurarora.treebo.Util.AbstractAsyncTask;
import com.example.madhurarora.treebo.Util.DateUtils;
import com.example.madhurarora.treebo.Util.ViewUtils;
import com.example.madhurarora.treebo.app.ApplicationClass;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class AddNoteActivity extends AppCompatActivity {

    private TextView title;
    private EditText noteTitle;
    private EditText noteBody;
    private Button addButton;

    private int ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_add_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noteTitle = (EditText) findViewById(R.id.notes_title);
        noteBody = (EditText) findViewById(R.id.notes_body);
        addButton = (Button) findViewById(R.id.add_notes);
        title = (TextView) findViewById(R.id.titleBar);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Mika Melvas - RionaSans-RegularItalic.ttf");
        title.setText(R.string.add_notes);
        title.setTypeface(font);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = addButton.getText().toString();
                if (buttonText.equalsIgnoreCase("Add Note")) {
                    addNotes();
                } else if (buttonText.equalsIgnoreCase("Update Note")) {
                    addNotes();
                } else if (buttonText.equalsIgnoreCase("Edit Note")) {
                    noteBody.setEnabled(true);
                    noteTitle.setEnabled(true);
                    noteTitle.setFocusableInTouchMode(true);
                    noteBody.setFocusable(true);
                    noteBody.setFocusableInTouchMode(true);
                    noteTitle.setFocusable(true);
                    addButton.setText("Update Note");
                }
            }
        });
        checkIntent();
    }

    private void checkIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String type = intent.getStringExtra("Instance");
            String title = intent.getStringExtra("TITLE");
            String body = intent.getStringExtra("BODY");
            ID = intent.getIntExtra("ID", -1);
            if (type != null) {
                if (type.equalsIgnoreCase("EDIT")) {
                    noteBody.setText(body);
                    noteTitle.setText(title);
                    addButton.setText("Update Note");
                } else if (type.equalsIgnoreCase("OPEN")) {
                    noteBody.setEnabled(false);
                    noteTitle.setEnabled(false);
                    noteBody.setFocusable(false);
                    noteTitle.dispatchWindowFocusChanged(true);
                    noteTitle.setFocusable(false);
                    noteBody.setText(body);
                    noteTitle.setText(title);
                    addButton.setText("Edit Note");
                }
            }
        }
    }

    private void endActivity() {
        String buttonText = addButton.getText().toString();
        if (buttonText.equalsIgnoreCase("Edit Note")) {
            finish();
            return;
        }
        if (!ViewUtils.isEmpty(noteBody) || !ViewUtils.isEmpty(noteTitle)) {
            final AlertDialog alertDialog = new AlertDialog.Builder(AddNoteActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert).create();
            alertDialog.setMessage("Note not saved. Still go back?");
            alertDialog.setCancelable(true);

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddNoteActivity.this.finish();
                   // alertDialog.dismiss();
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // alertDialog.dismiss();
                    //alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            AddNoteActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        endActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        endActivity();
        return super.onSupportNavigateUp();
    }

    private void addNotes() {

        if (ViewUtils.isEmpty(noteBody) || ViewUtils.isEmpty(noteTitle)) {
            Toast.makeText(this, "Body and Title required for saving", Toast.LENGTH_SHORT).show();
            return;
        }

        AbstractAsyncTask.runAsyncParallel(new Runnable() {
            @Override
            public void run() {
                try {
                    NotesDao notesDao = new NotesDao(ApplicationClass.getAppContext());
                    Notes noteData = null;
                    if(ID == -1) {
                        noteData = new Notes(DateUtils.getDate(), noteTitle.getText().toString(), noteBody.getText().toString());
                    } else {
                        noteData = new Notes(ID, DateUtils.getDate(), noteTitle.getText().toString(), noteBody.getText().toString());
                    }
                    if (noteData != null) {
                        notesDao.create(noteData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
