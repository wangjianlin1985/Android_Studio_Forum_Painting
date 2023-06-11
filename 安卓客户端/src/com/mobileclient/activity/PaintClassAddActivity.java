package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class PaintClassAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// �����滭�������������
	private EditText ET_paintClassName;
	protected String carmera_path;
	/*Ҫ����Ļ滭������Ϣ*/
	PaintClass paintClass = new PaintClass();
	/*�滭�������ҵ���߼���*/
	private PaintClassService paintClassService = new PaintClassService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.paintclass_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("��ӻ滭����");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_paintClassName = (EditText) findViewById(R.id.ET_paintClassName);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������ӻ滭���ఴť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�滭��������*/ 
					if(ET_paintClassName.getText().toString().equals("")) {
						Toast.makeText(PaintClassAddActivity.this, "�滭�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_paintClassName.setFocusable(true);
						ET_paintClassName.requestFocus();
						return;	
					}
					paintClass.setPaintClassName(ET_paintClassName.getText().toString());
					/*����ҵ���߼����ϴ��滭������Ϣ*/
					PaintClassAddActivity.this.setTitle("�����ϴ��滭������Ϣ���Ե�...");
					String result = paintClassService.AddPaintClass(paintClass);
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
	}
}
