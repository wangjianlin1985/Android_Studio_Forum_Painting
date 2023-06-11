package com.mobileclient.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Paint;
import com.mobileclient.service.PaintService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.PaintSimpleAdapter;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class PaintUserListActivity extends Activity {
	PaintSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int paintId;
	/* �滭����ҵ���߼������ */
	PaintService paintService = new PaintService();
	/*�����ѯ���������Ļ滭����*/
	private Paint queryConditionPaint;

	private MyProgressDialog dialog; //������	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.paint_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//�������ؼ�
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(PaintUserListActivity.this, PaintQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//�˴���requestCodeӦ�������������е��õ�requestCodeһ��
			}
		});
		search.setVisibility(View.GONE);
		
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�滭��ѯ�б�");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(PaintUserListActivity.this, PaintUserAddActivity.class);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		
		queryConditionPaint = new Paint();
		queryConditionPaint.setPaintName("");
		queryConditionPaint.setPaintClassObj(0);
		queryConditionPaint.setUserObj(username);
		queryConditionPaint.setAddTime("");
		  
		setViews();
	}

	//���������������secondActivity�з���ʱ���ô˺���
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionPaint = (Paint)extras.getSerializable("queryConditionPaint");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	setViews();
        }
    }

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		dialog.show();
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				//�����߳��н����������ݲ���
				list = getDatas();
				//������ʧ��handler��֪ͨ���߳��������
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						adapter = new PaintSimpleAdapter(PaintUserListActivity.this, list,
	        					R.layout.paint_list_item,
	        					new String[] { "paintName","paintClassObj","paintPhoto","hitNum","userObj","addTime" },
	        					new int[] { R.id.tv_paintName,R.id.tv_paintClassObj,R.id.iv_paintPhoto,R.id.tv_hitNum,R.id.tv_userObj,R.id.tv_addTime,},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// ��ӳ������
		lv.setOnCreateContextMenuListener(paintListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int paintId = Integer.parseInt(list.get(arg2).get("paintId").toString());
            	Intent intent = new Intent();
            	intent.setClass(PaintUserListActivity.this, PaintDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("paintId", paintId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener paintListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		 
			menu.add(0, 0, 0, "ɾ���滭��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {// ɾ���滭��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��Ʒid
			paintId = Integer.parseInt(list.get(position).get("paintId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(PaintUserListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = paintService.DeletePaint(paintId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* ��ѯ�滭��Ϣ */
			List<Paint> paintList = paintService.QueryPaint(queryConditionPaint);
			for (int i = 0; i < paintList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("paintId",paintList.get(i).getPaintId());
				map.put("paintName", paintList.get(i).getPaintName());
				map.put("paintClassObj", paintList.get(i).getPaintClassObj());
				/*byte[] paintPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ paintList.get(i).getPaintPhoto());// ��ȡͼƬ����
				BitmapFactory.Options paintPhoto_opts = new BitmapFactory.Options();  
				paintPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(paintPhoto_data, 0, paintPhoto_data.length, paintPhoto_opts); 
				paintPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(paintPhoto_opts, -1, 100*100); 
				paintPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap paintPhoto = BitmapFactory.decodeByteArray(paintPhoto_data, 0, paintPhoto_data.length, paintPhoto_opts);
					map.put("paintPhoto", paintPhoto);
				} catch (OutOfMemoryError err) { }*/
				map.put("paintPhoto", HttpUtil.BASE_URL+ paintList.get(i).getPaintPhoto());
				map.put("hitNum", paintList.get(i).getHitNum());
				map.put("userObj", paintList.get(i).getUserObj());
				map.put("addTime", paintList.get(i).getAddTime());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
