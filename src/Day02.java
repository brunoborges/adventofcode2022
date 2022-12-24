import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day02 {

    public static void main(String[] args) {
        var matches = readInputArrayFromFile();
        var totalPoints = matches.stream().map(Match::points).mapToLong(Long::longValue).sum();
        System.out.println(totalPoints);
    }

    static List<Match> readInputArrayFromFile() {
        try (var scanner = new Scanner(new File("bin/input_day02.txt"))) {
            var matches = new ArrayList<Match>();
            while (scanner.hasNext()) {
                var line = scanner.nextLine();
                var oppo = Play.fromChar(line.charAt(0));
                var mypl = Play.fromChar(line.charAt(2));
                matches.add(new Match(oppo, mypl));
            }
            return matches;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

record Match(Play opponent, Play myplay) {
    public long points() {
        return myplay.points() + (myplay.beats(opponent) ? 6 : myplay == opponent ? 3 : 0);
    }
};

enum Play {
    ROCK(1), PAPER(2), SCISSORS(3);

    private final int points;

    Play(int points) {
        this.points = points;
    }

    public int points() {
        return points;
    }

    public static Play fromChar(char c) {
        switch (c) {
            case 'A':
            case 'X':
                return ROCK;
            case 'B':
            case 'Y':
                return PAPER;
            case 'C':
            case 'Z':
                return SCISSORS;
            default:
                throw new IllegalArgumentException("Invalid character: " + c);
        }
    }

    public boolean beats(Play other) {
        switch (this) {
            case ROCK:
                return other == SCISSORS;
            case PAPER:
                return other == ROCK;
            case SCISSORS:
                return other == PAPER;
            default:
                return false;
        }
    }

}