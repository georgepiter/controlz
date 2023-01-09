package br.com.controlz.api;

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import sibModel.SendSmtpEmail;


import java.util.Map;
import java.util.concurrent.TimeUnit;

public class API implements Client {

    private long connectTimeout;
    private long readTimeout;
    private String url;

    public API(String url, long connectTimeout, long readTimeout) {
        this.url = url;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public API() {
    }

    private Client getApi() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .retryer(Retryer.NEVER_RETRY)
                .logLevel(Logger.Level.FULL)
                .options(new Request.Options(connectTimeout, TimeUnit.SECONDS, readTimeout, TimeUnit.SECONDS, true))
                .logger(new Slf4jLogger(API.class))
                .target(Client.class, url);
    }

    @Override
    public void sendMail(Map<String, String> headers, SendSmtpEmail sendSmtpEmail) {
        getApi().sendMail(headers, sendSmtpEmail);
    }
}
