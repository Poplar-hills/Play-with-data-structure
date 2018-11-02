package Set;

import Trie.Trie;

public class TrieSet implements Set<String> {  // 因为我们实现的 Trie 是专门为处理字符串的，不是泛型类
    private Trie trie;

    public TrieSet() { trie = new Trie(); }

    public void add(String s) { trie.add(s); }

    public void remove(String s) { trie.delete(s); }

    public boolean contains(String s) { return trie.contains(s); }

    public int getSize() { return trie.getSize(); }

    public boolean isEmpty() { return trie.isEmpty(); }
}
