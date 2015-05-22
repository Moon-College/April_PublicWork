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
	 * listview�����Ż�
	 * ��һ�㣺�Ż�����ÿ�ι�����Ҫ����һ��xml���֣�����convertView�������ļ�������ڴ��У����������ļ��еĿؼ�������һ�����ţ��ж��ٸ��ؼ��Ͷ��ٸ������ԣ�Ȼ���ڵ�һ�ν���xml�����ļ���ʱ�򣬽��ؼ��������ԣ�Ȼ������������convertView�У��ڴ��������������ֱ�������ã�������ÿ�ι���listview��Ҫ����һ��xml�����ǵ�һ���Ż�
	 * �ڶ��㣬ͼƬ���ݷǳ�����Դ�ģ�����ÿ��������ǽ�ͼƬ������һ��������֮����һ���Էŵ�listview����ʾ����Ȼ�ǲ����еģ�������һ���첽������ͼƬ��һ���첽���أ����Ч��
	 * �����㣬���һ��ҳ�棨activity����ͼƬ���࣬��û���ͷţ����ڴ����������ģ�OOM�ڴ��������������һ�������õķ�ʽSoftReference
	 * ��java�����������ã�ǿ���ã������ã������ã�ֱ��new������������ǿ����
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
		back_file.setFile_name("����");
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