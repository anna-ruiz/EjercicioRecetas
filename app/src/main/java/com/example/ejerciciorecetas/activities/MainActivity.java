package com.example.ejerciciorecetas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ejerciciorecetas.Adapters.CategoryAdapter;
import com.example.ejerciciorecetas.R;
import com.example.ejerciciorecetas.conexiones.ApiConexiones;
import com.example.ejerciciorecetas.conexiones.RetrofitObject;
import com.example.ejerciciorecetas.constantes.CONSTANTES;
import com.example.ejerciciorecetas.databinding.ActivityMainBinding;
import com.example.ejerciciorecetas.modelos.Categories;
import com.example.ejerciciorecetas.modelos.CategoriesItem;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<CategoriesItem> listCategories;
    private CategoryAdapter adapter;

    private RecyclerView.LayoutManager lm;

    private SharedPreferences sp; //para poder acceder a los valores guardados en el SP



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listCategories = new ArrayList<>();

        //inicializaamos el sp pasandole el key/nombre fichero y el modo
        sp = getSharedPreferences(CONSTANTES.ULTIMA_RECETA, MODE_PRIVATE);
        verUltimoAcceso();

        adapter = new CategoryAdapter(this, R.layout.row_view_holder, listCategories);
        lm = new LinearLayoutManager(this);

        binding.contentCategories.setAdapter(adapter);
        binding.contentCategories.setLayoutManager(lm);

        doGetCategories();


    }

    private void verUltimoAcceso() {
        //metodo para recoger la info guardada en las SharedPreferences
        String email = sp.getString(CONSTANTES.EMAIL, "");
        String receta = sp.getString(CONSTANTES.RECETA, "");

        Toast.makeText(this, "Email: "+email+" |  Receta: "+receta, Toast.LENGTH_SHORT).show();
    }

    private void doGetCategories() {
        Retrofit retrofit = RetrofitObject.getConexion();
        ApiConexiones api = retrofit.create(ApiConexiones.class);

        Call<Categories> getCategories = api.getCategories();

        getCategories.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK){
                    ArrayList<CategoriesItem> temp = (ArrayList<CategoriesItem>) response.body().getCategories();
                    listCategories.addAll(temp);
                    adapter.notifyItemRangeInserted(0, temp.size());
                }


            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
}