import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Day08 {
	public static void main(String[] args) {
		var input = readInputFromFile();

		var lines = input.split("\n");
		int[][] grid = new int[lines.length][];
		for (int i = 0; i < lines.length; i++) {
			var line = lines[i];
			int[] row = new int[line.length()];
			for (int j = 0; j < line.length(); j++) {
				row[j] = line.charAt(j) - '0';
			}
			grid[i] = row;
		}

		int visibleTrees = 0;

		// Part 1: Count visible trees
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (i == 0 || j == 0 || i == grid.length - 1 || j == grid[i].length - 1) {
					visibleTrees++;
					continue;
				}
				if (isVisibleFromNorth(grid, i, j) || isVisibleFromSouth(grid, i, j) || isVisibleFromWest(grid, i, j)
						|| isVisibleFromEast(grid, i, j)) {
					visibleTrees++;
				}
			}
		}
		System.out.println("Visible trees: " + visibleTrees);

		// Part 2: Find best spot
		int bestScore = 0;
		int[] bestSpot = new int[2];
		for (int row = 1; row < grid.length - 1; row++) {
			for (int col = 1; col < grid[row].length - 1; col++) {
				int northScore = getNorthScore(grid, row, col);
				int southScore = getSouthScore(grid, row, col);
				int westScore = getWestScore(grid, row, col);
				int eastScore = getEastScore(grid, row, col);
				int score = scenicScore(new int[] { northScore, southScore, westScore, eastScore });

				if (score > bestScore) {
					bestScore = score;
					bestSpot = new int[] { row, col };
				}
			}
		}
		System.out.println("Best scenic score: " + bestScore);
		System.out.println("Best spot: " + Arrays.toString(bestSpot));
	}

	static int getNorthScore(int[][] grid, int row, int col) {
		int score = 0;
		var currentTree = grid[row][col];
		for (int north = row - 1; north >= 0; north--) {
			var treeToCompare = grid[north][col];
			score++;
			if (treeToCompare >= currentTree) {
				break;
			}
		}
		return score;
	}

	static int getSouthScore(int[][] grid, int row, int col) {
		int score = 0;
		var currentTree = grid[row][col];
		for (int south = row + 1; south < grid.length; south++) {
			var treeToCompare = grid[south][col];
			score++;
			if (treeToCompare >= currentTree) {
				break;
			}
		}
		return score;
	}

	static int getWestScore(int[][] grid, int row, int col) {
		int score = 0;
		var currentTree = grid[row][col];
		for (int west = col - 1; west >= 0; west--) {
			var treeToCompare = grid[row][west];
			score++;
			if (treeToCompare >= currentTree) {
				break;
			}
		}
		return score;
	}

	static int getEastScore(int[][] grid, int row, int col) {
		int score = 0;
		var currentTree = grid[row][col];
		for (int east = col + 1; east < grid[row].length; east++) {
			var treeToCompare = grid[row][east];
			score++;
			if (treeToCompare >= currentTree) {
				break;
			}
		}
		return score;
	}

	static int scenicScore(int[] trees) {
		int score = 1;
		for (int t : trees) {
			score *= t;
		}
		return score;
	}

	static boolean isVisibleFromNorth(int[][] grid, int i, int j) {
		var currentTree = grid[i][j];
		for (int k = i - 1; k >= 0; k--) {
			if (grid[k][j] >= currentTree) {
				return false;
			}
		}
		return true;
	}

	static boolean isVisibleFromSouth(int[][] grid, int i, int j) {
		var currentTree = grid[i][j];
		for (int k = i + 1; k < grid.length; k++) {
			if (grid[k][j] >= currentTree) {
				return false;
			}
		}
		return true;
	}

	static boolean isVisibleFromWest(int[][] grid, int i, int j) {
		var currentTree = grid[i][j];
		for (int k = j - 1; k >= 0; k--) {
			if (grid[i][k] >= currentTree) {
				return false;
			}
		}
		return true;
	}

	static boolean isVisibleFromEast(int[][] grid, int i, int j) {
		var currentTree = grid[i][j];
		for (int k = j + 1; k < grid[i].length; k++) {
			if (grid[i][k] >= currentTree) {
				return false;
			}
		}
		return true;
	}

	static String readInputFromFile() {
		var filePath = Path.of("bin/input_day08.txt");
		try {
			return Files.readString(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
