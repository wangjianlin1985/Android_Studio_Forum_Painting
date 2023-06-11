package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.Article;
import com.mobileclient.service.ArticleService;
import com.mobileclient.domain.ArticleClass;
import com.mobileclient.service.ArticleClassService;
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

public class ArticleEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明文章idTextView
	private TextView TV_articleId;
	// 声明标题输入框
	private EditText ET_title;
	// 声明文章分类下拉框
	private Spinner spinner_articleClassObj;
	private ArrayAdapter<String> articleClassObj_adapter;
	private static  String[] articleClassObj_ShowText  = null;
	private List<ArticleClass> articleClassList = null;
	/*文章分类管理业务逻辑层*/
	private ArticleClassService articleClassService = new ArticleClassService();
	// 声明内容输入框
	private EditText ET_content;
	// 声明点击率输入框
	private EditText ET_hitNum;
	// 声明发布用户下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*发布用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明发布时间输入框
	private EditText ET_addTime;
	protected String carmera_path;
	/*要保存的文章信息*/
	Article article = new Article();
	/*文章管理业务逻辑层*/
	private ArticleService articleService = new ArticleService();

	private int articleId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.article_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑文章信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_articleId = (TextView) findViewById(R.id.TV_articleId);
		ET_title = (EditText) findViewById(R.id.ET_title);
		spinner_articleClassObj = (Spinner) findViewById(R.id.Spinner_articleClassObj);
		// 获取所有的文章分类
		try {
			articleClassList = articleClassService.QueryArticleClass(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int articleClassCount = articleClassList.size();
		articleClassObj_ShowText = new String[articleClassCount];
		for(int i=0;i<articleClassCount;i++) { 
			articleClassObj_ShowText[i] = articleClassList.get(i).getArticleClassName();
		}
		// 将可选内容与ArrayAdapter连接起来
		articleClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, articleClassObj_ShowText);
		// 设置图书类别下拉列表的风格
		articleClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_articleClassObj.setAdapter(articleClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_articleClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				article.setArticleClassObj(articleClassList.get(arg2).getArticleClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_articleClassObj.setVisibility(View.VISIBLE);
		ET_content = (EditText) findViewById(R.id.ET_content);
		ET_hitNum = (EditText) findViewById(R.id.ET_hitNum);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的发布用户
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
				article.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_addTime = (EditText) findViewById(R.id.ET_addTime);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		articleId = extras.getInt("articleId");
		/*单击修改文章按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取标题*/ 
					if(ET_title.getText().toString().equals("")) {
						Toast.makeText(ArticleEditActivity.this, "标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_title.setFocusable(true);
						ET_title.requestFocus();
						return;	
					}
					article.setTitle(ET_title.getText().toString());
					/*验证获取内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(ArticleEditActivity.this, "内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					article.setContent(ET_content.getText().toString());
					/*验证获取点击率*/ 
					if(ET_hitNum.getText().toString().equals("")) {
						Toast.makeText(ArticleEditActivity.this, "点击率输入不能为空!", Toast.LENGTH_LONG).show();
						ET_hitNum.setFocusable(true);
						ET_hitNum.requestFocus();
						return;	
					}
					article.setHitNum(Integer.parseInt(ET_hitNum.getText().toString()));
					/*验证获取发布时间*/ 
					if(ET_addTime.getText().toString().equals("")) {
						Toast.makeText(ArticleEditActivity.this, "发布时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_addTime.setFocusable(true);
						ET_addTime.requestFocus();
						return;	
					}
					article.setAddTime(ET_addTime.getText().toString());
					/*调用业务逻辑层上传文章信息*/
					ArticleEditActivity.this.setTitle("正在更新文章信息，稍等...");
					String result = articleService.UpdateArticle(article);
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
	    article = articleService.GetArticle(articleId);
		this.TV_articleId.setText(articleId+"");
		this.ET_title.setText(article.getTitle());
		for (int i = 0; i < articleClassList.size(); i++) {
			if (article.getArticleClassObj() == articleClassList.get(i).getArticleClassId()) {
				this.spinner_articleClassObj.setSelection(i);
				break;
			}
		}
		this.ET_content.setText(article.getContent());
		this.ET_hitNum.setText(article.getHitNum() + "");
		for (int i = 0; i < userInfoList.size(); i++) {
			if (article.getUserObj().equals(userInfoList.get(i).getUser_name())) {
				this.spinner_userObj.setSelection(i);
				break;
			}
		}
		this.ET_addTime.setText(article.getAddTime());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
