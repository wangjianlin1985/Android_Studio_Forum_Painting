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
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ������Ʒ���������
	private EditText ET_paintName;
	// �����滭����������
	private Spinner spinner_paintClassObj;
	private ArrayAdapter<String> paintClassObj_adapter;
	private static  String[] paintClassObj_ShowText  = null;
	private List<PaintClass> paintClassList = null;
	/*�滭�������ҵ���߼���*/
	private PaintClassService paintClassService = new PaintClassService();
	// ������ƷͼƬͼƬ��ؼ�
	private ImageView iv_paintPhoto;
	private Button btn_paintPhoto;
	protected int REQ_CODE_SELECT_IMAGE_paintPhoto = 1;
	private int REQ_CODE_CAMERA_paintPhoto = 2;
	// ������Ʒ���������
	private EditText ET_paintDesc;
	// ������Ʒ�ļ���ؿؼ�
	private TextView TV_paintFile;
	private Button btn_paintFile;
	private int REQ_CODE_SELECT_FILE_paintFile = 3;
	// ��������������
	private EditText ET_hitNum;
	// ���������û�������
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*�����û�����ҵ���߼���*/
	private UserInfoService userInfoService = new UserInfoService();
	// ��������ʱ�������
	private EditText ET_addTime;
	protected String carmera_path;
	/*Ҫ����Ļ滭��Ϣ*/
	Paint paint = new Paint();
	/*�滭����ҵ���߼���*/
	private PaintService paintService = new PaintService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.paint_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("��ӻ滭");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_paintName = (EditText) findViewById(R.id.ET_paintName);
		spinner_paintClassObj = (Spinner) findViewById(R.id.Spinner_paintClassObj);
		// ��ȡ���еĻ滭����
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
		// ����ѡ������ArrayAdapter��������
		paintClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, paintClassObj_ShowText);
		// ���������б�ķ��
		paintClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_paintClassObj.setAdapter(paintClassObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_paintClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				paint.setPaintClassObj(paintClassList.get(arg2).getPaintClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_paintClassObj.setVisibility(View.VISIBLE);
		iv_paintPhoto = (ImageView) findViewById(R.id.iv_paintPhoto);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
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
		// ��ȡ���еķ����û�
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
		// ����ѡ������ArrayAdapter��������
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// ���������б�ķ��
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_userObj.setAdapter(userObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				paint.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������ӻ滭��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ��Ʒ����*/ 
					if(ET_paintName.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "��Ʒ�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_paintName.setFocusable(true);
						ET_paintName.requestFocus();
						return;	
					}
					paint.setPaintName(ET_paintName.getText().toString());
					if(paint.getPaintPhoto() != null) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						PaintAddActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String paintPhoto = HttpUtil.uploadFile(paint.getPaintPhoto());
						PaintAddActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						paint.setPaintPhoto(paintPhoto);
					} else {
						paint.setPaintPhoto("upload/noimage.jpg");
					}
					/*��֤��ȡ��Ʒ����*/ 
					if(ET_paintDesc.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "��Ʒ�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_paintDesc.setFocusable(true);
						ET_paintDesc.requestFocus();
						return;	
					}
					paint.setPaintDesc(ET_paintDesc.getText().toString());
					if(paint.getPaintFile() != null) {
						//����ļ���ַ��Ϊ�գ�˵���û�ѡ�����ļ�����ʱ��Ҫ���ӷ������ϴ��ļ�
						PaintAddActivity.this.setTitle("�����ϴ��ļ����Ե�...");
						String paintFile = HttpUtil.uploadFile(paint.getPaintFile());
						PaintAddActivity.this.setTitle("�ļ��ϴ���ϣ�");
						paint.setPaintFile(paintFile); 
					} else {
						Toast.makeText(getApplicationContext(), "����ѡ����Ʒ�ļ�", 1).show();
						return;
					}
					/*��֤��ȡ�����*/ 
					if(ET_hitNum.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "��������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_hitNum.setFocusable(true);
						ET_hitNum.requestFocus();
						return;	
					}
					paint.setHitNum(Integer.parseInt(ET_hitNum.getText().toString()));
					/*��֤��ȡ����ʱ��*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(PaintAddActivity.this, "����ʱ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					paint.setAddTime(ET_addTime.getText().toString());
					/*����ҵ���߼����ϴ��滭��Ϣ*/
					PaintAddActivity.this.setTitle("�����ϴ��滭��Ϣ���Ե�...");
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
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
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
