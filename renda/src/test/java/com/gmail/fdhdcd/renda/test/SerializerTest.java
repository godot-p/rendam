package com.gmail.fdhdcd.renda.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.gmail.fdhdcd.renda.addresult.Result;
import com.gmail.fdhdcd.renda.util.Serializer;

@SuppressWarnings("static-method")
public class SerializerTest {

    @Test
    public void testSerialize() throws FileNotFoundException, IOException, ClassNotFoundException {
        List<Result> rList = new ArrayList<>();
        Result re = new Result("namae", 13, 2);
        rList.add(re);
        File f = new File("F:\\test.ser");
        Serializer.serialize(f, rList);
        List<Result> deserialized = Serializer.deserialize(f);
        if (!rList.equals(deserialized)) {
            Assertions.fail();
        }
    }

}
