package com.example.ejerciciorecetas.conexiones;

import com.example.ejerciciorecetas.modelos.Categories;
import com.example.ejerciciorecetas.modelos.Meals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiConexiones {

    //Sacar todas las categorias
    @GET("/api/json/v1/1/categories.php")
    Call<Categories> getCategories();


    //Buscar recetas por una categoria
    @GET("/api/json/v1/1/filter.php")
    //DEbemos poner c en el parametro xq es asi en la url original /filter.php?c=Seafood
    Call<Meals> getMeals(@Query("c") String categoria);




}
