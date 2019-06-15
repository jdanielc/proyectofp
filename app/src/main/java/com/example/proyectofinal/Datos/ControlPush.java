package com.example.proyectofinal.Datos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


    public class ControlPush extends AsyncTask<String,Integer,Boolean> {
        int upvotes;
        int alimento;
        boolean existe;

        public ControlPush(int upvotes, int alimento, boolean existe){
            this.upvotes = upvotes;
            this.alimento=alimento;
            this.existe = existe;

        }

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPut post;

            if(existe){
                post = new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/upmenos");

            }else{
                post = new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/upmas");
            }


            post.setHeader("content-type", "application/json");
            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("upvotes", upvotes);
                dato.put("alimento", alimento);
                dato.put("usuario", params[0]);

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
