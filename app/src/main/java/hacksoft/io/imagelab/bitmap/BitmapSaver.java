package hacksoft.io.imagelab.bitmap;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class BitmapSaver extends AsyncTaskLoader<String> {
    private Context context;
    private Bitmap bitmap;

    public BitmapSaver(Context context, Bitmap bitmap) {
        super(context);
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        OutputStream outStream;
        File dir = new File(root + "/Imagelab");
        dir.mkdirs();

        Random generator = new Random();
        int n = 1000000;
        n = generator.nextInt(n);
        String fname = "EditedImage-"+ n +".jpg";
        File file = new File (dir, fname);

        if (file.exists()) {
            file.delete();
            file = new File(dir, fname);
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {}
                });
        return file.getPath();
    }
}
