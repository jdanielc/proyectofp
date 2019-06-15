package com.example.proyectofinal.Datos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectofinal.Fragments.detalle_alimento_general;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ControlFavoritos {

/*Clase encargada de añadir un nuevo registro a la tabla de favoritos si el usuario no la había seleccionado antes

* */
public static class Insertar extends AsyncTask<String,Integer,Boolean> {
        String usuario;
        int alimento;

        public Insertar(String usuario, int alimento){
            this.usuario = usuario;
            this.alimento=alimento;

        }


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://damnation.ddns.net/daniel/phpRestPFG/public/api/masfav");
            post.setHeader("content-type", "application/json");
            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                dato.put("usuario", usuario);
                dato.put("alimento", alimento);

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


/*Se encarga de eliminar un elemento de la tabla de favoritos si el usuario ya tenía registrado como favorito ese alimento*/
    public static class Eliminar extends AsyncTask<String,Integer,Boolean> {
        String usuario;
        int alimento;

        public Eliminar(String usuario, int alimento){
            this.usuario = usuario;
            this.alimento=alimento;
        }

    public Eliminar(detalle_alimento_general detalle_alimento_general, Context context) {
    }


    protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPut del =
                    new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/menosfav");

            del.setHeader("content-type", "application/json");


            try
            {

                JSONObject dato = new JSONObject();

                dato.put("usuario", usuario);
                dato.put("alimento", alimento);


                StringEntity entity = new StringEntity(dato.toString());
                del.setEntity(entity);

                HttpResponse resp = httpClient.execute(del);
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
                Toast.makeText(getApplicationContext(), "ELEMENTO ELIMINADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
