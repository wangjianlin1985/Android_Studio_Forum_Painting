package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Reply;
import com.mobileclient.service.ReplyService;
import com.mobileclient.domain.PostInfo;
import com.mobileclient.service.PostInfoService;
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
public class ReplyDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明回复id控件
	private TextView TV_replyId;
	// 声明被回帖子控件
	private TextView TV_postInfoObj;
	// 声明回复内容控件
	private TextView TV_content;
	// 声明回复人控件
	private TextView TV_userObj;
	// 声明回复时间控件
	private TextView TV_replyTime;
	/* 要保存的帖子回复信息 */
	Reply reply = new Reply(); 
	/* 帖子回复管理业务逻辑层 */
	private ReplyService replyService = new ReplyService();
	private PostInfoService postInfoService = new PostInfoService();
	private UserInfoService userInfoService = new UserInfoService();
	private int replyId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.reply_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看帖子回复详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_replyId = (TextView) findViewById(R.id.TV_replyId);
		TV_postInfoObj = (TextView) findViewById(R.id.TV_postInfoObj);
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_replyTime = (TextView) findViewById(R.id.TV_replyTime);
		Bundle extras = this.getIntent().getExtras();
		replyId = extras.getInt("replyId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReplyDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    reply = replyService.GetReply(replyId); 
		this.TV_replyId.setText(reply.getReplyId() + "");
		PostInfo postInfoObj = postInfoService.GetPostInfo(reply.getPostInfoObj());
		this.TV_postInfoObj.setText(postInfoObj.getTitle());
		this.TV_content.setText(reply.getContent());
		UserInfo userObj = userInfoService.GetUserInfo(reply.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_replyTime.setText(reply.getReplyTime());
	} 
}
