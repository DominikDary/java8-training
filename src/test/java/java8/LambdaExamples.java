package java8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class LambdaExamples {
  public static final String DEV = "Dev";


  /**
   * Example 1: How to use lambdas in comparators.
   */
  @Test
  public void comparingWithLambdas() {
    // let's create an array of numbers
    final List<Integer> integers = Arrays.<Integer>asList(1, 100, 65, 77, 11, 13);

    Collections.sort(integers, new Comparator<Integer>() {
      @Override
      public int compare(final Integer x1, final Integer x2) {
        return x1 - x2;
      }
    });

    // Sort the list in increasing order.
    Collections.sort(integers, (x1, x2) -> x1 - x2);
    System.out.println(integers);

    // Sort the list in decreasing order.
    Collections.sort(integers, (x1, x2) -> x2 - x1);
    System.out.println(integers);
  }


  /**
   * Example 2 :  Reading files with lambdas.
   */
  @Test
  public void readFile() {

    // Create an input stream for the resource in class path.
    InputStream is = getClass().getResourceAsStream("availability.txt");

    // Read the file in Java 7 style.
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      br.lines().forEach(System.out::println);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


  /*
   * Example 3: Using regular expressions.
   */
  @Test
  public void splitStreams() {
    final String input = "1;2;3;4;5";
    Pattern pattern = Pattern.compile(";");
    pattern.splitAsStream(input).forEach(System.out::println);
  }


  /**
   * Example 4: Defining your own functional interface.
   */
  @FunctionalInterface
  public interface CustomFuncInterface {
    Integer apply(String value, Function<String, Integer> onError);
  }


  @Test
  public void lambdaCustomFunctionalInterfaces() {

    // define a transformer function with an error handler.
    CustomFuncInterface myTransformer = (String str, Function<String, Integer> errorHandler) -> {
      try {
        // if it does not go well, throws an exception.
        return Integer.parseInt(str);
      }
      catch (NumberFormatException e) {
        // error handling using the function passed as parameter.
        return errorHandler.apply(str);
      }
    };

    System.out.println(myTransformer.apply("42", (String) -> 0));
    System.out.println(myTransformer.apply("4a2", (String) -> 0));
  }


  /**
   * Example 5: An infinite integer generator.
   */
  @Test
  public void generateStreams() {

    // Create a random object using the current nanoTime as seed.
    Random random = new Random(System.nanoTime());

    // Create 10 random integer 1..6
    IntStream.generate(() -> (Math.abs(random.nextInt()) % 6) + 1).
            limit(10).
            forEach(System.out::println);
  }


  /**
   * Example 6: Intermediate operations are lazy.
   */
  @Test
  public void lazyStreams() {

    // let's create an array of numbers
    final List<Integer> integers = Arrays.<Integer>asList(1, 100, 65, 77, 11, 13, 14);

    // filter out the odd numbers
    integers.stream().filter(x -> x % 2 == 0).map(x -> {
      System.out.println(x);
      return Integer.toString(x);
    }); // expected nothing to see.

    System.out.println("> First stream really consumed?");

    // if we add an terminal operation
    long count = integers.stream().filter((x) -> x % 2 == 0).map(x -> {
      System.out.println(x);
      return Integer.toString(x);
    }).count();

    System.out.println("> Second stream consumed - total : " + count);
  }


  /**
   * Example 7: Finding average availability in the next sprint.
   */

  static class AvailabilityFinder {
    private static Pattern pattern = Pattern.compile("(\\d+)");


    public static int find(String line) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) return Integer.parseInt(matcher.group());
      else return 0;
    }
  }

  static class Average {
    private int total = 0;
    private int count = 0;


    public void accept(int i) {
      total += i;
      count++;
    }


    public double average() {
      return count > 0 ? ((double) total) / count : 0;
    }


    public void combine(Average other) {
      this.total += other.total;
      this.count += other.count;
    }
  }


  @Test
  public void findAvgAvailability() throws IOException {
    // Create an input stream for the resource in class path.
    InputStream is = getClass().getResourceAsStream("/availability.txt");

    // Read the file in Java 7 style.
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

      final Average avg = br.lines().
              mapToInt(AvailabilityFinder::find).
              collect(Average::new, Average::accept, Average::combine);
      // collect(<supplier>, <accumulator>, <combiner>);

      System.out.println(avg.average());
    }
    catch (Exception e) {
      System.out.println("Cannot read the file:" + e.getMessage());
    }
  }


  /**
   * Example 8: Finding average availability - easy way.
   */
  @Test
  public void findAvgAvailabilityEasyWay() throws IOException {
    // Create an input stream for the resource in class path.
    InputStream is = getClass().getResourceAsStream("/availability.txt");

    // Read the file in Java 7 style.
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      final OptionalDouble average = br.lines().
              mapToInt(AvailabilityFinder::find).average(); // average is terminal op.

      System.out.println(average.orElse(0.0d));
    }
    catch (Exception e) {
      System.out.println("Cannot read the file:" + e.getMessage());
    }
  }


  /**
   * Example 9: Collectors.
   */
  @Test
  public void collectors() throws IOException {
    // Create an input stream for the resource in class path.
    InputStream is = getClass().getResourceAsStream("/availability.txt");

    // Read the file in Java 7 style.
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

      // find out the name of the devs as sorted list.
      final List<String> dev = br.lines().filter((x) -> x.contains(DEV)).
              map(Extractor::findName).
              sorted().
              collect(Collectors.toList());

      System.out.println(dev);


      // get only developers sorted by their names as a comma separated string, they are more than 5 days available.
      final String devNames = br.lines().filter((x) -> AvailabilityFinder.find(x) > 5).
              map(Extractor::findName).
              sorted().
              collect(Collectors.joining(", "));

      System.out.println(devNames);
    }
    catch (Exception e) {
      System.out.println("Cannot read the file:" + e.getMessage());
    }
  }


  static class Extractor {

    public static String findName(String line) {
      final String[] split = line.split(",");
      return split[0].trim();
    }
  }
}
