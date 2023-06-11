package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.Reply;
import com.mobileclient.service.ReplyService;
import com.mobileclient.domain.PostInfo;
import com.mobileclient.service.PostInfoService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class ReplyEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明回复idTextView
	private TextView TV_replyId;
	// 声明被回帖子下拉框
	private Spinner spinner_postInfoObj;
	private ArrayAdapter<String> postInfoObj_adapter;
	private static  String[] postInfoObj_ShowText  = null;
	private List<PostInfo> postInfoList = null;
	/*被回帖子管理业务逻辑层*/
	private PostInfoService postInfoService = new PostInfoService();
	// 声明回复内容输入框
	private EditText ET_content;
	// 声明回复人下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*回复人管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明回复时间输入框
	private EditText ET_replyTime;
	protected String carmera_path;
	/*要保存的帖子回复信息*/
	Reply reply = new Reply();
	/*帖子回复管理业务逻辑层*/
	private ReplyService replyService = new ReplyService();

	private int replyId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.reply_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑帖子回复信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_replyId = (TextView) findViewById(R.id.TV_replyId);
		spinner_postInfoObj = (Spinner) findViewById(R.id.Spinner_postInfoObj);
		// 获取所有的被回帖子
		try {
			postInfoList = postInfoService.QueryPostInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int postInfoCount = postInfoList.size();
		postInfoObj_ShowText = new String[postInfoCount];
		for(int i=0;i<postInfoCount;i++) { 
			postInfoObj_ShowText[i] = postInfoList.get(i).getTitle();
		}
		// 将可选内容与ArrayAdapter连接起来
		postInfoObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, postInfoObj_ShowText);
		// 设置图书类别下拉列表的风格
		postInfoObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_postInfoObj.setAdapter(postInfoObj_adapter);
		// 添加事件Spinner事件监听
		spinner_postInfoObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				reply.setPostInfoObj(postInfoList.get(arg2).getPostInfoId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_postInfoObj.setVisibility(View.VISIBLE);
		ET_content = (EditText) findViewById(R.id.ET_content);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的回复人
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
		// 设置图书类别下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				reply.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_replyTime = (EditText) findViewById(R.id.ET_replyTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		replyId = extras.getInt("replyId");
		/*单击修改帖子回复按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取回复内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(ReplyEditActivity.this, "回复内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					reply.setContent(ET_content.getText().toString());
					/*验证获取回复时间*/ 
					if(ET_replyTime.getText().toString().equals("")) {
						Toast.makeText(ReplyEditActivity.this, "回复时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_replyTime.setFocusable(true);
						ET_replyTime.requestFocus();
						return;	
					}
					reply.setReplyTime(ET_replyTime.getText().toString());
					/*调用业务逻辑层上传帖子回复信息*/
					ReplyEditActivity.this.setTitle("正在更新帖子回复信息，稍等...");
					String result = replyService.UpdateReply(reply);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    reply = replyService.GetReply(replyId);
		this.TV_replyId.setText(replyId+"");
		for (int i = 0; i < postInfoList.size(); i++) {
			if (reply.getPostInfoObj() == postInfoList.get(i).getPostInfoId()) {
				this.spinner_postInfoObj.setSelection(i);
				break;
			}
		}
		this.ET_content.setText(reply.getContent());
		for (int i = 0; i < userInfoList.size(); i++) {
			if (reply.getUserObj().equals(userInfoList.get(i).getUser_name())) {
				this.spinner_userObj.setSelection(i);
				break;
			}
		}
		this.ET_replyTime.setText(reply.getReplyTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
