package com.example.si_0010_placehanter.uril;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
;

public class ImageFileDecoder {
	
	private static volatile ImageFileDecoder instatnce;
	
	final static String BYTE_DATA="DATA";
	final static String FILE_NAME="FILE_NAME";
	FileDecodeCollback fileDecodeCollback; 
	
	private ImageFileDecoder(){

	}
	
	public static ImageFileDecoder getInstance(){
		ImageFileDecoder localeInstance=instatnce;
		if(localeInstance == null){
			synchronized (ImageFileDecoder.class) {
				localeInstance=instatnce;
				if (localeInstance == null) {
					instatnce=localeInstance=new ImageFileDecoder();
				}
				
			}
		}
		return localeInstance;
		
	}
	
	public void decodeImage(byte[] data, ProgressBar progressBar, ImageView imageView, String fileName,FileDecodeCollback fileDecodeCollback){
		DecodeFileTask task= new DecodeFileTask(imageView,  progressBar,fileDecodeCollback);
		/*Map<String,Object> byteMap=new HashMap<String,Object>();
		byteMap.put(BYTE_DATA, data);
		byteMap.put(FILE_NAME, fileName);*/
		
		task.execute(data,fileName);
	}

	
	public interface FileDecodeCollback{
		void fileDecodeCallback(File file);
	}
	
	public class DecodeFileTask extends AsyncTask<Object, Void, File>{
		
		final WeakReference<ImageView> ivItemReference;
		final WeakReference<ProgressBar> pbItemReference;
		FileDecodeCollback fileDecodeCollback;
		
		public DecodeFileTask(ImageView ivItem, ProgressBar pbItem, FileDecodeCollback fileDecodeCollback) {
			this.ivItemReference=new WeakReference<ImageView>(ivItem);
			 this.pbItemReference=new WeakReference<ProgressBar>(pbItem);
			 this.fileDecodeCollback=fileDecodeCollback;
		}
		
		@Override
		protected void onPreExecute() {
			pbItemReference.get().setVisibility(View.VISIBLE);

		}

		@Override
		protected File doInBackground(Object... params) {
			/*Map<String,Object> map=params[0];
			byte[] data=(byte[]) map.get(BYTE_DATA);
			String fileName=(String) map.get(FILE_NAME);*/
			byte[] data=(byte[]) params[0];
			String fileName=(String) params[1];
			File file=new File( Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileName);
			
			if (!file.exists()) {
				String strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileName;
	            try {
	                 FileOutputStream fos = new FileOutputStream(strFilePath);

	                 fos.write(data);
	                 fos.close();
	                 file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileName);
	           }
	            
	          catch(FileNotFoundException ex)   {
	                 System.out.println("FileNotFoundException : " + ex);
	          }
	         catch(IOException ioe)  {
	                 System.out.println("IOException : " + ioe);
	          }
			}
			
			return file;
		}
		
		@Override
		protected void onPostExecute(File result) {
			if(pbItemReference.get()!=null){
			pbItemReference.get().setVisibility(View.GONE);
			}
			if (result!=null) {
				if(ivItemReference.get()!=null){}
				ivItemReference.get().setImageURI(Uri.fromFile(result));
			}
			fileDecodeCollback.fileDecodeCallback(result);
			super.onPostExecute(result);
		}
		
	}

}

