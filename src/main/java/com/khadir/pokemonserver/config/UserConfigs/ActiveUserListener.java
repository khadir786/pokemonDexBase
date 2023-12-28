package com.khadir.pokemonserver.config.UserConfigs;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveUserListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(ActiveUserListener.class);
    private static final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            activeSessions.put(session.getId(), username);
        } else {
            logger.info("Session created without username. Session ID: " + session.getId());
        }
    }


    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        activeSessions.remove(se.getSession().getId());
    }

    public static ConcurrentHashMap<String, String> getActiveSessions() {
        return activeSessions;
    }
}
