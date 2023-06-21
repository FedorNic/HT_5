import java.io.File;

public class Tree {

    /**
     *  Функция графического вывода в терминал структуры проекта (папки и файлы)
     *
     * @param file путь к корню нужной директории
     * @param indent
     * @param isLast
     */
    public static void print(File file, String indent, boolean isLast) {
        System.out.print(indent); // рисуем отступ
        if (isLast) {
            System.out.print("└─");
            indent += "  ";
        } else {
            System.out.print("├─");
            indent += "│ ";
        }
        System.out.println(file.getName());

        File[] files = file.listFiles();
        if (files == null)
            return;

        int subDirTotal = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() || files[i].isFile())
                subDirTotal++;
        }

        int subDirCounter = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() || files[i].isFile()) {
                print(files[i], indent, subDirCounter == subDirTotal - 1);
                subDirCounter++;
            }
        }
    }
}