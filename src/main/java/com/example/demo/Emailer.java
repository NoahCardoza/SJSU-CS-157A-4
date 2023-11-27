package com.example.demo;

import com.example.demo.beans.entities.Review;
import com.example.demo.beans.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class Emailer {
    static private void sendEmail(JSONObject payload) throws IOException, InterruptedException {
        if (payload.getJSONArray("to").getJSONObject(0).getString("email").endsWith("@testing.com")) {
            System.out.println("Emailer.sendEmail: " + payload.toString());
            return;
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .setHeader("accept", "application/json")
                .setHeader("api-key", Env.get("BREVO_API_KEY"))
                .setHeader("content-type", "application/json")
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    static public void sendVerificationEmail(User user, String baseUrl) throws IOException, InterruptedException {
        String token = JWTManager.getInstance().createEmailVerificationToken(user.getId());
        String url = baseUrl + "/signup?f=verify&token=" + token;

        JSONObject payload = new JSONObject();
        payload.put(
                "to",
                new JSONArray().put(
                        new JSONObject().put("email", user.getEmail())
                )
        );
        payload.put("templateId", 3);
        payload.put(
                "params",
                new JSONObject()
                        .put("username", user.getUsername())
                        .put("verification_link", url)
        );

        sendEmail(payload);
    }

    static public void sendBanNotice(User user) throws IOException, InterruptedException {
        JSONObject payload = new JSONObject();
        payload.put(
                "to",
                new JSONArray().put(
                        new JSONObject().put("email", user.getEmail())
                )
        );
        payload.put("templateId", 5);
        payload.put(
                "params",
                new JSONObject()
                        .put("username", user.getUsername())
        );

        sendEmail(payload);
    }

    static public void sendReviewHiddenNotice(User user, Review review) throws IOException, InterruptedException {
        JSONObject payload = new JSONObject();
        payload.put(
                "to",
                new JSONArray().put(
                        new JSONObject().put("email", user.getEmail())
                )
        );
        payload.put("templateId", 6);
        payload.put(
                "params",
                new JSONObject()
                        .put("username", user.getUsername())
                        .put("review_name", review.getName())
                        .put("review_description", review.getDescription())
        );

        sendEmail(payload);
    }
}
