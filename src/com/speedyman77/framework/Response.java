package com.speedyman77.framework;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StringTemplate.STR;

public class Response {
    private HttpStatus status;
    private String contentType;
    private Header[] headers;
    private String text;

    public static ResponseBuilder getBuilder(HttpStatus httpStatus, String contentType) {
        return new ResponseBuilder(httpStatus, contentType);
    }

    public static class ResponseBuilder {
        HttpStatus status;
        String contentType;
        List<Header> headers = new ArrayList<>();
        String content = "";

        public ResponseBuilder(HttpStatus status, String contentType) {
            this.status = status;
            this.contentType = contentType;

            headers.add(new Header("Content-Type", contentType));
            headers.add(new Header("Content-Length", "0"));
            headers.add(new Header("Date", java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now( ZoneOffset.UTC ))));
            headers.add(new Header("Server", "My Custom Server"));
        }

        public ResponseBuilder addHeader(String name, String value) {
            Header header = new Header(name, value);
            headers.add(header);

            return this;
        }

        public ResponseBuilder addContent(String content) {
            this.content = content;
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

            headers.stream()
                    .filter(h -> h.getName().equals("Content-Length"))
                    .findFirst()
                    .ifPresent(h -> h.setValue(String.valueOf(contentBytes.length)));

            return this;
        }

        public Response build() {
            Response res = new Response();

            res.setStatus(this.status);
            res.setContentType(this.contentType);
            res.setText(this.content);
            res.setHeaders(this.headers.toArray(new Header[0]));

            return res;
        }
    }

    public byte[] asBytes() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(STR."HTTP/1.1 \{status.getStatusCode()} \{status.name()}\n");

        for (Header header : this.headers) {
            responseBuilder.append(STR."\{header.getName()}: \{header.getValue()}\n");
        }
        responseBuilder.append("\n");

        responseBuilder.append(text);

        return responseBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
