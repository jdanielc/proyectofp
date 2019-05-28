package com.example.proyectofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectofinal.general.alimentoVo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class Listar extends AsyncTask<String, Integer, Boolean> {

    int AlimentosListar;
    ProgressDialog progress;
    Context context;
    private FragmentAlimentosPrincipal fragmentAlimentosPrincipal;

    public Listar(FragmentAlimentosPrincipal fragmentAlimentosPrincipal, int AlimentosListar, Context context) {
        this.fragmentAlimentosPrincipal = fragmentAlimentosPrincipal;
        this.AlimentosListar = AlimentosListar;
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
        HttpGet del = null;

        switch (AlimentosListar) {
            case 0:
                del = new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/inicial");
                resul = HttpGet(resul, httpClient, del);
                break;

            case 1:
                del = new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento");
                resul = HttpGet(resul, httpClient, del);
                break;
            case 2:
                del = new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento/" + fragmentAlimentosPrincipal.usuario);
                resul = HttpGet(resul, httpClient, del);

                break;
            case 3:
                del = new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento/" + fragmentAlimentosPrincipal.usuario);
                resul = HttpGet(resul, httpClient, del);

                //Filtro los alimentos
                resul = fragmentFavoritos(httpClient);

                break;
            default:
                del = new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/alimento");
                resul = HttpGet(resul, httpClient, del);
                break;
        }

        return resul;
    }

    private boolean HttpGet(boolean resul, HttpClient httpClient, HttpGet del) {
        try {
            del.setHeader("content-type", "application/json");

            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONArray mensajes = new JSONArray(respStr);

            for (int i = 0; i < mensajes.length(); i++) {

                JSONObject mensaje = mensajes.getJSONObject(i);

                int id = mensaje.getInt("ID");
                String nombre = mensaje.getString("nombre");
                String info = mensaje.getString("info");
                int icono = mensaje.getInt("iconoId");
                int upvotes = mensaje.getInt("upvotes");
                String descripcion = mensaje.getString("descripcion");
                int imagen = mensaje.getInt("imagenDetalle");
                String usuario = mensaje.getString("usuario");
                int seguro = mensaje.getInt("seguro");

                boolean s = isSeguro(seguro);


                alimentoVo elemento = new alimentoVo(id, nombre, info, icono, descripcion, upvotes,
                        usuario, imagen, s);

                fragmentAlimentosPrincipal.listaAlimentos.add(elemento);


            }

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }
        return resul;
    }

    private boolean isSeguro(int seguro) {
        boolean s;
        if (seguro == 0) {
            s = false;
        } else {
            s = true;
        }
        return s;
    }

    private boolean fragmentFavoritos(HttpClient httpClient) {
        ArrayList<Integer> listaID = new ArrayList<>();
        ArrayList<alimentoVo> listaFavoritos = new ArrayList<>();
        boolean resul = false;

        HttpGet del =
                new HttpGet("http://damnation.ddns.net/daniel/phpRestPFG/public/api/allfavorito/" + fragmentAlimentosPrincipal.usuario);

        del.setHeader("content-type", "application/json");

        try {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            JSONArray mensajes = new JSONArray(respStr);

            /*Tomo todos los IDs de los alimentos de la tabla de favoritos correspondiente a el usuario actual*/
            for (int i = 0; i < mensajes.length(); i++) {

                JSONObject mensaje = mensajes.getJSONObject(i);
                listaID.add(mensaje.getInt("alimento"));
            }

            /*Tomo los alimentos de la lista de alimentos cuyos IDs coinciden con los IDs de alimentos favoritos
             * y los almaceno en un ArrayList aparte*/
            if (listaID.size() > 0) {
                for (alimentoVo alimento :
                        fragmentAlimentosPrincipal.listaAlimentos) {
                    if (listaID.contains(alimento.getID())) {
                        listaFavoritos.add(alimento);
                    }
                }

                //Vacio la lista de alimentos y la relleno con los alimentos guardados aparte anteriormente
                fragmentAlimentosPrincipal.listaAlimentos.clear();
                fragmentAlimentosPrincipal.listaAlimentos = listaFavoritos;
                resul = true;
            } else {
                fragmentAlimentosPrincipal.listaAlimentos.clear();
                resul = false;
            }

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);

        }

        return resul;

    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result && fragmentAlimentosPrincipal.listaAlimentos.size() > 0) {
            //Rellenamos la lista con los resultados
            fragmentAlimentosPrincipal.adapter.updateList(fragmentAlimentosPrincipal.listaAlimentos);
        }
        progress.dismiss();
    }
}
