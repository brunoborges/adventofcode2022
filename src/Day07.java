import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Day07 {

	static Directory filesystem = new Directory("", new ArrayList<File>(), new ArrayList<Directory>(), null);
	static Directory currentDirectory = filesystem;
	static List<String> directories = new ArrayList<>();
	static int totalDiskSpace = 70_000_000;
	static int neededSpace = 30_000_000;

	public static void main(String[] args) {
		var input = readInputFromFile();
		var lines = input.split("\n");
		for (int i = 0; i < lines.length;) {
			var line = lines[i];
			if (isCommand(line)) {
				i = parseLineAsCommandUntilNextCommand(lines, i);
			}
		}

		// Part 1
		var sizeOfDirsAtMost100000 = filesystem.streamSubdirs().filter(d -> d.size() <= 100000)
				.mapToLong(Directory::size).sum();
		System.out.println("size Of Dirs At Most100000: " + sizeOfDirsAtMost100000);

		// part 2
		var filesystemUsedSpace = filesystem.size();
		System.out.println("Filesystem Used Space: " + filesystemUsedSpace);

		var filesystemFreeSpace = totalDiskSpace - filesystemUsedSpace;
		System.out.println("Filesystem Free Space: " + filesystemFreeSpace);

		var neededToFree = neededSpace - filesystemFreeSpace;
		System.out.println("Needed to free: " + neededToFree);

		var sizeToDelete = filesystem.streamSubdirs().filter(d -> d.size() >= neededToFree)
				.sorted(Comparator.comparing(Directory::size)).findFirst().map(Directory::size).get();
		System.out.println("sizeToDelete: " + sizeToDelete);
	}

	static int parseLineAsCommandUntilNextCommand(String[] lines, int i) {
		var command = lines[i];
		// System.out.println("command: " + command);
		// find next command
		int nextCommandIndex = -1;
		for (int j = i + 1; j < lines.length; j++) {
			if (isCommand(lines[j])) {
				nextCommandIndex = j;
				break;
			} else if (j == lines.length - 1) {
				nextCommandIndex = lines.length;
			}
		}

		var commandOutput = new ArrayList<String>();
		if (nextCommandIndex >= i + 1 && !lines[i + 1].startsWith("$ ")) {
			// current command has output
			for (int j = i + 1; j < nextCommandIndex; j++) {
				commandOutput.add(lines[j]);
			}
			// System.out.println("output: " + commandOutput);
		}

		parseCommand(command, commandOutput);

		return nextCommandIndex;
	}

	static void parseCommand(String command, ArrayList<String> commandOutput) {
		if (command.startsWith("$ cd ")) {
			// change directory
			var directoryName = command.substring(5);
			if (directoryName.equals("..")) {
				currentDirectory = currentDirectory.parent();
				// System.out.println("moved to parent: " + currentDirectory.name);
			} else {
				currentDirectory = currentDirectory.subdirectories.stream().filter(d -> d.name.equals(directoryName))
						.findFirst().orElseGet(() -> {
							var newdir = new Directory(directoryName, new ArrayList<File>(), new ArrayList<Directory>(),
									currentDirectory);
							currentDirectory.subdirectories.add(newdir);
							return newdir;
						});
			}

		} else if (command.startsWith("$ ls")) {
			for (var output : commandOutput) {
				if (output.startsWith("dir")) {
					var dirName = output.substring(4);
					currentDirectory.subdirectories.stream().filter(d -> d.name.equals(dirName)).findFirst()
							.orElseGet(() -> {
								var newdir = new Directory(dirName, new ArrayList<File>(), new ArrayList<Directory>(),
										currentDirectory);
								currentDirectory.subdirectories.add(newdir);
								directories.add(dirName);
								return newdir;
							});

				} else { // it is a file
					var fileEntry = output.split(" ");
					var fileName = fileEntry[1];
					var size = Long.parseLong(fileEntry[0]);
					currentDirectory.files.stream().filter(f -> f.name.equals(fileName)).findFirst().orElseGet(() -> {
						var newfile = new File(fileName, size, currentDirectory);
						currentDirectory.files.add(newfile);
						return newfile;
					});
				}
			}
		}

	}

	static boolean isCommand(String line) {
		return line.startsWith("$ ");
	}

	static record Directory(String name, List<File> files, List<Directory> subdirectories, Directory parent) {
		public long size() {
			return files.stream().mapToLong(File::size).sum()
					+ subdirectories.stream().mapToLong(Directory::size).sum();
		}

		@Override
		public int hashCode() {
			return name.hashCode() * (parent != null ? parent.hashCode() : 31) * 17;
		}

		public Stream<Directory> streamSubdirs() {
			return Stream.concat(Stream.of(this), subdirectories.stream().flatMap(Directory::streamSubdirs));
		}

	}

	static record File(String name, long size, Directory parent) {
	}

	static String readInputFromFile() {
		var filePath = Path.of("bin/input_day07.txt");
		try {
			return Files.readString(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}