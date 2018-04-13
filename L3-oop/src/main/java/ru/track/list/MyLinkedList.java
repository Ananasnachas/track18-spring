package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List {

    private Node first = null;
    private int length = 0;
    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node next;
        int val;

        Node(Node next, int val) {
            this.next = next;
            this.val = val;
        }
    }

    @Override
    void add(int item) {
        if(length == 0){
            first = new Node(null, item);
        }
        else{
            Node node = first;
            while (node.next != null){
                node = node.next;
            }
            node.next = new Node(null, item);
        }
        length++;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= length) {
            throw new NoSuchElementException();
        }
        int val = 0;
        if(length == 1){
            first = null;
        }
        else {
            Node node = first;
            for (int i = 0; i < idx - 1; i++) {
                node = node.next;
            }
            val = node.next.val;
            node.next = node.next.next;
        }
        length--;
        return val;
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= length) {
            throw new NoSuchElementException();
        }
        Node node = first;
        for (int i = 0; i < idx; i++) {
            node = node.next;
        }
        return node.val;
    }

    @Override
    int size() {
        return length;
    }
}
