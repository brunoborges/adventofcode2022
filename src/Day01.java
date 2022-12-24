import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day01 {

    public static void main(String[] args) {
        var elves = readInputArrayFromFile();

        // Part1
        var foundTop = elves.stream().sorted(Comparator.comparing(Elf::totalCalories).reversed()).findFirst();
        System.out.println("Elf with most calories: " + foundTop.get().totalCalories());

        // Part 2
        var foundTop3 = elves.stream().sorted(Comparator.comparing(Elf::totalCalories).reversed()).limit(3)
                .map(e -> e.totalCalories()).mapToLong(Long::longValue).sum();
        System.out.println("Top 3 elves' total calories: " + foundTop3);
    }

    static List<Elf> readInputArrayFromFile() {
        try (var scanner = new Scanner(new File("bin/input_day01.txt"))) {
            var calories = new ArrayList<Long>();
            var elves = new ArrayList<Elf>();
            int index = 0;
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                if (line.isBlank()) {
                    elves.add(new Elf(index++, new ArrayList<>(calories)));
                    calories.clear();
                } else {
                    calories.add(Long.parseLong(line));
                }
            }
            return elves;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

record Elf(int position, List<Long> calories) {
    public long totalCalories() {
        return calories.stream().mapToLong(Long::longValue).sum();
    }
};
