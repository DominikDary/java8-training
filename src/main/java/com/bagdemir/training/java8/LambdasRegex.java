package com.bagdemir.training.java8;

import java.util.regex.Pattern;

import org.junit.Test;

public class LambdasRegex {

    @Test
    public void splitStreams() {
        final String input = "1;2;3;4;5";
        Pattern pattern = Pattern.compile(";");
        pattern.splitAsStream(input).forEach((String number) -> System.out.println(number));
    }
}
