package com.example.sendMessage.bean;

import java.util.ArrayList;
import java.util.List;

//懒汉式单例模式，确保此类只有一个实例，且自行实例化并向整个系统提供这个实例。
public class FestivalLab {

    private List<Festival> mFestivals = new ArrayList<Festival>();
    private List<Msg> mMsgs = new ArrayList<Msg>();

    //懒汉式单例模式
    private FestivalLab() {
        mFestivals.add(new Festival(1, "国庆节"));
        mFestivals.add(new Festival(2, "儿童节"));
        mFestivals.add(new Festival(3, "端午节"));
        mFestivals.add(new Festival(4, "劳动节"));
        mFestivals.add(new Festival(5, "圣诞节"));
        mFestivals.add(new Festival(6, "建军节"));
        mFestivals.add(new Festival(7, "父亲节"));
        mFestivals.add(new Festival(8, "母亲节"));
        mMsgs.add(new Msg(1, 1, "1you are beautiful"));
        mMsgs.add(new Msg(2, 1, "2you are beautiful"));
        mMsgs.add(new Msg(3, 1, "3you are beautiful"));
        mMsgs.add(new Msg(4, 1, "4you are beautiful"));
        mMsgs.add(new Msg(5, 1, "5you are beautiful"));
        mMsgs.add(new Msg(6, 1, "6you are beautiful"));
        mMsgs.add(new Msg(7, 1, "7you are beautiful"));
        mMsgs.add(new Msg(8, 1, "8you are beautiful"));
        mMsgs.add(new Msg(9, 1, "9you are beautiful"));
        mMsgs.add(new Msg(1, 2, "21you are beautiful"));
        mMsgs.add(new Msg(2, 2, "22you are beautiful"));
        mMsgs.add(new Msg(1, 3, "31you are beautiful"));
        mMsgs.add(new Msg(2, 3, "32you are beautiful"));
        mMsgs.add(new Msg(3, 3, "33you are beautiful"));
        mMsgs.add(new Msg(1, 8, "81you are beautiful"));
        mMsgs.add(new Msg(2, 8, "82you are beautiful"));
        mMsgs.add(new Msg(3, 8, "83you are beautiful"));
        mMsgs.add(new Msg(4, 8, "84you are beautiful"));
    }

    private static FestivalLab mInstance = null;

    public static FestivalLab getInstance() {
        if (mInstance == null) {
            synchronized (FestivalLab.class) {
                if (mInstance == null) {
                    mInstance = new FestivalLab();
                }
            }
        }
        return mInstance;
    }

    public List<Msg> getMsgsByfesId(int fesId) {
        List<Msg> msgs = new ArrayList<>();
        for (Msg msg : mMsgs) {
            if (msg.getfesId() == fesId) {
                msgs.add(msg);
            }
        }
        return msgs;
    }


    public Msg getMsgById(int fesId,int id) {
        List<Msg> msgs = getMsgsByfesId(fesId);
        for (Msg msg : msgs) {
            if (msg.getId() == id) {
                return msg;
            }
        }
        return null;
    }

/*    public Msg getMsgById(int id) {
        for (Msg msg : mMsgs) {
            if (msg.getId() == id) {
                return msg;
            }
        }
        return null;
    }*/

    public List<Festival> getFestivals() {
        return new ArrayList<Festival>(mFestivals);//返回一个mFestivals的副本
    }

    public Festival getFestivalById(int fesId) {
        for (Festival festival : mFestivals) {
            if (fesId == festival.getId()) {
                return festival;
            }
        }
        return null;
    }

}
