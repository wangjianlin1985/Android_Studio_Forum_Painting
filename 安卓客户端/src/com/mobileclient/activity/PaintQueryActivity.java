package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Paint;
import com.mobileclient.domain.PaintClass;
import com.mobileclient.service.PaintClassService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class PaintQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明作品名称输入框
	private EditText ET_paintName;
	// 声明绘画分类下拉框
	private Spinner spinner_paintClassObj;
	private ArrayAdapter<String> paintClassObj_adapter;
	private static  String[] paintClassObj_ShowText  = null;
	private List<PaintClass> paintClassList = null; 
	/*绘画分类管理业务逻辑层*/
	private PaintClassService paintClassService = new PaintClassService();
	// 声明发布用户下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null; 
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明发布时间输入框
	private EditText ET_addTime;
	/*查询过滤条件保存到这个对象中*/
	private Paint queryConditionPaint = new Paint();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.paint_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置绘画查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_paintName = (EditText) findViewById(R.id.ET_paintName);
		spinner_paintClassObj = (Spinner) findViewById(R.id.Spinner_paintClassObj);
		// 获取所有的绘画分类
		try {
			paintClassList = paintClassService.QueryPaintClass(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int paintClassCount = paintClassList.size();
		paintClassObj_ShowText = new String[paintClassCount+1];
		paintClassObj_ShowText[0] = "不限制";
		for(int i=1;i<=paintClassCount;i++) { 
			paintClassObj_ShowText[i] = paintClassList.get(i-1).getPaintClassName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		paintClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, paintClassObj_ShowText);
		// 设置绘画分类下拉列表的风格
		paintClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_paintClassObj.setAdapter(paintClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_paintClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionPaint.setPaintClassObj(paintClassList.get(arg2-1).getPaintClassId()); 
				else
					queryConditionPaint.setPaintClassObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_paintClassObj.setVisibility(View.VISIBLE);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的用户
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int userInfoCount = userInfoList.size();
		userObj_ShowText = new String[userInfoCount+1];
		userObj_ShowText[0] = "不限制";
		for(int i=1;i<=userInfoCount;i++) { 
			userObj_ShowText[i] = userInfoList.get(i-1).getName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// 设置发布用户下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionPaint.setUserObj(userInfoList.get(arg2-1).getUser_name()); 
				else
					queryConditionPaint.setUserObj("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionPaint.setPaintName(ET_paintName.getText().toString());
					queryConditionPaint.setAddTime(ET_addTime.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionPaint", queryConditionPaint);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
