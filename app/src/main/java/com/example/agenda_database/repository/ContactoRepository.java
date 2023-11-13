package com.example.agenda_database.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agenda_database.Adaptador;
import com.example.agenda_database.OnBuscarContacto;
import com.example.agenda_database.OnContactoListener;
import com.example.agenda_database.model.Contacto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactoRepository{
    //definir las llamadas http
    private final String INSERTAR_CONTACTO = "http://192.168.0.14:8080/android_database_agenda/insertar_contacto.php";
    private final String BUSCAR_CONTACTOS = "http://192.168.0.14:8080/android_database_agenda/listar_contactos.php";
    private final String BUSCAR_CONTACTO = "http://192.168.0.14:8080/android_database_agenda/buscar_contacto.php?nombre=";
    private final String EDITAR_CONTACTOS = "http://192.168.0.14:8080/android_database_agenda/editar_contacto.php";
    private final String ELIMINAR_CONTACTO = "http://192.168.0.14:8080/android_database_agenda/eliminar_contacto.php";

    private RequestQueue requestQueue;
    private boolean estaInsertado;
    private static List<Contacto> contactos = new ArrayList<>();
    public Adaptador adaptador;
    Context context;

    public ContactoRepository(Context context){
        this.estaInsertado = false;
        this.context = context;
        this.adaptador = new Adaptador( context,contactos);

    }

    public void crearContacto(Contacto nuevoContacto, OnContactoListener onContactoListener){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                INSERTAR_CONTACTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //obtener la respuesta en json
                            JSONObject jsonObject = new JSONObject(response);

                            int success = jsonObject.getInt("success");

                            if(success == 1){
                                Log.d("Volley Response", "contacto registrado");
                                onContactoListener.onContactoListener(true);
                            }else {
                                Log.d("Volley Response", "contacto No registrado");
                                onContactoListener.onContactoListener(false);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context,"El movil introducido ya existe" ,Toast.LENGTH_LONG).show();
                            return;

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error ", error.getMessage());

                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();

                parametros.put("nombre",nuevoContacto.getNombre());
                parametros.put("movil",nuevoContacto.getMovil());
                parametros.put("email", nuevoContacto.getEmail());

                return parametros;
            }
        };
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.add(stringRequest);


    }

    public void listarContacto(Context context){

        StringRequest  request= new StringRequest(
                Request.Method.GET,
                BUSCAR_CONTACTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", "Response received: " + response.toString());
                        contactos.clear();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("datos");

                            if(success.equals("1")){

                                // Iterar sobre el JSONArray para obtener los detalles de cada contacto
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject contacto = jsonArray.getJSONObject(i);
                                    // Obtener los detalles del contacto
                                    String nombre = contacto.getString("nombre");
                                    String movil = contacto.getString("movil");
                                    String email = contacto.getString("email");

                                   Contacto c = new Contacto(nombre,movil,email);
                                   contactos.add(c);
                                   adaptador.notifyDataSetChanged();
                                }

                            }
                            // Llamar al método de retorno con la lista de contactos obtenida
                            Log.d("Volley Response", "Lista de contactos obtenida con éxito. Tamaño: " + contactos.size());
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Volley Error", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR DE CONEXIÓN: ", Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error","Error de conexion: " + error.toString(), error);
                    }
                });

        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.add(request);

    }

    public void editarContactoRepository(String nombreAnterior, Contacto contactoActualizado, OnContactoListener onContactoListener) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                EDITAR_CONTACTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int success = jsonObject.getInt("success");

                            if (success == 1) {
                                Log.d("Volley Response", "contacto actualizado");
                                onContactoListener.onContactoListener(true);
                            } else {
                                Log.d("Volley Response", "contacto No actualizado");
                                onContactoListener.onContactoListener(false);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error ", error.getMessage());

                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();

                parametros.put("nombreAnterior",nombreAnterior);
                parametros.put("nombre", contactoActualizado.getNombre());
                parametros.put("movil", contactoActualizado.getMovil());
                parametros.put("email", contactoActualizado.getEmail());

                return  parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void eliminarContacto(String nombre) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ELIMINAR_CONTACTO   ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int success = jsonObject.getInt("success");

                            if (success == 1) {
                                Log.d("Volley Response", "contacto actualizado");
                            } else {
                                Log.d("Volley Response", "contacto No actualizado");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error ", error.getMessage());

                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();

                parametros.put("nombre", nombre);
                return  parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}

