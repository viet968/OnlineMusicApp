package com.example.onlinemusicapp.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinemusicapp.Adapter.AlbumSpinnerAdapter;
import com.example.onlinemusicapp.Activity.LoginActivity;
import com.example.onlinemusicapp.Model.Album;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Presenter.UploadSongPresenter;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.View.UploadSongView;
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


public class UploadSongFragment extends Fragment implements UploadSongView {
    private TextView txtSongFileName,txtSongNameUploading,txtUploadPercent,dialogTxtImageFileName;
    private LinearLayout progressLayout,listProgressLayout;
    private EditText edtSongName,edtSingerName;
    private View uploadView;
    private Button btUpload;
    private ScrollView scrProgress;
    private AlbumSpinnerAdapter albumSpinnerAdapter;
    private List<Album> albumList;
    private Spinner spinnerAlbumList;
    private Uri audioUri,albumImageUri,songImageUri;
    private StorageTask mUploadSongTask,mUploadAlbumTask;
    private String albumName,songName,singerName,albumImageName;
    private  MediaMetadataRetriever mediaMetadataRetriever;
    byte[] art;
    private UploadSongPresenter ulPresenter;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    // for only a permission
    //audio
    private ActivityResultLauncher<String> audioPermissionActivityLauncher ;

    //image
    private ActivityResultLauncher<String> imagePermissionActivityLauncher ;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    private ProgressBar uploadProgress;
    Animation progressLayoutAnim;// = AnimationUtils.loadAnimation(getContext(),R.anim.show_progress_layout_anim);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        uploadView = inflater.inflate(R.layout.fragment_upload_song, container, false);
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
                            try {
                                art = mediaMetadataRetriever.getEmbeddedPicture();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                String path = MediaStore.Images.Media.
                                        insertImage(getContext().getContentResolver(), bitmap, "Title", null);
                                songImageUri = Uri.parse(path);
                            }catch (Exception e){

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
                        Toast.makeText(getContext(), result+"", Toast.LENGTH_SHORT).show();
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

        // addd data to album list
        getAlbumtoList();
        // album spinner adapter
        albumSpinnerAdapter = new AlbumSpinnerAdapter(getContext(),R.layout.item_spinner_selected,albumList);

        // show dialog event
        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddSongDialog();
            }
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
        ulPresenter = new UploadSongPresenter(this);


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
    // dùng để thêm album mới vào trong alnus list
    private void addAlbumtoList(Album album){
        if(!albumList.contains(album)) albumSpinnerAdapter.add(album);

    }

    private void showAddSongDialog(){
        Dialog addSongDialog = new Dialog(getContext());
        addSongDialog.setContentView(R.layout.add_song_dialog);
        addSongDialog.setCanceledOnTouchOutside(false);
        addSongDialog.getWindow()
                .setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);
        // animation for dialog
        addSongDialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;
        //anh xa cac view cua dialog
        edtSongName = (EditText) addSongDialog.findViewById(R.id.dialogEdtNameSong);
        edtSingerName = (EditText) addSongDialog.findViewById(R.id.dialogEdtSingerName);
        Button btNewAlbum = (Button) addSongDialog.findViewById(R.id.dialogBtCreateNewAlbum);
        Button btComplelte = (Button) addSongDialog.findViewById(R.id.dialogBtComplete);
        Button btCancel = (Button) addSongDialog.findViewById(R.id.dialogBtCancel);
        Button btChooseSong = (Button) addSongDialog.findViewById(R.id.dialogBtChooseSongFile);
        txtSongFileName = (TextView) addSongDialog.findViewById(R.id.dialogTxtSongFileName) ;
        spinnerAlbumList = (Spinner) addSongDialog.findViewById(R.id.dialogSpinnerAlbum);

        //set adapter for spinner
        spinnerAlbumList.setAdapter(albumSpinnerAdapter);


        //new album layout
        LinearLayout newAlbumLayout = addSongDialog.findViewById(R.id.dialogNewAlbumLayout);

        EditText dialogEditNewAlbumName = newAlbumLayout.findViewById(R.id.dialogEdtNewAlbumName);
        Button btFinishNewAlbum = newAlbumLayout.findViewById(R.id.dialogBtFinishNewAlbum);
        Button btChooseAlbumImage = newAlbumLayout.findViewById(R.id.dialogBtChooseAlbumImage);
        dialogTxtImageFileName = (TextView) newAlbumLayout.findViewById(R.id.dialogTxtImageFileName);

         // event for spinner
        spinnerAlbumList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Album getAlbum = albumList.get(i);
                albumName = getAlbum.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //choose song file event
        btChooseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // for only a permission
                audioPermissionActivityLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }
        });

        // create new album event
        btNewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(newAlbumLayout.getVisibility() == View.VISIBLE){
                    //when showing up
                    btNewAlbum.setText("New Album");
                    btComplelte.setEnabled(true);
                    newAlbumLayout.setVisibility(View.GONE);
                }else{
                    //when not show
                    Animation showNewAlbumAnim = AnimationUtils.loadAnimation(getContext(),R.anim.show_new_album_layout_animation);
                    newAlbumLayout.setAnimation(showNewAlbumAnim);
                    newAlbumLayout.setVisibility(View.VISIBLE);
                    btComplelte.setEnabled(false);
                    btNewAlbum.setText("Cancel");

                }
            }
        });

        //choose album image
        btChooseAlbumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePermissionActivityLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }
        });

        //finish new album event
        btFinishNewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAlbumName = dialogEditNewAlbumName.getText().toString().trim();
                if(!newAlbumName.equals("")){
                    albumImageName = dialogTxtImageFileName.getText().toString().trim();
                    Album newAlbum = new Album(newAlbumName,albumImageUri.toString());
//                    addAlbumtoList(newAlbum);
                    getAlbumtoList();
                    uploadAlbumToDB(newAlbum);
                    newAlbumLayout.setVisibility(View.GONE);
                    btNewAlbum.setText("New Album");
                    btComplelte.setEnabled(true);
                }else{
                    Snackbar.make(getContext(),view,"Album name is empty",Snackbar.LENGTH_SHORT).show();
                }

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
        btComplelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songName = edtSongName.getText().toString().trim();
                singerName = edtSingerName.getText().toString().trim();
                if(songName.equals("") || singerName.equals("")){
                    Snackbar.make(getContext(),view,"Song name or Singer name is empty",Snackbar.LENGTH_SHORT).show();
                }else {
                    addSongDialog.dismiss();
                    checkSongExist(songName,singerName,view,addSongDialog);

                }

            }
        });


        addSongDialog.show();

    }


    private int getScreenWidth(@NonNull Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    private void openAudioFiles(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        i = Intent.createChooser(i,"Choose a audio file");
        intentActivityResultLauncher.launch(i);

    }

    private void selectImageFromGallery(){
        TedImagePicker.with(getContext())
                .start(uri -> {
                    dialogTxtImageFileName.setText(getFileName(uri));
                    albumImageUri = uri;
                });
    }


    @Override
    @SuppressLint("Range")
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
            progressLayout = createProgressingLayout(songName);
            listProgressLayout.addView(progressLayout,0);

            StorageReference uploadStorageRef =
                    storageReference.child("SongLists").child(albumName).child(songName+"."+getFileExtension(audioUri));

            StorageReference uploadSongImageStorageRef =
                    storageReference.child("SongImages").child(songName).child(songName+"Image"+"."+getFileExtension(songImageUri));

            mUploadSongTask = uploadStorageRef.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //cach 1
                    uploadStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri songUri) {

//                                databaseReference.child("albums").child(albumName).child("album image").setValue("albumArt");
                            uploadSongImageStorageRef.putFile(songImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    uploadSongImageStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri songImageUri) {
                                            Song uploadSong = new Song(songName, singerName,
                                                    albumName, songUri.toString(),songImageUri.toString());
                                            databaseReference.child("albums").child(albumName).child("song list").child(songName).setValue(uploadSong);
                                        }
                                    });
                                }
                            });
                        }
                    });

                    // cach thu 2(bỏ "mUploadTask =" đi )
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while(!uriTask.isComplete());
//                    Uri url = uriTask.getResult();
//                    Song uploadSong = new Song(songName,singerName,
//                               albumName,url.toString());
//                databaseReference.child("albums").child(albumName).child(songName).setValue(uploadSong);
//                databaseReference.child("albums").child(albumName).child("album image").setValue("albumArt");

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    uploadProgress.setProgress((int)progress);
                    txtUploadPercent.setText((int)progress+"%");


                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    txtUploadPercent.setText("Done");

                }
            });

        }
    }

    public void checkSongExist(String songName,String singerName,View view,Dialog dialog){

        databaseReference.child("albums").child(albumName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(songName)){
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

    public void uploadAlbumToDB(Album albumUpload){
        if(albumImageUri!=null){
            StorageReference uploadStorageRef =
                    storageReference.child("Album Images").child(albumUpload.getName()).
                            child(albumImageName);

            mUploadAlbumTask = uploadStorageRef.putFile(albumImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
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

    public LinearLayout createProgressingLayout(String songName){
        //new animation
        progressLayoutAnim = AnimationUtils.loadAnimation(getContext(),R.anim.show_progress_layout_anim);

        LinearLayout.LayoutParams paramsParentLayout = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        paramsParentLayout.setMargins(0,10,0,10);

        LinearLayout multiProgressLayout =
                (LinearLayout) getLayoutInflater().inflate(R.layout.custom_in_progress_layout,null);
        multiProgressLayout.setAnimation(progressLayoutAnim);
        multiProgressLayout.setLayoutParams(paramsParentLayout);

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