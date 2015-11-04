package com.nitorac.highschedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class ColorPickerDialog extends MaterialDialog.Builder {

    private ColorPicker colorPickerView;

    public ColorPickerDialog(Context context, int initialColor, final OnColorSelectedListener onColorSelectedListener) {
        super(context);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        colorPickerView = new ColorPicker(context);
        colorPickerView.setColor(initialColor);

        relativeLayout.addView(colorPickerView, layoutParams);
        backgroundColor(Color.WHITE);

        title(R.string.chooseColor);
        titleColor(ContextCompat.getColor(context, R.color.colorPrimary));

        positiveColor(ContextCompat.getColor(context, R.color.positiveButton));
        positiveText(android.R.string.ok);
        onPositive(new MaterialDialog.SingleButtonCallback() {
            public void onClick(MaterialDialog md, DialogAction da) {
                int selectedColor = colorPickerView.getColor();
                onColorSelectedListener.onColorSelected(selectedColor);
            }
        });
        neutralColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        neutralText(android.R.string.cancel);
        onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                materialDialog.dismiss();
            }
        });

        customView(relativeLayout, false);

    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

}