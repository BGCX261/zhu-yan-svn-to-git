package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;

import java.util.List;

import weibo4android.Count;
import weibo4android.Status;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cc.software.calendar.activity.ImageShowActivity;
import com.cc.software.calendar.activity.WeiboRepostActivity;
import com.cc.software.calendar.activity.WeiboUpdateComments;
import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.LoadImageAysnc;
import com.cc.software.calendar.util.LoadImageAysnc.ImageCallBack;

public class MessageDetailView extends FrameLayout implements OnClickListener {

    private static final int STATUS_COUUT_ID = 1, RETWEERED_COUNT_ID = 2, FAVRITE_ID = 3;
    private ImageView mRetweetedIcon, mStatusIcon, mUserIcon;
    private TextView mRetweetedComments, mRetweetedReposts, mStatusComments, mStatusReposts;
    private LinearLayout MRetweetedContainer;
    private String mStatusId, mRetweedtedId;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case STATUS_COUUT_ID:
                @SuppressWarnings("unchecked")
                List<Count> statusCount = (List<Count>) msg.obj;
                if (statusCount != null && statusCount.size() != 0) {
                    mStatusComments.setText(String.valueOf(statusCount.get(0).getComments()));
                    mStatusReposts.setText(String.valueOf(statusCount.get(0).getRt()));
                } else {
                    mStatusComments.setText("...");
                    mStatusReposts.setText("...");
                }
                break;
            case RETWEERED_COUNT_ID:
                @SuppressWarnings("unchecked")
                List<Count> retweetedCount = (List<Count>) msg.obj;
                if (retweetedCount != null && retweetedCount.size() != 0) {
                    mRetweetedComments.setText(String.valueOf(retweetedCount.get(0).getComments()));
                    mRetweetedReposts.setText(String.valueOf(retweetedCount.get(0).getRt()));
                } else {
                    mRetweetedComments.setText("...");
                    mRetweetedReposts.setText("...");
                }
                break;
            case FAVRITE_ID:
                boolean b = (Boolean) msg.obj;
                if (b) {
                    CommonUtil.showToastMessage(R.string.weibo_favrite_successed, getContext());
                } else {
                    CommonUtil.showToastMessage(R.string.weibo_favrite_failed, getContext());
                }
                break;

            default:
                break;
            }

        };
    };

    public MessageDetailView(Context context) {
        this(context, null);
    }

    public MessageDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.weibo_message_status_detail_view, this);
        initComponent();
    }

    private void initComponent() {
        mUserIcon = (ImageView) findViewById(R.id.detail_publisher_user_photo);
        mRetweetedIcon = (ImageView) findViewById(R.id.retweet_ic);
        mStatusIcon = (ImageView) findViewById(R.id.status_ic);

        mRetweetedComments = (TextView) findViewById(R.id.retweet_comments_count);
        mRetweetedReposts = (TextView) findViewById(R.id.retweet_repost_count);
        mStatusComments = (TextView) findViewById(R.id.status_comments_count);
        mStatusReposts = (TextView) findViewById(R.id.status_repost_count);
        MRetweetedContainer = (LinearLayout) findViewById(R.id.retweet_container);

        mRetweetedComments.setOnClickListener(this);
        mRetweetedReposts.setOnClickListener(this);
        mStatusComments.setOnClickListener(this);
        mStatusReposts.setOnClickListener(this);
        findViewById(R.id.new_comments).setOnClickListener(this);
        findViewById(R.id.repost).setOnClickListener(this);
        findViewById(R.id.favrite).setOnClickListener(this);
    }

    boolean init(Status status) {
        String text = status.getText();
        Status mTranspodStatus = status.getRetweeted_status();
        ((TextView) findViewById(R.id.status_text)).setText(Html.fromHtml(MessageListView.Tag + text));
        mStatusId = String.valueOf(status.getId());
        /*List<Count> count1 = SinaWeibo.getInstance().getCount(getContext(), mStatusId);
        if (count1 != null && count1.size() != 0) {
            mStatusComments.setText(String.valueOf(count1.get(0).getComments()));
            mStatusReposts.setText(String.valueOf(count1.get(0).getRt()));
        } else {
            mStatusComments.setText("...");
            mStatusReposts.setText("...");
        }*/
        new CountAsyc(mStatusId, STATUS_COUUT_ID).execute();

        if (mTranspodStatus != null) {
            MRetweetedContainer.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.retweeted_text)).setText(Html.fromHtml(MessageListView
                            .formatTranspondContext(mTranspodStatus)));
            String retweeted_pic = mTranspodStatus.getBmiddle_pic();
            mRetweedtedId = String.valueOf(mTranspodStatus.getId());

            /*List<Count> count = SinaWeibo.getInstance().getCount(getContext(), mRetweedtedId);
            if (count != null && count.size() != 0) {
                mRetweetedComments.setText(String.valueOf(count.get(0).getComments()));
                mRetweetedReposts.setText(String.valueOf(count.get(0).getRt()));
            } else {
                mRetweetedComments.setText("...");
                mRetweetedReposts.setText("...");
            }*/
            new CountAsyc(mRetweedtedId, RETWEERED_COUNT_ID).execute();

            if (!TextUtils.isEmpty(retweeted_pic)) {
                mRetweetedIcon.setVisibility(View.VISIBLE);
                mRetweetedIcon.setOnClickListener(this);
                mRetweetedIcon.setTag(mTranspodStatus.getOriginal_pic());
                Bitmap drawable = LoadImageAysnc.getInstance().loadImage(retweeted_pic, new ImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap drawable, String iconSrc) {
                        if (drawable != null)
                            mRetweetedIcon.setImageBitmap(drawable);
                    }
                });
                if (drawable == null) {
                    mRetweetedIcon.setImageResource(R.drawable.weibo_detail_pic_loading);
                } else {
                    mRetweetedIcon.setImageBitmap(drawable);
                }
            } else {
                mRetweetedIcon.setVisibility(View.GONE);
            }
        } else {
            MRetweetedContainer.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.detail_msg_publisher_name)).setText(status.getUser().getName());
        ((TextView) findViewById(R.id.detail_msg_publish_time)).setText(status.getCreatedAt().toLocaleString());

        Bitmap userIcon = LoadImageAysnc.getInstance().loadImage(status.getUser().getProfileImageURL().toString(),
                        new ImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap drawable, String iconSrc) {
                                if (drawable != null)
                                    mUserIcon.setImageBitmap(drawable);
                            }
                        });

        if (userIcon == null) {
            mUserIcon.setImageResource(R.drawable.weibo_user_icon_default);
        } else {
            mUserIcon.setImageBitmap(userIcon);
        }

        String statusIcUrl = status.getBmiddle_pic();

        if (!TextUtils.isEmpty(statusIcUrl)) {
            mStatusIcon.setVisibility(View.VISIBLE);
            mStatusIcon.setOnClickListener(this);
            mStatusIcon.setTag(status.getOriginal_pic());
            Bitmap statusIcon = LoadImageAysnc.getInstance().loadImage(statusIcUrl, new ImageCallBack() {
                @Override
                public void imageLoaded(Bitmap drawable, String iconSrc) {
                    if (drawable != null)
                        mStatusIcon.setImageBitmap(drawable);
                }
            });

            if (statusIcon == null) {
                mStatusIcon.setImageResource(R.drawable.weibo_detail_pic_loading);
            } else {
                mStatusIcon.setImageBitmap(statusIcon);
            }
        } else {
            mStatusIcon.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
        case R.id.status_comments_count:
            intent = new Intent(getContext(), CommentsListActivity.class);
            intent.putExtra("id", mStatusId);
            getContext().startActivity(intent);
            break;
        case R.id.retweet_comments_count:
            intent = new Intent(getContext(), CommentsListActivity.class);
            intent.putExtra("id", mRetweedtedId);
            getContext().startActivity(intent);
            break;

        case R.id.new_comments:
            intent = new Intent(getContext(), WeiboUpdateComments.class);
            intent.putExtra("id", mStatusId);
            getContext().startActivity(intent);
            break;

        case R.id.status_repost_count:
        case R.id.repost:
            intent = new Intent(getContext(), WeiboRepostActivity.class);
            intent.putExtra("id", mStatusId);
            getContext().startActivity(intent);
            break;
        case R.id.retweet_repost_count:
            intent = new Intent(getContext(), WeiboRepostActivity.class);
            intent.putExtra("id", mRetweedtedId);
            getContext().startActivity(intent);
            break;
        case R.id.favrite:
            new favriteAsyc(mStatusId, FAVRITE_ID).execute();
            break;
        case R.id.retweet_ic:
        case R.id.status_ic:
            String tag = (String) v.getTag();
            if (tag != null) {
                intent = new Intent(getContext(), ImageShowActivity.class);
                intent.putExtra("url", tag);
                getContext().startActivity(intent);
            }
            break;

        default:
            break;
        }
    }

    private class CountAsyc extends AsyncTask<Void, Void, List<Count>> {
        private String id;
        private int countId;

        CountAsyc(String id, int countId) {
            this.id = id;
            this.countId = countId;
        }

        @Override
        protected List<Count> doInBackground(Void... params) {
            return SinaWeibo.getInstance().getCount(getContext(), id);
        }

        @Override
        protected void onPostExecute(List<Count> result) {
            mHandler.sendMessage(Message.obtain(mHandler, countId, result));
        }
    }

    private class favriteAsyc extends AsyncTask<Void, Void, Boolean> {
        private String id;
        private int countId;

        favriteAsyc(String id, int countId) {
            this.id = id;
            this.countId = countId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return SinaWeibo.getInstance().createFavorite(getContext(), Long.valueOf(id));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mHandler.sendMessage(Message.obtain(mHandler, countId, result));
        }
    }
}
