package android.ext;

import java.io.File;

public class Ls {
    public static void main(String[] args) {
        File[] arr_file = new File(args[0]).listFiles();
        if(arr_file != null) {
            for(int v = 0; v < arr_file.length; ++v) {
                File item = arr_file[v];
                if(item != null) {
                    System.out.println(item.getAbsolutePath());
                }
            }
        }
    }
}

