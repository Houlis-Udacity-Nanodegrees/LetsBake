package com.xaris.xoulis.letsbake.data.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xaris.xoulis.letsbake.data.model.Step;

import java.lang.reflect.Type;
import java.util.List;

public class StepListConverter {
    private static Type type = new TypeToken<List<Step>>() {
    }.getType();

    @TypeConverter
    public static List<Step> toList(String listInJson) {
        Gson gson = new Gson();
        return gson.fromJson(listInJson, type);
    }

    @TypeConverter
    public static String toString(List<Step> list) {
        Gson gson = new Gson();
        return gson.toJson(list, type);
    }
}
