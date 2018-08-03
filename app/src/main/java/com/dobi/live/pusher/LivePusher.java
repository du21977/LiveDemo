package com.dobi.live.pusher;


import android.hardware.Camera.CameraInfo;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

import com.dobi.live.jni.PushNative;
import com.dobi.live.listener.LiveStateChangeListener;
import com.dobi.live.params.AudioParam;
import com.dobi.live.params.VideoParam;


public class LivePusher implements Callback {

	private SurfaceHolder surfaceHolder;
	private VideoPusher videoPusher;
	private AudioPusher audioPusher;
	private PushNative pushNative;

	public LivePusher(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		surfaceHolder.addCallback(this);
		prepare();
	}

	/**
	 * 预览准备
	 */
	private void prepare() {
		pushNative = new PushNative();
		
		//实例化视频推流器
		//VideoParam videoParam = new VideoParam(1280, 720, CameraInfo.CAMERA_FACING_BACK);
		VideoParam videoParam = new VideoParam(480, 320, CameraInfo.CAMERA_FACING_BACK);
		videoPusher = new VideoPusher(surfaceHolder,videoParam,pushNative);
		
		//实例化音频推流器
		AudioParam audioParam = new AudioParam();
		audioPusher = new AudioPusher(audioParam,pushNative);
	}

	/**
	 * 切换摄像头
	 */
	public void switchCamera() {
		videoPusher.switchCamera();
	}

	/**
	 * 开始推流
	 * @param url
	 * @param liveStateChangeListener
	 */
	public void startPush(String url,LiveStateChangeListener liveStateChangeListener) {
		//java层采集给native层编码，相当于生产者
		videoPusher.startPush();
		audioPusher.startPush();
		//native层RTMP推流，相当于消费者
		pushNative.startPush(url);
		pushNative.setLiveStateChangeListener(liveStateChangeListener);
	}
	
	
	/**
	 * 停止推流
	 */
	public void stopPush() {
		videoPusher.stopPush();
		audioPusher.stopPush();
		pushNative.stopPush();
		pushNative.removeLiveStateChangeListener();
	}
	
	/**
	 * 释放资源
	 */
	private void release() {
		videoPusher.release();
		audioPusher.release();
		pushNative.release();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopPush();
	//	release();
	}
	
}
