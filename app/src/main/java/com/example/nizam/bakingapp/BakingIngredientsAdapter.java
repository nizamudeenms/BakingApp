package com.example.nizam.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nizamudeenms on 17/02/18.
 */

public class BakingIngredientsAdapter extends RecyclerView.Adapter<BakingIngredientsAdapter.BakingIngredientsHolder> {

    private ArrayList<String> ingredientsArrayList;

    public BakingIngredientsAdapter(ArrayList<String> ingredientsArrayList) {
        this.ingredientsArrayList = ingredientsArrayList;
    }

    @Override
    public BakingIngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_ingredients,parent,false);
        return new BakingIngredientsHolder(view,ingredientsArrayList);
    }

    @Override
    public void onBindViewHolder(BakingIngredientsHolder holder, int position) {
        String ingredients = ingredientsArrayList.get(position);
        holder.ingredients.setText(ingredients);

    }

    @Override
    public int getItemCount() {
        return ingredientsArrayList.size();
    }

    public class BakingIngredientsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ingredients_text_view)
        TextView ingredients;
        private ArrayList<String> ingredientsArrayList ;

        public BakingIngredientsHolder(View itemView, ArrayList<String> ingredientsArrayList) {
            super(itemView);
            this.ingredientsArrayList = ingredientsArrayList;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
