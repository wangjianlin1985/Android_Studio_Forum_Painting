package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Reply;
import com.mobileclient.domain.PostInfo;
import com.mobileclient.service.PostInfoService;
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
public class ReplyQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明被回帖子下拉框
	private Spinner spinner_postInfoObj;
	private ArrayAdapter<String> postInfoObj_adapter;
	private static  String[] postInfoObj_ShowText  = null;
	private List<PostInfo> postInfoList = null; 
	/*帖子管理业务逻辑层*/
	private PostInfoService postInfoService = new PostInfoService();
	// 声明回复人下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null; 
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明回复时间输入框
	private EditText ET_replyTime;
	/*查询过滤条件保存到这个对象中*/
	private Reply queryConditionReply = new Reply();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.reply_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置帖子回复查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_postInfoObj = (Spinner) findViewById(R.id.Spinner_postInfoObj);
		// 获取所有的帖子
		try {
			postInfoList = postInfoService.QueryPostInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int postInfoCount = postInfoList.size();
		postInfoObj_ShowText = new String[postInfoCount+1];
		postInfoObj_ShowText[0] = "不限制";
		for(int i=1;i<=postInfoCount;i++) { 
			postInfoObj_ShowText[i] = postInfoList.get(i-1).getTitle();
		} 
		// 将可选内容与ArrayAdapter连接起来
		postInfoObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, postInfoObj_ShowText);
		// 设置被回帖子下拉列表的风格
		postInfoObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_postInfoObj.setAdapter(postInfoObj_adapter);
		// 添加事件Spinner事件监听
		spinner_postInfoObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionReply.setPostInfoObj(postInfoList.get(arg2-1).getPostInfoId()); 
				else
					queryConditionReply.setPostInfoObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_postInfoObj.setVisibility(View.VISIBLE);
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
		// 设置回复人下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionReply.setUserObj(userInfoList.get(arg2-1).getUser_name()); 
				else
					queryConditionReply.setUserObj("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_replyTime = (EditText) findViewById(R.id.ET_replyTime);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionReply.setReplyTime(ET_replyTime.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionReply", queryConditionReply);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
