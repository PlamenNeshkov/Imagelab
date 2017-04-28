package hacksoft.io.imagelab;

public class Constants {
    public static final int WRITE_PERM_CODE = 0;
    public static final int READ_PERM_CODE = 1;
    public static final int INTENT_CAMERA = 0;
    public static final int INTENT_GALLERY = 1;
    public static final String EXTRA_IMG_SOURCE = "imageSource";
    public static final int LOADER_ID_BITMAP = 100;
    public static final int SAVER_ID_BITMAP = 200;

    public static enum FLIPPER_CHILDREN {
        TOOLBOX, FILTERS, LOADING, CONFIRMATION
    }
}
