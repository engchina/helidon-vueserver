package io.helidon.vueserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionManager {

    private List<GreetUser> users = Collections.synchronizedList(new ArrayList<>());

    public void add(GreetUser user) {

        GreetUser findUser = users.stream().filter(tempUser -> tempUser.getUsername().contentEquals(user.getUsername()))
                .findAny().orElse(null);

        if (findUser == null) {
            users.add(user);
        }
    }

    public GreetUser get(GreetUser user) {

        GreetUser findUser = users.stream().filter(tempUser -> tempUser.getUsername().contentEquals(user.getUsername())
                && tempUser.getPassword().contentEquals(user.getPassword())).findAny().orElse(null);
        return findUser;
    }
}