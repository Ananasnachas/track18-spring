package ru.track.cypher;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * Вспомогательные методы шифрования/дешифрования
 */
public class CypherUtil {

    public static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Генерирует таблицу подстановки - то есть каждой буква алфавита ставится в соответствие другая буква
     * Не должно быть пересечений (a -> x, b -> x). Маппинг уникальный
     *
     * @return таблицу подстановки шифра
     */
    @NotNull
    public static Map<Character, Character> generateCypher() {
        Map<Character, Character> hashMap = new HashMap<>();
        for (int i = 0; i < 'z'-'a'; i++) {
            hashMap.put((char)('a' + i), (char)('a' + i + 1));
        }
        hashMap.put('z', 'a');
        return hashMap;
    }
}
