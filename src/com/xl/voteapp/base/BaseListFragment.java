package com.xl.voteapp.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.xl.voteapp.R;
import com.xl.voteapp.adapter.AllVoteAdapter;
import com.xl.voteapp.bean.Entity;
import com.xl.voteapp.bean.Item;
import com.xl.voteapp.bean.RecData;
import com.xl.voteapp.bean.Vote;
import com.xl.voteapp.common.Contanst;
import com.xl.voteapp.ui.VotingActivity;
import com.xl.voteapp.ui.empty.EmptyLayout;
import com.xl.voteapp.upload.DownloadOptionInformation;
import com.xl.voteapp.util.DateUtil;
import com.xl.voteapp.util.GsonRequest;
import com.xl.voteapp.util.UIHelper;

@SuppressLint("NewApi")
public abstract class BaseListFragment<T extends Entity> extends BaseFragment
		implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnScrollListener {

	String messagefull = "false";
	private ProgressDialog mLoginProgressDialog;
	public static final int MESSAGE_STATE_ERROR = -1;
	public static final int MESSAGE_STATE_EMPTY = 0;
	public static final int MESSAGE_STATE_MORE = 1;
	public static final int MESSAGE_STATE_FULL = 2;
	// 没有状态
	public static final int LISTVIEW_ACTION_NONE = -1;
	// 更新状态，不显示toast
	public static final int LISTVIEW_ACTION_UPDATE = 0;
	// 初始化时，加载缓存状态
	public static final int LISTVIEW_ACTION_INIT = 1;
	// 刷新状态，显示toast
	public static final int LISTVIEW_ACTION_REFRESH = 2;
	// 下拉到底部时，获取下一页的状态
	public static final int LISTVIEW_ACTION_SCROLL = 3;
	// 当前页面已加载的数据总和
	private int mSumData;
	// 当前加载状态
	private int mState = STATE_NONE;
	// UI状态
	private int mListViewAction = LISTVIEW_ACTION_NONE;

	// 当前数据状态，如果是已经全部加载，则不再执行滚动到底部就加载的情况
	private int mMessageState = MESSAGE_STATE_MORE;
	private boolean isPauseLife = true;

	static final int STATE_NONE = -1;
	static final int STATE_LOADING = 0;
	static final int STATE_LOADED = 1;

	@InjectView(R.id.error_layout)
	protected EmptyLayout mErrorLayout;

	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
	protected SwipeRefreshLayout mSwipeRefreshLayout;
	protected ListView mListView;

	private View mFooterView;
	private View mFooterProgressBar;
	private TextView mFooterTextView;

	protected ArrayAdapter<T> mAdapter;
	List<Vote> voteList = new ArrayList<Vote>();;
	ArrayList<Item> optionList;

	private static final int REFRESH_COMPLETE = 0X110;
	protected int mStoreEmptyState = -1;
	protected int mCurrentPage = 0;
	protected int mCatalog = 1;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				getVoteList(1, LISTVIEW_ACTION_REFRESH);
				mAdapter.notifyDataSetChanged();
				mSwipeRefreshLayout.setRefreshing(false);
				break;
			case 0:
				RecData recdata = (RecData) msg.obj;
				List<Vote> result = recdata.getListvote();
				messagefull = recdata.getStateFull();
				if (messagefull.equals("true")) {
					setFooterFullState();
				}

				if (msg.arg2 == 1) {
					voteList = result;
					mAdapter = (ArrayAdapter<T>) new AllVoteAdapter(getActivity(), R.layout.list_cell_news, voteList);
					mListView.setAdapter(mAdapter);
					mSumData = result.size();
					mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
				} else {
					mSumData += result.size();
					voteList.addAll(result);
				}
				mAdapter.notifyDataSetChanged();
				break;
			case 1:
				UIHelper.showVoteActivity(getActivity(), voteList.get(msg.arg1).getV_id(),
						voteList.get(msg.arg1).getV_title(), voteList.get(msg.arg1).getV_content(), optionList);
				break;
			case 2:
				mErrorLayout.setErrorType(EmptyLayout.NODATA);
				break;

			}
		};
	};

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_pull_refresh_listview;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mFooterView = inflater.inflate(R.layout.list_cell_footer, null);
		mFooterProgressBar = mFooterView.findViewById(R.id.progressbar);
		mFooterTextView = (TextView) mFooterView.findViewById(R.id.listview_foot_more);
		View view = inflater.inflate(getLayoutId(), container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.inject(this, view);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(this);
		mListView.addFooterView(mFooterView);
		initView(view);
		getVoteList(1, LISTVIEW_ACTION_INIT);
	}

	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mCatalog = args.getInt(BUNDLE_KEY_CATALOG, 0);
		}
	}

	@Override
	public void initView(View view) {
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		  mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	               // mState = STATE_REFRESH;
	            	//Log.i("mErrorLayout", "mErrorLayoutclick");
	                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
	                getVoteList(1, LISTVIEW_ACTION_INIT);
	            }
	        });
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected abstract ArrayAdapter<T> getListAdapter();

	// 下拉刷新数据
	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);

	}

	protected boolean requestDataIfViewCreated() {
		return true;
	}

	protected String getCacheKeyPrefix() {
		return null;
	}

	public void beforeLoading(int action) {
		// 开始加载
		mState = STATE_LOADING;
		if (action == LISTVIEW_ACTION_REFRESH) {
			// setSwipeRefreshLoadingState();
		} else if (action == LISTVIEW_ACTION_SCROLL) {
			setFooterLoadingState();
		}
	}

	public void getVoteList(final int page, final int action) {
		beforeLoading(action);
		boolean refresh = true;
		if (action == LISTVIEW_ACTION_INIT) {
			refresh = false;
		}
		GsonRequest<RecData> gsonObjRequest;
		String url = Contanst.URLPATH+"ReceiveVote";
		Map<String, String> appendHeader = new HashMap<String, String>();  
        appendHeader.put("u_no", String.valueOf(0));  
        appendHeader.put("pageNo", String.valueOf(page)); 
        AppContext.getInstance().addSessionCookie(appendHeader);
        gsonObjRequest = new GsonRequest<RecData>(Request.Method.POST, url/*builder.toString()*/,
        		RecData.class, null, new Response.Listener<RecData>() {
			@Override
			public void onResponse(RecData response) {
				try {
					RecData recdata = response;
					if (recdata == null) {
						mHandler.sendEmptyMessage(2);
					} else {
						Message m = new Message();
						m.obj = recdata;
						m.arg1 = action;
						m.arg2 = page;
						m.what = 0;
						mHandler.sendMessage(m);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
				// For AuthFailure, you can re login with user credentials.
				// For ClientError, 400 & 401, Errors happening on client side when sending api request.
				// In this case you can check how client is forming the api and debug accordingly.
				// For ServerError 5xx, you can do retry or handle accordingly.
				if( error instanceof NetworkError) {
				} else if( error instanceof ClientError) { 
				} else if( error instanceof ServerError) {
				} else if( error instanceof AuthFailureError) {
				} else if( error instanceof ParseError) {
				} else if( error instanceof NoConnectionError) {
				} else if( error instanceof TimeoutError) {
				}
			}
		}, appendHeader);
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(gsonObjRequest);
	
	}


	public void getOptionList(final int v_id, final String v_title, final int position) {
		if(mLoginProgressDialog == null) {
    		mLoginProgressDialog = new ProgressDialog(getActivity());
    		mLoginProgressDialog.setCancelable(true);
    		mLoginProgressDialog.setCanceledOnTouchOutside(false);
    		mLoginProgressDialog.setMessage("正在加载投票信息...");
    	}
    	if(mLoginProgressDialog != null) {
			mLoginProgressDialog.show();
		}
		optionList = new ArrayList<Item>();
		new Thread() {
			public void run() {
				try {
					optionList = DownloadOptionInformation.save(v_title, v_id);
					mLoginProgressDialog.dismiss();	
					Message m = new Message();
					m.arg1 = position;
					m.what = 1;
					mHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position<voteList.size())
		getOptionList(voteList.get(position).getV_id(), voteList.get(position).getV_title(), position);
	}

	// 是否需要自动刷新
	protected boolean needAutoRefresh() {
		return true;
	}

	/***
	 * 获取列表数据
	 * 
	 * 
	 * @author 火蚁 2015-2-9 下午3:16:12
	 * 
	 * @return void
	 * @param refresh
	 */
	protected void requestData(boolean refresh) {
		// String key = getCacheKey();
		// if (isReadCacheData(refresh)) {
		// readCacheData(key);
		// } else {
		// 取新的数据
		sendRequestData();
		// }
	}

	/***
	 * 自动刷新的时间
	 * 
	 * 默认：自动刷新的时间为半天时间
	 * 
	 * @author 火蚁 2015-2-9 下午5:55:11
	 * 
	 * @return long
	 * @return
	 */
	protected long getAutoRefreshTime() {
		return 12 * 60 * 60;
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		 * if (onTimeRefresh()) { onRefresh(); }
		 */
	}

	protected void sendRequestData() {
	}

	/**
	 * 是否需要隐藏listview，显示无数据状态
	 * 
	 * @author 火蚁 2015-1-27 下午6:18:59
	 * 
	 */
	protected boolean needShowEmptyNoData() {
		return true;
	}

	protected boolean compareTo(List<? extends Entity> data, Entity enity) {
		int s = data.size();
		if (enity != null) {
			for (int i = 0; i < s; i++) {
				if (enity.getId() == data.get(i).getId()) {
					return true;
				}
			}
		}
		return false;
	}

	protected void onRefreshNetworkSuccess() {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mAdapter == null || mAdapter.getCount() == 0) {
			return;
		}
		if (messagefull.equals("true"))
			return;
		// 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
		/*
		 * if (mMessageState == MESSAGE_STATE_FULL || mMessageState ==
		 * MESSAGE_STATE_EMPTY || mState == STATE_LOADING) { Log.i("loading",
		 * String.valueOf("3")); return; }
		 */
		// 判断是否滚动到底部

		boolean scrollEnd = false;
		try {
			if (view.getPositionForView(mFooterView) == view.getLastVisiblePosition()) {
				scrollEnd = true;
			}
		} catch (Exception e) {
			scrollEnd = false;
		}
		if (scrollEnd) {
			onLoadNextPage();
		}
	}

	/** 加载下一页 */
	protected void onLoadNextPage() {
		// 当前pageIndex

		int pageIndex = mSumData / 20 + 1;
		loadList(pageIndex, LISTVIEW_ACTION_SCROLL);
	}

	/**
	 * 加载数据
	 * 
	 * @param page
	 *            页码
	 * @param action
	 *            加载的触发事件
	 */
	void loadList(int page, int action) {
		mListViewAction = action;
		getVoteList(page, action);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	// ** 设置顶部正在加载的状态 *//*
	void setSwipeRefreshLoadingState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(true);
			// 防止多次重复刷新
			mSwipeRefreshLayout.setEnabled(false);
		}
	}

	/** 设置顶部加载完毕的状态 */
	void setSwipeRefreshLoadedState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(false);
			mSwipeRefreshLayout.setEnabled(true);
		}

	}

	/** 设置底部有错误的状态 */
	void setFooterErrorState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText(R.string.load_error);
		}
	}

	/** 设置底部有更多数据的状态 */
	void setFooterHasMoreState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText(R.string.load_more);
		}
	}

	/** 设置底部已加载全部的状态 */
	void setFooterFullState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText(R.string.load_full);
		}
	}

	/** 设置底部无数据的状态 */
	void setFooterNoMoreState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.GONE);
			mFooterTextView.setText(R.string.load_empty);
		}
	}

	/** 设置底部加载中的状态 */
	void setFooterLoadingState() {
		if (mFooterView != null) {
			mFooterProgressBar.setVisibility(View.VISIBLE);
			mFooterTextView.setText(R.string.load_ing);
		}
	}

}
