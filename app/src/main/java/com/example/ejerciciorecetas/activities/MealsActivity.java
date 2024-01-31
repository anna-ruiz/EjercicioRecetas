package com.example.ejerciciorecetas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ejerciciorecetas.Adapters.MealAdapter;
import com.example.ejerciciorecetas.R;
import com.example.ejerciciorecetas.conexiones.ApiConexiones;
import com.example.ejerciciorecetas.conexiones.RetrofitObject;
import com.example.ejerciciorecetas.constantes.CONSTANTES;
import com.example.ejerciciorecetas.databinding.ActivityMealsBinding;
import com.example.ejerciciorecetas.modelos.Meals;
import com.example.ejerciciorecetas.modelos.MealsItem;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealsActivity extends AppCompatActivity {

    private ActivityMealsBinding binding; //IMPORTANTE EL BINDING DE LA PROPIA ACTIVIDAD
    private MealAdapter adapter;
    private ArrayList<MealsItem> listMeals;
    private RecyclerView.LayoutManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PARA INICIALIZAR EL BINDING
        binding = ActivityMealsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //incializamos lista
        listMeals = new ArrayList<>();

        //Cargamos la info en el recycler view
        adapter = new MealAdapter(this, R.layout.row_view_holder, listMeals);
        lm = new LinearLayoutManager(this);

        binding.contentMeals.setAdapter(adapter);
        binding.contentMeals.setLayoutManager(lm);

        //Recogemos la info
        Intent intent = getIntent();
        String category = intent.getExtras().getString(CONSTANTES.NOMBRE_CATEGORIA);

        //usamos la categoria para sacar la lista de comidas de esa categoria
        if (category != null){
            doGetMeals(category);
        }

    }

    private void doGetMeals(String category) {
                                //cogemos la conexion y le decimos q vamos a instanciar
        ApiConexiones api = RetrofitObject.getConexion().create(ApiConexiones.class);

        //preparamos la llamada a la api con el tipo d respuesta q va a recibir
        Call<Meals> getMeals = api.getMeals(category);

        //ejecutamos la llamada
        getMeals.enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {
                //si va bien
                if (response.code() == HttpsURLConnection.HTTP_OK){ //Si el codigo de respuesta es ok (200)
                    //recogemos la lista q va en el body (pero es un  objeto) por eso el .getMeals
                    listMeals.addAll(response.body().getMeals());
                    adapter.notifyItemRangeInserted(0, listMeals.size()); //avisamos al adapter q hemos insertado datos
                }

            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
                //En caso de fallo, mandamos toast
                Toast.makeText(MealsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}