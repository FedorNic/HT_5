/**
 * 3***. Предположить, что числа в исходном массиве из 9 элементов имеют диапазон [0, 3], и представляют собой,
 * например, состояния ячеек поля для игры в крестики-нолики, где 0 – это пустое поле, 1 – это поле с крестиком,
 * 2 – это поле с ноликом, 3 – резервное значение. Такое предположение позволит хранить в одном числе типа int
 * всё поле 3х3. Записать в файл 9 значений так, чтобы они заняли три байта.
 */

/*
Не знаю правильно ли я понял задачу, но сделать ее смог только для диапазона [0, 2]. Диапазон [0, 3] меня победил.
Для файла в 3 байта необходимо закодировать двумерный массив тремя отдельными байтами и записать их.
Максимально возможное число 333 уместить в диапазон -128...127 я никак не смог. Не перевод его в шестнадцатеричную
систему (после десятичной начинаются буквенные обозначения, которые в byte не положить), ни побитовый сдвиг
эту задачу не решают (обратно уменьшенное число точно не вернуть).
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TicTacToe {
    public static void main(String[] args) throws IOException {

        String fileDest = "./src/ticTacToe.txt";
        //  Заполеннный исходный массив байт (двумерный как поле для игры в крестики-нолики)
        byte[][] ticTacToeInitialField = new byte[][]{{2, 2, 0}, {2, 1, 0}, {0, 0, 0}};
        System.out.println("Исходный массив байт: \n" + Arrays.deepToString(ticTacToeInitialField));

        //  Склеиваем подмассивы в каждый в свою строку
        String zeroLineString = lineOfTwoDimArrayToString(ticTacToeInitialField, 0);
        String firstLineString = lineOfTwoDimArrayToString(ticTacToeInitialField, 1);
        String secondLineString = lineOfTwoDimArrayToString(ticTacToeInitialField, 2);

        //  Приводим полученные строки к байтам (для возможности впихнуть максимально возможное число 222 в диапазон
        //  -128...127 проводим "кодировку" - умышленно уменьшая число на 127)
        byte zeroLine = encoding(zeroLineString);
        byte firstLine = encoding(firstLineString);
        byte secondLine = encoding(secondLineString);

        // Открываем поток на запись и пишем байты в файл по очереди
        try (FileOutputStream output = new FileOutputStream(fileDest)) {
            output.write(zeroLine);
            output.write(firstLine);
            output.write(secondLine);
        }

        // Открываем поток на чтение и берем из файла массив байт
        try (FileInputStream input = new FileInputStream(fileDest)) {
            byte[] bytesFromFile = input.readAllBytes();

            //  Возвращаем числам исходное значение ("декодируем" их обратно). Для этого конвертируем их в int
            //  (число 222 в byte не влезет).
            int[] decoderedIns = decoding(bytesFromFile);

            //  Режем число на части и обратно собираем массив byte (в соответствии с позицией)
            byte[][] ticTacToeFinalField = splitToDimArray(decoderedIns);
            System.out.println("Взятый из файла массив байт: \n" + Arrays.deepToString(ticTacToeFinalField));
        }
        File file = new File(fileDest);
        System.out.println("Размер записанного файла в байтах: \n" + file.length());
    }

    /**
     * Функция разделения двумерного массива на подмассивы и конвертации полученных значений в строку
     *
     * @param array двумерный массив
     * @param index индекс для которого нужно произвести операцию
     * @return
     */
    private static String lineOfTwoDimArrayToString(byte[][] array, int index) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            res = new StringBuilder("" + res + array[index][i]);
        }
        return res.toString();
    }

    /**
     * Функция кодировки строки в один байт (учитывая возможное число 222 делаем кодировку, уменьшая исходное на 127)
     *
     * @param str Строка для кодировки
     * @return
     */
    private static byte encoding(String str) {
        return (byte) (Integer.parseInt(str) - 127);
    }

    /**
     * Функция обратной кодировки исходного числа. Для этого полученное число (может быть больше допустимого интервала
     * для byte) приводим к int
     *
     * @param byteArray Массив байт
     * @return
     */
    private static int[] decoding(byte[] byteArray) {
        int[] decoderedNumbers = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            decoderedNumbers[i] = byteArray[i] + 127;
        }
        return decoderedNumbers;
    }

    /**
     * Функция разрезки число на части и обратной сборки в массив byte (в соответствии с позицией)
     *
     * @param decoderedNumbers Массив для разрезки
     * @return
     */
    private static byte[][] splitToDimArray(int[] decoderedNumbers) {
        byte[][] totalArray = new byte[decoderedNumbers.length][decoderedNumbers.length];
        for (int i = 0; i < decoderedNumbers.length; i++) {
            totalArray[i][0] = (byte) (decoderedNumbers[i] / 100);
            totalArray[i][1] = (byte) ((decoderedNumbers[i] / 10) % 10);
            totalArray[i][2] = (byte) (decoderedNumbers[i] % 10);
        }
        return totalArray;
    }
}