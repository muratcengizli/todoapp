package com.hepsi.todoapp.util;

import com.google.gson.Gson;
import com.hepsi.todoapp.pojo.DecodedToken;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtilsH {

    public boolean validateToken(String token, boolean checkExp) {
        if (ObjectUtils.isEmpty(token)) {
            throw new Error("Please provide valid token");
        }
        DecodedToken decodedToken;
        try {
            decodedToken = parseJwt(token);
            int expTime = decodedToken.getExp();

            if (checkExp) {
                if (new Date().getTime() >= expTime * 1000L) {
                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private DecodedToken parseJwt(String token) {
        String[] chunks = token.split("\\.");

        java.util.Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        Gson gson = new Gson();

        return gson.fromJson(payload, DecodedToken.class);
    }
}
