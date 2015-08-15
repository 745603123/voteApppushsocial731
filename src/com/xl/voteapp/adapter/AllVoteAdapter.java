package com.xl.voteapp.adapter;

import java.util.List;

import com.xl.voteapp.R;
import com.xl.voteapp.base.ListBaseAdapter;
import com.xl.voteapp.bean.Vote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllVoteAdapter extends ArrayAdapter<Vote> {
	private int resourceId;
//	List<Vote> votes;
	
	public AllVoteAdapter(Context context, int resource, List<Vote> votes) {
		super(context, resource, votes);
		resourceId = resource;
	}

	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Vote vote = getItem(position);

		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(getContext()).inflate(resourceId,
					null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.title.setText(vote.getV_title());
		vh.title.setTextColor(parent.getContext().getResources()
				.getColor(R.color.main_black));
		String description = vote.getV_content();
		vh.description.setText(description.trim());
		vh.tv_time.setText(vote.getPubtime());
		vh.source.setText(vote.getU_name());
		vh.comment_count.setText(vote.getV_tnum() + "");

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView description;
		TextView source;
		TextView comment_count;
		TextView tv_time;

		public ViewHolder(View view) {
			tv_time=(TextView)view.findViewById(R.id.tv_time);
			title = (TextView) view.findViewById(R.id.tv_title);
			description = (TextView) view.findViewById(R.id.tv_description);
			source = (TextView) view.findViewById(R.id.tv_source);
			comment_count = (TextView) view.findViewById(R.id.tv_comment_count);

		}
	}
}
