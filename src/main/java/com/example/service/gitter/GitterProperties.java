package com.example.service.gitter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "gitter")
public class GitterProperties {

    private GenericProperties api;

    public GenericProperties getApi() {
        return api;
    }

    public void setApi(GenericProperties api) {
        this.api = api;
    }

    public static class GenericProperties {
        private URI endpoint;
        private URI messagesResource;
        private String version = "v1";
        private Auth auth = new Auth();

        public URI getMessagesResource() {
            return messagesResource;
        }

        public void setMessagesResource(URI messagesResource) {
            this.messagesResource = messagesResource;
        }

        public URI getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(URI endpoint) {
            this.endpoint = endpoint;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Auth getAuth() {
            return auth;
        }

        public void setAuth(Auth auth) {
            this.auth = auth;
        }

        public static class Auth {
            private String token;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
