package android.sup;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri.Builder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;

public class FileProvider extends ContentProvider {
    private static final String[] COLUMNS;

    static {
        FileProvider.COLUMNS = new String[]{"_display_name", "_size"};
    }

    @Override  // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if(info.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if(!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static String[] copyOf(String[] original, int newLength) {
        String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    @Override  // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    private static File getApk(Uri uri) {
        String path = uri.getPath();
        if(path == null || !path.endsWith(".apk")) {
            return new File("/wrong_path/" + path);
        }
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        return new File(path);
    }

    @Override  // android.content.ContentProvider
    public String getType(Uri uri) {
        return "application/vnd.android.package-archive";
    }

    public static Uri getUriForFile(Context context, String authority, File file) {
        return new Uri.Builder().scheme("content").authority(authority).encodedPath(file.getAbsolutePath()).build();
    }

    @Override  // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No external inserts");
    }

    @Override  // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override  // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return ParcelFileDescriptor.open(FileProvider.getApk(uri), 0x10000000);
    }

    @Override  // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int i;
        File file0 = FileProvider.getApk(uri);
        if(projection == null) {
            projection = FileProvider.COLUMNS;
        }
        String[] cols = new String[projection.length];
        Object[] values = new Object[projection.length];
        int v = 0;
        int i;
        for(i = 0; v < projection.length; i = i) {
            String col = projection[v];
            if("_display_name".equals(col)) {
                cols[i] = "_display_name";
                i = i + 1;
                values[i] = file0.getName();
            }
            else if("_size".equals(col)) {
                cols[i] = "_size";
                i = i + 1;
                values[i] = file0.length();
            }
            else {
                i = i;
            }
            ++v;
        }
        String[] cols = FileProvider.copyOf(cols, i);
        Object[] values = FileProvider.copyOf(values, i);
        Cursor cursor0 = new MatrixCursor(cols, 1);
        ((MatrixCursor)cursor0).addRow(values);
        return cursor0;
    }

    @Override  // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("No external updates");
    }
}

