package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.PaintClassService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class PaintSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //ͼƬ�첽���������,���ڴ滺����ļ�����
    private SyncImageLoader syncImageLoader;

    public PaintSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*��һ��װ�����viewʱ=null,���½�һ������inflate��Ⱦһ��view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.paint_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*�󶨸�view�����ؼ�*/
	  holder.tv_paintName = (TextView)convertView.findViewById(R.id.tv_paintName);
	  holder.tv_paintClassObj = (TextView)convertView.findViewById(R.id.tv_paintClassObj);
	  holder.iv_paintPhoto = (ImageView)convertView.findViewById(R.id.iv_paintPhoto);
	  holder.tv_hitNum = (TextView)convertView.findViewById(R.id.tv_hitNum);
	  holder.tv_userObj = (TextView)convertView.findViewById(R.id.tv_userObj);
	  holder.tv_addTime = (TextView)convertView.findViewById(R.id.tv_addTime);
	  /*���ø����ؼ���չʾ����*/
	  holder.tv_paintName.setText("��Ʒ���ƣ�" + mData.get(position).get("paintName").toString());
	  holder.tv_paintClassObj.setText("�滭���ࣺ" + (new PaintClassService()).GetPaintClass(Integer.parseInt(mData.get(position).get("paintClassObj").toString())).getPaintClassName());
	  holder.iv_paintPhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener paintPhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_paintPhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("paintPhoto"),paintPhotoLoadListener);  
	  holder.tv_hitNum.setText("����ʣ�" + mData.get(position).get("hitNum").toString());
	  holder.tv_userObj.setText("�����û���" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userObj").toString()).getName());
	  holder.tv_addTime.setText("����ʱ�䣺" + mData.get(position).get("addTime").toString());
	  /*�����޸ĺõ�view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_paintName;
    	TextView tv_paintClassObj;
    	ImageView iv_paintPhoto;
    	TextView tv_hitNum;
    	TextView tv_userObj;
    	TextView tv_addTime;
    }
} 
