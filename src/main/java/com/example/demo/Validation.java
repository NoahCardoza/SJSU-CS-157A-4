package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Validation {
    List<String> messages;

    public Validation() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public boolean isValid() {
        return this.messages.size() == 0;
    }

    public List<String> getMessages() {
        return this.messages;
    }
}
