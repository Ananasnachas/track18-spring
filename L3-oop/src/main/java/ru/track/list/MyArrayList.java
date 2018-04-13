package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    private int length = 0;

    private int[] list;

    public MyArrayList() {
        this(10);
    }

    public MyArrayList(int capacity) {
        list = new int[capacity*2];
    }

    @Override
    public void add(int item) {
        if(list.length > length) {
            list[length] = item;
        }
        else {
            int[] newList = new int[length * 2 + 2];
            System.arraycopy(list, 0, newList, 0, length);
            newList[length] = item;
            list = newList;
        }
        length++;
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if(idx>=length||idx<0) {
            throw new NoSuchElementException(Integer.toString(idx));
        }
        int a = list[idx];
        if(list.length > length*2) {
            int[] newList = new int[length];
            System.arraycopy(list, 0, newList, 0, idx);
            System.arraycopy(list, idx + 1, newList, idx, length - (idx + 1));
            list = newList;
        }
        else
            System.arraycopy(list, idx + 1, list, idx, length - (idx + 1));
        length--;
        return a;
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if(idx>=length||idx<0) {
            throw new NoSuchElementException();
        }else
            return list[idx];
    }

    @Override
    public int size() {
        return length;
    }
}
