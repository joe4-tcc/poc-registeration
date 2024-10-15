package com.tcc.poc.feature.ui.fragments;

import android.graphics.Bitmap;

public class SavedData {
    private static Bitmap faceResultBitmap;
    public static String faceBase64;
    public static String faceTransactionId;
    public static String fingerTransactionId;
    public static String ocrTransactionId;

    public static Bitmap getFaceResultBitmap() {
        return faceResultBitmap;
    }

    public static void setFaceResultBitmap(Bitmap bitmap) {
        faceResultBitmap = bitmap;
    }



}
