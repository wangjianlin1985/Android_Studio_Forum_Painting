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
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
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
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.paint_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_paintName = (TextView)convertView.findViewById(R.id.tv_paintName);
	  holder.tv_paintClassObj = (TextView)convertView.findViewById(R.id.tv_paintClassObj);
	  holder.iv_paintPhoto = (ImageView)convertView.findViewById(R.id.iv_paintPhoto);
	  holder.tv_hitNum = (TextView)convertView.findViewById(R.id.tv_hitNum);
	  holder.tv_userObj = (TextView)convertView.findViewById(R.id.tv_userObj);
	  holder.tv_addTime = (TextView)convertView.findViewById(R.id.tv_addTime);
	  /*设置各个控件的展示内容*/
	  holder.tv_paintName.setText("作品名称：" + mData.get(position).get("paintName").toString());
	  holder.tv_paintClassObj.setText("绘画分类：" + (new PaintClassService()).GetPaintClass(Integer.parseInt(mData.get(position).get("paintClassObj").toString())).getPaintClassName());
	  holder.iv_paintPhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener paintPhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_paintPhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("paintPhoto"),paintPhotoLoadListener);  
	  holder.tv_hitNum.setText("点击率：" + mData.get(position).get("hitNum").toString());
	  holder.tv_userObj.setText("发布用户：" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userObj").toString()).getName());
	  holder.tv_addTime.setText("发布时间：" + mData.get(position).get("addTime").toString());
	  /*返回修改好的view*/
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
