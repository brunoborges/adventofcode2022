import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day09 {

	Subpoint start = new Subpoint(0, 0);
	Subpoint head = new Subpoint(0, 0);
	Subpoint tail = new Subpoint(head.x, head.y);

	String readInputFromFile() {
		var filePath = Path.of("bin/input_day09.txt");
		try {
			return Files.readString(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		new Day09().run();
	}

	public void run() {
		// var input = "R 4\nU 4\nL 3\nD 1\nR 4\nD 1\nL 5\nR 2";
		var input = readInputFromFile();

		printPositions();

		var cmds = input.split("\n");
		for (var cmd : cmds) {
			var dir = cmd.charAt(0);
			var dist = Integer.parseInt(cmd.substring(2));
			System.out.println("# Command: " + dir + " " + dist);

			switch (dir) {
			case 'R': // right
			case 'L': // left
				moveLeftRight(dir, dist);
				break;
			case 'U': // up
			case 'D': // down
				moveUpDown(dir, dist);
				break;
			}
		}

		System.out.println("# of pos visited by tail: " + tail.history.stream().distinct().count());
	}

	void printPositions() {
		System.out.println("Head: " + head);
		System.out.println("Tail: " + tail);
		System.out.println("_");
	}

	void moveUpDown(char dir, int dist) {
		int step = dir == 'D' ? 1 : -1; // 'U' -> -1, 'D' -> 1
		for (int i = 0; i < dist; i++) {
			head.move(head.x, head.y + step);

			var isFurtherApart = Math.abs(head.y - tail.y) > 1;

			if (isFurtherApart) {
				// Move tail to follow head
				if (tail.x != head.x) {
					// head and tail are not on the same column, must adjust diagnoally
					if (tail.x < head.x) { // tail is left of head and further apart
						tail.move(tail.x + 1, tail.y - 1);
					} else if (tail.x > head.x) { // tail is right of head and further apart
						tail.move(tail.x - 1, tail.y + 1);
					}
				} else {// same column, simple vertical move
					if (head.y != tail.y) { // check if head and tail are on diff lines
						tail.move(tail.x, tail.y + step);
					}
				}
			}

			printPositions();
		}

	}

	void moveLeftRight(char dir, int dist) {
		int step = dir == 'R' ? 1 : -1; // 'R' -> 1, 'L' -> -1
		for (int i = 0; i < dist; i++) {
			head.move(head.x + step, head.y);

			var isFurtherApart = Math.abs(head.x - tail.x) > 1;

			// Move tail to follow head
			if (isFurtherApart) {
				if (tail.y != head.y) {
					// head and tail are not on the same line, must adjust diagnoally first
					if (tail.y < head.y) { // tail is above head and further apart
						tail.move(tail.x + step, tail.y + 1);
					} else if (tail.y > head.y) { // tail is below head and further apart
						tail.move(tail.x + step, tail.y - 1);
					}
				} else { // same line, simple horizontal move
					if (head.x != tail.x) { // check if head and tail are on diff position
						tail.move(tail.x + step, tail.y);
					}
				}
			}

			printPositions();
		}
	}

	class Subpoint extends Point {
		List<Point> history = new ArrayList<Point>();

		public Subpoint(int x, int y) {
			super(x, y);
			history.add(getLocation());
		}

		@Override
		public void move(int x, int y) {
			super.move(x, y);
			history.add(getLocation());
		}

		public List<Point> getHistory() {
			return history;
		}

		@Override
		public String toString() {
			return String.format("(row=%d, col=%d)", y, x);
		}
	}

}
