package com.ottokonek.ottokasir.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static Bitmap resizeImage(Bitmap bitmap, int newSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if (width > height) {
            newWidth = newSize;
            newHeight = (newSize * height) / width;
        } else if (width < height) {
            newHeight = newSize;
            newWidth = (newSize * width) / height;
        } else if (width == height) {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);

        return resizedBitmap;
    }

    public static String bitmapToBase64(Bitmap bmp, Bitmap.CompressFormat format, int compression) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(format, compression, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
            stream = null;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP);
        }
    }

    public static Bitmap Base64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64.replace("data:image/jpeg;base64,", ""), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Uri rotateImage(Bitmap source, float angle, Context mContext) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap temp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        temp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), temp, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap rotateImage(Bitmap source, float angle, Context mContext, Bitmap.CompressFormat format, int compressionLevel) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap temp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        temp.compress(format, compressionLevel, bytes);
        return temp;
    }


}
