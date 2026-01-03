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
    // NOTE: Keep your API Key secure. For this demo, it's included here.
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im90bWxmbWd5c2Nyb2h0YnBxcW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTc4MjkxNjUsImV4cCI6MjAzMzQwNTE2NX0.LkJePEXbi7jvDWzopTOdPmuh-UCTmY83NQYpvxaxuTE";

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // =========================================================================
    // 1. LOGIN / REGISTER (UNIQUE NAME + SAME DEVICE ALLOWED)
    // =========================================================================
    public static boolean loginOrRegister(String player, String hardwareId) {
        try {
            String encName = URLEncoder.encode(player, StandardCharsets.UTF_8);
            String encHw   = URLEncoder.encode(hardwareId, StandardCharsets.UTF_8);

            // 🔍 1. Check if player name exists (any device)
            String checkNameUrl = BASE_URL + "?player=eq." + encName + "&select=hardware_id";

            HttpRequest checkNameReq = HttpRequest.newBuilder()
                    .uri(URI.create(checkNameUrl))
                    .header("apikey", API_KEY)
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> nameResp =
                    client.send(checkNameReq, HttpResponse.BodyHandlers.ofString());

            String body = nameResp.body();

            // 🆕 Name does NOT exist → register new user
            if (body.equals("[]") || body.length() < 3) {

                String jsonBody = String.format(
                        "{\"player\":\"%s\",\"hardware_id\":\"%s\",\"score\":0}",
                        player, hardwareId
                );

                HttpRequest insertReq = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .header("apikey", API_KEY)
                        .header("Authorization", "Bearer " + API_KEY)
                        .header("Content-Type", "application/json")
                        .header("Prefer", "return=minimal")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                client.send(insertReq, HttpResponse.BodyHandlers.ofString());
                return true; // ✅ new user
            }

            // 👤 Name exists → check hardware_id
            if (body.contains(hardwareId)) {
                // ✅ Same user, same device
                return true;
            }

            // ❌ Same name, DIFFERENT device
            return false;

        } catch (Exception e) {
            System.err.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // 2. SAVE SCORE (Update specific User + Hardware row)
    // =========================================================================
    public static void saveScore(String player, String hardwareId, int increment) {
        new Thread(() -> {
            try {
                String encName = URLEncoder.encode(player, StandardCharsets.UTF_8);
                String encHw = URLEncoder.encode(hardwareId, StandardCharsets.UTF_8);

                // A. Fetch current score first
                String queryUrl = BASE_URL + "?player=eq." + encName + "&hardware_id=eq." + encHw + "&select=score";
                HttpRequest getRequest = HttpRequest.newBuilder()
                        .uri(URI.create(queryUrl))
                        .header("apikey", API_KEY)
                        .header("Authorization", "Bearer " + API_KEY)
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
                String body = response.body();

                int currentScore = parseJsonInt(body, "score");

                if (currentScore != -1) {
                    // B. Calculate new score and PATCH update
                    int newScore = currentScore + increment;
                    String jsonBody = String.format("{\"score\": %d}", newScore);

                    // URL specifically targets the row with matching player AND hardware_id
                    String updateUrl = BASE_URL + "?player=eq." + encName + "&hardware_id=eq." + encHw;

                    HttpRequest patchRequest = HttpRequest.newBuilder()
                            .uri(URI.create(updateUrl))
                            .header("apikey", API_KEY)
                            .header("Authorization", "Bearer " + API_KEY)
                            .header("Content-Type", "application/json")
                            .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    client.send(patchRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Score updated to: " + newScore);
                }
            } catch (Exception e) {
                System.err.println("Save failed: " + e.getMessage());
            }
        }).start();
    }

    // =========================================================================
    // 3. GET LEADERBOARD (Top 10 Scores)
    // =========================================================================
    public static Object[][] getTopScores() {
        try {
            // Select relevant fields, order by score descending, limit 10
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

    // =========================================================================
    // 4. CHECK IF USER EXISTS (For Player 2 Validation)
    // =========================================================================
    public static boolean userExists(String player) {
        try {
            // Encode parameter for URL safety
            String encName = URLEncoder.encode(player, StandardCharsets.UTF_8);

            String queryUrl = BASE_URL + "?player=eq." + encName + "&select=player";

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(queryUrl))
                    .header("apikey", API_KEY)
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            // If response is empty array "[]", user doesn't exist
            return !body.equals("[]") && body.length() >= 3;

        } catch (Exception e) {
            System.err.println("User check error: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // HELPER METHODS (JSON Parsing)
    // =========================================================================

    private static int parseJsonInt(String json, String key) {
        try {
            String val = extractJsonValue(json, key);
            // Clean non-numeric chars just in case
            val = val.replaceAll("[^0-9-]", "");
            return val.isEmpty() ? -1 : Integer.parseInt(val);
        } catch (Exception e) { return -1; }
    }

    private static Object[][] parseJsonArray(String json) {
        if (json == null || !json.startsWith("[")) return new Object[0][0];

        // Remove outer brackets []
        String content = json.substring(1, json.length() - 1);
        if (content.isEmpty()) return new Object[0][0];

        // Split objects by "}," regex
        String[] rows = content.split("(?<=\\}),");

        // Prepare data array: [Rows][3 Columns: Player, Score, Date]
        Object[][] data = new Object[rows.length][3];

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];

            // 1. Extract Player Name
            String player = extractJsonValue(row, "player");

            // 2. Extract Score
            String scoreStr = extractJsonValue(row, "score").replaceAll("[^0-9]", "");
            int score = scoreStr.isEmpty() ? 0 : Integer.parseInt(scoreStr);

            // 3. Extract Date (and shorten it)
            String rawDate = extractJsonValue(row, "created_at");
            String cleanDate = (rawDate.length() >= 10) ? rawDate.substring(0, 10) : rawDate;

            data[i][0] = player;
            data[i][1] = score;
            data[i][2] = cleanDate;
        }
        return data;
    }

    // Helper to extract value string from JSON key
    private static String extractJsonValue(String json, String key) {
        String keySearch = "\"" + key + "\":";
        int start = json.indexOf(keySearch);
        if (start == -1) return "";

        start += keySearch.length();

        // Check if value is a string (starts with quote)
        if (json.charAt(start) == '"') {
            start++; // skip opening quote
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } else {
            // It's a number or boolean
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }
    }
}