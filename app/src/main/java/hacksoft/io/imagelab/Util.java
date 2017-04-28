package hacksoft.io.imagelab;

import android.widget.Toast;

import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.CropTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import jp.wasabeef.picasso.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

public class Util {
    public static void cancelToast(Toast toast) {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static List<Class<? extends Transformation>> getFilters() {
        List<Class<? extends Transformation>> filters = new ArrayList<>();
//        filters.add(CropCircleTransformation.class);
//        filters.add(CropSquareTransformation.class);
//        filters.add(RoundedCornersTransformation.class);
//        filters.add(GrayscaleTransformation.class);
//        filters.add(BlurTransformation.class);
        filters.add(ToonFilterTransformation.class);
        filters.add(SepiaFilterTransformation.class);
        filters.add(InvertFilterTransformation.class);
        filters.add(PixelationFilterTransformation.class);
        filters.add(SketchFilterTransformation.class);
        filters.add(SwirlFilterTransformation.class);
        filters.add(VignetteFilterTransformation.class);
        return filters;
    }
}
