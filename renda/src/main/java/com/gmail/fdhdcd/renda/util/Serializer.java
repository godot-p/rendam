package com.gmail.fdhdcd.renda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.gmail.fdhdcd.renda.addresult.Result;

public class Serializer {

    private Serializer() {
    }

    @SuppressWarnings("unchecked")
    public static List<Result> deserialize(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Result> results = new ArrayList<>();
            Object readed = ois.readObject();
            if (readed instanceof Result) {
                results.add((Result)readed);
            }
            if (readed instanceof List<?>) {
                List<?> list = (List<?>)readed;
                for (Object o : list) {
                    if (!(o instanceof Result)) {
                        throw new IllegalArgumentException();
                    }
                }
                results.addAll((List<Result>)list);
            }
            return results;
        }
    }

    public static void serialize(File file, List<Result> results) throws FileNotFoundException, IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(results);
        }
    }

}
