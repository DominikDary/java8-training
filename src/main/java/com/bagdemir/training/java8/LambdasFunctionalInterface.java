package com.bagdemir.training.java8;

import java.util.function.Function;

import org.junit.Test;

public class LambdasFunctionalInterface {

    @FunctionalInterface
    public interface CustomFuncInterface {
        Integer apply(String value, Function<String, Integer> onError);
    }

    @Test
    public void lambdaCustom() {
        CustomFuncInterface myTransformer = (String str,
                Function<String, Integer> errorHandler) -> {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return errorHandler.apply(str);
            }
        };

        System.out.println(myTransformer.apply("42", (String) -> 0));
        System.out.println(myTransformer.apply("4a2", (String) -> 0));
    }
}
