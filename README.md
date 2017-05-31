# NGallery
利用Viewpager实现真正的Gallery, 左右条目可点击, 左右条目可滑动
# 详情请看博客 http://blog.csdn.net/qq_33408235/article/details/72650452
## 首先看一下效果:
![这里写图片描述](https://github.com/niezhiyang/NGallery/blob/master/1495592800462~1.gif)
<br>
从图上可以看出, 两边的item可以被点击, 可以被滑动,也就是可以获得焦点
## demoapk位置:[demo.apk](https://github.com/niezhiyang/NGallery/blob/master/app-debug.apk)
## 使用方式
### 添加依赖
1.在根目录的build.gradle中添加

	allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }// 添加jitpack.依赖
    }
2.在app的build.gradle中添加

		compile 'com.github.niezhiyang:NGallery:v1.0.1'
### 在使用的xml中,用一个父控件包裹住GalleryViewPager,目的是为了控制父控件的滑动,和点击,来设置Viewpager的状态

	<RelativeLayout
        android:id="@+id/root"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipChildren="false"
        >
        <!--是否限制子View在其范围内，我们将其值设置为false后那么当子控件的高度高于父控件时也会完全显示,而不会被压缩-->
        <com.nie.ngallerylibrary.GalleryViewPager
            android:id="@+id/viewpager"
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_centerInParent="true"
            android:clipChildren="false"
            android:overScrollMode="never"/>
    </RelativeLayout>
### 代码中设置

	 	mViewPager = (GalleryViewPager) findViewById(R.id.viewpager);//找到这个控件
        mViewPager.setPageTransformer(true, new ScalePageTransformer());//设置PageTransformer,本库只有一个ScalePageTransformer,如果这个ScalePageTransformer满足不了您的需求,您可以自己写一个PageTransformer
        findViewById(R.id.root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });//找到这个父控件设置他的listener

        mPagerAdapter = new SimpleAdapter(this);//初始化adapter
        mViewPager.setAdapter(mPagerAdapter);//设置adapter
adapter要继承MyPageradapter;例如
	    
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
                    if ((mViewPager.getCurrentItem() ) == position) {
                        Toast.makeText(mContext, "点击的位置是:::"+position, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            return imageView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
    
 ## 这只不过是简单的添加imageview,你也可以添加fragment做出绚丽的效果,还可以跟任意的viewpgaerindicator使用了,是不是想想就高兴了


