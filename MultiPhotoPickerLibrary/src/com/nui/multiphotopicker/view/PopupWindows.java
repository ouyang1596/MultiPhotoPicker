package com.nui.multiphotopicker.view;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.nui.multiphotopicker.R;
import com.nui.multiphotopicker.model.ImageItem;
import com.nui.multiphotopicker.util.CustomConstants;
import com.nui.multiphotopicker.util.IntentConstants;

public class PopupWindows extends PopupWindow {
	private Context mContext;
	public static List<ImageItem> mDataList;
	private static final int TAKE_PICTURE = 1000;
	private static final int ALBUM = 2000;

	public PopupWindows(Context context, List<ImageItem> datalist, View parent) {
		mContext = context;
		mDataList = datalist;
		View view = View.inflate(mContext, R.layout.item_popupwindow, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				takePhoto();
				dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ImageChooseActivity.class);
				intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize());
				Activity activity = (Activity) mContext;
				activity.startActivityForResult(intent, ALBUM);
				dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private String path = "";

	public void takePhoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File vFile = new File(Environment.getExternalStorageDirectory() + "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		path = vFile.getPath();
		Uri cameraUri = Uri.fromFile(vFile);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		Activity activity = (Activity) mContext;
		activity.startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	private int getAvailableSize() {
		int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
		if (availSize >= 0) {
			return availSize;
		}
		return 0;
	}
}
