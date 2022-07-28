package com.example.onlinemusicapp.Presenter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.View.UploadSongView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadSongPresenter {
    private UploadSongView uploadSongView;

    public UploadSongPresenter(UploadSongView uploadSongView) {
        this.uploadSongView = uploadSongView;
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
