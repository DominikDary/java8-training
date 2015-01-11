package com.bagdemir.training.java8;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class LambdasFileIO {

    @Test
    public void readFile() {
        InputStream is = getClass().getResourceAsStream("team-availability.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines().forEach((String line) -> System.out.println(line));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
