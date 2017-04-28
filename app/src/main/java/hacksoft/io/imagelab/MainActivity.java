package hacksoft.io.imagelab;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import hacksoft.io.imagelab.permissions.PermissionUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static hacksoft.io.imagelab.Constants.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toast currentToast;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSelectCamera(View view) {
        String perm = WRITE_EXTERNAL_STORAGE;
        if (!PermissionUtils.isGranted(this, perm)) {
            PermissionUtils.request(this, perm, WRITE_PERM_CODE);
            return;
        }
        imageUri = getOutputMediaFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, INTENT_CAMERA);
    }

    public void onSelectGallery(View view) {
        String perm = READ_EXTERNAL_STORAGE;
        if (!PermissionUtils.isGranted(this, perm)) {
            PermissionUtils.request(this, perm, READ_PERM_CODE);
            return;
        }
        String action = Intent.ACTION_PICK;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent intent = new Intent(action, uri);
        startActivityForResult(intent, INTENT_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Util.cancelToast(currentToast);
            String text = "No image received";
            currentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            currentToast.show();
            return;
        }
        if (requestCode == INTENT_GALLERY) {
            try {
                imageUri = data.getData();
            } catch (Exception e) {
                Util.cancelToast(currentToast);
                String text = "Error getting image from gallery";
                currentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
                currentToast.show();
            }
        }
        showEditActivity(requestCode);
    }

    private void showEditActivity(int imgSource) {
        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
        intent.setData(imageUri);
        intent.putExtra(EXTRA_IMG_SOURCE, imgSource);
        startActivity(intent);
    }

    private Uri getOutputMediaFile(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Imagelab");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image taken with Imagelab");
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_PERM_CODE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionUtils.request(this, permissions[0], requestCode);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
