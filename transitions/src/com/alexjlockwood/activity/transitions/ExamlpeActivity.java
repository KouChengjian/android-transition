package com.alexjlockwood.activity.transitions;

import static com.alexjlockwood.activity.transitions.Constants.ALBUM_IMAGE_URLS;
import static com.alexjlockwood.activity.transitions.Constants.ALBUM_NAMES;

import com.squareup.picasso.Picasso;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;




public class ExamlpeActivity extends Activity{

	private RecyclerView mRecyclerView;
	
	
	static final String EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position";
	static final String EXTRA_CURRENT_ALBUM_POSITION =  "extra_current_item_position";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		initview();
	}
	
	public void initview(){
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new GridLayoutManager(this,getResources().getInteger(R.integer.activity_main_num_grid_columns)));
		mRecyclerView.setAdapter(new CardAdapter());
	}
	
	
	
	private class CardAdapter extends RecyclerView.Adapter<CardHolder> {
		private final LayoutInflater mInflater;

		public CardAdapter() {
			mInflater = LayoutInflater.from(ExamlpeActivity.this);
		}

		@Override
		public CardHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			return new CardHolder(mInflater.inflate(R.layout.album_image_card,viewGroup, false));
		}

		@Override
		public void onBindViewHolder(CardHolder holder, int position) {
			holder.bind(position);
		}

		@Override
		public int getItemCount() {
			return ALBUM_IMAGE_URLS.length;
		}
	}

	private class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		
		private final ImageView mAlbumImage;
		private int mAlbumPosition;

		public CardHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			mAlbumImage = (ImageView) itemView.findViewById(R.id.main_card_album_image);
		}

		@TargetApi(21)
		public void bind(int position) {
			Picasso.with(ExamlpeActivity.this).load(ALBUM_IMAGE_URLS[position]).into(mAlbumImage);
			mAlbumImage.setTransitionName(ALBUM_NAMES[position]);
			mAlbumImage.setTag(ALBUM_NAMES[position]);
			mAlbumPosition = position;
		}

		@TargetApi(21)
		@Override
		public void onClick(View v) {
			// starting activity twice?
			Intent intent = new Intent(ExamlpeActivity.this, TestActivity.class);
			intent.putExtra(EXTRA_STARTING_ALBUM_POSITION, mAlbumPosition);
			startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ExamlpeActivity.this, mAlbumImage,mAlbumImage.getTransitionName()).toBundle());
		}
	}
}
