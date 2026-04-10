package jdm.model;

/**
Represents a single CMAS assessment at a specific date.
 */
public class CmasEntry {
    private final String date;
    private final String scoreAbove10;   // value or empty
    private final String score4to9;      // value or empty

    public CmasEntry(String date, String scoreAbove10, String score4to9) {
        this.date         = date;
        this.scoreAbove10 = scoreAbove10;
        this.score4to9    = score4to9;
    }

    public String getDate()         { return date; }
    public String getScoreAbove10() { return scoreAbove10; }
    public String getScore4to9()    { return score4to9; }

    /** Returns the recorded score (whichever range has a value), or "-" if none. */
    public String getDisplayScore() {
        if (scoreAbove10 != null && !scoreAbove10.isBlank()) return scoreAbove10 + " (>10 range)";
        if (score4to9    != null && !score4to9.isBlank())    return score4to9    + " (4-9 range)";
        return "-";
    }
}
