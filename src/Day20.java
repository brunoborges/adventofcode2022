import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {

    static Element ZERO = null;
    static long INDEX_OF_ZERO = -1;
    static long key = 811589153;

    public static void main(String[] args) throws Exception {
        // Part 1
        var data = readInputArrayFromFile();
        var inputPart1 = new ArrayList<>(data);
        mix(inputPart1, new ArrayList<>(data));
        calculateCoordinates(inputPart1);

        // Part 2
        var inputPart2 = data.stream().map((e) -> new Element(e.number() * key, e.position()))
                .collect(Collectors.toList());
        var orig = new ArrayList<>(inputPart2);
        IntStream.range(0, 10).forEach(i -> mix(inputPart2, orig));
        calculateCoordinates(inputPart2);
    }

    static void calculateCoordinates(List<Element> input) {
        INDEX_OF_ZERO = input.indexOf(ZERO);
        long val1 = findCoordinate(input, 1000L + INDEX_OF_ZERO);
        long val2 = findCoordinate(input, 2000L + INDEX_OF_ZERO);
        long val3 = findCoordinate(input, 3000L + INDEX_OF_ZERO);
        long puzzle = val1 + val2 + val3;
        System.out.println("Total: " + puzzle);
    }

    static long findCoordinate(List<Element> input, long loops) {
        return input.get(Math.floorMod(loops, input.size())).number();
    }

    static void mix(List<Element> input, List<Element> orig) {
        for (var iterator = orig.iterator(); iterator.hasNext();) {
            var element = iterator.next();
            var currentPos = input.indexOf(element);
            input.remove(element);
            var nextPos = Math.floorMod(currentPos + element.number(), input.size());
            input.add(nextPos, element);
        }
    }

    static List<Element> readInputArrayFromFile() {
        try (var scanner = new Scanner(new File("bin/input_day20.txt"))) {
            var result = new ArrayList<Element>();
            int index = 0;
            while (scanner.hasNext()) {
                var number = scanner.nextInt();
                var element = new Element(number, index++);
                if (number == 0)
                    ZERO = element;
                result.add(element);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}

record Element(long number, int position) {
};