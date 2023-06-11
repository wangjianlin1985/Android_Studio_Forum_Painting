package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.PaintClass;
import com.mobileclient.service.PaintClassService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class PaintClassEditActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// �����滭����idTextView
	private TextView TV_paintClassId;
	// �����滭�������������
	private EditText ET_paintClassName;
	protected String carmera_path;
	/*Ҫ����Ļ滭������Ϣ*/
	PaintClass paintClass = new PaintClass();
	/*�滭�������ҵ���߼���*/
	private PaintClassService paintClassService = new PaintClassService();

	private int paintClassId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.paintclass_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�༭�滭������Ϣ");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_paintClassId = (TextView) findViewById(R.id.TV_paintClassId);
		ET_paintClassName = (EditText) findViewById(R.id.ET_paintClassName);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		paintClassId = extras.getInt("paintClassId");
		/*�����޸Ļ滭���ఴť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�滭��������*/ 
					if(ET_paintClassName.getText().toString().equals("")) {
						Toast.makeText(PaintClassEditActivity.this, "�滭�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_paintClassName.setFocusable(true);
						ET_paintClassName.requestFocus();
						return;	
					}
					paintClass.setPaintClassName(ET_paintClassName.getText().toString());
					/*����ҵ���߼����ϴ��滭������Ϣ*/
					PaintClassEditActivity.this.setTitle("���ڸ��»滭������Ϣ���Ե�...");
					String result = paintClassService.UpdatePaintClass(paintClass);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* ��ʼ����ʾ�༭��������� */
	private void initViewData() {
	    paintClass = paintClassService.GetPaintClass(paintClassId);
		this.TV_paintClassId.setText(paintClassId+"");
		this.ET_paintClassName.setText(paintClass.getPaintClassName());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
