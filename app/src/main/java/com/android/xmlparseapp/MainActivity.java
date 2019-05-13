package com.android.xmlparseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XmlPullParserFactory parserFactory;

        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getResources().openRawResource(R.raw.recipes);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            processParsing(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        ArrayList<Recipe> recipes = new ArrayList<>();
        int eventType = parser.getEventType();
        Recipe currentRecipe = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("recipe".equals(eltName)) {
                        currentRecipe = new Recipe();
                        currentRecipe.id = Integer.parseInt(parser.getAttributeValue(0));
                        recipes.add(currentRecipe);
                    } else if (currentRecipe != null) {
                        if ("name".equals(eltName)) {
                            currentRecipe.name = parser.nextText();
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

        for (int index = 0; index < recipes.size(); index++) {
            Log.i(MainActivity.class.getSimpleName(),recipes.get(index).id + " : " +recipes.get(index).name);
        }
    }
}
