package com.hanul.pis.authentication.service.algo.trie;

public class Trie {
    Node root;

    public Trie() {
        root = new Node(null);
    }

    public void insert(String word) {
        boolean end;
        if (!word.isEmpty()) {
            Node node = root;
            int i = 0;
            do {
                char ch = word.charAt(i);
                end = i == word.length()-1;
                Node child = node.getChild(ch);
                if (child == null) {
                    child = node.addChild(new Node(ch));
                }
                child.end = end || child.end;
                node = child;
                i++;
            } while (!end);
        }
    }

    public boolean search(String word) {
        return search(word, false);
    }

    public boolean startsWith(String prefix) {
        return search(prefix, true);
    }

    private boolean search(String word, boolean prefix) {
        int i = 0;
        Node node = root;
        while (true) {
            char ch = word.charAt(i);
            Node child = node.getChild(ch);
            if (child == null) {
                return false;
            }
            if (i == word.length() - 1) {
                return prefix || child.end;
            }
            i++;
            node = child;
        }
    }
}
