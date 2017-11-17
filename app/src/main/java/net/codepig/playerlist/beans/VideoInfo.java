package net.codepig.playerlist.beans;

/**
 * 视频相关信息
 * Created by QZD on 2017/11/13.
 */

public class VideoInfo {
    private int _id=0;
    private String _url="";
    private String _name="";
    private boolean _playing=false;

    public void set_id(int id){
        _id=id;
    }
    public int get_id(){
        return _id;
    }

    public void set_url(String url){
        _url=url;
    }
    public String get_url(){
        return _url;
    }

    public void set_name(String name){
        _name=name;
    }
    public String get_name(){
        return _name;
    }

    public void set_playing(boolean _b){
        _playing=_b;
    }
    public boolean get_playing(){
        return _playing;
    }
}
