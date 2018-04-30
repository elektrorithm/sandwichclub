package com.udacity.sandwichclub.utils;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_WORD;

public class JsonParser {
    private static final char TOKEN_QUOTE = '"';
    private static final char TOKEN_OBJECT_OPEN = '{';
    private static final char TOKEN_ARRAY_OPEN = '[';
    private static final char TOKEN_ASSIGN = ':';
    private static final char TOKEN_PAIR = ',';

    private static final char TOKEN_OBJECT_CLOSE = '}';
    private static final char TOKEN_ARRAY_CLOSE = ']';

    private HashMap<String, ArrayList<String>> _map;

    public static final String KEY_MAINNAME = "mainName";
    public static final String KEY_ALSOKNOWNAS = "alsoKnownAs";
    public static final String KEY_PLACEOFORIGIN = "placeOfOrigin";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_INGREDIENTS = "ingredients";

    public JsonParser() {
        _map = new HashMap<String, ArrayList<String>>();
    }

    public void parse(String json) throws IOException {
        if(json == null || json.isEmpty()) return;

        StringReader reader = new StringReader(json);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.eolIsSignificant(false);
        tokenizer.quoteChar(TOKEN_QUOTE);

        __parsing(tokenizer);
    }

    private void __parsing(StreamTokenizer tokens) throws IOException {
        boolean inArray = false;
        boolean inAssign = false;

        String keyBuffer = "";
        ArrayList<String> valueBuffer = new ArrayList<String>();

        int token = tokens.nextToken();
        while(token != TT_EOF) {
            // quote - ignore
            // word - stash
            // assign - inAssign = true
            // pair - !inArray => inAssign = false
            // object - recursively call __parsing
            // array - inArray = true

            switch(token) {
                case TT_WORD:
                case TOKEN_QUOTE:
                    if(inAssign) valueBuffer.add(tokens.sval);
                    else keyBuffer = tokens.sval;
                    break;
                case TOKEN_ASSIGN:
                    inAssign = true;
                    break;
                case TOKEN_OBJECT_CLOSE:
                case TOKEN_PAIR:
                    if(!inArray) {
                        inAssign = false;
                        _map.put(keyBuffer, valueBuffer);
                        valueBuffer = new ArrayList<String>();
                    }
                    break;
                case TOKEN_OBJECT_OPEN:
                    __parsing(tokens);
                    valueBuffer.add("~object");
                    break;
                case TOKEN_ARRAY_OPEN:
                    inArray = true;
                    break;
                case TOKEN_ARRAY_CLOSE:
                    inArray = false;
                    break;
            }

            token = tokens.nextToken();
            if (token == TOKEN_OBJECT_CLOSE) break;
        }

        // that last little lagging bit
        _map.put(keyBuffer, valueBuffer);
    }

    public String getFirst(String key) {
        if(_map.isEmpty() || !_map.containsKey(key)) return "";

        ArrayList<String> list = _map.get(key);
        if(list.isEmpty()) return "";
        return list.get(0);
    }

    public List<String> getList(String key) {
        if(_map.isEmpty() || !_map.containsKey(key)) return null;

        ArrayList<String> list = _map.get(key);
        if(list.isEmpty()) return null;
        return list;
    }
}
