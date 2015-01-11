package com.bagdemir.training.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LambdasComparator {

    @Test
    public void lambdasComparator() {
        final int [] theNumbers = new int [] { 1, 100, 65, 77, 11, 13 };
        final List<Integer> numbersList = new ArrayList<>(theNumbers.length);
        Arrays.stream(theNumbers).forEach((int number) -> numbersList.add(number));
        System.out.println(numbersList);
       
        numbersList.sort((x1, x2) -> x1 - x2); 
        System.out.println(numbersList);
    }
}
