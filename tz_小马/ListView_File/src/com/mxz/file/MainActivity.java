package com.mxz.file;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mxz.file.adapter.FileAdapter;
import com.mxz.file.bean.MyFile;
import com.mxz.file.constant.Constant;

public class MainActivity extends Activity implements OnItemClickListener {
    /** Called when the activity is first created. */
	/**
	 * listview三层优化
	 * 第一层：优化界面每次滚动都要加载一次xml布局，利用convertView将布局文件存放在内存中，而，布局文件中的控件，利用一个类存放，有多少个控件就多少个类属性，然后，在第一次解析xml布局文件的时候，将控件赋给属性，然后将整个类存放在convertView中，内存中有这个东西就直接拿来用，而不是每次滚动listview都要解析一次xml，这是第一层优化
	 * 第二层，图片数据非常耗资源的，所以每次如果都是将图片跟文字一起加载完毕之后再一次性放到listview中显示，显然是不可行的，所以用一个异步处理，将图片做一个异步加载，提高效率
	 * 第三层，如果一个页面（activity）中图片过多，又没能释放，那内存迟早是溢出的（OOM内存溢出），所以用一个软引用的方式SoftReference
	 * 在java中有三种引用，强引用，软引用，虚引用，直接new出来的是属于强引用
	 * 
	 * 
	 */
	private List<MyFile> data;
	private ListView lv;
	private FileAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData(Constant.ROOT);
       
    }
	private void initView() {
		lv=(ListView) findViewById(R.id.lv);
		
		lv.setOnItemClickListener(this);
	}
	private void initData(String root) {
		List<MyFile> files=new ArrayList<MyFile>();
		File file=new File(root);
		MyFile back_file = new MyFile();
		String back_path=root.substring(0, root.lastIndexOf("/"));
		back_file.setFile_path(back_path);
		back_file.setFile_name("返回");
		back_file.setFile(new File(back_path));
		back_file.setBitmap(new SoftReference<Bitmap>(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher)));
		files.add(back_file);
		for (File f : file.listFiles()) {
			String path=f.getAbsolutePath();
			MyFile myfile = new MyFile();
			myfile.setFile_path(path);
			myfile.setFile_name(path.substring(path.lastIndexOf("/")+1));
			myfile.setFile(f);
			if(f.isDirectory()){
				myfile.setBitmap(new SoftReference<Bitmap>(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_launcher)));
			}else if(path.toUpperCase().endsWith("JPG")||path.toUpperCase().endsWith("PNG")){
				Log.i("INFO",path);
//				myfile.setBitmap(BitmapFactory.decodeFile(path));
				myfile.setPic(true);
				myfile.setBitmap(new SoftReference<Bitmap>(null));
				
			}else{
				myfile.setBitmap(new SoftReference<Bitmap>(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_launcher)));
			}
			files.add(myfile);
		}
		data=files;
		adapter=new FileAdapter(this,data);
		lv.setAdapter(adapter);
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String path=data.get(position).getFile_path();
		File file=new File(path);
		if(file.isDirectory()){
			initData(path);
		}
		
	}
}