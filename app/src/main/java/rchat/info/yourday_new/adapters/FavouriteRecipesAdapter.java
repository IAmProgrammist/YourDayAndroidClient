package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.content.Intent;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.recipe.NewRecipe;
import rchat.info.yourday_new.containers.recipe.NewRecipes;
import rchat.info.yourday_new.containers.recipe.Step;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class FavouriteRecipesAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    List<Step> modelList;
    List<NewRecipe> news = new ArrayList<>();
    List<NewRecipe> newsArrayList;
    Map<Integer, Boolean> opened;


    public FavouriteRecipesAdapter(Context mContext, NewRecipes recipe) {
        inflater = LayoutInflater.from(mContext);
        this.ctx = mContext;
        news.addAll(recipe.getRecipes());
        newsArrayList = recipe.getRecipes();
        opened = new HashMap<>();
        for (int i = 0; i < news.size(); i++) {
            opened.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return news.size() + 4;

    }

    @Override
    public Object getItem(int position) {
        return news.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (position == 0) {
            DisplayMetrics display = ctx.getResources().getDisplayMetrics();
            int height = display.heightPixels;
            View v = View.inflate(ctx, R.layout.empty, null);
            v.setEnabled(false);
            v.setClickable(false);
            v.setPadding(0, 0, 0, ((int) height / 6));
            return v;
        } else if (position == 1) {
            View va = View.inflate(ctx, R.layout.favourite_recipes_heading, null);
            va.setEnabled(false);
            va.setClickable(false);
            return va;
        } else if (position == news.size() + 2) {
            View vs = View.inflate(ctx, R.layout.recipes_end, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            return vs;
        } else if (position == news.size() + 3) {
            View vs = View.inflate(ctx, R.layout.empty, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            vs.setPadding(0, 0, 0, 100);
            return vs;
        } else {
            view = View.inflate(this.ctx, R.layout.recipe_row, null);
            ViewGroup sceneRoot = (ViewGroup) view.findViewById(R.id.scene_root);
            final ViewGroup closed = (ViewGroup) View.inflate(this.ctx, R.layout.recipe_first_scene, null);
            final ViewGroup opened = (ViewGroup) View.inflate(this.ctx, R.layout.recipe_second_scene, null);
            final NewRecipe recipe = news.get(position - 2);
            final String shareText = "Название блюда: " + recipe.getName() + "\n" + "Описание блюда: " + recipe.getEverything() + "\n" + "Приятного аппетита!";
            closed.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    ctx.startActivity(sendIntent);
                }
            });
            opened.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    ctx.startActivity(sendIntent);
                }
            });
            ((TextView) closed.findViewById(R.id.recipe_name)).setText(recipe.getName());
            Picasso.get().load(recipe.getUrl()).into((ImageView) closed.findViewById(R.id.recipe_img));
            ((TextView) closed.findViewById(R.id.recipe_name)).setText(recipe.getName());
            if (SaveSharedPreferences.isFavorite(ctx, recipe)) {
                ((ImageButton) opened.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_on);
                ((ImageButton) closed.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                ((ImageButton) opened.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_off);
                ((ImageButton) closed.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_off);
            }
            ((ImageButton) closed.findViewById(R.id.favourite)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SaveSharedPreferences.isFavorite(ctx, recipe)) {
                        SaveSharedPreferences.removeFavoriteRecipe(ctx, recipe);
                        ((ImageButton) opened.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_off);
                        ((ImageButton) closed.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_off);
                    } else {
                        SaveSharedPreferences.addFavoriteRecipe(ctx, recipe);
                        ((ImageButton) opened.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_on);
                        ((ImageButton) closed.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_on);
                    }
                }
            });
            Picasso.get().load(recipe.getUrl()).into((ImageView) opened.findViewById(R.id.recipe_img));
            ((TextView) opened.findViewById(R.id.recipe_name)).setText(recipe.getName());
            ((TextView) opened.findViewById(R.id.desc)).setText(recipe.getEverything());
            ((ImageButton) opened.findViewById(R.id.favourite)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SaveSharedPreferences.isFavorite(ctx, recipe)) {
                        SaveSharedPreferences.removeFavoriteRecipe(ctx, recipe);
                        ((ImageButton) opened.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_off);
                        ((ImageButton) closed.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_off);
                    } else {
                        SaveSharedPreferences.addFavoriteRecipe(ctx, recipe);
                        ((ImageButton) opened.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_on);
                        ((ImageButton) closed.findViewById(R.id.favourite)).setImageResource(android.R.drawable.btn_star_big_on);
                    }
                }
            });
            final Scene closedScene = new Scene(sceneRoot, closed);
            final Scene openedScene = new Scene(sceneRoot, opened);
            if (((Boolean) this.opened.get(Integer.valueOf(position - 2))).booleanValue()) {
                TransitionManager.go(openedScene, null);
            } else {
                TransitionManager.go(closedScene, null);
            }
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Transition fadeTransition = TransitionInflater.from(FavouriteRecipesAdapter.this.ctx).inflateTransition(R.transition.fade_transition);
                    if ((FavouriteRecipesAdapter.this.opened.get(Integer.valueOf(position - 2))).booleanValue()) {
                        TransitionManager.go(closedScene, fadeTransition);
                        FavouriteRecipesAdapter.this.opened.put(Integer.valueOf(position - 2), Boolean.valueOf(false));
                    } else {
                        TransitionManager.go(openedScene, fadeTransition);
                        FavouriteRecipesAdapter.this.opened.put(Integer.valueOf(position - 2), Boolean.valueOf(true));
                    }
                }
            });
            return view;
        }

    }

    public void filter(String s) {
        s = s.toLowerCase(Locale.getDefault());
        news.clear();
        if (s.length() == 0) {
            news.addAll(newsArrayList);
        } else {
            for (NewRecipe recipe : newsArrayList) {
                if (recipe.getName().toLowerCase().contains(s) || recipe.getEverything().toLowerCase().contains(s)) {
                    news.add(recipe);
                }
            }
        }
        opened = new HashMap<>();
        for (int i = 0; i < news.size(); i++) {
            opened.put(i, false);
        }
        notifyDataSetChanged();
    }
}
