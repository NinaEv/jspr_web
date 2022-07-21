package ru.netology;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    private static final List<String> pathsList = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/events.html", "/events.js", "/default-get.html");

    public static void main(String[] args) {
        final var server = new Server();
        pathsList.forEach(x -> server.addHandler("GET" + x, ((request, out) -> {
            try {
                final var type = Files.probeContentType(request.getPath());
                final var length = Files.size(request.getPath());
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + type + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(request.getPath(), out);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })));
        server.addHandler("GET/classic.html", ((request, out) -> {
            try {
                final var type = Files.probeContentType(request.getPath());
                final var template = Files.readString(request.getPath());
                final var content = template.replace("{time}", LocalDateTime.now().toString()).getBytes(StandardCharsets.UTF_8);
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + type + "\r\n" +
                                "Content-Length: " + content.length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.write(content);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        server.addHandler("GET/forms.html", ((request, out) -> {
            if (request.getQuery().isEmpty()) {
                try {
                    final var type = Files.probeContentType(request.getPath());
                    final var length = Files.size(request.getPath());
                    out.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + type + "\r\n" +
                                    "Content-Length: " + length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    Files.copy(request.getPath(), out);
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println(request.getQuery());
            }
        }));
        server.addHandler("POST/default-get.html", ((request, out) -> System.out.println(request.getBody())));
        server.start(9999);
    }
}


