package com.example.proyectofinal.ObjetosFire;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ControlFavoritos {

    //NUEVO FAVORITO
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

            HttpPost post = new HttpPost("http://damnation.ddns.net/phpRestPFG/public/index.php/api/masfav");
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



    public static class Eliminar extends AsyncTask<String,Integer,Boolean> {
        String usuario;
        int alimento;

        public Eliminar(String usuario, int alimento){
            this.usuario = usuario;
            this.alimento=alimento;
        }


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();




            HttpPut del =
                    new HttpPut("http://damnation.ddns.net/phpRestPFG/public/index.php/api/menosfav");

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

    /*

    public static class Eliminar extends AsyncTask<String,Integer,Boolean> {
        String usuario;
        int alimento;

        public Eliminar(String usuario, int alimento){
            this.usuario = usuario;
            this.alimento=alimento;
        }


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = usuario+"."+alimento;



            HttpDelete del =
                    new HttpDelete("http://damnation.ddns.net/phpRestPFG/public/index.php/api/menosfav/" + id);

            del.setHeader("content-type", "application/json");



            try
            {

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
*/


}
