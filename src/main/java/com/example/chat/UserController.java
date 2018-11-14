package com.example.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public User create(@RequestBody User user) {
        //TODO hash passwords
        //TODO implement real security
        user.setTimeCreated(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws IncorrectLoginException {

        User authUser = userRepository.findByNameAndPassword(user.getName(), user.getPassword()).orElseThrow(
                () -> new IncorrectLoginException("incorrect login")
        );

        authUser.setSessionId(this.randString());
        userRepository.save(authUser);
        return "{ token: \"" + authUser.getSessionId() + "\" }";
        //TODO expire session id
    }

    @GetMapping
    public User getUser(@RequestHeader("token") String sessionId) throws IncorrectLoginException {
        return userRepository.findBySessionId(sessionId).orElseThrow(
                () -> new IncorrectLoginException("invalid session token")
        );
    }

    private String randString() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890,./;'[]\\-=`~<>?:{}|_+!@#$%^&*()\"";
        StringBuilder builder = new StringBuilder();
        Random rnd = new Random();
        while (builder.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHARS.length());
            builder.append(CHARS.charAt(index));
        }
        return builder.toString();
    }
}
