package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Paint;
import com.mobileclient.service.PaintService;
import com.mobileclient.domain.PaintClass;
import com.mobileclient.service.PaintClassService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
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
public class PaintDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ������Ʒid�ؼ�
	private TextView TV_paintId;
	// ������Ʒ���ƿؼ�
	private TextView TV_paintName;
	// �����滭����ؼ�
	private TextView TV_paintClassObj;
	// ������ƷͼƬͼƬ��
	private ImageView iv_paintPhoto;
	// ������Ʒ�����ؼ�
	private TextView TV_paintDesc;
	// ������Ʒ�ļ��ؼ�
	private TextView TV_paintFile;
	private Button btnDownPaintFile;
	// ��������ʿؼ�
	private TextView TV_hitNum;
	// ���������û��ؼ�
	private TextView TV_userObj;
	// ��������ʱ��ؼ�
	private TextView TV_addTime;
	/* Ҫ����Ļ滭��Ϣ */
	Paint paint = new Paint(); 
	/* �滭����ҵ���߼��� */
	private PaintService paintService = new PaintService();
	private PaintClassService paintClassService = new PaintClassService();
	private UserInfoService userInfoService = new UserInfoService();
	private int paintId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.paint_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴�滭����");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_paintId = (TextView) findViewById(R.id.TV_paintId);
		TV_paintName = (TextView) findViewById(R.id.TV_paintName);
		TV_paintClassObj = (TextView) findViewById(R.id.TV_paintClassObj);
		iv_paintPhoto = (ImageView) findViewById(R.id.iv_paintPhoto); 
		TV_paintDesc = (TextView) findViewById(R.id.TV_paintDesc);
		TV_paintFile = (TextView) findViewById(R.id.TV_paintFile);
		TV_hitNum = (TextView) findViewById(R.id.TV_hitNum);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_addTime = (TextView) findViewById(R.id.TV_addTime);
		Bundle extras = this.getIntent().getExtras();
		paintId = extras.getInt("paintId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaintDetailActivity.this.finish();
			}
		}); 
		btnDownPaintFile = (Button)findViewById(R.id.btnDownPaintFile);
		btnDownPaintFile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaintDetailActivity.this.setTitle("���ڿ�ʼ������Ʒ�ļ�....");
				HttpUtil.downloadFile(paint.getPaintFile()); 
				Toast.makeText(getApplicationContext(), "���سɹ�����Ҳ������mobileclient/uploadĿ¼�鿴��", 1).show();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    paint = paintService.GetPaint(paintId); 
	    paint.setHitNum(paint.getHitNum() + 1);
	    paintService.UpdatePaint(paint);
		this.TV_paintId.setText(paint.getPaintId() + "");
		this.TV_paintName.setText(paint.getPaintName());
		PaintClass paintClassObj = paintClassService.GetPaintClass(paint.getPaintClassObj());
		this.TV_paintClassObj.setText(paintClassObj.getPaintClassName());
		byte[] paintPhoto_data = null;
		try {
			// ��ȡͼƬ����
			paintPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + paint.getPaintPhoto());
			Bitmap paintPhoto = BitmapFactory.decodeByteArray(paintPhoto_data, 0,paintPhoto_data.length);
			this.iv_paintPhoto.setImageBitmap(paintPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_paintDesc.setText(paint.getPaintDesc());
		this.TV_paintFile.setText(paint.getPaintFile());
		if(paint.getPaintFile().equals("")) {
			// ��ȡ��Ʒ�ļ�����
			this.btnDownPaintFile.setVisibility(View.GONE);
		}
		this.TV_hitNum.setText(paint.getHitNum() + "");
		UserInfo userObj = userInfoService.GetUserInfo(paint.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_addTime.setText(paint.getAddTime());
	} 
}
