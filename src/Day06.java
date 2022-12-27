import java.nio.file.Files;
import java.nio.file.Path;

public class Day06 {
	public static void main(String[] args) {
		var input = readInputFromFile();
		char[] data = input.toCharArray();
		var bufferCounter = 0;
		char[] buffer = new char[4];
		for (int i = 0; i < data.length; i++) {
			buffer[bufferCounter] = data[i];
			bufferCounter++;
			if (bufferCounter == 4) {
				bufferCounter = 0;
				i = i - 3; // reset to the next char in original input data
				boolean foundDuplicate = false;
				for (int k = 0; k < buffer.length; k++) {
					for (int j = 0; j < buffer.length; j++) {
						if (buffer[k] == buffer[j] && k != j) {
							foundDuplicate = true;
							break;
						}
					}

					if (foundDuplicate) {
						break;
					}
				}

				if (foundDuplicate == false) {
					var toString = new String(buffer);
					var indexOf = input.indexOf(toString) + toString.length();
					System.out.println("found unique: " + toString + " at marker " + indexOf);
					break;
				}
			}
		}
	}

	static String readInputFromFile() {
		Path filePath = Path.of("bin/input_day06.txt");
		try {
			return Files.readString(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
