package com.example.madhurarora.treebo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.madhurarora.treebo.DB.Notes;
import com.example.madhurarora.treebo.R;
import com.example.madhurarora.treebo.activities.AddNoteActivity;

import java.util.ArrayList;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private ArrayList<Notes> notes;
    private Context context;

    public NotesAdapter(Context context, ArrayList<Notes> notes) {
        this.notes = notes;
        this.context = context;
    }

    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notes, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notes note = notes.get(position);
        if(note != null) {
            holder.NotesTitle.setText(note.getTitle());
            holder.NotesDescription.setText(note.getBody());

            holder.NotesCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddNoteActivity.class);
                    intent.putExtra("Instance", "OPEN");
                    intent.putExtra("TITLE", note.getTitle());
                    intent.putExtra("BODY", note.getBody());
                    context.startActivity(intent);
                }
            });

            holder.NotesCV.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (notes == null)
            return 0;
        return notes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {

        CardView NotesCV;
        TextView NotesTitle;
        TextView NotesDescription;

        public ViewHolder(View view) {
            super(view);
            view.setOnCreateContextMenuListener(this);
            NotesCV = (CardView) itemView.findViewById(R.id.notes_card_view);

            NotesTitle = (TextView) itemView.findViewById(R.id.notes_title);
            NotesDescription = (TextView) itemView.findViewById(R.id.notes_description);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //    int position = info.position;

            menu.setHeaderTitle("Select The Action");
            menu.add(0, getPosition(), 0, "Delete");
            menu.add(0, getPosition(), 0, "Edit");
        }
    }
}
