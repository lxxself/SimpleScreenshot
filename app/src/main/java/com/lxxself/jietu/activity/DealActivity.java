package com.lxxself.jietu.activity;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.isseiaoki.simplecropview.CropImageView;
import com.lxxself.jietu.R;
import com.lxxself.jietu.model.MosaicView;

import java.io.File;
import java.io.FileOutputStream;


public class DealActivity extends AppCompatActivity implements View.OnClickListener,
        MenuItem.OnMenuItemClickListener {

    private static final String TAG = "DealActivity";
    private LinearLayout bottomLayout;
    private LinearLayout imgLayout;
    private MosaicView mosaicView;
    private LinearLayout lyCut;
    private LinearLayout lyEdit;
    private LinearLayout lyShare;
    private TextView tvCut;
    private TextView tvDraw;
    private TextView tvMosaic;
    private TextView tvShare;
    private ImageView mImageView;
    private MenuItem itemCancel;
    private MenuItem itemConfirm;
    private MenuItem itemSave;
    private MenuItem itemExit;
    private String path;
    private CropImageView cropImageView;
    private Context mContext;
    private boolean isShowSave = false;
    private boolean isShowMosaic = false;
    private TextView btncropfree;
    private TextView btncroporiginal;
    private TextView btncropsquare;
    private TextView btncrop169;
    private TextView btncrop43;
    private TextView btncrop32;
    private TextView btncrop54;
    private ImageButton ibClickRotate;
    private ImageButton ibClickCancel;
    private int ratioX = 0;
    private int ratioY = 0;

    static MosaicView.Effect currentEffect = MosaicView.Effect.GRID;
    static MosaicView.Mode currentMode = MosaicView.Mode.PATH;
    static int currentFuzzyNum = 8;
    static int currentFingerNum = 20;
    private int currentColor = Color.parseColor("#ff0000");
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        setTitle("简截");
        initialize();
        mContext = this;
//        mImageView = new ImageView(this);
//        imgLayout.addView(mImageView);
//
//        if (getIntent() != null) {
//            String path = getIntent().getStringExtra("path");
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            mImageView.setImageBitmap(bitmap);
//        }
        Log.d(TAG, "onCreate ");
        cropImageView = new CropImageView(this);
        imgLayout.addView(cropImageView);
//        if (getIntent() != null) {
//            path = getIntent().getStringExtra("path");
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            cropImageView.setImageBitmap(bitmap);
//            cropImageView.setCropEnabled(false);
//        }


        lyCut.setOnClickListener(this);
        lyEdit.setOnClickListener(this);
        lyShare.setOnClickListener(this);


    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart ");
        super.onRestart();

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart ");

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
//        cropImageView = null;
//        mosaicView.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume ");
        if (getIntent() != null) {
            path = getIntent().getStringExtra("path");
            Log.d(TAG, path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            cropImageView.setImageBitmap(bitmap);
            cropImageView.setCropEnabled(false);
//            if (path.endsWith("Root.png")) {
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                cropImageView.setImageBitmap(bitmap);
//                cropImageView.setCropEnabled(false);
//            } else {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Bitmap bitmap = BitmapFactory.decodeFile(path);
//                        cropImageView.setImageBitmap(bitmap);
//                        cropImageView.setCropEnabled(false);
//
//                    }
//                }, 1500);
//            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           showExitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        itemCancel = menu.add(0, 1, 1, "取消");
        itemConfirm = menu.add(0, 2, 2, "确定");
        itemSave = menu.add(0, 3, 3, "保存");
        itemExit = menu.add(0, 4, 4, "退出");
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemConfirm.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemExit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        showSave(true);
        itemCancel.setOnMenuItemClickListener(this);
        itemConfirm.setOnMenuItemClickListener(this);
        itemSave.setOnMenuItemClickListener(this);
        itemExit.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        //在resume前执行，这样使每次跳转到该activity是都是最新的Intent
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "onNewIntent " + intent.getStringExtra("path"));
    }


    private void initialize() {
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        lyCut = (LinearLayout) findViewById(R.id.lyCut);
        lyEdit = (LinearLayout) findViewById(R.id.lyEdit);
        lyShare = (LinearLayout) findViewById(R.id.lyShare);
        mosaicView = (MosaicView) findViewById(R.id.mosaicLayout);
        imgLayout = (LinearLayout) findViewById(R.id.imgLayout);
        tvCut = (TextView) findViewById(R.id.tvCut);
        tvMosaic = (TextView) findViewById(R.id.tvMosaic);
        tvShare = (TextView) findViewById(R.id.tvShare);
    }

    @Override
    public void onClick(View v) {
        hideOther(v);
        switch (v.getId()) {
            case R.id.lyCut:
                if (isShowSave) {
                    showMosaic(false);
                    showSave(false);
                    showCutPopupWindows(v);
                    cropImageView.setCropEnabled(true);
                    cropImageView.setHandleShowMode(CropImageView.ShowMode.NOT_SHOW);
                    cropImageView.setGuideShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
                    cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
                    tvCut.setText("自由");
                } else {
                    showCutPopupWindows(v);
                }
                break;
            case R.id.lyEdit:
                if (isShowSave) {
                    showMosaic(true);
                    showSave(false);
                    showMosaicPopupWindows(v);
                    cropImageView.setCropEnabled(false);
                    mosaicView.setSrcImage(cropImageView.getImageBitmap());
                } else {
                    showMosaicPopupWindows(v);
                }
                break;
            case R.id.lyShare:
                showSave(false);
                sharePicture();
                break;
        }
    }

    private void sharePicture() {
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("1111", "111111111");
                saveImg();
                Intent intent = new Intent(Intent.ACTION_SEND);
                if (path == null || path.equals("")) {
                    intent.setType("text/plain"); // 纯文本
                } else {
                    File f = new File(path);
                    if (f != null && f.exists() && f.isFile()) {
                        intent.setType("image/jpg");
                        Uri u = Uri.fromFile(f);
                        intent.putExtra(Intent.EXTRA_STREAM, u);
                    }
                }
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "分享自简截");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));

            }
        });
        thread.start();
    }


    private void hideOther(View v) {
        lyCut.setVisibility(View.GONE);
        lyEdit.setVisibility(View.GONE);
        lyShare.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    private void NoHide() {
        lyCut.setVisibility(View.VISIBLE);
        lyEdit.setVisibility(View.VISIBLE);
        lyShare.setVisibility(View.VISIBLE);
    }
    private void showSave(boolean b) {
        itemCancel.setVisible(!b);
        itemConfirm.setVisible(!b);
        itemSave.setVisible(b);
        itemExit.setVisible(b);
        isShowSave = b;
    }

    private void showMosaic(boolean b) {
        mosaicView.setVisibility(b ? View.VISIBLE : View.GONE);
        imgLayout.setVisibility(b ? View.GONE : View.VISIBLE);
        isShowMosaic = b;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        NoHide();
        switch (item.getItemId()) {
            case 1:
                mosaicView.clear();
                if (isShowMosaic) {
                    showMosaic(false);
                } else {
                    cropImageView.setCropEnabled(false);
                    tvCut.setText("裁剪");
                }
                showSave(true);
                break;
            case 2:
                if (isShowMosaic) {
                    cropImageView.setImageBitmap(mosaicView.getBitmap());
                    mosaicView.clear();
                    showMosaic(false);
                } else {
                    cropImageView.setImageBitmap(cropImageView.getCroppedBitmap());
                    cropImageView.setCropEnabled(false);
                    tvCut.setText("裁剪");
                }
                showSave(true);
                break;
            case 3:
                showSaveDialog();
                break;
            case 4:
                showExitDialog();
                break;
        }
        return false;
    }

    private void showSaveDialog() {
        AlertDialog.Builder saveBuilder = new AlertDialog.Builder(this);
        saveBuilder.setMessage("图片保存至：" + path);
        saveBuilder.setTitle("保存提示");
        saveBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImg();
                finish();
            }
        });
        saveBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        saveBuilder.create().show();
    }

    private void showExitDialog() {
        AlertDialog.Builder exitBuilder = new AlertDialog.Builder(this);
        exitBuilder.setMessage("退出后是否删除位于：\n" + path+"\n的图片");
        exitBuilder.setTitle("退出提示");
        exitBuilder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteImg();
                finish();
            }
        });
        exitBuilder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        exitBuilder.create().show();

    }

    private void saveImg() {
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap bp = cropImageView.getImageBitmap();
                File imageFile = new File(path);
                FileOutputStream fos = null;
                File file = new File(path);
                try {
                    fos = new FileOutputStream(file);
                    if (fos != null) {
                        bp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.close();
                    }
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void DeleteImg() {
        File file = new File(path);
        file.delete();
    }
    private void showMosaicPopupWindows(View v) {

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.mosaic_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        final String[] spEffectList = getResources().getStringArray(R.array.mosaic_effect);
        final String[] spModeList = getResources().getStringArray(R.array.mosaic_mode);

        Spinner spEffect = (Spinner) contentView.findViewById(R.id.sp_effect);
        Spinner spMode = (Spinner) contentView.findViewById(R.id.sp_mode);
        TextView tvFinger = (TextView) contentView.findViewById(R.id.finger);
        TextView tvFuzzy = (TextView) contentView.findViewById(R.id.fuzzy);
        final TextView tvFingerNum = (TextView) contentView.findViewById(R.id.finger_num);
        final TextView tvFuzzyNum = (TextView) contentView.findViewById(R.id.fuzzy_num);
        ImageButton colorPicker = (ImageButton) contentView.findViewById(R.id.color_select);
        ImageButton cancel = (ImageButton) contentView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        colorPicker.setBackgroundColor(currentColor);
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPicker(v);
            }
        });
        if (currentEffect == MosaicView.Effect.GRID) {
            spEffect.setSelection(0);
        }else if (currentEffect == MosaicView.Effect.BLUR) {
            spEffect.setSelection(1);
        } else {
            spEffect.setSelection(2);
        }
        spEffect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.bright_foreground_inverse_material_light));    //设置颜色
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);   //设置居中
                switch (position) {
                    case 0:
                        mosaicView.setEffect(MosaicView.Effect.GRID);
                        currentEffect = MosaicView.Effect.GRID;
                        break;
                    case 1:
                        mosaicView.setEffect(MosaicView.Effect.BLUR);
                        currentEffect = MosaicView.Effect.BLUR;
                        break;
                    case 2:
                        mosaicView.setEffect(MosaicView.Effect.COLOR);
                        currentEffect = MosaicView.Effect.COLOR;
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (currentMode == MosaicView.Mode.PATH) {
            spMode.setSelection(0);
        } else {
            spMode.setSelection(1);
        }
        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.bright_foreground_inverse_material_light));    //设置颜色
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                switch (position) {
                    case 0:
                        mosaicView.setMode(MosaicView.Mode.PATH);
                        currentMode = MosaicView.Mode.PATH;
                        break;
                    case 1:
                        mosaicView.setMode(MosaicView.Mode.GRID);
                        currentMode = MosaicView.Mode.GRID;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvFingerNum.setText(currentFingerNum + "");
        tvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFingerSeekbarDialog(v, tvFingerNum);
            }
        });

        tvFuzzyNum.setText(currentFuzzyNum + "");
        tvFuzzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFuzzySeekbarDialog(v, tvFuzzyNum);
            }
        });
//        popupWindow.setTouchable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
            }

        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                android.R.drawable.screen_background_light));

//        popupWindow.showAsDropDown(v, 0, -(bottomLayout.getHeight()));
        popupWindow.showAtLocation(v,Gravity.BOTTOM,0,-100);
    }



    private void showColorPicker(final View view) {
        view.setBackgroundColor(currentColor);
        ColorPickerDialogBuilder
                .with(mContext)
                .setTitle("Choose color")
                .initialColor(currentColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        String s = "onColorSelected: 0x" + Integer.toHexString(selectedColor);
//                        Toast.makeText(DealActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("确定", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        view.setBackgroundColor(selectedColor);
                        currentColor = selectedColor;
                        mosaicView.setMosaicColor(selectedColor);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void showFuzzySeekbarDialog(View v, final TextView NumView) {
        final Dialog seekbarDialog = new Dialog(DealActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) DealActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View seekView = layoutInflater.inflate(R.layout.seekbar_dialog, (ViewGroup) findViewById(R.id.seekbar_dialog));
        seekbarDialog.setTitle("模糊程度  " + currentFuzzyNum);
        seekbarDialog.setContentView(seekView);
        SeekBar seekBar = (SeekBar) seekView.findViewById(R.id.sb_finger);
        seekBar.setProgress(Integer.parseInt(NumView.getText().toString()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarDialog.setTitle("模糊程度  "+progress);
                mosaicView.setGridWidth(progress);
                mosaicView.setRadius(progress);
                NumView.setText(progress + "");
                currentFuzzyNum = progress;
                Log.d(TAG, "onProgressChanged "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbarDialog.show();
    }
    private void showFingerSeekbarDialog(View view, final TextView NumView) {
        NumView.setText(currentFingerNum+"");
        final Dialog seekbarDialog = new Dialog(DealActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) DealActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View seekView = layoutInflater.inflate(R.layout.seekbar_dialog, (ViewGroup) findViewById(R.id.seekbar_dialog));
        seekbarDialog.setTitle("手触大小  "+currentFingerNum);
        seekbarDialog.setContentView(seekView);
        SeekBar seekBar = (SeekBar) seekView.findViewById(R.id.sb_finger);
        seekBar.setProgress(Integer.parseInt(NumView.getText().toString()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarDialog.setTitle("手触大小  " + progress);
//                mosaicView.setStrokeWidth(progress);
                mosaicView.setPathWidth(progress);
                NumView.setText(progress + "");
                currentFingerNum = progress;
                Log.d(TAG, "onProgressChanged "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbarDialog.show();
    }

    private void showCutPopupWindows(View v) {
        Log.d(TAG, "showCutPopupWindows ");
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.crop_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        initializePopupWindow(contentView);

        ibClickCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

//        popupWindow.setTouchable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                android.R.drawable.screen_background_light));
//        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
//        popupWindow.showAsDropDown(v,0,-(bottomLayout.getHeight()));
        popupWindow.showAtLocation(v, Gravity.BOTTOM,0,-100);
    }
    private void initializePopupWindow(View view) {

        btncropfree = (TextView) view.findViewById(R.id.btn_crop_free);
        btncroporiginal = (TextView) view.findViewById(R.id.btn_crop_original);
        btncropsquare = (TextView) view.findViewById(R.id.btn_crop_square);
        btncrop169 = (TextView) view.findViewById(R.id.btn_crop_16_9);
        btncrop43 = (TextView) view.findViewById(R.id.btn_crop_4_3);
        btncrop32 = (TextView) view.findViewById(R.id.btn_crop_3_2);
        btncrop54 = (TextView) view.findViewById(R.id.btn_crop_5_4);
        ibClickRotate = (ImageButton) view.findViewById(R.id.ibClickRotate);
        ibClickCancel = (ImageButton) view.findViewById(R.id.ibClickCancel);

        btncropfree.setOnClickListener(cropListener);
        btncroporiginal.setOnClickListener(cropListener);
        btncropsquare.setOnClickListener(cropListener);
        btncrop169.setOnClickListener(cropListener);
        btncrop43.setOnClickListener(cropListener);
        btncrop32.setOnClickListener(cropListener);
        btncrop54.setOnClickListener(cropListener);
        ibClickRotate.setOnClickListener(cropListener);
    }

    View.OnClickListener cropListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_crop_free:
                    cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
                    ratioX = ratioY = 0;
                    break;
                case R.id.btn_crop_original:
                    cropImageView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);
                    ratioX = cropImageView.getWidth();
                    ratioY = cropImageView.getHeight();
                    break;
                case R.id.btn_crop_square:
                    cropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
                    ratioX = ratioY = 1;
                    break;
                case R.id.btn_crop_16_9:
                    cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    ratioX = 16;
                    ratioY = 9;
                    break;
                case R.id.btn_crop_4_3:
                    cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    ratioX = 4;
                    ratioY = 3;
                    break;
                case R.id.btn_crop_3_2:
                    cropImageView.setCustomRatio(3, 2);
                    ratioX = 3;
                    ratioY = 2;
                    break;
                case R.id.btn_crop_5_4:
                    cropImageView.setCustomRatio(5, 4);
                    ratioX = 5;
                    ratioY = 4;
                    break;
                case R.id.ibClickRotate:
                    if (ratioX != 0) {
                        int temp = ratioX;
                        ratioX = ratioY;
                        ratioY = temp;
                        cropImageView.setCustomRatio(ratioX, ratioY);
                    }
                    break;
            }
            if (v.getId() == R.id.btn_crop_free) {
                tvCut.setText("自由");
            }else if (v.getId() == R.id.btn_crop_original) {
                tvCut.setText("原始");
            } else if (v.getId() != R.id.ibClickRotate && v.getId() != R.id.ibClickCancel) {
                tvCut.setText(ratioX + ":" + ratioY);
            }
        }
    };
}
