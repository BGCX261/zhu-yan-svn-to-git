package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;

import java.util.ArrayList;
import java.util.List;

import weibo4android.Status;
import weibo4android.Weibo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cc.software.calendar.activity.WeiboInfoActivity;
import com.cc.software.calendar.activity.WeiboPostActivity;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.LoadImageAysnc;
import com.cc.software.calendar.util.LoadImageAysnc.ImageCallBack;
import com.cc.software.calendar.view.PullToRefreshListView;
import com.cc.software.calendar.view.PullToRefreshListView.OnRefreshListener;

public class MessageListView extends LinearLayout implements OnScrollListener, Callback, OnClickListener,
                OnItemClickListener {

    static final String STATUS_ID = "status_id";
    static final String STATUS_NUMS = "status_num";
    static final String STATUS_NAME = "status_name";
    static final String STATUS_TEXT = "status_text";
    static final String STATUS_DATE = "status_date";
    static final String STATUS_USERICONURL = "user_icon_url";
    private Handler mHandler = null;

    private PullToRefreshListView mListView = null;

    public static List<Status> mLatestInfo = new ArrayList<Status>();
    private WeiboListAdapter mListAdapter = null;

    private final int ITEM_TYPE_NUM = 0x2;
    private final int ITEM_TYPE_1 = 0x0;
    private final int ITEM_TYPE_2 = 0x1;
    final static String Tag = "<em>\"</em>&nbsp;";

    private static final int MESSAGE_NEW_INFORMATION_UPDATED = 700001;

    private static final int MESSAGE_REFRESH_WEIBO = 700002;

    private LoadImageAysnc loadImageAysnc;

    public MessageListView(Context context) {
        this(context, null);
    }

    public MessageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.weibo_message_list_view, this);
        findViewById(R.id.refresh_weibo).setOnClickListener(this);
        findViewById(R.id.post_new_weibo).setOnClickListener(this);
        mHandler = new Handler(this);
        loadImageAysnc = LoadImageAysnc.getInstance();
    }

    public void release() {
        if (mHandler != null)
            mHandler = null;

        if (mLatestInfo != null) {
            mLatestInfo.clear();
        }
    }

    public boolean init(List<Status> status) {
        if (status == null || status.size() == 0) {
            return false;
        }
        ((TextView) findViewById(R.id.name)).setText(SinaWeibo.getInstance().getUser().getName());
        mLatestInfo = status;

        //((TextView) findViewById(R.id.userId)).setText("ID: " + user.getId());
        /*loadImageAysnc.loadImage(user.getProfileImageURL().toString(), new ImageCallBack() {

            @Override
            public void imageLoaded(Drawable drawable, String iconSrc) {
                ((ImageView) findViewById(R.id.login_user_photo)).setBackgroundDrawable(drawable);
            }
        });*/

        mListView = (PullToRefreshListView) findViewById(R.id.infor_list);

        mListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });

        mListView.setOnItemClickListener(this);
        mListAdapter = new WeiboListAdapter();
        mListView.setAdapter(mListAdapter);
        mListView.setOnScrollListener(this);
        return true;
    }

    private List<weibo4android.Status> getNewWeibos(List<weibo4android.Status> newinfo, int page) {
        List<weibo4android.Status> info = SinaWeibo.getInstance().getFriendsTimeline(getContext(), page,
                        SinaWeibo.PAGE_COUNT);
        int count = info.size();
        for (int i = 0; i < count; i++) {
            if (mLatestInfo.contains(info.get(i))) {
                break;
            } else {
                newinfo.add(info.get(i));
                if (count - 1 == i && newinfo.size() <= 100) {
                    page++;
                    getNewWeibos(newinfo, page);
                }
            }
        }
        return newinfo;
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<Status>> {
        @Override
        protected List<weibo4android.Status> doInBackground(Void... params) {
            List<weibo4android.Status> newinfo = new ArrayList<weibo4android.Status>();
            getNewWeibos(newinfo, 1);
            return newinfo;
        }

        @Override
        protected void onPostExecute(List<weibo4android.Status> result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            mListView.onRefreshComplete();
            Message msg = mHandler.obtainMessage(MESSAGE_REFRESH_WEIBO);
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }

    class Holder {
        ImageView userIcon;
        ImageView chatIcon;
        TextView userName;
        TextView publishTime;
        TextView content;
        TextView contentRe;
    }

    public class WeiboListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLatestInfo.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position < mLatestInfo.size())
                return mLatestInfo.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position >= mLatestInfo.size()) {
                if (convertView == null) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.weibo_process_bar_view, null);
                    return view;
                }
                return convertView;
            }

            final Status status = mLatestInfo.get(position);

            if (status == null) {
                return null;
            }

            Holder holder;
            if (convertView == null) {
                convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                                R.layout.weibo_message_list_item, null);
                holder = new Holder();
                holder.content = (TextView) convertView.findViewById(R.id.info_content_shot);
                holder.publishTime = (TextView) convertView.findViewById(R.id.info_publish_time);
                holder.userName = (TextView) convertView.findViewById(R.id.info_user);
                holder.userIcon = (ImageView) convertView.findViewById(R.id.info_user_photo);
                holder.contentRe = (TextView) convertView.findViewById(R.id.info_content_shot1);
                holder.chatIcon = (ImageView) convertView.findViewById(R.id.chat_icon);               
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            final int id = status.hashCode();
            holder.userIcon.setTag(id);
            final int id1 = id + 1;
            holder.chatIcon.setTag(id1);
            String text = status.getText();
            holder.content.setText(Html.fromHtml(Tag + text));
            final Status mTranspodStatus = status.getRetweeted_status();

            String chatIconUrl = status.getThumbnail_pic();

            if (mTranspodStatus != null) {
                if (TextUtils.isEmpty(chatIconUrl)) {
                    chatIconUrl = mTranspodStatus.getThumbnail_pic();
                }
                holder.contentRe.setVisibility(View.VISIBLE);
                holder.contentRe.setText(Html.fromHtml(formatTranspondContext(mTranspodStatus)));
            } else {
                holder.contentRe.setVisibility(View.GONE);
            }

            holder.userName.setText(status.getUser().getName());

            holder.publishTime.setText(status.getCreatedAt().toLocaleString());

            Bitmap userPhoto = loadImageAysnc.loadImage(status.getUser().getProfileImageURL().toString(),
                            new ImageCallBack() {
                                @Override
                                public void imageLoaded(Bitmap drawable, String iconSrc) {
                                    ImageView imageViewTag = (ImageView) mListView.findViewWithTag(id);

                                    if (imageViewTag != null) {
                                        if (drawable != null)
                                            imageViewTag.setImageBitmap(drawable);

                                    }
                                }
                            });

            if (userPhoto != null) {
                holder.userIcon.setImageBitmap(userPhoto);
            } else {
                holder.userIcon.setBackgroundResource(R.drawable.icon);
            }

            if (!TextUtils.isEmpty(chatIconUrl)) {
                holder.chatIcon.setVisibility(View.VISIBLE);
                Bitmap bitmap = loadImageAysnc.loadImage(chatIconUrl, new ImageCallBack() {

                    @Override
                    public void imageLoaded(Bitmap drawable, String iconSrc) {
                        ImageView imageViewTag = (ImageView) mListView.findViewWithTag(id1);

                        if (imageViewTag != null) {
                            if (drawable != null)
                                imageViewTag.setImageBitmap(drawable);

                        }
                    }
                });

                if (bitmap != null) {
                    holder.chatIcon.setImageBitmap(bitmap);
                } else {
                    holder.chatIcon.setImageResource(R.drawable.weibo_chat_pic_loading);
                }
            } else {
                holder.chatIcon.setVisibility(View.GONE);
            }
            //Log.d("syso", status.getId() + " " + status.getUser().getName());
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return ITEM_TYPE_NUM;
        }

        @Override
        public int getItemViewType(int position) {
            if (position >= mLatestInfo.size())
                return ITEM_TYPE_2;
            else
                return ITEM_TYPE_1;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private boolean isLoading;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount <= firstVisibleItem + visibleItemCount) {
            if (!isLoading) {
                isLoading = true;
                new AsyncGetMoreInfo().execute();
            }
        }
    }

    public static String formatTranspondContext(Status status) {
        String name = "<i><b><font color=\"#9D9D9D\">@" + status.getUser().getName() + "</font></b></i>&nbsp;&nbsp;";
        String text = status.getText();
        return name + text;
    }

    public class AsyncGetMoreInfo extends AsyncTask<Weibo, Integer, List<Status>> {
        //private int pageNum = 0;

        public AsyncGetMoreInfo() {
            //this.pageNum = pageNum;
        }

        @Override
        protected List<weibo4android.Status> doInBackground(Weibo... params) {
            synchronized (this) {
                int page = mLatestInfo.size() / 20 + 1;
                //int size = mLatestInfo.size();
                List<weibo4android.Status> newInfo = SinaWeibo.getInstance().getFriendsTimeline(getContext(), page,
                                SinaWeibo.PAGE_COUNT);
                /*List<weibo4android.Status> newInfo = SinaWeibo.getInstance().getFriendsTimeline(getContext(),
                                mLatestInfo.get(size - 1).getId(), SinaWeibo.PAGE_COUNT);*/

                int removed = 0;
                for (int i = 0, length = newInfo.size(); i < length; i++) {
                    if (mLatestInfo.contains(newInfo.get(i - removed))) {
                        newInfo.remove(i - removed);
                        removed++;
                    } else {
                        System.out.println("i break");
                        break;
                    }
                }
                return newInfo;
            }
        }

        @Override
        protected void onPostExecute(List<weibo4android.Status> result) {
            Message msg = mHandler.obtainMessage(MESSAGE_NEW_INFORMATION_UPDATED);
            msg.obj = result;
            mHandler.sendMessage(msg);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == MESSAGE_NEW_INFORMATION_UPDATED) {
            List<Status> newInfo = (List<Status>) msg.obj;
            if (newInfo != null && !newInfo.isEmpty()) {
                mLatestInfo.addAll(newInfo);
                mListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
            return true;
        } else if (msg.what == MESSAGE_REFRESH_WEIBO) {
            List<Status> newInfo = (List<Status>) msg.obj;
            if (newInfo != null && !newInfo.isEmpty()) {
                mLatestInfo.addAll(0, newInfo);
                mListAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.refresh_weibo:
            new GetDataTask().execute();
            CommonUtil.showToastMessage(R.string.loding_data, getContext());
            break;
        case R.id.post_new_weibo:
            getContext().startActivity(new Intent(getContext(), WeiboPostActivity.class));
            break;

        default:
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((WeiboInfoActivity) getContext()).getMessageDetailView().init(mLatestInfo.get(position - 1));
        ((WeiboInfoActivity) getContext()).showList(false);
    }
}
