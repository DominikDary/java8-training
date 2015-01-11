package com.bagdemir.training.java8;

import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

public class StreamsGenerator {

    @Test
    public void generateStreams() {
        IntStream.generate(() -> Math.abs(new Random(System.nanoTime()).nextInt()) % 6 + 1).
        limit(10).
        forEach((number) -> System.out.println(number));
    }
}
