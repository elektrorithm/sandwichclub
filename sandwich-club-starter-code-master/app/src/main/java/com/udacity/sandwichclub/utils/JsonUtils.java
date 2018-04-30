package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import java.io.IOException;

public class JsonUtils {
    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();

        try {
            JsonParser parsed = new JsonParser();
            parsed.parse(json);

            sandwich.setMainName(parsed.getFirst(JsonParser.KEY_MAINNAME));
            sandwich.setAlsoKnownAs(parsed.getList(JsonParser.KEY_ALSOKNOWNAS));
            sandwich.setPlaceOfOrigin(parsed.getFirst(JsonParser.KEY_PLACEOFORIGIN));
            sandwich.setDescription(parsed.getFirst(JsonParser.KEY_DESCRIPTION));
            sandwich.setImage(parsed.getFirst(JsonParser.KEY_IMAGE));
            sandwich.setIngredients(parsed.getList(JsonParser.KEY_INGREDIENTS));
        } catch (IOException ioe) {
            sandwich = null;
        } catch (Exception ex) {
            sandwich = null;
        }

        return sandwich;
    }
}
