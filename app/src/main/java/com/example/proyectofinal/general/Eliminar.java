package com.example.proyectofinal.general;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class Eliminar extends AsyncTask<String,Integer,Boolean> {


    private detalle_alimento_general detalle_alimento_general;

    ProgressDialog progress;
    Context context;

    public Eliminar(detalle_alimento_general detalle_alimento_general, Context context) {
        this.detalle_alimento_general = detalle_alimento_general;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Cargando desde el servidor...");
        progress.setMax(100);
        progress.show();
    }

    protected Boolean doInBackground(String... params) {

        boolean resul = true;

        HttpClient httpClient = new DefaultHttpClient();


        HttpPut del =
                new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/delalimento");

        del.setHeader("content-type", "application/json");

        try
        {
            JSONObject dato = new JSONObject();

            dato.put("usuario", detalle_alimento_general.usuario);
            dato.put("alimento", detalle_alimento_general.nombreAlimento);


            StringEntity entity = new StringEntity(dato.toString());
            del.setEntity(entity);

            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
            resul = false;
        }

        return resul;
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result == true) { }
        progress.dismiss();
    }
}
