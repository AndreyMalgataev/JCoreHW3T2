import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String installPath = "C://Games/JCoreHW3/Games";
        ArrayList<String> filesToZip = new ArrayList<>();

        GameProgress[] points = {
                new GameProgress(1, 1, 1, 1),
                new GameProgress(2, 2, 2, 2),
                new GameProgress(3, 3, 3, 3)
        };

        int i = 0;
        for (GameProgress point : points) {
            String saveFilePath = installPath + "/savegames/save" + i + ".dat";
            saveGame(saveFilePath, point);
            filesToZip.add(saveFilePath);
            i++;
        }

        zipFiles(installPath + "/savegames/zip.zip", filesToZip);
    }

    private static void saveGame(String saveFilePath, GameProgress savePoint) {
        try (FileOutputStream fos = new FileOutputStream(saveFilePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(savePoint);
            System.out.println("Сохранен прогресс: " + savePoint.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void zipFiles(String zipFilePath, ArrayList<String> filesToZip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (String filePath : filesToZip) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    String[] pathParts = filePath.split("/");
                    ZipEntry entry = new ZipEntry(pathParts[pathParts.length - 1]);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                if (Path.of(filePath).toFile().delete()) {
                    System.out.println("Файл добавлен в архив и удален: " + filePath);
                }
            }
            System.out.println("Архив создан: " + zipFilePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
