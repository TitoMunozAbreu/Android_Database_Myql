package com.example.agenda_database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_database.model.Contacto;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyviewHolder> {
    private RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<Contacto> contactos;

    public Adaptador(Context context, RecyclerViewInterface recyclerViewInterface){
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        contactos = new ArrayList<>();
    }
    public Adaptador(Context context, List<Contacto> contactos){
        this.context = context;
        this.contactos = contactos;

    }

    public void setOnItemClick(RecyclerViewInterface recyclerViewInterface){
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Adaptador.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent,false);

        return new MyviewHolder(view, recyclerViewInterface, contactos);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.MyviewHolder holder, int position) {
        holder.rNombre.setText(contactos.get(position).getNombre());
        holder.rMovil.setText(contactos.get(position).getMovil());
        holder.rEmail.setText(contactos.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return contactos.size() ;
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        TextView rNombre, rMovil, rEmail;

        public MyviewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface,
                            List<Contacto> contactos) {
            super(itemView);

            this.rNombre = itemView.findViewById(R.id.rNombre);
            this.rMovil = itemView.findViewById(R.id.rMovil);
            this.rEmail = itemView.findViewById(R.id.rEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(contactos.get(position));
                        }
                    }
                }
            });
        }
    }
}

