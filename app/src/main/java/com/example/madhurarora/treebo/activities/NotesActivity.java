package com.example.madhurarora.treebo.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madhurarora.treebo.DB.Notes;
import com.example.madhurarora.treebo.DB.NotesDao;
import com.example.madhurarora.treebo.R;
import com.example.madhurarora.treebo.adapter.NotesAdapter;
import com.example.madhurarora.treebo.app.ApplicationClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class NotesActivity extends AppCompatActivity {

    private TextView title;
    private TextView noItems;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NotesAdapter adapter;

    private ArrayList<Notes> notes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView) findViewById(R.id.noteList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter(this, notes);
        recyclerView.setAdapter(adapter);

        noItems = (TextView) findViewById(R.id.noItems);
        title = (TextView) findViewById(R.id.titleBar);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Mika Melvas - RionaSans-RegularItalic.ttf");
        title.setText(R.string.activity_notes);
        title.setTypeface(font);

        FloatingActionButton compose = (FloatingActionButton) findViewById(R.id.fab);
        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        noItems.setVisibility(View.GONE);
        getData();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        int id = item.getItemId();
        Notes note = notes.get(id);
        NotesDao notesDao = new NotesDao(ApplicationClass.getAppContext());
        if(title.equalsIgnoreCase("DELETE")){
            notesDao.delete(note);
            getData();
        } else if(title.equalsIgnoreCase("EDIT")) {
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("Instance", "EDIT");
            intent.putExtra("ID", note.getID());
            intent.putExtra("TITLE", note.getTitle());
            intent.putExtra("BODY", note.getBody());
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        NotesDao notesDao = new NotesDao(ApplicationClass.getAppContext());
        List<Notes> notes = notesDao.getAll();

        this.notes = new ArrayList<>(notes);
        if(adapter == null) {
            adapter = new NotesAdapter(this, this.notes);
        }
        if(this.notes.size() == 0) {
            noItems.setVisibility(View.VISIBLE);
        } else {
            noItems.setVisibility(View.GONE);
        }
        adapter.setNotes(this.notes);
        adapter.notifyDataSetChanged();
    }
}
