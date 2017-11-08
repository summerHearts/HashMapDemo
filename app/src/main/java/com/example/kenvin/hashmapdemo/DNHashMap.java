package com.example.kenvin.hashmapdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenvin on 2017/11/8.
 */

public class DNHashMap <K,V> implements DNMap<K,V>{

    private static int defaultLength = 16;

    private static double defaultLoader = 0.75;//警示标识

    private Entry<K,V>[] table = null;

    private int size = 0;

    public DNHashMap(int length ,double loader) {

        defaultLength = length;
        defaultLoader = loader;
    }

    public DNHashMap(){
        this(defaultLength,defaultLoader);
        table = new Entry[defaultLength];
    }

    private int getIndex(K k){
        int m = defaultLength;

        int  index = k.hashCode() %(m-1);

        return index >= 0 ? index : -index;
    }

    @Override
    public V put(K k, V v) {

        //判断size是否达到扩容的标准

        if (size>=defaultLength*defaultLoader){
            //当哈希表的容量超过默认容量时，必须调整table的大小。
            // 当容量已经达到最大可能值时，那么该方法就将容量调整到Integer.MAX_VALUE返回，
            // 这时，需要创建一张新表，将原表的映射到新表中。
            upDateSize();
        }

        int index = getIndex(k);
        Entry<K,V> entry =  table[index] ;
        if (entry == null){
            table[index] = newEntry(k,v,null);
            size++;
        }else {
            //如何index 位置元素不为空，说明有数据，指针指向老数据
            table[index] = newEntry(k,v,entry);
        }
        return table[index].getValue();
    }

    private void upDateSize(){
       Entry<K,V>[] newTable = new Entry[2*defaultLength];//扩容之后表的数据或者链表的数据需要重新放置
        againHash(newTable);
    }

    private void  againHash(Entry<K,V>[] newTable){

        List<Entry<K,V>> list = new ArrayList<>();
        //如何拿之前的老数据
        for (int i = 0; i <table.length ; i++) {
            if (table[i] == null){
                continue;
            }
            findEntryByNext(table[i],list);
        }


        //不为空可能是一个链表。此时用一个集合来处理
        if (list.size() > 0 ){
            //进行新数组的再散列
            size = 0;
            defaultLength= 2*defaultLength;
            table = newTable;
            for (Entry<K,V> entry:list
                 ) {
                if (entry.next !=null){
                    entry.next =null;
                }

                put(entry.getKey(),entry.getValue());
            }

        }

    }

    // 递归操作
    private void findEntryByNext(Entry<K,V> entry,List<Entry<K,V>> list){

        if (entry!= null && entry.next!=null){
            list.add(entry);
            findEntryByNext(entry.next,list);
        }else {
            list.add(entry);
        }


    }

    private Entry<K,V> newEntry(K k,V v,Entry<K,V> next){
        return new Entry<>(k,v,next);
    }

    @Override
    public V get(K k) {

        int index = getIndex(k);

        if (table[index] == null){
            return null;
        }

        return findValueByEqualKey(k,table[index]);
    }

    public V findValueByEqualKey(K k,Entry<K,V> entry){

        if (k == entry.getKey() || k.equals(entry.getKey())){
            return entry.getValue();
        }else {
            if (entry.next != null){
                return findValueByEqualKey(k,entry.next);
            }
        }
        return entry.getValue();
    }

    @Override
    public int size() {
        return size;
    }

    class  Entry<K,V> implements DNMap.Entry<K,V> {

        K k;
        V v;
        Entry<K,V> next ;

        public Entry(K k, V v, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }
}
