package com.example.agenda_database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agenda_database.model.Contacto;
import com.example.agenda_database.repository.ContactoRepository;

public class EditarContacto extends AppCompatActivity {
    private EditText nombre, movil, email;
    private Contacto contactoSeleccionado;
    private ContactoRepository contactoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto);

        this.nombre = findViewById(R.id.Anombre);
        this.movil = findViewById(R.id.Amovil);
        this.email = findViewById(R.id.Aemail);

        this.contactoRepository = new ContactoRepository(EditarContacto.this);

        datosUsuarioSeleccionado();

    }

    public void datosUsuarioSeleccionado(){

        contactoSeleccionado = (Contacto) getIntent().getSerializableExtra("contacto");
        this.nombre.setText(contactoSeleccionado.getNombre());
        this.movil.setText(contactoSeleccionado.getMovil());
        this.email.setText(contactoSeleccionado.getEmail());
    }

    public void actualizarContacto(View view){
        String nombre = this.nombre.getText().toString();
        String movil = this.movil.getText().toString();
        String email = this.email.getText().toString();
        //comprobar que los datos se han itroducido
        ProgressDialog progressDialog = new ProgressDialog(this);

        if(nombre.isEmpty()){
            this.nombre.setError("Ingresar nombre");
            return;
        }else if(movil.isEmpty()){
            this.movil.setError("Ingresar movil");
            return;
        }else if(email.isEmpty()){
            this.email.setError("Ingresar email");
            return;
        }

        //crear nuevo contacto
        Contacto contactoActualizado = new Contacto(nombre,movil,email);

        this.contactoRepository.editarContactoRepository(
                contactoSeleccionado.getNombre(),
                contactoActualizado,
                new OnContactoListener() {
                    @Override
                    public void onContactoListener(boolean success) {
                        if(success){
                            limpiarCampos();
                            Toast.makeText(EditarContacto.this, "Contacto: " + contactoActualizado.getNombre() + " actualizado",Toast.LENGTH_SHORT).show();
                            volverPantallaPrincipal(view);
                        }else {
                            Toast.makeText(EditarContacto.this, "Contacto: " + contactoActualizado.getNombre() + " NO actualizado",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void limpiarCampos(){
        this.nombre.setText("");
        this.movil.setText("");
        this.email.setText("");
    }

    public void volverPantallaPrincipal(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}