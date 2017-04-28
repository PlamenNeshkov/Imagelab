package hacksoft.io.imagelab;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

import hacksoft.io.imagelab.bitmap.BitmapLoader;
import hacksoft.io.imagelab.bitmap.BitmapSaver;
import hacksoft.io.imagelab.bitmap.BitmapUtils;
import hacksoft.io.imagelab.filters.FiltersAdapter;

import static hacksoft.io.imagelab.Constants.*;

public class EditActivity extends AppCompatActivity implements FiltersAdapter.ListItemClickListener {
    private static final String TAG = EditActivity.class.getSimpleName();

    private Uri imageUri;
    private ViewFlipper flipper;

    private Bitmap image;
    private ImageView imageView;

    private RecyclerView filterRecyclerView;
    private FiltersAdapter filterAdapter;
    private RecyclerView.LayoutManager filterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageView = (ImageView) findViewById(R.id.image_view_source);
        flipper = (ViewFlipper) findViewById(R.id.view_flipper);

        Intent intent = getIntent();
        imageUri = intent.getData();

        BitmapLoaderCallback bitmapLoaderCallback = new BitmapLoaderCallback();
        getLoaderManager().initLoader(LOADER_ID_BITMAP, null, bitmapLoaderCallback);

        filterRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_filters);
        filterRecyclerView.setHasFixedSize(true);

        filterManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView.setLayoutManager(filterManager);

        filterAdapter = new FiltersAdapter(this);
        filterRecyclerView.setAdapter(filterAdapter);
    }

    public void onSelectFilters(View view) {
        flipper.setDisplayedChild(FLIPPER_CHILDREN.FILTERS.ordinal());
    }

    @Override
    public void onBackPressed() {
        if (flipper.getDisplayedChild() == FLIPPER_CHILDREN.FILTERS.ordinal()) {
            flipper.setDisplayedChild(FLIPPER_CHILDREN.TOOLBOX.ordinal());
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onFilterItemClick(final int clickedItemIndex) {
        final Context context = this;
        imageView.animate().setDuration(500).alpha(0.0f);
        showLoading();
        String path = BitmapUtils.getPath(this, imageUri);
        final int sampleSize = BitmapUtils.calcSampleSize(this, imageUri, path);
        final Callback filterCallback = new Callback() {
            @Override
            public void onSuccess() {
                imageView.animate().setDuration(500).alpha(1.0f);
                flipper.setDisplayedChild(Constants.FLIPPER_CHILDREN.CONFIRMATION.ordinal());
            }

            @Override
            public void onError() {}
        };
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Transformation filter = filterAdapter.newFilterInstance(context, clickedItemIndex);
                File f = BitmapUtils.toFile(context, image);
                final RequestCreator requestCreator = Picasso .with(context).load(f)
                        .priority(Picasso.Priority.HIGH)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .resize(image.getWidth() / sampleSize, image.getHeight() / sampleSize)
                        .transform(filter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestCreator.into(imageView, filterCallback);
                    }
                });
                f = null;
                return null;
            }
        }.execute();
    }

    public void onSave(View view) {
        BitmapSaverCallback bitmapSaverCallback = new BitmapSaverCallback();
        getLoaderManager().initLoader(SAVER_ID_BITMAP, null, bitmapSaverCallback);
    }

    public void onUpload(View view) {

    }

    public void onApply(View view) {
        image = ((BitmapDrawable) imageView.getDrawable())
                .getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        flipper.setDisplayedChild(FLIPPER_CHILDREN.FILTERS.ordinal());
    }

    public void onCancel(View view) {
        imageView.animate().setDuration(300).alpha(0.0f);
        imageView.setImageBitmap(image);
        imageView.animate().setDuration(300).alpha(1.0f);
        flipper.setDisplayedChild(FLIPPER_CHILDREN.TOOLBOX.ordinal());
    }

    private void showLoading() {
        flipper.setDisplayedChild(FLIPPER_CHILDREN.LOADING.ordinal());
    }

    private class BitmapLoaderCallback implements LoaderManager.LoaderCallbacks<Bitmap> {
        @Override
        public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
            return new BitmapLoader(getApplicationContext(), imageUri);
        }

        @Override
        public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
            image = data;
            imageView.setImageBitmap(data);
        }

        @Override
        public void onLoaderReset(Loader<Bitmap> loader) {
            imageView.setImageDrawable(getDrawable(R.drawable.placeholder_image));
        }
    }

    private class BitmapSaverCallback implements LoaderManager.LoaderCallbacks<String> {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            flipper.setDisplayedChild(FLIPPER_CHILDREN.LOADING.ordinal());
            return new BitmapSaver(getApplicationContext(), image);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            flipper.setDisplayedChild(FLIPPER_CHILDREN.TOOLBOX.ordinal());
            Toast.makeText(getApplicationContext(), "Saved in " + data, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {}
    }

    //    public void onFaceDetect(View view) {
//        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
//                .setTrackingEnabled(false)
//                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
//                .build();
//        Frame frame = new Frame.Builder().setBitmap(image).build();
//        SparseArray<Face> faces = detector.detect(frame);
//        for (int i = 0; i < faces.size(); i++) {
//            Face face = faces.valueAt(i);
//            PointF pos = face.getPosition();
//
//            Bitmap temp = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(temp);
//
//            float left = pos.x;
//            float right = pos.x + face.getWidth();
//            float top = pos.y;
//            float bottom = pos.y + face.getHeight();
//
//            Paint paint = new Paint();
//            paint.setColor(Color.WHITE);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(10);
//
//            canvas.drawBitmap(image, 0, 0, null);
//            canvas.drawRect(left, top, right, bottom, paint);
//            imageView.setImageBitmap(temp);
//            image = temp;
//        }
//    }
}
