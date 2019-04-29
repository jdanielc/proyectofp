package com.example.proyectofinal;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.general.alimentoVo;
import com.example.proyectofinal.general.detalle_alimento_general;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentAlimentosPrincipal.OnFragmentInteractionListener,
        FragmentInicial.OnFragmentInteractionListener, IComunicaFragments, detalle_alimento_general.OnFragmentInteractionListener
        {

    FragmentAlimentosPrincipal fragmentAlimentosPrincipal;
    FragmentInicial fragmentInicial;
    detalle_alimento_general detalle_alimento_general;
    menu_creacion pantallaCreacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //CARGA EL FRAGMENT AL INICIAR LA APLICACIÓN
        FragmentTransaction transaction = ((MainActivity) this).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, new FragmentInicial(), "fragment_preguntas");
        transaction.commit();

       // getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new FragmentInicial()).commit();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem añadir = menu.findItem(R.id.idMenuAñadir);
        MenuItem mod = menu.findItem(R.id.idMenuModificar);
        MenuItem eliminar = menu.findItem(R.id.idMenuEliminar);

        añadir.setVisible(false);
        mod.setVisible(false);
        eliminar.setVisible(false);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.idMenuAñadir) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        boolean fragmentSeleccionado = false;


        if (id == R.id.nav_camera) {
            fragment = new FragmentInicial();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_gallery) {
            fragment = new FragmentAlimentosPrincipal();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_slideshow) {
            fragment = new FragmentInicial();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        if(fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void enviarAlimento(alimentoVo alimentoVo) {
        detalle_alimento_general = new detalle_alimento_general();

        Bundle bundleEnvio = new Bundle();

        bundleEnvio.putSerializable("objeto", alimentoVo);

        detalle_alimento_general.setArguments(bundleEnvio);

        //cargar el fragment en el activity

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, detalle_alimento_general).addToBackStack(null)
                .commit();
    }

    @Override
    public void tipoAccion(String action) {
        //
        //
    }

    public void changeScene(Fragment fragment, int out){
        getSupportFragmentManager().beginTransaction()
                .replace(out, fragment).addToBackStack(null)
                .commit();
    }
}
