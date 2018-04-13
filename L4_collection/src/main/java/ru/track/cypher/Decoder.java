package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;

public class Decoder {

    // Расстояние между A-Z -> a-z
    public static final int SYMBOL_DIST = 32;

    private Map<Character, Character> cypher;

    /**
     * Конструктор строит гистограммы открытого домена и зашифрованного домена
     * Сортирует буквы в соответствие с их частотой и создает обратный шифр Map<Character, Character>
     *
     * @param domain - текст по кторому строим гистограмму языка
     */
    public Decoder(@NotNull String domain, @NotNull String encryptedDomain) {
        Map<Character, Integer> domainHist = createHist(domain);
        Map<Character, Integer> encryptedDomainHist = createHist(encryptedDomain);
        cypher = new LinkedHashMap<>();

        Character[] keys = new Character[0];
        Character[] values = new Character[0];
        keys = encryptedDomainHist.keySet().toArray(keys);
        values= domainHist.keySet().toArray(values);

        for (int i = 0; i < keys.length; i++) {
            cypher.put(keys[i],values[i]);
        }
    }

    public Map<Character, Character> getCypher() {
        return cypher;
    }

    /**
     * Применяет построенный шифр для расшифровки текста
     *
     * @param encoded зашифрованный текст
     * @return расшифровка
     */
    @NotNull
    public String decode(@NotNull String encoded) {
        return new Encoder().encode(cypher, encoded);
    }

    /**
     * Считывает входной текст посимвольно, буквы сохраняет в мапу.
     * Большие буквы приводит к маленьким
     *
     *
     * @param text - входной текст
     * @return - мапа с частотой вхождения каждой буквы (Ключ - буква в нижнем регистре)
     * Мапа отсортирована по частоте. При итерировании на первой позиции наиболее частая буква
     */
    @NotNull
    Map<Character, Integer> createHist(@NotNull String text) {
        Map<Character, Integer> hashMap = new HashMap<>();
        for (int i = 0; i <= 'z' - 'a'; i++) {
            hashMap.put((char)('a' + i), 0);
        }
        text = text.toLowerCase();
        char[] chars = text.toCharArray();
        Integer mapInt;
        for (char c :chars) {
            mapInt = hashMap.get(c);
            if(mapInt != null){
                hashMap.replace(c,mapInt + 1);
            }
        }
        List<Map.Entry<Character, Integer>> list = new ArrayList<>(hashMap.entrySet());
        list.sort(Comparator.comparing(o -> (-o.getValue())));

        Map<Character, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> aList : list) {
            result.put(aList.getKey(), aList.getValue());
        }
        return result;
    }

}
