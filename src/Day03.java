import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day03 {
    public static void main(String[] args) {
        var sacks = readInputArrayFromFile();
        var total = sacks.stream().map(GroupRucksack::badge).map(c -> priority(c)).mapToLong(Long::longValue).sum();
        System.out.println("Total: " + total);
        System.out.println("Total groups: " + sacks.size());
    }

    static long priority(char c) {
        int val = (int) c;
        if (c >= 'a' && c <= 'z') {
            return val - 97 + 1;
        } else if (c >= 'A' && c <= 'Z') {
            return val - 65 + 27;
        } else {
            throw new IllegalArgumentException("Invalid character: " + c);
        }
    }

    static List<GroupRucksack> readInputArrayFromFile() {
        try (var scanner = new Scanner(new File("bin/input_day03.txt"))) {
            var groups = new ArrayList<GroupRucksack>();
            int groupSize = 3;
            int grouping = 0;
            Rucksack[] group = new Rucksack[groupSize];
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                var array = line.toCharArray();
                group[grouping++] = new Rucksack(array);
                if (grouping == groupSize) {
                    grouping = 0;
                    groups.add(new GroupRucksack(Arrays.copyOf(group, groupSize)));
                }
            }
            return groups;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}

record GroupRucksack(Rucksack[] sacks) {
    public char badge() {
        int groupArrayLength = 0;
        for (int i = 0; i < sacks.length; i++) {
            char[] items = sacks[i].items();

            char[] uniqueItems = new char[items.length];

            for (int j = 0; j < items.length; j++) {
                char c = items[j];
                boolean found = false;
                for (int k = 0; k < uniqueItems.length; k++) {
                    if (uniqueItems[k] == c) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    uniqueItems[j] = c;
                }
            }

            Arrays.sort(uniqueItems);

            // find index of first non-empty char
            int firstIndex = 0;
            for (int j = 0; j < uniqueItems.length; j++) {
                if (uniqueItems[j] > 0) {
                    firstIndex = j;
                    break;
                }
            }

            char[] buffer = new char[uniqueItems.length - firstIndex];
            System.arraycopy(uniqueItems, firstIndex, buffer, 0, buffer.length);
            uniqueItems = buffer;

            // System.out.println(Arrays.toString(items));
            System.out.println(Arrays.toString(uniqueItems));

            Arrays.fill(items, (char) 0);
            System.arraycopy(uniqueItems, 0, items, 0, uniqueItems.length);
            groupArrayLength += uniqueItems.length;
        }

        // now all arrays have only unique elements. Let's group all elements in a
        // single array, and find which one appears groupSize or more
        char[] groupArray = new char[groupArrayLength];
        int pos = 0;
        for (int i = 0; i < sacks.length; i++) {
            char[] items = sacks[i].items();

            // find index of first empty value
            int firstIndex = 0;
            for (int j = 0; j < items.length; j++) {
                if (items[j] == 0) {
                    firstIndex = j;
                    break;
                }
            }

            System.arraycopy(items, 0, groupArray, pos, firstIndex);
            pos += firstIndex;
        }

        // now groupArray has all elements from all sacks
        // find which element appears more than 3 times
        for (int i = 0; i < groupArray.length; i++) {
            char c = groupArray[i];
            int count = 0;

            for (int j = 0; j < groupArray.length; j++) {
                if (groupArray[j] == c) {
                    count++;
                }
            }

            if (count >= sacks.length) {
                System.out.println("Found badge: " + c);
                return c;
            }
        }

        return ' ';
    }

}

record Rucksack(char[] items) {
    public long priorityOfElementFound() {
        int mid = items.length / 2;
        var found = ' ';
        for (int i = 0; i < mid; i++) {
            for (int j = mid; j < items.length; j++) {
                if (items[i] == items[j]) {
                    found = items[i];
                    break;
                }
            }
        }

        return Day03.priority(found);
    }
}