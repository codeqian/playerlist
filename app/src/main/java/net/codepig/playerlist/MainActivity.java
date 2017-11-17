package net.codepig.playerlist;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import net.codepig.playerlist.adapers.PlayerAdapter;
import net.codepig.playerlist.beans.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView myVideoList;
    private PlayerAdapter playerAdapter;
    private List<VideoInfo> _infoList;
    private Button testButton;
    private int _listLength=5;
    private int _oldItem=0;//前一个活动元素
    private final String TAG="LOGCAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVideoList=(ListView)this.findViewById(R.id.myvideolist);
        testButton=(Button) this.findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplication(), PlayerTest.class);
                startActivity(intent);
            }
        });

        DisplayMetrics dm =getResources().getDisplayMetrics();
        deviceInfo._screenWidth = dm.widthPixels;
        deviceInfo._screenHeight = new Double(dm.widthPixels*9/16).intValue();
        Log.d("LOGCAT","screenSize:"+deviceInfo._screenWidth+"-"+deviceInfo._screenHeight+"_"+dm.widthPixels);

        //put test info
        _infoList=new ArrayList<>();
        for (int i=0;i<_listLength;i++){
            VideoInfo _videoInfo=new VideoInfo();
            _videoInfo.set_id(i);
            _videoInfo.set_name("视频"+i);
            if (i == 0) {
                _videoInfo.set_playing(true);
            }else{
                _videoInfo.set_playing(false);
            }
            if(i%2==0) {//test video
                _videoInfo.set_url("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
            }else{
                _videoInfo.set_url("http://media.w3.org/2010/05/video/movie_300.mp4");
            }
            _infoList.add(_videoInfo);
        }
        createList(_listLength);
    }

    private void createList(int listLength){
        playerAdapter=new PlayerAdapter(this,_infoList);
        myVideoList.setAdapter(playerAdapter);
        myVideoList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //SCROLL_STATE_FLING = 滚动中；SCROLL_STATE_IDLE = 结束滚动；SCROLL_STATE_TOUCH_SCROLL = 开始滚动;
                if(scrollState==SCROLL_STATE_IDLE){
                    int _index=myVideoList.getFirstVisiblePosition()+1;
                    View v1=myVideoList.getChildAt(1);//取可见元素的第二个
                    if(v1!=null){
                        int scrollTop=v1.getTop();
//                        Log.d(TAG,"cur position:"+_oldItem+"_"+_index+"_"+scrollTop);
                        if(scrollTop<200){
                            if(_oldItem!=_index) {
                                _infoList.get(_index).set_playing(true);
                                _infoList.get(_oldItem).set_playing(false);
                                _oldItem=_index;
                                playerAdapter.notifyDataSetChanged();
                            }
                        }
//                        Log.d(TAG,"scroll top:"+scrollTop);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
}
