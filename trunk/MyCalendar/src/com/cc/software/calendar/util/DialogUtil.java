package com.cc.software.calendar.util;

import hut.cc.software.calendar.R;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.cc.software.calendar.dialog.CustomDialog;

public class DialogUtil {

    public final static CustomDialog createConfirmDeleteDialog(Context mContext,
                    DialogInterface.OnClickListener listener) {
        CustomDialog dialog = new CustomDialog(mContext);
        dialog.setTitle(mContext.getResources().getString(R.string.confirm_delete));
        dialog.setPositiveButton(R.string.ok, listener);
        dialog.setNegativeButton(R.string.cancel, listener);
        dialog.findViewById(R.id.custom).setVisibility(View.GONE);
        return dialog;
    }

    public final static CustomDialog createConfirmExitDialog(Context mContext,
                    DialogInterface.OnClickListener listener) {
        CustomDialog dialog = new CustomDialog(mContext);
        dialog.setTitle(mContext.getResources().getString(R.string.confirm_exit));
        dialog.setPositiveButton(R.string.ok, listener);
        dialog.setNegativeButton(R.string.cancel, listener);
        dialog.findViewById(R.id.custom).setVisibility(View.GONE);
        return dialog;
    }

}
