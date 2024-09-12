package com.hanul.pis.authentication.service;

import java.util.List;

public interface CachingService {
    void initialize(List<String> usernames);
    boolean findUsername(String username);
    void addUsername(String username);
}
