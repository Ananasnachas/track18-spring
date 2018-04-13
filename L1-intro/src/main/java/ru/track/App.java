package ru.track;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * TASK:
 * POST request to  https://guarded-mesa-31536.herokuapp.com/track
 * fields: name,github,email
 *
 * LIB: http://unirest.io/java.html
 *
 *
 */
public class App {

    public static final String URL = "http://guarded-mesa-31536.herokuapp.com/track";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_GITHUB = "github";
    public static final String FIELD_EMAIL = "email";

    public static void main(String[] args) throws Exception {
       // 1) Use Unirest.post()
        // 2) Get response .asJson()
        // 3) Get json body and JsonObject
        // 4) Get field "success" from JsonObject
        HttpResponse<JsonNode> response = Unirest.post(URL)
                .header("accept", "application/json")
                .field(FIELD_NAME, "Maslov Nikita")
                .field(FIELD_GITHUB, "https://github.com/Ananasnachas")
                .field(FIELD_EMAIL, "nikita_maslov_warspear@mail.ru")
                .asJson();
            response.getBody().getObject().get("success");
        boolean success = false;
    }

}
