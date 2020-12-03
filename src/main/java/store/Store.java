package store;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import org.openjfx.hellofx.Result;

public class Store {
    public static String text = "";
    public static ArrayDeque<Result> list = new ArrayDeque<>();
    public static  Map<String,byte[]> bytes_store = new HashMap<String, byte[]>();
    public static Boolean isVideo = false;
}
