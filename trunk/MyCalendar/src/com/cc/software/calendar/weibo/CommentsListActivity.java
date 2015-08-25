package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;

import java.util.ArrayList;
import java.util.List;

import weibo4android.Comment;
import weibo4android.Weibo;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cc.software.calendar.activity.WeiboUpdateComments;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.view.PullToRefreshListView;
import com.cc.software.calendar.view.PullToRefreshListView.OnRefreshListener;

public class CommentsListActivity extends Activity implements OnClickListener, OnScrollListener, Callback {

    private PullToRefreshListView mCommentsList;
    private ProgressBar mProgressBar;
    private List<Comment> mComments = new ArrayList<Comment>();
    private Handler mHandler;
    private CommentsListAdapter mCommentsListAdapter;
    public static final int MESSAGE_ID_UPDATE_COMMENTS = 1001, MESSAGE_ID_REFRESH_COMMENTS = 1002;
    private String id;
    private final int ITEM_TYPE_NUM = 0x2;
    private final int ITEM_TYPE_1 = 0x0;
    private final int ITEM_TYPE_2 = 0x1;
    int currentPage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_comments_list);
        mCommentsList = (PullToRefreshListView) findViewById(R.id.comments_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.publish_comments).setOnClickListener(this);
        mHandler = new Handler(this);
        isLoading = true;
        id = getIntent().getStringExtra("id");
        new AsyncGetMoreInfo().execute();
        mCommentsList.setOnScrollListener(this);
        mCommentsList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    isLoading = true;
                    new GetDataTask().execute();
                }
            }
        });
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<Comment>> {
        @Override
        protected List<Comment> doInBackground(Void... params) {
            List<Comment> newinfo = new ArrayList<Comment>();
            getComments(newinfo, 1);
            return newinfo;
        }

        @Override
        protected void onPostExecute(List<Comment> result) {
            if (result.size() == 0) {
                CommonUtil.showToastMessage(R.string.already_latest, CommentsListActivity.this);
            }
            mCommentsList.onRefreshComplete();
            Message msg = mHandler.obtainMessage(MESSAGE_ID_REFRESH_COMMENTS);
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }

    private List<Comment> getComments(List<Comment> newinfo, int page) {
        List<Comment> info = SinaWeibo.getInstance().getCommentsById(CommentsListActivity.this, id, page,
                        SinaWeibo.PAGE_COUNT);
        if (info == null) {
            return null;
        }
        int count = info.size();
        for (int i = 0; i < count; i++) {
            if (mComments.contains(info.get(i))) {
                break;
            } else {
                newinfo.add(info.get(i));
                if (count - 1 == i) {
                    page++;
                    getComments(newinfo, page);
                }
            }
        }
        return newinfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.back:
            finish();
            break;
        case R.id.publish_comments:
            Intent intent = new Intent(CommentsListActivity.this, WeiboUpdateComments.class);
            intent.putExtra("id", id);
            startActivity(intent);
            break;

        default:
            break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case MESSAGE_ID_UPDATE_COMMENTS:
            List<Comment> infos = (List<Comment>) msg.obj;
            if (infos != null && infos.size() != 0) {
                mComments.addAll(infos);
            }
            if (mCommentsListAdapter == null) {
                mCommentsListAdapter = new CommentsListAdapter();
                mCommentsList.setAdapter(mCommentsListAdapter);
            } else {
                mCommentsListAdapter.notifyDataSetChanged();
            }
            mProgressBar.setVisibility(View.GONE);
            isLoading = false;
            break;
        case MESSAGE_ID_REFRESH_COMMENTS:
            isLoading = false;
            List<Comment> newInfo = (List<Comment>) msg.obj;
            if (newInfo != null && !newInfo.isEmpty()) {
                mComments.addAll(0, newInfo);
                mCommentsListAdapter.notifyDataSetChanged();
            }
            break;
        default:
            break;
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private boolean isLoading = false;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount <= firstVisibleItem + visibleItemCount) {
            if (!isLoading && mHasOldData) {
                isLoading = true;
                new AsyncGetMoreInfo().execute();
            }
        }
    }

    private boolean mHasOldData = true;

    public class AsyncGetMoreInfo extends AsyncTask<Weibo, Integer, List<Comment>> {

        public AsyncGetMoreInfo() {
        }

        @Override
        protected List<Comment> doInBackground(Weibo... params) {
            synchronized (this) {
                int page = mComments.size() / 20 + 1;
                if (currentPage == page) {
                    return new ArrayList<Comment>();
                }
                currentPage = page;
                List<Comment> newInfo = SinaWeibo.getInstance().getCommentsById(CommentsListActivity.this, id, page,
                                SinaWeibo.PAGE_COUNT);
                int removed = 0;
                for (int i = 0, length = newInfo.size(); i < length; i++) {
                    if (mComments.contains(newInfo.get(i - removed))) {
                        newInfo.remove(i - removed);
                        removed++;
                    } else {
                        break;
                    }
                }
                return newInfo;
            }
        }

        @Override
        protected void onPostExecute(List<Comment> result) {
            if (result.size() == 0) {
                mHasOldData = false;
                CommonUtil.showToastMessage(R.string.no_more_data, CommentsListActivity.this);
            }
            Message msg = mHandler.obtainMessage(MESSAGE_ID_UPDATE_COMMENTS);
            msg.obj = result;
            mHandler.sendMessage(msg);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class Holder {
        TextView userName;
        TextView publishTime;
        TextView content;
    }

    private class CommentsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mHasOldData)
                return mComments.size() + 1;
            else {
                return mComments.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (position < mComments.size())
                return mComments.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position >= mComments.size()) {
                if (convertView == null) {
                    View view = LayoutInflater.from(CommentsListActivity.this).inflate(R.layout.weibo_process_bar_view,
                                    null);
                    return view;
                }
                return convertView;
            }

            final Comment comment = mComments.get(position);

            if (comment == null) {
                return null;
            }

            Holder holder;
            if (convertView == null) {
                convertView = (LinearLayout) LayoutInflater.from(CommentsListActivity.this).inflate(
                                R.layout.weibo_comments_list_item, null);
                holder = new Holder();
                holder.content = (TextView) convertView.findViewById(R.id.comment_content);
                holder.publishTime = (TextView) convertView.findViewById(R.id.date);
                holder.userName = (TextView) convertView.findViewById(R.id.user_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.content.setText(comment.getText());
            holder.userName.setText(comment.getUser().getName());
            holder.publishTime.setText(comment.getCreatedAt().toLocaleString());
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return ITEM_TYPE_NUM;
        }

        @Override
        public int getItemViewType(int position) {
            if (position >= mComments.size())
                return ITEM_TYPE_2;
            else
                return ITEM_TYPE_1;
        }

    }
}
