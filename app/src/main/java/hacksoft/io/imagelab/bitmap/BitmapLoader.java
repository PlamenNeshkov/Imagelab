package hacksoft.io.imagelab.bitmap;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapLoader extends AsyncTaskLoader<Bitmap> {

    private Context context;
    private Uri uri;

    private Bitmap bitmap;

    public BitmapLoader(Context context, Uri uri) {
        super(context);
        this.context = context;
        this.uri = uri;
    }

    @Override
    protected void onStartLoading() {
        if (bitmap == null) {
            forceLoad();
        } else {
            deliverResult(bitmap);
        }
    }

    @Override
    public Bitmap loadInBackground() {
        String path = BitmapUtils.getPath(context, uri);
        int sampleSize = BitmapUtils.calcSampleSize(context, uri, path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        options.inMutable = true;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            Bitmap workingBitmap = BitmapUtils.orientate(bitmap, path);
            this.bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
