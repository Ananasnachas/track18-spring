package ru.track.cypher;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * Класс умеет кодировать сообщение используя шифр
 */
public class Encoder {

    /**
     * Метод шифрует символы текста в соответствие с таблицей
     * NOTE: Текст преводится в lower case!
     *
     * Если таблица: {a -> x, b -> y}
     * то текст aB -> xy, AB -> xy, ab -> xy
     *
     * @param cypherTable - таблица подстановки
     * @param text - исходный текст
     * @return зашифрованный текст
     */
    public String encode(@NotNull Map<Character, Character> cypherTable, @NotNull String text) {
        text = text.toLowerCase();
        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        Character mapChar;
        for (char c :chars) {
            mapChar = cypherTable.get(c);
            if(mapChar == null){
                sb.append(c);
            }
            else{
                sb.append(mapChar);
            }
        }
        return new String(sb);
    }
}
