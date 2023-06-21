/**
 * 1. Написать функцию, создающую резервную копию всех файлов в директории (без поддиректорий) во вновь созданную
 * папку ./backup
 * 2. Доработайте класс Tree и метод print который мы разработали на семинаре. Ваш метод должен распечатать полноценное
 * дерево директорий и файлов относительно корневой директории.
*/

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) throws IOException {

        Tree.print(new File("."), "", true);

        backup("src/needBackup");
        //backup("src/main/java");
    }


    /**
     * Функция, создающая резервную копию всех файлов в указанной директории (без поддиректорий) во вновь созданную
     * папку "./backup" (устаревшие файлы в папке "./backup" удаляются).
     *
     * @param path Путь к папке, содержание которой нужно скопировать
     * @throws IOException
     */
    private static void backup(String path) throws IOException {
        Path srcDirectory = Path.of(path).toAbsolutePath();
        Path destDirectory = Path.of("./backup");
        // Метод .createDirectory любит кидать исключение вместо перезаписи папки
        if (!Files.exists(destDirectory)) {
            Files.createDirectory(destDirectory);
        } else {
            // Метод .copy (см ниже) тоже любит кидать исключения при копировании файлов. Удаляем предыдущий backup
            try (DirectoryStream<Path> files = Files.newDirectoryStream(destDirectory)) {
                for (Path dir : files)
                    Files.delete(dir);
            }
        }
        try (DirectoryStream<Path> files = Files.newDirectoryStream(srcDirectory)) {
            for (Path dir : files)
                // Копируем только файлы.
                // Поскольку destDirectory - это имя пустой папки, а мне в методе .copy нужно прописать пути с именем
                // файла, приходиться склеить destDirectory и имя файла из srcDirectory.
                if (Files.isRegularFile(dir)) {
                    Path resolve = destDirectory.resolve(dir.getFileName());
                    Files.copy(dir, resolve);
                }
        }
    }
}