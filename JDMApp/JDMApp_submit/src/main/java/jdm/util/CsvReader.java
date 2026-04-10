package jdm.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/* 
 Handles quoted fields and returns rows as String arrays.
*/
public class CsvReader {

    public static List<String[]> readAll(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(parseLine(line));
            }
        }
        return rows;
    }

    /** Split a CSV line, respecting double-quoted fields. */
    private static String[] parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        fields.add(sb.toString().trim());
        return fields.toArray(new String[0]);
    }
}
