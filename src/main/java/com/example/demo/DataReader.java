package com.example.demo;

import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DataReader {

    JSONObject root;

    public DataReader(String filename)
    {
        try (Reader reader = new FileReader(filename)) {
            BufferedReader rd = new BufferedReader(reader);
            String jsonText = readAll(rd);
            root = new JSONObject(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray resultatRecherche = root.getJSONObject("query").getJSONArray("search");
        JSONObject article = resultatRecherche.getJSONObject(0);
        System.out.println(article.getString("title"));
        System.out.println(article.getString("snippet"));
        System.out.println(article.getInt("wordcount"));
        article = resultatRecherche.getJSONObject(1);
        System.out.println(article.getString("title"));

        JSONObject wiki = readJsonFromUrl("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=Whale&format=json");
        System.out.println(wiki.getJSONObject("query").getJSONArray("search").getJSONObject(0).getString("snippet"));

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url)
    {
        String json = "";
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject(json);
    }
}
