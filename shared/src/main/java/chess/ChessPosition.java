package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int rank;
    private final int file;

    public ChessPosition(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public ChessPosition(ChessPosition other) {
        this.rank = other.rank;
        this.file = other.file;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getFile() {
        return this.file;
    }

    public char getFileAlgNotation() {
        char fileName = switch (this.file) {
            case 1 -> 'a';
            case 2 -> 'b';
            case 3 -> 'c';
            case 4 -> 'd';
            case 5 -> 'e';
            case 6 -> 'f';
            case 7 -> 'g';
            case 8 -> 'h';
            // TODO: Add default switch case
            default -> ' ';
        };

        return fileName;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) return true;
        if (otherObj == null || getClass() != otherObj.getClass()) return false;
        ChessPosition that = (ChessPosition) otherObj;
        return rank == that.rank && file == that.file;
    }

    @Override
    public int hashCode() {
        int result = rank;
        result = 31 * result + file;
        return result;
    }

    @Override
    public String toString() {
        char fileName = getFileAlgNotation();

        return String.valueOf(fileName) + this.rank;
    }
}
