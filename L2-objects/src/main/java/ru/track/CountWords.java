package ru.track;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;


/**
 * Задание 1: Реализовать два метода
 *
 * Формат файла: текстовый, на каждой его строке есть (или/или)
 * - целое число (int)
 * - текстовая строка
 * - пустая строка (пробелы)
 *
 * Числа складываем, строки соединяем через пробел, пустые строки пропускаем
 *
 *
 * Пример файла - words.txt в корне проекта
 *
 * ******************************************************************************************
 *  Пожалуйста, не меняйте сигнатуры методов! (название, аргументы, возвращаемое значение)
 *
 *  Можно дописывать новый код - вспомогательные методы, конструкторы, поля
 *
 * ******************************************************************************************
 *
 */
public class CountWords {

    String skipWord;

    public CountWords(String skipWord) {
        this.skipWord = skipWord;
    }

    /**
     * Метод на вход принимает объект File, изначально сумма = 0
     * Нужно пройти по всем строкам файла, и если в строке стоит целое число,
     * то надо добавить это число к сумме
     * @param file - файл с данными
     * @return - целое число - сумма всех чисел из файла
     */
    public long countNumbers(File file) throws Exception {
        long result = 0;
        Scanner scanner = new Scanner(file);
        String line;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            try {
                result += new Integer(line);
            }catch(Exception ignored){}
        }
        return result;
    }

    private boolean isStringRight(String string){
        if(string.equals(skipWord)){
            return false;
        }
        char[] chars = string.toCharArray();
        for (char c:chars){
            if(!(c=='0'||c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||c=='8'||c=='9'||c==' ')){
                return true;
            }
        }
        return false;
    }
    /**
     * Метод на вход принимает объект File, изначально результат= ""
     * Нужно пройти по всем строкам файла, и если в строка не пустая и не число
     * то надо присоединить ее к результату через пробел
     * @param file - файл с данными
     * @return - результирующая строка
     */
    public String concatWords(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(file);
        String string;
        while (scanner.hasNextLine()){
            string = scanner.nextLine();
            if (isStringRight(string)) {
                sb.append(string);
                sb.append(' ');
            }
        }
        return new String(sb);
    }

}

