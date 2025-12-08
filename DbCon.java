package tictactoe;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DbCon {

    private static final String BASE_URL = "https://otmlfmgyscrohtbpqqoi.supabase.co/rest/v1/vp_scores";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im90bWxmbWd5c2Nyb2h0YnBxcW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTc4MjkxNjUsImV4cCI6MjAzMzQwNTE2NX0.LkJePEXbi7jvDWzopTOdPmuh-UCTmY83NQYpvxaxuTE";

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // --- 1. SAVE SCORE ---
    public static void saveScore(String player, int i) {
        new Thread(() -> {
            try {
                String encodedName = URLEncoder.encode(player, StandardCharsets.UTF_8);
                String queryUrl = BASE_URL + "?player=eq." + encodedName + "&select=id,score";

                HttpRequest getRequest = HttpRequest.newBuilder()
                        .uri(URI.create(queryUrl))
                        .header("apikey", API_KEY)
                        .header("Authorization", "Bearer " + API_KEY)
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();

                if (responseBody.equals("[]") || responseBody.length() < 5) {
                    insertNewUser(player);
                } else {
                    int id = parseJsonInt(responseBody, "id");
                    int currentScore = parseJsonInt(responseBody, "score");

                    if (id != -1 && currentScore != -1) {
                        updateExistingUser(id, currentScore + 1);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error saving score: " + e.getMessage());
            }
        }).start();
    }

    // --- 2. GET TOP SCORES (Fixed array size to 3) ---
    public static Object[][] getTopScores() {
        try {
            // Request 3 fields: player, score, created_at
            String queryUrl = BASE_URL + "?select=player,score,created_at&order=score.desc&limit=10";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(queryUrl))
                    .header("apikey", API_KEY)
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseJsonArray(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    // --- Helper: JSON Parsing ---
    private static Object[][] parseJsonArray(String json) {
        if (json == null || !json.startsWith("[")) return new Object[0][0];

        String content = json.substring(1, json.length() - 1);
        if (content.isEmpty()) return new Object[0][0];

        String[] rows = content.split("(?<=\\}),");

        // IMPORTANT: Size is [rows][3] to hold {Player, Score, Date}
        Object[][] data = new Object[rows.length][3];

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];

            // 1. Player
            String player = extractJsonValue(row, "player");

            // 2. Score
            String scoreStr = extractJsonValue(row, "score").replaceAll("[^0-9]", "");
            int score = scoreStr.isEmpty() ? 0 : Integer.parseInt(scoreStr);

            // 3. Date
            String rawDate = extractJsonValue(row, "created_at");
            String cleanDate = (rawDate.length() >= 10) ? rawDate.substring(0, 10) : rawDate;

            data[i][0] = player;
            data[i][1] = score;
            data[i][2] = cleanDate;
        }
        return data;
    }

    private static String extractJsonValue(String json, String key) {
        String keySearch = "\"" + key + "\":";
        int start = json.indexOf(keySearch);
        if (start == -1) return "";
        start += keySearch.length();

        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } else {
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }
    }

    // Helper for Save Logic
    private static void insertNewUser(String player) throws Exception {
        String jsonBody = String.format("{\"player\": \"%s\", \"score\": 1}", player);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).header("apikey", API_KEY).header("Authorization", "Bearer " + API_KEY).header("Content-Type", "application/json").header("Prefer", "return=minimal").POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void updateExistingUser(int id, int newScore) throws Exception {
        String jsonBody = String.format("{\"score\": %d}", newScore);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "?id=eq." + id)).header("apikey", API_KEY).header("Authorization", "Bearer " + API_KEY).header("Content-Type", "application/json").method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static int parseJsonInt(String json, String key) {
        try {
            String val = extractJsonValue(json, key);
            return Integer.parseInt(val);
        } catch (Exception e) { return -1; }
    }
}