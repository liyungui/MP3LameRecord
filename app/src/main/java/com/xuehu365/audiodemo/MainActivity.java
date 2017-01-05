package com.xuehu365.audiodemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private MP3Recorder mp3Recorder;
    private String outputFilePath;
    private String fileName;
    private final int FILE_SELECT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outputFilePath = Environment.getExternalStorageDirectory().getPath() + "/test.amr";
        fileName = outputFilePath;
        Log.e("lyg", outputFilePath);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                Toast.makeText(this, "record amr", Toast.LENGTH_SHORT).show();
                start();
                break;
            case R.id.stop:
                Toast.makeText(this, "stop record amr", Toast.LENGTH_SHORT).show();
                stop();
                break;
            case R.id.startMp3:
                Toast.makeText(this, "record mp3", Toast.LENGTH_SHORT).show();
                startMp3();
                break;
            case R.id.stopMp3:
                Toast.makeText(this, "stop record mp3", Toast.LENGTH_SHORT).show();
                stopMp3();
                break;
            case R.id.play:
                Toast.makeText(this, "play", Toast.LENGTH_SHORT).show();
                showFileChooser();
                break;
            case R.id.stopPlay:
                Toast.makeText(this, "stop play", Toast.LENGTH_SHORT).show();
                stopPlay();
                break;
        }
    }

    private void stopMp3() {
        if (null != mp3Recorder) {
            mp3Recorder.stop();
        }
    }

    private void startMp3() {
        if (mp3Recorder == null) {
            mp3Recorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(), "test.mp3"));
        }
        try {
            mp3Recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);//Android默认就是amr
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(1);
        mRecorder.setOutputFile(outputFilePath);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder.start();
    }

    private void stop() {
        if (null != mRecorder) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }


    public void play() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopPlay() {
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getPhotoPathFromContentUri(this, uri);
                    Log.d("lyg", path);
                    fileName = path;
                    play();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
