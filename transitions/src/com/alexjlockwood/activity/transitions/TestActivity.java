package com.alexjlockwood.activity.transitions;

import static com.alexjlockwood.activity.transitions.Constants.ALBUM_IMAGE_URLS;
import static com.alexjlockwood.activity.transitions.Constants.ALBUM_NAMES;
import static com.alexjlockwood.activity.transitions.Constants.BACKGROUND_IMAGE_URLS;
import static com.alexjlockwood.activity.transitions.MainActivity.EXTRA_CURRENT_ALBUM_POSITION;
import static com.alexjlockwood.activity.transitions.MainActivity.EXTRA_STARTING_ALBUM_POSITION;

import java.util.List;
import java.util.Map;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends Activity{

	private int mCurrentPosition;
    private int mStartingPosition;
    private boolean mIsReturning;
    
    private ImageView mAlbumImage;
    private ImageView backgroundImage;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_details);
		
		
		mStartingPosition = getIntent().getIntExtra(EXTRA_STARTING_ALBUM_POSITION, 0);
		mCurrentPosition = mStartingPosition;
		
//		postponeEnterTransition();
//		setEnterSharedElementCallback(mCallback);
//		
		initview();
	}
	
	@TargetApi(21)
	public void initview(){
		mAlbumImage = (ImageView) findViewById(R.id.details_album_image);
		backgroundImage = (ImageView) findViewById(R.id.details_background_image);
		
		View textContainer = findViewById(R.id.details_text_container);
        TextView albumTitleText = (TextView) textContainer.findViewById(R.id.details_album_title);

        String albumImageUrl = ALBUM_IMAGE_URLS[mCurrentPosition];
        String backgroundImageUrl = BACKGROUND_IMAGE_URLS[mCurrentPosition];
        String albumName = ALBUM_NAMES[mCurrentPosition];

        albumTitleText.setText(albumName);
        backgroundImage.setTransitionName(albumName);

        RequestCreator albumImageRequest = Picasso.with(this).load(albumImageUrl);
        albumImageRequest.noFade();
        albumImageRequest.into(backgroundImage, mImageCallback);
        
//        RequestCreator backgroundImageRequest = Picasso.with(this).load(backgroundImageUrl).fit().centerCrop();
//		backgroundImageRequest.noFade();
//		backgroundImage.setAlpha(0f);
//		getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
//			@Override
//			public void onTransitionEnd(Transition transition) {
//				backgroundImage.animate().setDuration(1000).alpha(1f);
//			}
//		});
//		backgroundImageRequest.into(backgroundImage);
	}
	
	
	
	
//    @Override
//    public void finishAfterTransition() {
//        mIsReturning = true;
//        Intent data = new Intent();
//        data.putExtra(EXTRA_STARTING_ALBUM_POSITION, mStartingPosition);
//        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, mCurrentPosition);
//        setResult(RESULT_OK, data);
//        super.finishAfterTransition();
//    }

	@TargetApi(21)
    public void startPostponedEnterTransition() {
        //if (mAlbumPosition == mStartingPosition) {
            mAlbumImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mAlbumImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    TestActivity.this.startPostponedEnterTransition();
                    return true;
                }
            });
        //}
    }
	
	@SuppressLint("NewApi")
	private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = mAlbumImage;
                if (sharedElement == null) {
                    // If shared element is null, then it has been scrolled off screen and
                    // no longer visible. In this case we cancel the shared element transition by
                    // removing the shared element from the shared elements map.
                    names.clear();
                    sharedElements.clear();
                } else if (mStartingPosition != mCurrentPosition) {
                    // If the user has swiped to a different ViewPager page, then we need to
                    // remove the old shared element and replace it with the new shared element
                    // that should be transitioned instead.
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };
    
    @SuppressLint("NewApi")
	private final Callback mImageCallback = new Callback() {
        @Override
        public void onSuccess() {
            startPostponedEnterTransition();
        }

        @Override
        public void onError() {
            startPostponedEnterTransition();
        }
    };
}
