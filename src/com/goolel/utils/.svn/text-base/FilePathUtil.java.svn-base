package com.goolel.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

public class FilePathUtil {
	public String getRealPathFromURI(Context context, Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			CursorLoader loader = new CursorLoader(context,
					contentUri, proj, null, null, null);
			Cursor cursor = loader.loadInBackground();
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String result = cursor.getString(column_index);
			cursor.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
