package com.studio.plaster.tweetporter.converter;


import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studio.plaster.tweetporter.model.Post;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PostTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Post> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Post>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Post> someObjects) {
        return gson.toJson(someObjects);
    }
}
