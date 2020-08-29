package rchat.info.yourday_new.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.FavouriteRecipesAdapter;
import rchat.info.yourday_new.containers.recipe.NewRecipes;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class FavouriteRecipesActivity extends AppCompatActivity {

    FavouriteRecipesAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ImageView imageView = findViewById(R.id.bg);
        imageView.setImageResource(R.drawable.favourite_recipes_bg);
        try {
            NewRecipes newRecipes = SaveSharedPreferences.getAllFavoriteNewRecipes(this);
            adapter = new FavouriteRecipesAdapter(this, newRecipes);
            list = findViewById(R.id.main_list);
            list.setAdapter(adapter);
            list.setDivider(null);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(this, "У Вас нет любимых рецептов, Вы можете добавить их!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_recipes_menu, menu);

        MenuItem searchAction = menu.findItem(R.id.filter);
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    list.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
