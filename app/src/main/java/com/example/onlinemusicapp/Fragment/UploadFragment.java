package com.example.onlinemusicapp.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.onlinemusicapp.Activity.LoginActivity;
import com.example.onlinemusicapp.Adapter.AlbumSpinnerAdapter;
import com.example.onlinemusicapp.Model.Album;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.Implements.UploadSongImpl;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.Interface.UploadSong;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;


public class UploadFragment extends Fragment implements UploadSong {
    private TextView txtSongFileName,txtSongNameUploading,txtUploadPercent,dialogTxtImageFileName;
    private LinearLayout progressLayout,listProgressLayout;
    private EditText edtSongName,edtSingerName;
    private View uploadView;
    private Button btUpload, btNewAlbum, btComplete,btCancel, btChooseSong;
    private ScrollView scrProgress;
    private Dialog addSongDialog;
    //Select album adapter
    private AlbumSpinnerAdapter albumSpinnerAdapter;
    private List<Album> albumList;
    private AutoCompleteTextView spinnerAlbumList;
    private Uri audioUri,albumImageUri,songImageUri;
    private StorageTask mUploadSongTask,mUploadAlbumTask;
    private String albumName,songName,singerName,albumImageName;

    MediaMetadataRetriever mediaMetadataRetriever;// dùng để lấy hình ảnh, ca sĩ, tác giả của bài hát
    byte[] art;
    private UploadSongImpl ulPresenter;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    // for only a permission
    //hỏi cấp quyền truy cập file( cho file audio)
    private ActivityResultLauncher<String> audioPermissionActivityLauncher ;

    //image
    private ActivityResultLauncher<String> imagePermissionActivityLauncher ;
    // truy cập filebase
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private ProgressBar uploadProgress;
    Animation progressLayoutAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Configuration config = getResources().getConfiguration();
        // Inflate the layout for this fragment
        if (config.smallestScreenWidthDp >= 400) {
            uploadView = inflater.inflate(R.layout.fragment_upload_4xx, container, false);
        } else {
            uploadView = inflater.inflate(R.layout.fragment_upload, container, false);
        }


        // anh xa
        anhXaViewFragment();

        //open choose file intent
        intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            Intent data = result.getData();
                            audioUri = data.getData();
                            mediaMetadataRetriever.setDataSource(getContext(),audioUri);
                            art =  mediaMetadataRetriever.getEmbeddedPicture();
                            
                            try {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                String path = MediaStore.Images.Media.
                                        insertImage(getContext().getContentResolver(), bitmap, "Title", null);
                                songImageUri = Uri.parse(path);
                                edtSongName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                                edtSingerName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

                            }catch (Exception e){
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_song_image);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                String path = MediaStore.Images.Media.
                                        insertImage(getContext().getContentResolver(), bitmap, "Title", null);
                                songImageUri = Uri.parse(path);
                                Toast.makeText(getContext(), "Have error when get song image!!!!", Toast.LENGTH_SHORT).show();
                            }
                            txtSongFileName.setText(getFileName(audioUri));
                        }
                    }
                });

        audioPermissionActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            openAudioFiles();
                        }else{
                            Toast.makeText(getContext(), "Permission DENIED!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imagePermissionActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            selectImageFromGallery();
                        }else{
                            Toast.makeText(getContext(), "Permission DENIED!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // add data to album list
        getAlbumtoList();
        // album spinner adapter
        albumSpinnerAdapter = new AlbumSpinnerAdapter(requireContext(), R.layout.item_spinner_selected,albumList);

        // show dialog event
        btUpload.setOnClickListener(view -> {
            showAddSongDialog();
        });

        return uploadView;
    }

    private void anhXaViewFragment(){
        btUpload = (Button) uploadView.findViewById(R.id.btLUploadSongs);
        listProgressLayout = (LinearLayout) uploadView.findViewById(R.id.listProgressLayout);
        scrProgress = (ScrollView) uploadView.findViewById(R.id.scrProgress);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        databaseReference = LoginActivity.databaseReference;
        storageReference = LoginActivity.storageReference;
        albumList = new ArrayList<>();
        ulPresenter = new UploadSongImpl(this);

        //anh xa Dialog
        addSongDialog = new Dialog(getContext());
        addSongDialog.setContentView(R.layout.add_song_dialog);
        addSongDialog.setCanceledOnTouchOutside(false);
        addSongDialog.getWindow()
                .setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);
        // animation for dialog
        addSongDialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;

        //anh xa cac view cua dialog
        edtSongName = (EditText) addSongDialog.findViewById(R.id.dialogEdtNameSong);
        edtSingerName = (EditText) addSongDialog.findViewById(R.id.dialogEdtSingerName);
        btNewAlbum = (Button) addSongDialog.findViewById(R.id.dialogBtCreateNewAlbum);
        btComplete = (Button) addSongDialog.findViewById(R.id.dialogBtComplete);
        btCancel = (Button) addSongDialog.findViewById(R.id.dialogBtCancel);
        btChooseSong = (Button) addSongDialog.findViewById(R.id.dialogBtChooseSongFile);
        txtSongFileName = (TextView) addSongDialog.findViewById(R.id.dialogTxtSongFileName) ;
    }

    // dùng để get data từ db vào trong albums list
    private void getAlbumtoList(){
       databaseReference.child("albums").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               snapshot.getChildren().forEach(cSnap->{
                   Album getAlbum = cSnap.getValue(Album.class);
                   addAlbumtoList(getAlbum);
                       });
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }
    // dùng để thêm album mới vào trong albums list
    private void addAlbumtoList(Album album){
        if(!albumList.contains(album)) albumSpinnerAdapter.add(album);

    }

    private void showAddSongDialog(){
        edtSongName.setText("");
        edtSongName.setText("");
        txtSongFileName.setText("");
        //ánh xạ
        spinnerAlbumList =  addSongDialog.findViewById(R.id.dialogSpinnerAlbum);
        spinnerAlbumList.setInputType(0);
        //set adapter for spinner
        spinnerAlbumList.setAdapter(albumSpinnerAdapter);

        //new album layout_ánh xạ
        LinearLayout newAlbumLayout = addSongDialog.findViewById(R.id.dialogNewAlbumLayout);

        EditText dialogEditNewAlbumName = newAlbumLayout.findViewById(R.id.dialogEdtNewAlbumName);
        Button btFinishNewAlbum = newAlbumLayout.findViewById(R.id.dialogBtFinishNewAlbum);
        Button btChooseAlbumImage = newAlbumLayout.findViewById(R.id.dialogBtChooseAlbumImage);
        dialogTxtImageFileName = (TextView) newAlbumLayout.findViewById(R.id.dialogTxtImageFileName);

        //show dialog
        addSongDialog.show();

        // event for spinner_chọn album
        spinnerAlbumList.setOnItemClickListener((adapterView,  view,  position,  l) ->{
              Album selectedItem = (Album) adapterView.getItemAtPosition(position);
                spinnerAlbumList.setText(selectedItem.getName(),false);
            Toast.makeText(getContext(), selectedItem.getName(), Toast.LENGTH_SHORT).show();
            albumName = selectedItem.getName();
                });


        //choose song file event
        btChooseSong.setOnClickListener(view->{
            audioPermissionActivityLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        });

        // create new album event
        btNewAlbum.setOnClickListener(view->{

            if(newAlbumLayout.getVisibility() == View.VISIBLE){
                //when showing up
                btNewAlbum.setText("New Album");
                btComplete.setEnabled(true);
                newAlbumLayout.setVisibility(View.GONE);
            }else{
                //when not show
                Animation showNewAlbumAnim = AnimationUtils.loadAnimation(getContext(), R.anim.show_new_album_layout_animation);
                newAlbumLayout.setAnimation(showNewAlbumAnim);
                newAlbumLayout.setVisibility(View.VISIBLE);
                btComplete.setEnabled(false);
                btNewAlbum.setText("Cancel");

            }
        });

        //choose album image
        btChooseAlbumImage.setOnClickListener(view ->{
            imagePermissionActivityLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        });

        //finish new album event
        btFinishNewAlbum.setOnClickListener(view ->{
        String newAlbumName = dialogEditNewAlbumName.getText().toString().trim();
        albumImageName = dialogTxtImageFileName.getText().toString().trim();
            if(!newAlbumName.equals("") && !albumImageName.equals("")){

                Album newAlbum = new Album(newAlbumName, albumImageUri.toString());
                if(albumList.contains(newAlbum)){
                    //album da ton tai
                    Snackbar.make(getContext(),view,"Album has been exist",Snackbar.LENGTH_SHORT).show();
                }else {
                    // thêm album vào d/s
                    getAlbumtoList();
                    //upload
                    uploadAlbumToDB(newAlbum);
                    newAlbumLayout.setVisibility(View.GONE);//ẩn new album layout
                    btNewAlbum.setText("New Album");
                    dialogEditNewAlbumName.setText("");
                    dialogTxtImageFileName.setText("");
                    btComplete.setEnabled(true);
                }
            }else {
                Snackbar.make(getContext(),view,"Album name or album image is empty",Snackbar.LENGTH_SHORT).show();
            }
        });

        //cancel dialog event
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongDialog.cancel();
            }
        });

        //upload song to db
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lấy tên bài hát và tên ca sĩ
                songName = edtSongName.getText().toString().trim();
                singerName = edtSingerName.getText().toString().trim();
                if(songName.equals("") || singerName.equals("")|| txtSongFileName.getText().toString().equals("")){
                    Snackbar.make(getContext(),view,"Song name or Singer name or file is empty",Snackbar.LENGTH_SHORT).show();
                }else {
                    checkSongExist(songName,singerName,view,addSongDialog);
                }
            }
        });

    }


    private int getScreenWidth(@NonNull Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    //chọn audio file
    private void openAudioFiles(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        i = Intent.createChooser(i,"Choose a audio file");
        intentActivityResultLauncher.launch(i);

    }
    // chọn ảnh
    private void selectImageFromGallery(){
        TedImagePicker.with(getContext())
                .start(uri -> {
                    dialogTxtImageFileName.setText(getFileName(uri));
                    albumImageUri = uri;
                });
    }


    @Override
    public String getFileName(Uri uri){
        Cursor cursor = getContext().getContentResolver().query(uri,null,null,null,null);
        return ulPresenter.getFileName(uri,cursor);
    }

    @Override
    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        return ulPresenter.getFileExtension(uri,contentResolver);
    }


    public void upLoadSongToDB(String songName,String singerName) {
       if(txtSongFileName.equals("No Such File Selected")){
           Toast.makeText(getContext(), "Please choose an song", Toast.LENGTH_SHORT).show();
       }else{
           uploadSongFile(songName, singerName);
       }
    }

    private void uploadSongFile(String songName,String singerName){
        if(audioUri!=null){
            Toast.makeText(getContext(), "Uploads please waiting!!!", Toast.LENGTH_SHORT).show();
            // khởi tạo layout
            progressLayout = createProgressingLayout(songName);
            //scrollview sẽ thêm layout vừa khởi tạo vào
            listProgressLayout.addView(progressLayout,0);
            //upload hình
            StorageReference uploadSongImageStorageRef =
                    storageReference.child("SongImages").child(songName).child(songName+"Image"+"."+getFileExtension(songImageUri));
            // upload nhạc vào filebase storage
            StorageReference uploadStorageRef =
                    storageReference.child("SongLists").child(albumName).child(songName+"."+getFileExtension(audioUri));

             uploadStorageRef.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri songUri) {
                            uploadSongImageStorageRef.putFile(songImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    uploadSongImageStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        //upload
                                        public void onSuccess(Uri songImageUri) {
                                            SongDB uploadSong = new SongDB(songName, singerName,
                                                    albumName, songUri.toString(),songImageUri.toString());
                                            databaseReference.child("albums").child(albumName).child("song list").child(songName).setValue(uploadSong);
                                            txtUploadPercent.setText("Done");
                                        }
                                    });
                                }
                            });

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @SuppressLint("NewApi")
                 @Override
                 // hiển thị tiến trình upload
                 public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                     double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                     uploadProgress.setProgress((int)progress);
                     txtUploadPercent.setText((int)progress+"%");


                 }
             });;

        }
    }
    //kiểm tra bài nhạc đó có tồn tại trong cái album đc chọn ko
    public void checkSongExist(String songName,String singerName,View view,Dialog dialog){

        databaseReference.child("albums").child(albumName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("song list").hasChild(songName)){
                    Snackbar.make(getContext(),view,"Song already exist",Snackbar.LENGTH_SHORT).show();
                }else{
                    upLoadSongToDB(songName,singerName);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // thêm album
    public void uploadAlbumToDB(Album albumUpload){
        if(albumImageUri!=null){
            StorageReference uploadStorageRef =
                    storageReference.child("Album Images").child(albumUpload.getName()).
                            child(albumImageName);

            uploadStorageRef.putFile(albumImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            albumUpload.setImageUri(uri.toString());
                            databaseReference.child("albums").child(albumUpload.getName()).setValue(albumUpload);
                        }
                    });

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    // tạo layout upload
    public LinearLayout createProgressingLayout(String songName){
        //new animation
        progressLayoutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.show_progress_layout_anim);

        LinearLayout.LayoutParams paramsParentLayout = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        paramsParentLayout.setMargins(0,10,0,10);

        LinearLayout multiProgressLayout =
                (LinearLayout) getLayoutInflater().inflate(R.layout.custom_in_progress_layout,null);
        multiProgressLayout.setAnimation(progressLayoutAnim);
        multiProgressLayout.setLayoutParams(paramsParentLayout);
        // ánh xạ
        uploadProgress = (ProgressBar) multiProgressLayout.findViewById(R.id.progressBarUploading);
        txtSongNameUploading = (TextView) multiProgressLayout.findViewById(R.id.txtSongNameUploading);
        txtUploadPercent = (TextView) multiProgressLayout.findViewById(R.id.txtUploadPercent);
//        progressLayout = (LinearLayout) multiProgressLayout.findViewById(R.id.progressing_layout);

        //set process data
        txtSongNameUploading.setText(songName+".mp3");
        txtUploadPercent.setText(0+"%");

        return multiProgressLayout;
    }



}