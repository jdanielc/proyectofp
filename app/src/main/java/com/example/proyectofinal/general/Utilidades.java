package com.example.proyectofinal.general;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Utilidades {

    public static WaitingServer waitingServer;

    public static void waitingServer(Context context){
        waitingServer = (WaitingServer) new WaitingServer(context).execute();

    }



    public static class WaitingServer extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog progress;
        private Context context;

        public WaitingServer(Context context) {
            this.context = context;



        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            for (int i = 0; i < 2; i++) {
                tarea();
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("Cargando desde el servidor...");
            progress.setMax(100);
            progress.show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            progress.dismiss();
        }

        private void tarea() {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException iee){
                iee.printStackTrace();
            }
        }
    }
}
