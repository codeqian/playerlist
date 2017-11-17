package net.codepig.playerlist.adapers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import net.codepig.playerlist.R;
import net.codepig.playerlist.beans.VideoInfo;
import net.codepig.playerlist.deviceInfo;

import java.util.List;

/**
 * 视频单元页面
 * Created by QZD on 2017/11/13.
 */

public class PlayerAdapter extends BaseAdapter{
    private Context _context;
    private Activity mainActivity;
    private List<VideoInfo> myVideoData;
    private LayoutInflater inflater;
    private ViewHolder hodler = null;
    private PLMediaPlayer mPlayer=null;
//    private PLVideoTextureView myVideoView;
    private int _id;
    private String _name;
    private String _url="";

    private final String TAG="LOGCAT";

    public PlayerAdapter(Context context, List<VideoInfo> data) {
        super();
        _context = context;
        mainActivity=(Activity) context;
        myVideoData = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int postion, View convertView, ViewGroup parent) {
        hodler = new ViewHolder();
        convertView = inflater.inflate(R.layout.player_adapter_l, null);
        hodler.videoName_t = convertView.findViewById(R.id.videoName_t);
        hodler.videoTable = convertView.findViewById(R.id.videoTable);
        hodler.myVideoView = convertView.findViewById(R.id.myVideoView);
        convertView.setTag(hodler);

        hodler.videoTable.getLayoutParams().width= deviceInfo._screenWidth;
        hodler.videoTable.getLayoutParams().height=deviceInfo._screenHeight;
        Log.d(TAG,"screenSize:"+deviceInfo._screenWidth+"-"+deviceInfo._screenHeight);

        VideoInfo _vInfo=myVideoData.get(postion);
        _name=_vInfo.get_name();
        hodler.videoName_t.setText(_vInfo.get_name());
        _id=_vInfo.get_id();
        _url=_vInfo.get_url();
        //视频的播放和停止
        if(!_url.equals("")) {
            if(_vInfo.get_playing()){
                setVideo(_url);
            }else{
                stopVideo();
            }
        }
        return convertView;
    }

    /**
     * 初始化播放器
     * @param url
     */
    private void setVideo(String url){
        int codec = mainActivity.getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        hodler.myVideoView.setAVOptions(options);
        hodler.myVideoView.setVideoPath(url);
        hodler.myVideoView.start();

        hodler.myVideoView.setOnErrorListener(new PLMediaPlayer.OnErrorListener(){
            @Override
            public boolean onError(PLMediaPlayer mp, int errorCode) {
                Log.d(TAG,"errorCode:"+errorCode);
                return true;
            }
        });
        hodler.myVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer mp) {
//                Log.d(TAG, "player onCompletion:"+videoDuration/1000+"-"+_curTime/1000);
                hodler.myVideoView.seekTo(0);
                hodler.myVideoView.start();
            }
        });
        hodler.myVideoView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer mediaPlayer, int percent) {
                Log.d(TAG, "player onPrepared");
                if(mPlayer==null){
                    mPlayer=mediaPlayer;
                }
                //播放
                if(hodler.myVideoView!=null){
                    hodler.myVideoView.start();
                }else{
                    Log.d(TAG, _name+"no myVideoView");
                }
            }
        });
        hodler.myVideoView.setOnBufferingUpdateListener(new PLMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
                try {
                    int _pec = hodler.myVideoView.getBufferPercentage();//百分比到99就停，进度条会留空
                    if (_pec == 99) {
                        _pec = 100;
                    }
                }catch (Exception e){
                    Log.d(TAG,"percentage error:"+e.toString());
                }
            }
        });
        hodler.myVideoView.setOnVideoSizeChangedListener(new PLMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
                Log.d(TAG,"VideoSize:"+width+"_"+height);
            }
        });
    }

    /**
     * 停止视频
     */
    public void stopVideo(){
        if(hodler.myVideoView!=null) {
            if (hodler.myVideoView.isPlaying()) {
                hodler.myVideoView.stopPlayback();
                hodler.myVideoView.releaseSurfactexture();
            }
        }
    }

    @Override
    public int getCount() {
        if (myVideoData != null) {
            return myVideoData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return myVideoData.get(position);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    public static class ViewHolder {
        public TextView videoName_t;
        public RelativeLayout videoTable;
        public PLVideoTextureView myVideoView;
    }
}
