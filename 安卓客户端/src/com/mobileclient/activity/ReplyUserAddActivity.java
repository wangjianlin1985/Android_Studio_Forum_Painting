package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.app.Declare;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class ReplyUserAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	 
	/*被回帖子管理业务逻辑层*/
	private PostInfoService postInfoService = new PostInfoService();
	// 声明回复内容输入框
	private EditText ET_content;
	 
	protected String carmera_path;
	/*要保存的帖子回复信息*/
	Reply reply = new Reply();
	/*帖子回复管理业务逻辑层*/
	private ReplyService replyService = new ReplyService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.reply_user_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加帖子回复");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		}); 
		Bundle bundle = this.getIntent().getExtras();
		int postInfoId = bundle.getInt("postInfoId"); 
		reply.setPostInfoObj(postInfoId); 
		TextView TV_postInfo = (TextView)this.findViewById(R.id.TV_postInfo);
		TV_postInfo.setText(postInfoService.GetPostInfo(postInfoId).getTitle());
		 
		 
		ET_content = (EditText) findViewById(R.id.ET_content);
		
		Declare declare = (Declare)ReplyUserAddActivity.this.getApplication();
		
		reply.setUserObj(declare.getUserName()); 
		 
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加帖子回复按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取回复内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(ReplyUserAddActivity.this, "回复内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					reply.setContent(ET_content.getText().toString());
					/*回复时间*/ 
					reply.setReplyTime("--");
					/*调用业务逻辑层上传帖子回复信息*/
					ReplyUserAddActivity.this.setTitle("正在上传帖子回复信息，稍等...");
					String result = replyService.AddReply(reply);
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
