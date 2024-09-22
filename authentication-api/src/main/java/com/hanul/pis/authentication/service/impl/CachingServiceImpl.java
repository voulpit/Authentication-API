package com.hanul.pis.authentication.service.impl;

import com.hanul.pis.authentication.service.algo.trie.Trie;
import com.hanul.pis.authentication.service.CachingService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hanul.pis.authentication.service.algo.trie.Node.SLASH_ASCII;

@Service
public class CachingServiceImpl implements CachingService {
    private final Trie usernameCache = new Trie();

    public void initialize(List<String> usernames) {
        for (String username : usernames) {
            usernameCache.insert(adjust(username));
        }
    }

    @Override
    public boolean findUsername(String username) {
        return usernameCache.search(adjust(username));
    }

    @Override
    public void addUsername(String username) {
        this.usernameCache.insert(adjust(username));
    }

    private String adjust(String email) {
        return email.replace('@', SLASH_ASCII).toLowerCase();
    }
}
