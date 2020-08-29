package rchat.info.yourday_new.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.RecipesAdapter;
import rchat.info.yourday_new.containers.recipe.NewRecipes;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class RecipeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ImageView imageView = findViewById(R.id.bg);
        imageView.setImageResource(R.drawable.recipes_bg);
        try {
            NewRecipes newRecipes = new NewRecipes(SaveSharedPreferences.getNewRecipe(this));
            ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.recipes_bg);
            ListView listView = findViewById(R.id.main_list);
            RecipesAdapter recipesAdapter = new RecipesAdapter(this, newRecipes);
            listView.setAdapter(recipesAdapter);
            listView.setDivider(null);
        } catch (NullPointerException e) {
            new LoadRecipes().execute();
        } catch (JSONException e) {
            new LoadRecipes().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipes_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                new LoadRecipes().execute(findViewById(R.id.bg), findViewById(R.id.main_list));
                return true;
            case R.id.favourites:
                Intent intent = new Intent(this, FavouriteRecipesActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    class LoadRecipes extends AsyncTask<View, Object, Void> {
        @Override
        protected void onProgressUpdate(Object... values) {
            LoadStates state = (LoadStates) values[0];
            ProgressBar progressBar = findViewById(R.id.load);
            ListView listView = findViewById(R.id.main_list);
            switch (state) {
                case STARTED:
                    progressBar.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    listView.setAdapter(null);
                    break;
                case FINISHED:
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    RecipesAdapter adapter = (RecipesAdapter) values[1];
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(RecipeActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    try {
                        NewRecipes newRecipes = new NewRecipes(SaveSharedPreferences.getNewRecipe(RecipeActivity.this));
                        RecipesAdapter recipesAdapter = new RecipesAdapter(RecipeActivity.this, newRecipes);
                        listView.setAdapter(recipesAdapter);
                        listView.setDivider(null);
                    } catch (Exception ignore) {

                    }
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
            NewRecipes d = null;
            Connection connection = Connection.getConnection();
            List<Object> props = connection.props;
            synchronized (props) {
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof NewRecipes) {
                        props.remove(i);
                        break;
                    }
                }
            }
            boolean foundd = false;
            try {
                JSONObject request = new JSONObject();
                request.put("type", "newRecipe");
                connection.send(request);
                boolean found = false;
                Long startTime = new Date().getTime();
                Long diffTime = new Date().getTime();
                while (diffTime - startTime <= 10000 && !found) {
                    try {
                        for (Object a : connection.props) {
                            if (a instanceof NewRecipes) {
                                d = (NewRecipes) a;
                                found = true;
                            }
                        }
                    } catch (ConcurrentModificationException e) {

                    }
                    diffTime = new Date().getTime();
                }
            } catch (JSONException e) {

            }
            if (d == null) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                try {
                    SaveSharedPreferences.setNewRecipe(RecipeActivity.this, d.createJSON().toString());
                } catch (JSONException e) {

                }
                RecipesAdapter adapter = new RecipesAdapter(RecipeActivity.this, d);
                publishProgress(LoadStates.FINISHED, adapter);
                return null;
            }
            return null;
        }
    }
}
