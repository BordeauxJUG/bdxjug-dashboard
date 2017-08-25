/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdxjug.api.infrastructure.meetup;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("meetup")
public class MeetupFeignConfiguration implements MeetupConfiguration {

    @Value("${bdxjug.meetup.key}")
    private String apiKey;

    @Value("${bdxjug.meetup.oauth2.consumer-key}")
    private String consumerKey;

    @Value("${bdxjug.meetup.oauth2.consumer-secret}")
    private String consumerSecret;

    @Value("${bdxjug.meetup.oauth2.redirect-uri}")
    private String redirecturi;

    @Override
    public String authorizeUri() {
        return "https://secure.meetup.com/oauth2/authorize" +
                "?client_id=" + this.consumerKey +
                "&response_type=code" +
                "&redirect_uri=" + this.redirecturi;
    }

    @Override
    public MeetupClient.Reader reader() {
        return buildClient(MeetupClient.Reader.class, r -> r.query("key", this.apiKey));
    }

    @Override
    public MeetupClient.Admin admin(String authorizationCode) {
        HttpRequest requestingAccessToken = HttpRequest.get(
                "https://secure.meetup.com/oauth2/access", false,
                "client_id", this.consumerKey,
                "client_secret", this.consumerSecret,
                "grant_type", "authorization_code",
                "redirect_uri", this.redirecturi,
                "code", authorizationCode);
        if (requestingAccessToken.ok()) {
            JsonElement accessToken = new JsonParser().parse(requestingAccessToken.body()).getAsJsonObject().get("access_token");
            return buildClient(MeetupClient.Admin.class, r -> r.header("Authorization", "Bearer " + accessToken));
        } else {
            throw new IllegalStateException("Cannot retrieve access token for meetup api");
        }
    }

    private <T> T buildClient(Class<T> clientClazz, RequestInterceptor authenticationInterceptor) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(authenticationInterceptor)
                .target(clientClazz, "https://api.meetup.com");
    }
}
