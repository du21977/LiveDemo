package com.blueberry.media;

import android.app.Activity;
import android.media.MediaCodec;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by blueberry on 3/7/2017.
 * 发送数据。
 */

public class MediaPublisher {
    private static final String TAG = "MediaPublisher";

    private Config mConfig;

    public static final int NAL_SLICE = 1;//非关键帧
    public static final int NAL_SLICE_DPA = 2;
    public static final int NAL_SLICE_DPB = 3;
    public static final int NAL_SLICE_DPC = 4;
    public static final int NAL_SLICE_IDR = 5;//关键帧
    public static final int NAL_SEI = 6;
    public static final int NAL_SPS = 7;//SPS
    public static final int NAL_PPS = 8;//PPS
    public static final int NAL_AUD = 9;
    public static final int NAL_FILLER = 12;

    //初始化队列
    private LinkedBlockingQueue<Runnable> mRunnables = new LinkedBlockingQueue<>();
    //线程
    private Thread workThread;

    private VideoGatherer mVideoGatherer;
    private AudioGatherer mAudioGatherer;
    //音视频编码类
    private MediaEncoder mMediaEncoder;
    //RTMP推送类
    private RtmpPublisher mRtmpPublisher;
    //是否开始推送
    public boolean isPublish;

    private AudioGatherer.Params audioParams;
    private VideoGatherer.Params videoParams;
    private boolean loop;


    //初始化音视频参数信息
    public static MediaPublisher newInstance(Config config) {
        return new MediaPublisher(config);
    }

    private MediaPublisher(Config config) {
        this.mConfig = config;
    }

    /**
     * 初始化视频采集器，音频采集器，视频编码器，音频编码器
     */
    public void init() {
        //初始化视频参数
        mVideoGatherer = VideoGatherer.newInstance(mConfig);
        //初始化音频参数
        mAudioGatherer = AudioGatherer.newInstance(mConfig);
        //初始化编码参数
        mMediaEncoder = MediaEncoder.newInstance(mConfig);
        //初始化RTMP推流
        mRtmpPublisher = RtmpPublisher.newInstance();

        setListener();

        workThread = new Thread("publish-thread") {
            @Override
            public void run() {
                while (loop && !Thread.interrupted()) {
                    try {
                        Runnable runnable = mRunnables.take();
                        runnable.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        loop = true;
        workThread.start();
    }

    /**
     * 初始化音频采集器
     */
    public void initAudioGatherer() {
        audioParams = mAudioGatherer.initAudioDevice();
    }

    /**
     * 初始化摄像头
     *
     * @param act
     * @param holder
     */
    public void initVideoGatherer(Activity act, SurfaceHolder holder) {
        videoParams = mVideoGatherer.initCamera(act, holder);
    }

    /**
     * 开始采集
     */
    public void startGather() {
        mAudioGatherer.start();
    }

    /**
     * 初始化编码器
     */
    public void initEncoders() {
        try {
            mMediaEncoder.initAudioEncoder(audioParams.sampleRate, audioParams.channelCount);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "初始化音频编码器失败");
        }

        try {
            int colorFormat = mMediaEncoder.initVideoEncoder(videoParams.previewWidth,
                    videoParams.previewHeight, mConfig.fps);
            mVideoGatherer.setColorFormat(colorFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始编码
     */
    public void startEncoder() {
        mMediaEncoder.start();
    }

    /**
     * 发布
     */
    public void starPublish() {
        if (isPublish) {
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //初始化
                int ret = mRtmpPublisher.init(mConfig.publishUrl,
                        videoParams.previewWidth,
                        videoParams.previewHeight, mConfig.timeOut);
                if (ret < 0) {
                    Log.e(TAG, "连接失败");
                    return;
                }

                isPublish = true;
            }
        };
        mRunnables.add(runnable);
    }


    /**
     * 停止发布
     */
    public void stopPublish() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mRtmpPublisher.stop();
                loop = false;
                workThread.interrupt();
            }
        };

        mRunnables.add(runnable);
    }

    /**
     * 停止编码
     */
    public void stopEncoder() {
        mMediaEncoder.stop();
    }

    /**
     * 停止采集
     */
    public void stopGather() {
        mAudioGatherer.stop();
    }

    /**
     * 释放
     */
    public void release() {
        Log.i(TAG, "release: ");
        mMediaEncoder.release();
        mVideoGatherer.release();
        mAudioGatherer.release();

        loop = false;
        if (workThread != null) {
            workThread.interrupt();
        }
    }

    private void setListener() {
        //开启回调，添加视频数据到视频队列，data为采集到的视频数据
        //将采集到的视频数据添加到视频队列
        mVideoGatherer.setCallback(new VideoGatherer.Callback() {
            @Override
            public void onReceive(byte[] data, int colorFormat) {
                if (isPublish) {
                    mMediaEncoder.putVideoData(data);
                }
            }
        });
        //开启回调，添加音频数据到音频队列,data为采集到的音频数据
        //将采集到的音频数据放入音频队列
        mAudioGatherer.setCallback(new AudioGatherer.Callback() {
            @Override
            public void audioData(byte[] data) {
                if (isPublish) {
                    mMediaEncoder.putAudioData(data);
                }
            }
        });

        //开启回调，拿到视频音频输出的缓冲编码数据，并开始组包RTMP数据
        //编码数据为bb
        mMediaEncoder.setCallback(new MediaEncoder.Callback() {
            @Override
            public void outputVideoData(ByteBuffer bb, MediaCodec.BufferInfo info) {
                onEncodedAvcFrame(bb, info);
            }

            @Override
            public void outputAudioData(ByteBuffer bb, MediaCodec.BufferInfo info) {
                onEncodeAacFrame(bb, info);
            }
        });
    }

    /**
     * 将视频编码数据按RTMP协议格式组包
     * @param bb
     * @param vBufferInfo
     */
    private void onEncodedAvcFrame(ByteBuffer bb, final MediaCodec.BufferInfo vBufferInfo) {


        //判断起始码是3个字节 00 00 01，还是4个字节 00 00 00 01
        int offset = 4;
        //判断帧的类型
        if (bb.get(2) == 0x01) {//3字节起始码，第三位为0x01，bb.get(0)从0开始算
            offset = 3;
        }
        //当NAL头信息中，type（5位）等于5，说明这是关键帧NAL单元
        //bb.get(offset)是 NAL Header，和0x1f进行与运算，获取type，根据type判断关键帧和普通帧
        //00000101 & 00011111(0x1f) = 00000101
        int type = bb.get(offset) & 0x1f;
        Log.d(TAG, "type=" + type);
        if (type == NAL_SPS) {//发送SPS和PPS
            //[0, 0, 0, 1, 103, 66, -64, 13, -38, 5, -126, 90, 1, -31, 16, -115, 64, 0, 0, 0, 1, 104, -50, 6, -30]
            //打印发现这里将 SPS帧和 PPS帧合在了一起发送
            // SPS为 [4，len-8]
            // PPS为后4个字节
            final byte[] pps = new byte[4];
            final byte[] sps = new byte[vBufferInfo.size - 12];//??
            bb.getInt();// 抛弃 0,0,0,1
            bb.get(sps, 0, sps.length);
            bb.getInt();
            bb.get(pps, 0, pps.length);
            Log.e("SPS数据",  Arrays.toString(sps) );
            Log.e("PPS数据",  Arrays.toString(pps));
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mRtmpPublisher.sendSpsAndPps(sps, sps.length, pps, pps.length,
                            vBufferInfo.presentationTimeUs / 1000);
                }
            };
            try {
                mRunnables.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (type == NAL_SLICE || type == NAL_SLICE_IDR) {//发送真正的视频信息，非关键帧和关键帧，在nal头中type分别为1和5
            final byte[] bytes = new byte[vBufferInfo.size];
            bb.get(bytes);
            if(type == NAL_SLICE){
             //   Log.e("普通帧数据",Arrays.toString(bytes));
            }else if(type == NAL_SLICE_IDR){
             //   Log.e("关键帧数据",Arrays.toString(bytes));
            }

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mRtmpPublisher.sendVideoData(bytes, bytes.length,
                            vBufferInfo.presentationTimeUs / 1000);
                }
            };
            try {
                mRunnables.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 将音频编码数据按RTMP协议格式组包
     * @param bb
     * @param aBufferInfo
     */
    private void onEncodeAacFrame(ByteBuffer bb, final MediaCodec.BufferInfo aBufferInfo) {


        if (aBufferInfo.size == 2) {
            // 我打印发现，这里应该已经是吧关键帧计算好了，所以我们直接发送
            final byte[] bytes = new byte[2];
            bb.get(bytes);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mRtmpPublisher.sendAacSpec(bytes, 2);
                }
            };
            try {
                mRunnables.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            final byte[] bytes = new byte[aBufferInfo.size];
            bb.get(bytes);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mRtmpPublisher.sendAacData(bytes, bytes.length, aBufferInfo.presentationTimeUs / 1000);
                }
            };
            try {
                mRunnables.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
