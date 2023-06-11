package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.PaintClass;
import com.mobileclient.service.PaintClassService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class PaintClassDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �����滭����id�ؼ�
	private TextView TV_paintClassId;
	// �����滭�������ƿؼ�
	private TextView TV_paintClassName;
	/* Ҫ����Ļ滭������Ϣ */
	PaintClass paintClass = new PaintClass(); 
	/* �滭�������ҵ���߼��� */
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
		setContentView(R.layout.paintclass_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴�滭��������");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_paintClassId = (TextView) findViewById(R.id.TV_paintClassId);
		TV_paintClassName = (TextView) findViewById(R.id.TV_paintClassName);
		Bundle extras = this.getIntent().getExtras();
		paintClassId = extras.getInt("paintClassId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaintClassDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    paintClass = paintClassService.GetPaintClass(paintClassId); 
		this.TV_paintClassId.setText(paintClass.getPaintClassId() + "");
		this.TV_paintClassName.setText(paintClass.getPaintClassName());
	} 
}
