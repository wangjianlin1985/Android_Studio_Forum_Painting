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
	// 声明返回按钮
	private Button btnReturn;
	// 声明绘画分类id控件
	private TextView TV_paintClassId;
	// 声明绘画分类名称控件
	private TextView TV_paintClassName;
	/* 要保存的绘画分类信息 */
	PaintClass paintClass = new PaintClass(); 
	/* 绘画分类管理业务逻辑层 */
	private PaintClassService paintClassService = new PaintClassService();
	private int paintClassId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.paintclass_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看绘画分类详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
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
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    paintClass = paintClassService.GetPaintClass(paintClassId); 
		this.TV_paintClassId.setText(paintClass.getPaintClassId() + "");
		this.TV_paintClassName.setText(paintClass.getPaintClassName());
	} 
}
