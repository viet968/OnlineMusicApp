package com.example.onlinemusicapp.Implements;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import com.example.onlinemusicapp.Interface.UploadSong;

public class UploadSongImpl {
    private UploadSong uploadSong;

    public UploadSongImpl(UploadSong uploadSong) {
        this.uploadSong = uploadSong;
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri, Cursor cursor){
        String res = null;
        if(uri.getScheme().equals("content")){
//            Cursor cursor = getContext().getContentResolver().query(uri,null,null,null,null);
            try{
                if(cursor !=null && cursor.moveToFirst()){
                    res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }

            if(res ==  null){
                res = uri.getPath();
                int cutt = res.lastIndexOf('/');
                if(cutt!=-1){
                    res = res.substring(cutt + 1);
                }
            }

        }
        return res;
    }

    public String getFileExtension(Uri uri,ContentResolver contentResolver){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


}
