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

public class CrearContacto extends AppCompatActivity {
    private EditText nombre, movil, email;
    private ContactoRepository contactoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_contacto);

        this.nombre = findViewById(R.id.Anombre);
        this.movil = findViewById(R.id.Amovil);
        this.email = findViewById(R.id.Aemail);

        //incializar repositorio
        this.contactoRepository = new ContactoRepository(CrearContacto.this);
    }

    public void crearContacto(View view){
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
        Contacto nuevoContacto = new Contacto(nombre,movil,email);

        //guardar la respuesta a la llamada crear contacto
       this.contactoRepository.crearContacto(
            nuevoContacto,
            new OnContactoListener() {
                @Override
                public void onContactoListener(boolean success) {
                    if(success){
                        limpiarCampos();
                        Toast.makeText(CrearContacto.this, "Contacto: " + nuevoContacto.getNombre() + " creado",Toast.LENGTH_SHORT).show();
                        volverPantallaPrincipal(view);
                    }else {
                        Toast.makeText(CrearContacto.this, "Contacto: " + nuevoContacto.getNombre() + " NO creado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );

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