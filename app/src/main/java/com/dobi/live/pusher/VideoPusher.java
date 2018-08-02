package com.dobi.live.pusher;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;


import com.dobi.live.jni.PushNative;
import com.dobi.live.params.VideoParam;

import java.io.File;
import java.io.IOException;

public class VideoPusher extends Pusher implements Callback, PreviewCallback{

	private SurfaceHolder surfaceHolder;
	private Camera mCamera;
	private VideoParam videoParams;
	private byte[] buffers;
	private boolean isPushing = false;
	private PushNative pushNative;
	//输出保存h264文件
	String output = new File(Environment.getExternalStorageDirectory(),"du.h264").getAbsolutePath();

	public VideoPusher(SurfaceHolder surfaceHolder, VideoParam videoParams, PushNative pushNative) {
		this.surfaceHolder = surfaceHolder;
		this.videoParams = videoParams;
		this.pushNative = pushNative;
		surfaceHolder.addCallback(this);
	}

	@Override
	public void startPush() {
		//设置视频参数
		pushNative.setVideoOptions(videoParams.getWidth(), 
				videoParams.getHeight(), videoParams.getBitrate(), videoParams.getFps());
		isPushing = true;

		//add by du
		//开始后删除存储的已有的h264文件
		File file = new File(output);
		if(file.exists()){
			file.delete();
		}
	}

	@Override
	public void stopPush() {
		isPushing = false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		startPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	@Override
	public void release() {
		stopPreview();
	}


	/**
	 * 切换摄像头
	 */
	public void switchCamera() {
		if(videoParams.getCameraId() == CameraInfo.CAMERA_FACING_BACK){
			videoParams.setCameraId(CameraInfo.CAMERA_FACING_FRONT);
		}else{
			videoParams.setCameraId(CameraInfo.CAMERA_FACING_BACK);
		}
		//重新预览
		stopPreview();
		startPreview();
	}
	
	/**
	 * 开始预览
	 */
	private void startPreview() {
		try {
			//SurfaceView初始化完成，开始相机预览
			mCamera = Camera.open(videoParams.getCameraId());
			Camera.Parameters parameters = mCamera.getParameters();
			//设置相机参数
			parameters.setPreviewFormat(ImageFormat.NV21); //YUV 预览图像的像素格式
			parameters.setPreviewSize(videoParams.getWidth(), videoParams.getHeight()); //预览画面宽高、

			/////////////////////////////////////////下面//////addby du 2017-11-20/////////////////////
			/*
			//获取摄像头支持的数据格式
			List<Integer> list = parameters.getSupportedPreviewFormats();
			for(Integer val : list){
				Log.e("jw","val："+ val);
			}
			//选择合适的预览尺寸
			List<Camera.Size> sizeList =parameters.getSupportedPictureSizes();
			if(sizeList.size()>1){
				Iterator<Camera.Size> itor = sizeList.iterator();
				while(itor.hasNext()){
					Camera.Size cur = itor.next();
					Log.e("jw","size=="+cur.width +"  "+cur.height);
				}
			}
			*/
			//////////////////////////////////////////上面/////addby du 2017-11-20/////////////////////

			mCamera.setParameters(parameters);//参数真正设置上去了
			//parameters.setPreviewFpsRange(videoParams.getFps()-1, videoParams.getFps());
			mCamera.setPreviewDisplay(surfaceHolder);
			//获取预览图像数据
			buffers = new byte[videoParams.getWidth() * videoParams.getHeight() * 4];
			mCamera.addCallbackBuffer(buffers);
			mCamera.setPreviewCallbackWithBuffer(this);
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 停止预览
	 */
	private void stopPreview() {
		if(mCamera != null){			
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		//一帧回调一次
		if(mCamera != null){
			mCamera.addCallbackBuffer(buffers);
			//System.out.println("isInMainThread()=" + ThreadUtils.isInMainThread());//在这里调用一下方法
		}
		
		if(isPushing){
			//回调函数中获取图像数据，然后给Native代码编码
			pushNative.fireVideo(data,output);
		}
	}

	


}
