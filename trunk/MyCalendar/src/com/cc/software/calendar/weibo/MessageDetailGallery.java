package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;

import java.util.List;

import weibo4android.Status;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cc.software.calendar.util.LoadImageAysnc;
import com.cc.software.calendar.util.LoadImageAysnc.ImageCallBack;

public class MessageDetailGallery extends LinearLayout implements OnClickListener {
    FlingGallery mGallery = null;
    GalleryAdapter mAdapter = null;
    private LoadImageAysnc loadImageAysnc;

    public MessageDetailGallery(Context context) {
        this(context, null);
    }

    public MessageDetailGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.weibo_message_detail_gallery, this);
        loadImageAysnc = new LoadImageAysnc();
    }

    public boolean init(List<Status> lastInfo, int position) {

        mGallery = (FlingGallery) findViewById(R.id.detail_msg_gallery);

        if (mGallery == null)
            return false;

        mAdapter = new GalleryAdapter(lastInfo);

        mGallery.setAdapter(mAdapter, position);
        mGallery.setIsGalleryCircular(false);

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //return mGallery.onGalleryTouchEvent(ev);

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGallery.onGalleryTouchEvent(event);
    }

    public class GalleryAdapter extends BaseAdapter {
        List<Status> mLastInfo = null;

        public GalleryAdapter(List<Status> lastinfo) {
            mLastInfo = lastinfo;
        }

        @Override
        public int getCount() {
            if (mLastInfo != null)
                return mLastInfo.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mLastInfo != null && position < mLastInfo.size())
                return mLastInfo.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mLastInfo != null && position < mLastInfo.size())
                return position;
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mLastInfo == null || mLastInfo.isEmpty())
                return null;
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.weibo_message_detail_view, null);
                holder = new Holder();
                holder.content = (TextView) convertView.findViewById(R.id.detail_msg_detail_content);
                holder.publishTime = (TextView) convertView.findViewById(R.id.detail_msg_publish_time);
                holder.userName = (TextView) convertView.findViewById(R.id.detail_msg_publisher_name);
                holder.userIcon = (ImageView) convertView.findViewById(R.id.detail_publisher_user_photo);
                holder.contentFiture = (ImageView) convertView.findViewById(R.id.msg_figure);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            Status status = mLastInfo.get(position);
            if (status == null) {
                return convertView;
            }
            final int id1 = status.hashCode() + 1;
            holder.userIcon.setTag(id1);

            final int id2 = status.hashCode() + 2;
            holder.contentFiture.setTag(id2);
            holder.userName.setText(status.getUser().getName());
            holder.publishTime.setText(status.getCreatedAt().toLocaleString());
            holder.content.setText(Html.fromHtml(MessageListView.Tag + status.getText()));
            Bitmap drawable1 = loadImageAysnc.loadImage(status.getUser().getProfileImageURL().toString(),
                            new ImageCallBack() {
                                @Override
                                public void imageLoaded(Bitmap drawable, String iconSrc) {
                                    ImageView imageViewTag = (ImageView) mGallery.findViewWithTag(id1);
                                    if (imageViewTag != null) {
                                        if (drawable != null)
                                            imageViewTag.setImageBitmap(drawable);
                                    }
                                }
                            });
            if (drawable1 != null)
                holder.userIcon.setImageBitmap(drawable1);
            else {
                holder.userIcon.setBackgroundResource(R.drawable.icon);
            }
            Bitmap drawable2 = loadImageAysnc.loadImage(status.getThumbnail_pic(), new ImageCallBack() {
                @Override
                public void imageLoaded(Bitmap drawable, String iconSrc) {
                    ImageView imageViewTag = (ImageView) mGallery.findViewWithTag(id2);
                    if (imageViewTag != null) {
                        if (drawable != null)
                            imageViewTag.setImageBitmap(drawable);

                    }
                }
            });
            if (drawable2 != null) {
                holder.contentFiture.setImageBitmap(drawable2);
            } else {
                holder.contentFiture.setImageDrawable(null);
            }

            holder.contentFiture.setOnClickListener(MessageDetailGallery.this);

            return convertView;
        }
    }

    class Holder {
        ImageView userIcon;
        TextView userName;
        TextView publishTime;
        TextView content;
        ImageView contentFiture;
    }

    @Override
    public void onClick(View v) {
        //System.out.println("zhixing");
    }
}
