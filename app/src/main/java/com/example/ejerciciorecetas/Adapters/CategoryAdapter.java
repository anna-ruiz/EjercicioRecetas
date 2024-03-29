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
import com.example.ejerciciorecetas.activities.MealsActivity;
import com.example.ejerciciorecetas.constantes.CONSTANTES;
import com.example.ejerciciorecetas.modelos.CategoriesItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {
    private Context context;
    private int resource;
    private List<CategoriesItem> objects;

    public CategoryAdapter(Context context, int resource, List<CategoriesItem> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(resource, null);

        //PAra q el contenido se adapte al tamaño de la pantalla
        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        return new CategoryVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {

        CategoriesItem category = objects.get(position);

        holder.lbName.setText(category.getStrCategory());

        Picasso.get()
                .load(category.getStrCategoryThumb())
                .into(holder.imPhoto);

        //para X al hacer click en cualquier parte de la fila debemos usar el holder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para pasar de ventana    debemos decirle dnd esta (context)
                Intent intent = new Intent(context, MealsActivity.class);
                //para pasar info al cambiar de ventana
                Bundle bundle = new Bundle();
                bundle.putString(CONSTANTES.NOMBRE_CATEGORIA, category.getStrCategory());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder{
        ImageView imPhoto;
        TextView lbName;

        public CategoryVH(@NonNull View itemView) {
            super(itemView);
            imPhoto = itemView.findViewById(R.id.imPhotoRowViewHolder);
            lbName = itemView.findViewById(R.id.lbNameRowViewHolder);
        }
    }
}
