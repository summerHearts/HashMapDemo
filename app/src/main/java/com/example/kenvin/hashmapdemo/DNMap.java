package com.example.kenvin.hashmapdemo;

/**
 * Created by Kenvin on 2017/11/8.
 */

public interface DNMap <K,V> {
    public V put(K k ,V v);

    public V get(K k);

    public int size();

    public interface Entry<K ,V>{
        public K getKey();

        public V getValue();
    }
}
