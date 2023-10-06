package edu.wkd.userappbanghangonline.ultil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import edu.wkd.userappbanghangonline.databinding.LayoutLoadingBinding;

public class ProgressDialogLoading extends Dialog {
    private LayoutLoadingBinding binding;
    public ProgressDialogLoading(@NonNull Context context) {
        super(context);
        configProgressDialog();
        binding = LayoutLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void configProgressDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
    }
}
