package com.hanul.pis.authentication.service.impl;

import com.hanul.pis.authentication.algo.trie.Trie;
import com.hanul.pis.authentication.service.CachingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CachingServiceImpl implements CachingService {
    private Trie trie = new Trie();

    public void initialize(List<String> usernames) {
        for (String username : usernames) {
            trie.insert(adjust(username));
        }
    }

    @Override
    public boolean findUsername(String username) {
        return trie.search(adjust(username));
    }

    @Override
    public void addUsername(String username) {
        this.trie.insert(adjust(username));
    }

    private String adjust(String email) {
        return email.substring(0, email.indexOf('@')).toLowerCase();
    }
}
