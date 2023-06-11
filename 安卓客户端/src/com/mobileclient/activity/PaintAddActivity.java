package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.Paint;
import com.mobileclient.service.PaintService;
import com.mobileclient.domain.PaintClass;
import com.mobileclient.service.PaintClassService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class PaintAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明作品名称输入框
	private EditText ET_paintName;
	// 声明绘画分类下拉框
	private Spinner spinner_paintClassObj;
	private ArrayAdapter<String> paintClassObj_adapter;
	private static  String[] paintClassObj_ShowText  = null;
	private List<PaintClass> paintClassList = null;
	/*绘画分类管理业务逻辑层*/
	private PaintClassService paintClassService = new PaintClassService();
	// 声明作品图片图片框控件
	private ImageView iv_paintPhoto;
	private Button btn_paintPhoto;
	protected int REQ_CODE_SELECT_IMAGE_paintPhoto = 1;
	private int REQ_CODE_CAMERA_paintPhoto = 2;
	// 声明作品描述输入框
	private EditText ET_paintDesc;
	// 声明作品文件相关控件
	private TextView TV_paintFile;
	private Button btn_paintFile;
	private int REQ_CODE_SELECT_FILE_paintFile = 3;
	// 声明点击率输入框
	private EditText ET_hitNum;
	// 声明发布用户下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*发布用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明发布时间输入框
	private EditText ET_addTime;
	protected String carmera_path;
	/*要保存的绘画信息*/
	Paint paint = new Paint();
	/*绘画管理业务逻辑层*/
	private PaintService paintService = new PaintService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.paint_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加绘画");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_paintName = (EditText) findViewById(R.id.ET_paintName);
		spinner_paintClassObj = (Spinner) findViewById(R.id.Spinner_paintClassObj);
		// 获取所有的绘画分类
		try {
			paintClassList = paintClassService.QueryPaintClass(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int paintClassCount = paintClassList.size();
		paintClassObj_ShowText = new String[paintClassCount];
		for(int i=0;i<paintClassCount;i++) { 
			paintClassObj_ShowText[i] = paintClassList.get(i).getPaintClassName();
		}
		// 将可选内容与ArrayAdapter连接起来
		paintClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, paintClassObj_ShowText);
		// 设置下拉列表的风格
		paintClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_paintClassObj.setAdapter(paintClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_paintClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				paint.setPaintClassObj(paintClassList.get(arg2).getPaintClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_paintClassObj.setVisibility(View.VISIBLE);
		iv_paintPhoto = (ImageView) findViewById(R.id.iv_paintPhoto);
		/*单击图片显示控件时进行图片的选择*/
		iv_paintPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PaintAddActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_paintPhoto);
			}
		});
		btn_paintPhoto = (Button) findViewById(R.id.btn_paintPhoto);
		btn_paintPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_paintPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_paintPhoto);  
			}
		});
		ET_paintDesc = (EditText) findViewById(R.id.ET_paintDesc);
		TV_paintFile = (TextView) findViewById(R.id.TV_paintFile);
		btn_paintFile = (Button) findViewById(R.id.btn_paintFile);
		btn_paintFile.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(PaintAddActivity.this,fileListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_FILE_paintFile);
			}
		});
		ET_hitNum = (EditText) findViewById(R.id.ET_hitNum);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的发布用户
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int userInfoCount = userInfoList.size();
		userObj_ShowText = new String[userInfoCount];
		for(int i=0;i<userInfoCount;i++) { 
			userObj_ShowText[i] = userInfoList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// 设置下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				paint.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加绘画按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取作品名称*/ 
					if(ET_paintName.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "作品名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_paintName.setFocusable(true);
						ET_paintName.requestFocus();
						return;	
					}
					paint.setPaintName(ET_paintName.getText().toString());
					if(paint.getPaintPhoto() != null) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						PaintAddActivity.this.setTitle("正在上传图片，稍等...");
						String paintPhoto = HttpUtil.uploadFile(paint.getPaintPhoto());
						PaintAddActivity.this.setTitle("图片上传完毕！");
						paint.setPaintPhoto(paintPhoto);
					} else {
						paint.setPaintPhoto("upload/noimage.jpg");
					}
					/*验证获取作品描述*/ 
					if(ET_paintDesc.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "作品描述输入不能为空!", Toast.LENGTH_LONG).show();
						ET_paintDesc.setFocusable(true);
						ET_paintDesc.requestFocus();
						return;	
					}
					paint.setPaintDesc(ET_paintDesc.getText().toString());
					if(paint.getPaintFile() != null) {
						//如果文件地址不为空，说明用户选择了文件，这时需要连接服务器上传文件
						PaintAddActivity.this.setTitle("正在上传文件，稍等...");
						String paintFile = HttpUtil.uploadFile(paint.getPaintFile());
						PaintAddActivity.this.setTitle("文件上传完毕！");
						paint.setPaintFile(paintFile); 
					} else {
						Toast.makeText(getApplicationContext(), "请先选择作品文件", 1).show();
						return;
					}
					/*验证获取点击率*/ 
					if(ET_hitNum.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "点击率输入不能为空!", Toast.LENGTH_LONG).show();
						ET_hitNum.setFocusable(true);
						ET_hitNum.requestFocus();
						return;	
					}
					paint.setHitNum(Integer.parseInt(ET_hitNum.getText().toString()));
					/*验证获取发布时间*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "发布时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					paint.setAddTime(ET_addTime.getText().toString());
					/*调用业务逻辑层上传绘画信息*/
					PaintAddActivity.this.setTitle("正在上传绘画信息，稍等...");
					String result = paintService.AddPaint(paint);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_paintPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_paintPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_paintPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_paintPhoto.setImageBitmap(booImageBm);
				this.iv_paintPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.paint.setPaintPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_paintPhoto && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_paintPhoto.setImageBitmap(bm); 
				this.iv_paintPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			paint.setPaintPhoto(filename); 
		}
		if(requestCode == REQ_CODE_SELECT_FILE_paintFile && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			this.TV_paintFile.setText(filepath); 
			paint.setPaintFile(filename);
		}

	}
}
