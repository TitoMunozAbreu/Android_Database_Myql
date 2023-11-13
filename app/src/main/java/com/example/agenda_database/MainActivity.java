package com.example.agenda_database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.agenda_database.model.Contacto;
import com.example.agenda_database.repository.ContactoRepository;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{


    ContactoRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new ContactoRepository(MainActivity.this);
        repository.listarContacto(MainActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(repository.adaptador);

        repository.adaptador.setOnItemClick(MainActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    @Override
    public void onItemClick(Contacto contacto) {
        mostrarDialogOpciones(contacto);
    }

    private void mostrarDialogOpciones(Contacto contacto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contacto.getNombre() + " opciones");
        builder.setItems(new CharSequence[]{"Editar","Eliminar"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        onEditarPantalla(contacto);
                        break;
                    case 1:
                    onEliminarContacto(contacto.getNombre());
                }
            }
        });

        builder.create().show();
    }

    private void onEliminarContacto(String nombre) {
        repository.eliminarContacto(nombre);
        Toast.makeText(MainActivity.this, "Contacto eliminado", Toast.LENGTH_LONG).show();
    }

    private void onEditarPantalla(Contacto contacto) {
        Intent i = new Intent(MainActivity.this, EditarContacto.class);
        i.putExtra("contacto", contacto);
        startActivity(i);

    }

    public void CrearContacto(View view){
        Intent i = new Intent(this, CrearContacto.class);
        startActivity(i);
    }

}