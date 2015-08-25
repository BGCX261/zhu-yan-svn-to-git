package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.io.File;
import java.util.StringTokenizer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.util.FileUtil;
import com.cc.software.calendar.util.NoteManager;

public class RecordActivity extends Activity implements OnClickListener {

    private ImageView btnStart, btnStop, btnPlay, btnSecondOne, btnSecondTen, btnMiniteOne, btnMiniteTen;
    //private File dir;
    private MediaRecorder recorder;
    private MediaPlayer mediaPlayer;
    private File myRecAudioFile;
    private SeekBar seekBar;
    private boolean isPlaying = false, isRecording = false;

    private static final int Refresh = 1;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.record);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

        btnStart = (ImageView) findViewById(R.id.btn_record);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnStop = (ImageView) findViewById(R.id.btn_stop);

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnPlay.setEnabled(false);

        btnSecondOne = (ImageView) findViewById(R.id.second_one);
        btnSecondTen = (ImageView) findViewById(R.id.second_ten);
        btnMiniteOne = (ImageView) findViewById(R.id.minite_one);
        btnMiniteTen = (ImageView) findViewById(R.id.minite_ten);

        /*File defaultDir = Environment.getExternalStorageDirectory();
        //        File defaultDir = getDir("multi_function_calendar", Activity.MODE_PRIVATE);
        String path = defaultDir.getAbsolutePath() + File.separator + "V" + File.separator;//创建文件夹存放视频

        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }*/
        recorder = new MediaRecorder();

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onClick(View v) {
        seekBar.setVisibility(View.GONE);
        switch (v.getId()) {
        case R.id.btn_play:
            btnStart.setEnabled(false);
            btnStop.setEnabled(false);
            btnPlay.setEnabled(true);
            seekBar.setVisibility(View.VISIBLE);
            playRecorder();
            break;
        case R.id.btn_record:
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            btnPlay.setEnabled(false);

            recorder();
            break;
        case R.id.btn_stop:
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            btnPlay.setEnabled(true);

            stopRecorder();
            break;
        default:
            break;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (isPlaying || isRecording) {
                i++;
                updateTime(i);
            }
        };
    };

    private void updateTime(int i) {
        if (i < 10) {
            btnSecondOne.setImageResource(getDrawableResId(i));
            btnSecondTen.setImageResource(getDrawableResId(0));
            btnMiniteOne.setImageResource(getDrawableResId(0));
            btnMiniteTen.setImageResource(getDrawableResId(0));
        } else if (i < 60) {
            btnSecondOne.setImageResource(getDrawableResId(i % 10));
            btnSecondTen.setImageResource(getDrawableResId(i / 10));
            btnMiniteOne.setImageResource(getDrawableResId(0));
            btnMiniteTen.setImageResource(getDrawableResId(0));
        } else if (i < 600) {
            btnSecondOne.setImageResource(getDrawableResId(i % 10));
            btnSecondTen.setImageResource(getDrawableResId(i % 60 / 10));
            btnMiniteOne.setImageResource(getDrawableResId(i / 60));
            btnMiniteTen.setImageResource(getDrawableResId(0));
        } else {
            btnSecondOne.setImageResource(getDrawableResId(i % 10));
            btnSecondTen.setImageResource(getDrawableResId(i % 60 / 10));
            btnMiniteOne.setImageResource(getDrawableResId(i / 60 % 10));
            btnMiniteTen.setImageResource(getDrawableResId(i / 600));
        }
    }

    private int getDrawableResId(int number) {
        int resId = -1;
        switch (number) {
        case 0:
            resId = R.drawable.n0;
            break;
        case 1:
            resId = R.drawable.n1;
            break;
        case 2:
            resId = R.drawable.n2;
            break;
        case 3:
            resId = R.drawable.n3;
            break;
        case 4:
            resId = R.drawable.n4;
            break;
        case 5:
            resId = R.drawable.n5;
            break;
        case 6:
            resId = R.drawable.n6;
            break;
        case 7:
            resId = R.drawable.n7;
            break;
        case 8:
            resId = R.drawable.n8;
            break;
        case 9:
            resId = R.drawable.n9;
            break;

        default:
            break;
        }
        return resId;
    }

    public void recorder() {
        try {
            isRecording = true;
            myRecAudioFile = File.createTempFile("video", ".amr", FileUtil.getFileDir(this, Constants.RECORD));//创建临时文件
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //录音源为麦克风
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//音频编码
            recorder.setMaxDuration(10000);//最大期限
            recorder.setOutputFile(myRecAudioFile.getAbsolutePath());//保存路径
            StringTokenizer tokenizer = new StringTokenizer(myRecAudioFile.getAbsolutePath(), "/");
            String title = "";
            while (tokenizer.hasMoreElements()) {
                title = (String) tokenizer.nextElement();
            }
            NoteManager.addNote(this, title, "", myRecAudioFile.getAbsolutePath(), Constants.RECORD,
                            DateUtil.getDateSimpleString());
            recorder.prepare();
            recorder.start();
            new TimeSync().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    public void playRecorder() {
        if (myRecAudioFile != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    i = 0;
                    mediaPlayer.reset();
                    isPlaying = false;
                }
                mediaPlayer.setDataSource(myRecAudioFile.getAbsolutePath());

                seekBar.setProgress(0);

                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;

                new aseekth().start();
                new TimeSync().start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.reset();
                    isPlaying = false;
                    btnStart.setEnabled(true);
                }
            });
        }

    }

    public void stopRecorder() {
        isRecording = false;
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.seekTo(seekBar.getProgress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class aseekth extends Thread {
        public void run() {
            try {
                seekBar.setMax(mediaPlayer.getDuration());
                while (isPlaying) {
                    if (seekBar.getProgress() < mediaPlayer.getCurrentPosition()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        sleep(100);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private class TimeSync extends Thread {
        @Override
        public void run() {
            i = 0;
            while (isPlaying || isRecording) {
                try {
                    handler.sendEmptyMessage(Refresh);
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
