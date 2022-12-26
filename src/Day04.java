import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day04 {
	public static void main(String[] args) {
		var pairs = readInputArrayFromFile();

		var totalOverlap = pairs.stream().filter(SectionPair::overlap).count();
		var partialOverlap = pairs.stream().filter(SectionPair::partialOverlap).count();

		System.out.println("Total pairs: " + pairs.size());
		System.out.println("Pairs total overlapping: " + totalOverlap);
		System.out.println("Pairs partial overlapping: " + partialOverlap);
	}

	static record SectionPair(Section first, Section second) {
		public boolean overlap() {
			return first.contains(second) || second.contains(first);
		}

		public boolean partialOverlap() {
			return first.overlaps(second) || second.overlaps(first);
		}
	}

	static record Section(int start, int end) {
		public boolean contains(int value) {
			return value >= start && value <= end;
		}

		public boolean contains(Section other) {
			return contains(other.start) && contains(other.end);
		}

		public boolean overlaps(Section other) {
			return contains(other.start) || contains(other.end);
		}
	}

	static SectionPair parseLine(String line) {
		var parts = line.split(",");
		var section1 = parts[0].split("-");
		var section2 = parts[1].split("-");
		var first = new Section(Integer.parseInt(section1[0]), Integer.parseInt(section1[1]));
		var second = new Section(Integer.parseInt(section2[0]), Integer.parseInt(section2[1]));
		return new SectionPair(first, second);
	}

	static List<SectionPair> readInputArrayFromFile() {
		try (var scanner = new Scanner(new File("bin/input_day04.txt"))) {
			var result = new ArrayList<SectionPair>();
			while (scanner.hasNext()) {
				var line = scanner.nextLine();
				result.add(parseLine(line));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	static List<SectionPair> readExample() {
		String input = """
				2-4,6-8
				2-3,4-5
				5-7,7-9
				2-8,3-7
				6-6,4-6
				2-6,4-8
		""";
		var result = new ArrayList<SectionPair>();
		for(var line : input.trim().split("\n")) {
			result.add(parseLine(line.trim()));
		}
		return result;
	}

}
