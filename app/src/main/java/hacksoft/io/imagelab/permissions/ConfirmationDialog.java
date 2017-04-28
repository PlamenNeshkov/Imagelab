package hacksoft.io.imagelab.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import hacksoft.io.imagelab.R;


public class ConfirmationDialog extends DialogFragment {
    private static final String ARG_PERMISSION = "permission";
    private static final String ARG_REQUEST_CODE = "requestCode";

    public static ConfirmationDialog newInstance(String permission, int requestCode) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PERMISSION, permission);
        args.putInt(ARG_REQUEST_CODE, requestCode);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final Activity activity = getActivity();

        Bundle args = getArguments();
        final String[] permissions = new String[]{args.getString(ARG_PERMISSION)};
        final int requestCode = args.getInt(ARG_REQUEST_CODE);

        return new AlertDialog.Builder(activity)
                .setMessage(R.string.request_permission)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity activity = getActivity();
                                if (activity != null) {
                                    activity.finish();
                                }
                            }
                        })
                .create();
    }
}
