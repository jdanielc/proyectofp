package com.example.proyectofinal.ObjetosFire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectofinal.general.Modelo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRegistrar;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public  class MySQLFirebase {


    //NUEVO USUARIO
    public static class Insertar extends AsyncTask<String,Integer,Boolean>  {




        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://damnation.ddns.net/daniel/phpRestPFG/public/api/newusuario");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();


                dato.put("ID", params[0]);
                dato.put("nickname", params[1]);
                dato.put("email",params[2]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        @SuppressLint("RestrictedApi")
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(getApplicationContext(), "INSERTADO NUEVO ELEMENTO", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public static class Actualizar extends AsyncTask<String,Integer,Boolean> {

        int ID;
        String nombre;
        String info;
        int icono;
        int upvotes;
        String descripcion;
        int imagen;
        boolean favoritos;
        String usuario;


       public Actualizar(int ID, String nombre, String info, int icono, int upvotes, String descripcion, int imagen, boolean favoritos, String usuario){
           this.ID = ID;
           this.nombre=nombre;
           this.info=info;
           this.icono=icono;
           this.upvotes=upvotes;
           this.descripcion=descripcion;
           this.imagen=imagen;
           this.favoritos=favoritos;
           this.usuario = usuario;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPut put =new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento/"
                    + ID);


            put.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("id", ID);
                dato.put("nombre", nombre);
                dato.put("info", info);
                dato.put("descripcion", descripcion);

                dato.put("icono", icono);
                dato.put("upvotes", upvotes);
                dato.put("imagen", imagen);

                String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                 dato.put("favoritos", 1);

                dato.put("usuario", usuario);


                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());


                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        @SuppressLint("RestrictedApi")
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(getApplicationContext(), "ELEMENTO ACTUALIZADO", Toast.LENGTH_SHORT).show();

            }
        }
    }//



    public static class ListarAllFavorites extends AsyncTask<String,Integer,Boolean> {


        String usuario;
        int alimento;
        Context context;

        boolean existe = false;

        public ListarAllFavorites(Context context){
            this.context = context;
        }

        protected Boolean doInBackground(String... params) {

            boolean resul = false;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/allfavorito/" + params[0]);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray mensajes = new JSONArray(respStr);

                for(int i =0; i<mensajes.length(); i++){

                    JSONObject mensaje = mensajes.getJSONObject(i);



                    if(mensaje.getInt("alimento") == Integer.parseInt(params[1])){
                        resul = true;
                        existe = true;
                        break;
                    }

                }

                /*Tomo los datos desde los paramentos*/

                usuario = params[0];

                alimento = Integer.parseInt(params[1]);


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }



        protected void onPostExecute(Boolean result) {

            if (result)
            {
                FancyToast.makeText(context, "Quitado de Favoritos", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();
                new ControlFavoritos.Eliminar(usuario, alimento).execute();



            }else {
                FancyToast.makeText(context, "Añadido a Favoritos", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                new ControlFavoritos.Insertar(usuario, alimento).execute();
            }
        }
    }


  /*  private class Action extends AsyncTask<String,Integer,Boolean> {

        int tipoAccion;

        public Action(int tipoAccion){
            this.tipoAccion = tipoAccion;
        }


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            switch (tipoAccion) {
                case 1:
                    //AÑADIR
                    resul = ejecucion(resul, httpClient, params);
                    break;
                case 2:
                    //MODIFICAR
                    resul = ejecucion(resul, httpClient, params);
                    break;
            }

            return resul;
        }


        private boolean ejecucion(boolean resul, HttpClient httpClient, String[] params) {


            HttpPost post = new HttpPost("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento");
            post.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();


                dato.put("nombre", params[0]);
                dato.put("info", params[1]);
                dato.put("descripcion", params[2]);
                dato.put("icono", params[4]);
                dato.put("imagen", params[4]);
                dato.put("upvotes", 0);
                dato.put("favorito", null);
                dato.put("usuario", params[3]);


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }

        @SuppressLint("RestrictedApi")
        protected void onPostExecute(Boolean result) {

            if (result) {

            }
        }


    }
*/
    public static class Action extends AsyncTask<String,Integer,Boolean> {

        int tipoAccion;

        public Action(int tipoAccion){
            this.tipoAccion = tipoAccion;
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            switch(tipoAccion){
                case 1:

                    resul = añadirAlimento(resul, httpClient, params);

                    break;

                case 2:
                    resul = modificarAlimento(resul, httpClient, params);

                    break;
            }

            return resul;
        }

        private boolean añadirAlimento(boolean resul, HttpClient httpClient, String[] params) {



            HttpPost post = new HttpPost("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();


                int imagen = Integer.parseInt(params[4]);

                dato.put("nombre", params[0]);
                dato.put("info", params[1]);
                dato.put("descripcion", params[2]);
                dato.put("icono", imagen);
                dato.put("imagen", imagen);
                dato.put("upvotes", 0);
                dato.put("usuario", params[3]);





                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }
            return resul;
        }


        private boolean modificarAlimento(boolean resul, HttpClient httpClient, String[] params) {


            HttpPut put = new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento/"+params[5]);
            put.setHeader("content-type", "application/json");


            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("id", params[5]);
                dato.put("nombre", params[0]);
                dato.put("info", params[1]);
                dato.put("descripcion", params[2]);
                dato.put("icono", params[4]);
                dato.put("imagen", params[4]);
                dato.put("upvotes", 0);
                dato.put("usuario", params[3]);


                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());


                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

            }
        }
    }
}
