package app; 

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.*;

public class ImageHelper {

    public static Image safeLoadImage(String path, double width, double height) {
        try {
            if (path == null || path.trim().isEmpty()) {
                throw new Exception("Empty path");
            }
            // إذا الرابط ويب
            if (path.startsWith("http://") || path.startsWith("https://")) {
                return new Image(path, width, height, true, true, true);
            }
            // إذا ملف محلي
            Path p = Paths.get(path);
            if (!p.isAbsolute()) {
                p = Paths.get(System.getProperty("user.dir")).resolve(path);
            }
            try (InputStream is = new FileInputStream(p.toFile())) {
                return new Image(is, width, height, true, true);
            }
        } catch (Exception ex) {
            // صورة افتراضية (تأكد تضيفها في resources/images/placeholder.png)
            return new Image(ImageHelper.class.getResourceAsStream("/images/placeholder.png"));
        }
    }
}

