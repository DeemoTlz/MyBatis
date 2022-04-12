package com.deemo.test;

import org.junit.Test;

import java.util.Arrays;

public class SomeTest {

    @Test
    public void test() {
        String str = "abcdsadc123";

        char[] chars = str.toCharArray();

        Arrays.sort(chars);

        System.out.println(Arrays.toString(chars));

        System.out.println(String.valueOf(chars));
    }

    @Test
    public void test1() {
        String str = "abcdef123";
        String con = "cdef12";

        int index = str.indexOf(con);

        char[] chars = str.toCharArray();

        for (int i = index; i < index + con.length(); i++) {
            char aChar = chars[i];
        }

        System.out.println(String.valueOf(chars));
    }
}
