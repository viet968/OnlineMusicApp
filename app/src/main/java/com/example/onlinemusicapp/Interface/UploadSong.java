package com.example.onlinemusicapp.Interface;

import android.net.Uri;

public interface UploadSong {
    String getFileName(Uri uri);
    String getFileExtension(Uri uri);
}
