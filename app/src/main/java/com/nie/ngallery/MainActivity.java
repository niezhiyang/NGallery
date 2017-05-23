package com.nie.ngallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nie.ngallery.adater.MyPageradapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GalleryViewPager mViewPager;
    private SimpleAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (GalleryViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        mPagerAdapter = new SimpleAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        initData();
    }

    private void initData() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.wo);
        list.add(R.drawable.wo1);
        list.add(R.drawable.wo2);
        list.add(R.drawable.wo3);
        list.add(R.drawable.wo4);
        list.add(R.drawable.wo5);
        list.add(R.drawable.wo);
        list.add(R.drawable.wo1);


        //设置OffscreenPageLimit
        mViewPager.setOffscreenPageLimit(Math.min(list.size(), 5));
        mPagerAdapter.addAll(list);
    }

    public class SimpleAdapter extends MyPageradapter {

        private final List<Integer> mList;
        private final Context mContext;

        public SimpleAdapter(Context context) {
            mList = new ArrayList<>();
            mContext = context;
        }

        public void addAll(List<Integer> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            ImageView imageView = null;
            if (convertView == null) {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setTag(position);
            imageView.setImageResource(mList.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tag = (Integer) mViewPager.getChildAt(mViewPager.getCurrentItem()).getTag();
                    Log.i("wwwwwwwwss", mViewPager.getCurrentItem()+ "------" + position+"_______"+tag);
                    if ((mViewPager.getCurrentItem() ) == position) {
                        Toast.makeText(mContext, "点击的位置是:::"+position, Toast.LENGTH_SHORT).show();
                    }

                }
            });
//            }

            return imageView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }


}