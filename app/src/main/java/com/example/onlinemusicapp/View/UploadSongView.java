package com.example.onlinemusicapp.View;

import android.net.Uri;
import android.view.View;

public interface UploadSongView {
    String getFileName(Uri uri);
    String getFileExtension(Uri uri);
}
