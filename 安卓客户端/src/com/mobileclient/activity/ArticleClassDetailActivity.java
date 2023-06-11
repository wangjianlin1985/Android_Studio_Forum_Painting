package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ArticleClass;
import com.mobileclient.service.ArticleClassService;
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
public class ArticleClassDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明文章分类id控件
	private TextView TV_articleClassId;
	// 声明文章分类名称控件
	private TextView TV_articleClassName;
	/* 要保存的文章分类信息 */
	ArticleClass articleClass = new ArticleClass(); 
	/* 文章分类管理业务逻辑层 */
	private ArticleClassService articleClassService = new ArticleClassService();
	private int articleClassId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.articleclass_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看文章分类详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_articleClassId = (TextView) findViewById(R.id.TV_articleClassId);
		TV_articleClassName = (TextView) findViewById(R.id.TV_articleClassName);
		Bundle extras = this.getIntent().getExtras();
		articleClassId = extras.getInt("articleClassId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ArticleClassDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    articleClass = articleClassService.GetArticleClass(articleClassId); 
		this.TV_articleClassId.setText(articleClass.getArticleClassId() + "");
		this.TV_articleClassName.setText(articleClass.getArticleClassName());
	} 
}
