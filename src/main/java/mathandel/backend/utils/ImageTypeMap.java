package mathandel.backend.utils;

import java.util.HashMap;
import java.util.Map;

public class ImageTypeMap {

    private static Map<String, String> typesToExtensions = new HashMap<>();

    static {
        typesToExtensions.put("image/bmp", "bmp");
        typesToExtensions.put("image/cgm", "cgm");
        typesToExtensions.put("image/gif", "gif");
        typesToExtensions.put("image/ief", "ief");
        typesToExtensions.put("image/tiff", "tiff");
        typesToExtensions.put("image/png", "png");
    }

    public static String getExtension(String type) {
        return typesToExtensions.get(type);
    }

}
