package com.example.proyectofinal.ObjetosFire;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public  class MySQLFirebase {

    //NUEVO USUARIO
    public static class Insertar extends AsyncTask<String,Integer,Boolean> {




        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juandcepeda/phpRestPFG/public/index.php/api/newusuario");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));

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

}
