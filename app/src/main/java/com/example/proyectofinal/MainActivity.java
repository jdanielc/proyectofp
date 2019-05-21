package com.example.proyectofinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.proyectofinal.ObjetosFire.MySQLFirebase;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.general.alimentoVo;
import com.example.proyectofinal.general.detalle_alimento_general;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentAlimentosPrincipal.OnFragmentInteractionListener,
        FragmentInicial.OnFragmentInteractionListener, IComunicaFragments, detalle_alimento_general.OnFragmentInteractionListener,
        Opciones.OnFragmentInteractionListener, menu_creacion.OnFragmentInteractionListener, FragmentMenu.OnFragmentInteractionListener{


    private static final int MY_REQUEST_CODE = 7117;

    private static String usuario;
    List<AuthUI.IdpConfig> providers;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);








        //AUTENTIFICACIÓN
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),//Email Builder
                new AuthUI.IdpConfig.PhoneBuilder().build(),//Phone Builder
                new AuthUI.IdpConfig.GoogleBuilder().build()//Email Builder

        );


        showSignInOption();

        //CARGA EL FRAGMENT AL INICIAR LA APLICACIÓN
        /*
        FragmentTransaction transaction = ((MainActivity) this).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, new FragmentInicial(),"fragment_preguntas");
        transaction.commit();*/

        FragmentTransaction transaction = ((MainActivity) this).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, new FragmentMenu(),"fragment_preguntas");
        transaction.commit();

    }

    public void showSignInOption() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                usuario = user.getUid();
                //Show email


                if (response.isNewUser()) {
                    MySQLFirebase.Insertar insertar = new MySQLFirebase.Insertar();
                    insertar.execute(user.getUid(),
                            user.getDisplayName(),
                            user.getEmail()
                    );


                }
            } else {
                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {




        return super.onOptionsItemSelected(item);
    }

    ///SELECCIONA EL FRAGMENT EN EL MENU LATERAL
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Bundle bundle = new Bundle();


        int id = item.getItemId();
        Fragment fragment = null;
        boolean fragmentSeleccionado = false;
        //ASIGNO LOS FRAGMENT SELECCIONADOS Y LE PASO AL FRAGMENT A TRAVES DE UN BUNDLE QUE HA DE MOSTRAR
        //TRUE MOSTRARA TODOS LOS ALIMENTOS, FALSE SOLO MOSTRARA LOS DEL USUARIO CONECTADO

        if (id == R.id.nav_inicio) {
            fragment = new FragmentMenu();
            fragmentSeleccionado = true;

        }else if(id == R.id.nav_lista){
            fragment = new FragmentInicial();
            fragmentSeleccionado = true;
        }
        else if (id == R.id.nav_forum) {

            fragment = new FragmentAlimentosPrincipal();
            bundle.putString("usuario", usuario);
            bundle.putInt("tipo", 1);
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_mis) {

            fragment = new FragmentAlimentosPrincipal();
            bundle.putString("usuario", usuario);
            bundle.putInt("tipo", 2);
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_fav) {

            fragment = new FragmentAlimentosPrincipal();
            bundle.putString("usuario", usuario);
            bundle.putInt("tipo", 3);
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_share) {

            Toast.makeText(this, "No implementado", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            fragment = new Opciones();
            fragmentSeleccionado = true;
        }

        fragment.setArguments(bundle);

        //SI SE HA SELECCIONADO UN FRAGMENTO, ESTE SE CARGA
        if (fragmentSeleccionado) {
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
    public void enviarAlimento(alimentoVo alimentoVo, boolean feed) {
        detalle_alimento_general = new detalle_alimento_general();

        Bundle bundleEnvio = new Bundle();

        bundleEnvio.putSerializable("objeto", alimentoVo);

        bundleEnvio.putSerializable("usuario", usuario);


        //Este boleano nos indicara si se carga el recycler desde el feed o el de mis alimentos
        //Lo usaremos para saber si ocultar o no el menu
        if(feed){
            bundleEnvio.putBoolean("feed", true);
        }else{
            bundleEnvio.putBoolean("feed", false);

        }

        detalle_alimento_general.setArguments(bundleEnvio);

        //cargar el fragment en el activity

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, detalle_alimento_general).addToBackStack(null)
                .commit();
    }


}

