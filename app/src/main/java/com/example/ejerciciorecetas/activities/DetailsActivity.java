package com.example.ejerciciorecetas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ejerciciorecetas.R;
import com.example.ejerciciorecetas.conexiones.ApiConexiones;
import com.example.ejerciciorecetas.conexiones.RetrofitObject;
import com.example.ejerciciorecetas.constantes.CONSTANTES;
import com.example.ejerciciorecetas.databinding.ActivityDetailsBinding;
import com.example.ejerciciorecetas.modelos.Meals;
import com.example.ejerciciorecetas.modelos.MealsItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private MealsItem meal;
    private FirebaseUser user;

    //necesarias para la bd y acceder a los nodos q contienen la info
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<MealsItem> listaRecipes; //para almacenar la lista
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaRecipes = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://ejerciciorecetas-1ee98-default-rtdb.europe-west1.firebasedatabase.app/");

        //inicializamos las shared preferences, el modo debe ser privado!!!! los otros estan deprecated
        sp = getSharedPreferences(CONSTANTES.ULTIMA_RECETA, MODE_PRIVATE);


        //cogemos la info del id del intent
        String id = getIntent().getExtras().getString(CONSTANTES.ID_RECETA);

        //comprobamos q hay algo en el id
        if (id != null){
            doGetRecipe(id);
        }

        //Funcion del boton favoritos
        binding.btnGuardarRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cogemos el ultimo user conectado
                user = FirebaseAuth.getInstance().getCurrentUser();

                //Comprobamos si el user esta loggeado
                if (user == null){ //Si no esta logeado, abrimos la actividad Login
                    //como si q estamos en una actividad y no queremos mandar info, podemos usar startActivity
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));

                }else {//Si si esta logeado
                    //Abrimos editor de sp para poder insertar la info de la receta en las SP
                    SharedPreferences.Editor editor = sp.edit();
                    //le passamos la key y la info
                    editor.putString(CONSTANTES.EMAIL, user.getEmail());
                    editor.putString(CONSTANTES.RECETA, meal.getStrMeal());
                    editor.apply(); //si no no se efectuan los cambios!!!

                    //Cuando uso la BD de RealTime necesito la variable de datbase (q es una instancia a la BD q nos conectamos)
                    //Como es una BD no relacional , va por nodos, debemos saber a que nodo de la BD debemos llegar (la referencia)
                            //le decimos BD, referencia (el UID del user) y le decimos q escribe dentro del hijo (en este caso recipes)
                    reference = database.getReference(user.getUid()).child("recipes"); //Creamos nodo con la raiz con el Uid del user


                    //cargamos la info ya existente en el nodo si hay y si no añadimos info y ya
                    //ponemos listener con el get xq queremos q solo lo haga una vez cada vez q hacemos click
                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            if (task.isSuccessful()){
                                //Para traer los datos de la lista
                                GenericTypeIndicator<ArrayList<MealsItem>> gti = new GenericTypeIndicator<ArrayList<MealsItem>>() {
                                };                                //debemos hacer esto xq es una lista


                                ArrayList<MealsItem> temp = task.getResult().getValue(gti);

                                if (temp != null){ //Si el temp no esta vacio añadimos los datos de la BD
                                    listaRecipes.addAll(temp); //El addAll machaca la info existente en la lista
                                }


                                //Escribir los datos en la BD
                                listaRecipes.add(meal); //añadimos la receta pulsada a la lista
                                reference.setValue(listaRecipes); //machacamos la lista vieja con la nueva q ya tiene la receta nueva
                                Toast.makeText(DetailsActivity.this, "Noticia añadida con éxito", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });

    }

    private void doGetRecipe(String id) {
        //cogemos la conexion y decimos q recibe una instancia
        ApiConexiones api = RetrofitObject.getConexion().create(ApiConexiones.class);

        //Recogemos el objeto del tipo Meals
        Call<Meals> getRecipe = api.getRecipe(id);

        getRecipe.enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {
                //Si va bien
                if (response.code() == HttpsURLConnection.HTTP_OK){
                    //La api devuelve un objeto con una lista de objetos asi q tenemos q acceder al primer y unico objeto (0)
                    meal = response.body().getMeals().get(0);
                    if (meal != null){
                        rellenarVista();
                    }
                }
            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
                //si hay error
                Toast.makeText(DetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void rellenarVista() {
        binding.lbNameRecipe.setText(meal.getStrMeal());
        binding.lbCategoryRecipe.setText(meal.getStrCategory());
        binding.lbAreaRecipe.setText(meal.getStrArea());
        binding.lbInstructionsRecipe.setText(meal.getStrInstructions());

        Picasso.get()
                .load(meal.getStrMealThumb())
                .into(binding.imPhotoRecipe);
    }


    //Para el Logout y el menu
    //debemos implementaar el onOptions y onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //inflamos el menu para verlo q debe recibir la vista y el menu objeto
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true; //xq debe devolver un bool
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.itemLogOut){
            FirebaseAuth.getInstance().signOut();
        }
        return true;
    }
}