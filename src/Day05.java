import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day05 {

	static List<CrateStack> crateStacks;
	static List<Operation> operations;

	public static void main(String[] args) {
		var blocksFromFile = readInputArrayFromFile();

		var crateStackConfig = blocksFromFile[0];
		parseCrates(crateStackConfig);	
		crateStacks.forEach(System.out::println);

		var rawOperations = blocksFromFile[1];
		parseOperations(rawOperations);
		operations.forEach(System.out::println);

		executeOperations();
		crateStacks.forEach(System.out::println);

		var message = new ArrayList<String>();
		crateStacks.stream().map(CrateStack::crates).map(c -> c.get(0).name).forEach(message::add);
		System.out.println(String.join(" ", message));
	}

	static List<Crate> bufferCrates = new ArrayList<>(20);

	static void executeOperations() {
		for (var operation : operations) {
			var quantity = operation.quantity;
			var from = operation.from;
			var to = operation.to;

			var fromStack = crateStacks.get(from - 1);
			var toStack = crateStacks.get(to - 1);

			bufferCrates.addAll(fromStack.crates.subList(0, quantity));
			fromStack.crates.removeAll(bufferCrates);
			// Collections.reverse(bufferCrates); // Part 2 asks to keep order, so.. commented this line
			toStack.crates.addAll(0, bufferCrates);
			bufferCrates.clear();

			crateStacks.forEach(System.out::println);
		}
	}

	static String[] parseLine(String line) {
		var data = new ArrayList<String>();
		char[][] splitted = splitArray(line.toCharArray(), 4);
		for (char[] chars : splitted) {
			data.add(new String(chars).trim());
		}
		return data.toArray(new String[data.size()]);
	}

	static char[][] splitArray(char[] arrayToSplit, int chunkSize) {
		// borrowed from StackOverflow:
		// https://stackoverflow.com/questions/27857011/how-to-split-a-string-array-into-small-chunk-arrays-in-java
		int rest = arrayToSplit.length % chunkSize;
		int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
		char[][] arrays = new char[chunks][];
		for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++) {
			arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
		}
		if (rest > 0) {
			arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize,
					(chunks - 1) * chunkSize + rest);
		}
		return arrays;
	}

	static void parseCrates(String input) {
		var lines = input.lines().toArray(String[]::new);

		crateStacks = new ArrayList<CrateStack>();
		for (int i = 0; i < lines.length; i++) {
			var line = lines[i].concat(" ");
			if (line.trim().startsWith("1"))
				break;

			var entries = parseLine(line);

			var index = 0;
			for (var entry : entries) {
				var crateStackId = index + 1;
				var crateStack = crateStacks.size() > index ? crateStacks.get(index) : null;

				if (crateStack == null) {
					crateStacks.add(new CrateStack(crateStackId, new ArrayList<Crate>()));
					crateStack = crateStacks.get(index);
				}

				if (entry.isEmpty() == false) {
					crateStack.crates.add(new Crate(crateStackId, entry));
				}

				index++;
			}
		}
	}

	static void parseOperations(String rawOperations) {
		var operations = new ArrayList<Operation>();
		var lines = rawOperations.trim().lines().toArray(String[]::new);
		var regex = Pattern.compile("\\d+");
		for (var line : lines) {
			var array = regex.matcher(line.trim()).results().mapToInt(m -> Integer.parseInt(m.group())).toArray();
			var quantity = array[0];
			var from = array[1];
			var to = array[2];
			operations.add(new Operation(quantity, from, to));
		}

		Day05.operations = operations;
	}

	static String[] readInputArrayFromFile() {
		try (var scanner = new Scanner(new File("bin/input_day05.txt"))) {
			scanner.useDelimiter("\n\n");
			var result = new String[2];
			var index = 0;
			while (scanner.hasNext()) {
				var block = scanner.next();
				result[index++] = block;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static record CrateStack(int id, List<Crate> crates) {
	}

	static record Crate(int originalStack, String name) {
	}

	static record Operation(int quantity, int from, int to) {
	}

}
