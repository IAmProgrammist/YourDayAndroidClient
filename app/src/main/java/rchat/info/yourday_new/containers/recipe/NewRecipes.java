package rchat.info.yourday_new.containers.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewRecipes {
    List<NewRecipe> recipes = new ArrayList<>();

    public NewRecipes(JSONObject o) throws JSONException {
        JSONArray array = o.getJSONArray("recipes");
        for (int i = 0; i < array.length(); i++) {
            recipes.add(new NewRecipe(array.getJSONObject(i)));
        }
    }

    public NewRecipes() {

    }

    public void addRecipe(String name, String url, String everything, NewRecipeType type) {
        recipes.add(new NewRecipe(name, url, everything, type));
    }

    public void addRecipe(NewRecipe recipe) {
        recipes.add(recipe);
    }

    public JSONObject createJSON() throws JSONException {
        JSONArray array = new JSONArray();
        for (NewRecipe a : recipes) {
            array.put(a.createJSON());
        }
        JSONObject res = new JSONObject();
        res.put("recipes", array);
        return res;
    }

    public List<NewRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<NewRecipe> recipes) {
        this.recipes = recipes;
    }

    public String createShareText() {
        String recipe = "";
        for (NewRecipe a : recipes) {
            recipe += a.createShareText() + "\n";
        }
        recipe += "Приятного аппетита!";
        return recipe;
    }

    public NewRecipes getYouKnow(NewRecipeType type) {
        NewRecipes castr = new NewRecipes();
        for (NewRecipe a : recipes) {
            if (a.type == type) {
                castr.addRecipe(a.name, a.url, a.everything, type);
                break;
            }
        }
        return castr;
    }
}
