package net.codepig.playerlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

/**
 * 测试用页面
 * Created by QZD on 2017/11/16.
 */

public class PlayerTest extends AppCompatActivity {
    private PLMediaPlayer mPlayer=null;
    private PLVideoTextureView myVideoView;

    private final String TAG="LOGCAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_test_l);
        myVideoView =(PLVideoTextureView) findViewById(R.id.myVideoView);
        setVideo("http://media.w3.org/2010/05/video/movie_300.mp4");
    }

    /**
     * 初始化播放器
     * @param url
     */
    private void setVideo(String url){
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        myVideoView.setAVOptions(options);
        myVideoView.setVideoPath(url);
        myVideoView.setOnErrorListener(new PLMediaPlayer.OnErrorListener(){
            @Override
            public boolean onError(PLMediaPlayer mp, int errorCode) {
                Log.d(TAG,"errorCode:"+errorCode);
                return true;
            }
        });
        myVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer mp) {
//                Log.d(TAG, "player onCompletion:"+videoDuration/1000+"-"+_curTime/1000);
                myVideoView.seekTo(0);
                myVideoView.start();
            }
        });
        myVideoView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer mediaPlayer, int percent) {
                Log.d(TAG, "player onPrepared on test page");
                if(mPlayer==null){
                    mPlayer=mediaPlayer;
                }
                //播放
                if(myVideoView!=null){
                    myVideoView.start();
                }
            }
        });
        myVideoView.setOnBufferingUpdateListener(new PLMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
                try {
                    int _pec = myVideoView.getBufferPercentage();//百分比到99就停，进度条会留空
                    Log.d(TAG, "pec:" + _pec);
                    if (_pec == 99) {
                        _pec = 100;
                    }
                }catch (Exception e){
                    Log.d(TAG,"percentage error:"+e.toString());
                }
            }
        });
        myVideoView.setOnVideoSizeChangedListener(new PLMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
                Log.d(TAG,"VideoSize:"+width+"_"+height);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOGCAT", "player onDestroy");
        myVideoView.stopPlayback();
        myVideoView.releaseSurfactexture();
    }
}
