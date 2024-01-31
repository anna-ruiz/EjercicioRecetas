package com.example.ejerciciorecetas.Adapters;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejerciciorecetas.R;
import com.example.ejerciciorecetas.activities.DetailsActivity;
import com.example.ejerciciorecetas.constantes.CONSTANTES;
import com.example.ejerciciorecetas.modelos.MealsItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealVH> {

    private Context context;
    private int resources;
    private List<MealsItem> objects;

    public MealAdapter(Context context, int resources, List<MealsItem> objects) {
        this.context = context;
        this.resources = resources;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MealVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(resources,null);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new MealVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealVH holder, int position) {
        MealsItem meal = objects.get(position);

        holder.lbName.setText(meal.getStrMeal()); //asignamos nombre

        Picasso.get() //asignamos valor foto
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background) //Mientras carga
                .error(R.drawable.ic_launcher_foreground) //En caso de error
                .into(holder.imPhoto);

        //para poder acceder a detalles/Details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para cambiar de actividad
                Intent intent = new Intent(context, DetailsActivity.class);
                //para pasar el id
                Bundle bundle = new Bundle();
                bundle.putString(CONSTANTES.ID_RECETA, meal.getIdMeal());
                intent.putExtras(bundle);

                //lanzamos la nueva actividad
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MealVH extends RecyclerView.ViewHolder{
        ImageView imPhoto;
        TextView lbName;

        public MealVH(@NonNull View itemView) {
            super(itemView);

            imPhoto = itemView.findViewById(R.id.imPhotoRowViewHolder);
            lbName = itemView.findViewById(R.id.lbNameRowViewHolder);
        }
    }
}
