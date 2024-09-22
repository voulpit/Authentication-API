package com.hanul.pis.authentication.service.algo.trie;

import com.hanul.pis.authentication.model.exception.UserValidationException;
import com.hanul.pis.authentication.utils.ErrorMessages;

public class Node {
    Character value;
    Node[] children;
    boolean end;

    public Node(Character value) {
        this.value = value;
        this.children = new Node[TOTAL_CHILDREN];
        this.end = false;
    }

    public Node getChild(char ch) {
        return children[getOrder(ch)];
    }

    public Node addChild(Node child) {
        this.children[getOrder(child.value)] = child;
        return child;
    }

    /*
     * 39 allowed characters:
     * - = 45
     * . = 46
     * / = 47, to replace @
     * 0-9 = 48-57
     * _ = 95
     * a-z = 97-122
     * Total children/node will be 39 + 1 unused (96) = 42
     */
    public static final char SLASH_ASCII = 47; // unused, will replace @
    private static final int TOTAL_CHILDREN = 42;
    private int getOrder(char ch) {
        int order = ch <= 57 ? ch-45 : ch-83; // 95-12
        if (order < 0 || order >= TOTAL_CHILDREN) {
            throw new UserValidationException(ErrorMessages.EMAIL_CONTAINS_INVALID_CHAR);
        }
        return order;
    }
}
