package com.example.administrator.myapplication;

import android.util.Log;

/**
 * Created by Z.Tao on 2016/12/3.
 */

public class MyLog {
    private static MyLog mylog ;
    public static  boolean isDebug = true; //不需要log时候设为false
    private MyLog() {
    }
  /*  //懒汉模式哈哈
    public static synchronized MyLog getInstance(){
         if(mylog==null){
             mylog = new MyLog();
         }
        return mylog;
    }
    //DCL模式
    public static MyLog getInstance2(){
        if(mylog==null){
            synchronized (MyLog.class){
                if(mylog==null){
                    mylog = new MyLog();
                }
            }
        }
        return mylog;
    }
    //内部类形式,推荐
    public static MyLog getInsance1(){
        return MyLogHolder.sInstance;
    }*/
    public static void d(String TAG,String msg){
        if(isDebug){
            Log.d(TAG,msg);
        }
    }
  /*  private static class MyLogHolder{
        private static final MyLog sInstance = new MyLog();
    }*/
}
