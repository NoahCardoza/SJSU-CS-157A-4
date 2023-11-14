import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Env;
import com.example.demo.servlets.auth.SignupServlet;
import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Main {
    static public class Email {
        public Sender sender;
        public ArrayList<To> to;
        public String subject;
        public String htmlContent;

        public Email(Sender sender, ArrayList<To> to, String subject, String htmlContent) {
            this.sender = sender;
            this.to = to;
            this.subject = subject;
            this.htmlContent = htmlContent;
        }
    }

    static public class Sender {
        public String name;
        public String email;

        public Sender(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    static public class To {
        public String email;
        public String name;

        public To(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = JWT.create()
                .withIssuer("littlehiddengems.link")
                .withClaim("user_id", 1)
                .sign(algorithm);


        DecodedJWT decodedJWT;
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("littlehiddengems.link")
                    .build();

            decodedJWT = verifier.verify(token);
            System.out.println(decodedJWT.getClaim("user_id").asInt());
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return;
        }

        System.out.println(token);
        return;


//        HttpClient client = HttpClient.newHttpClient();
//
//        JSONObject email = new JSONObject();
//        email.put(
//                "to",
//                new JSONArray().put(
//                        new JSONObject().put("email", "noahcardoza@gmail.com")
//                )
//        );
//        email.put("templateId", 3);
//        email.put(
//                "params",
//                new JSONObject()
//                    .put("username", "John Doe")
//                    .put("verification_link", "https://littlehiddengems.link/verify?token=123")
//        );
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
//                .POST(HttpRequest.BodyPublishers.ofString(email.toString()))
//                .setHeader("accept", "application/json")
//                .setHeader("api-key", Env.get("BREVO_API_KEY"))
//                .setHeader("content-type", "application/json")
//                .build();
//
//        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
