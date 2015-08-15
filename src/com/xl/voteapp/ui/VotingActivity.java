package com.xl.voteapp.ui;

import java.util.ArrayList;
import java.util.List;

import com.xl.voteapp.R;
import com.xl.voteapp.bean.Item;
import com.xl.voteapp.draw.DrawActivity;
import com.xl.voteapp.upload.UploadVotingInformation;
import com.xl.voteapp.util.UIHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class VotingActivity extends Activity {
	private ProgressDialog mLoginProgressDialog;
	private RadioGroup radioGroup = null;
	private Button child;
	TextView etModifyTitle = null;
	TextView etModifyContent = null;
	private Button vote_result;
	String uno = null;
	String visitor = null; // 访问者ID
	String rid = null; // 日记的ID
	String cid = null;
	String a = null;
	String title;
	String content;
	ArrayList<Item> optionlist;
	List<String[]> ticketnum;
	private LinearLayout mLayout;
	private Button addvote;
	private String v_id;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.voting);
		Intent intent = getIntent();
		content = intent.getStringExtra("content");
		title = intent.getStringExtra("v_title");
		v_id=intent.getStringExtra("v_id");
		
		
		optionlist =  (ArrayList<Item>) getIntent().getSerializableExtra("optionlist");  		
		etModifyTitle = (TextView) findViewById(R.id.zhuti); // 获得标题EditText
		etModifyTitle.setText(title);
		etModifyContent = (TextView) findViewById(R.id.xiangxi); // 获得内容EditText
		etModifyContent.setText(content);
		radioGroup = (RadioGroup) findViewById(R.id.select_radiogroup);
		mLayout = (LinearLayout) findViewById(R.id.optionlist);
		
		for (int i = 0; i < optionlist.size(); i++) {
			RadioButton  newRadio =new RadioButton(getApplicationContext());  
            newRadio.setText(optionlist.get(i).getI_content()); 
            newRadio.setId(i);
            newRadio.setTextAppearance(getApplicationContext(), R.style.option_item_text_parent);
            newRadio.setTextSize(16);
            newRadio.setBackgroundResource(R.drawable.list_item_background);
            newRadio.setTextColor(Color.BLACK);
            RadioGroup.LayoutParams lp=new  RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);   
            lp.setMargins(0, 10, 0, 0);;
            newRadio.setLayoutParams(lp);
            radioGroup.addView(newRadio, radioGroup.getChildCount());
		}

		/*
		 * child=(Button)findViewById(R.id.btnAdd); child.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { RadioButton newRadio =new
		 * RadioButton(getApplicationContext()); newRadio.setText("新增");
		 * radioGroup.addView(newRadio, radioGroup.getChildCount()); } } );
		 */

		
		addvote = (Button) findViewById(R.id.addvote);
		addvote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < optionlist.size(); i++)
					if (((RadioButton) findViewById(i)).isChecked()) {
						cid = String.valueOf(optionlist.get(i).getI_id());
					}
	/*			if (btnXuanxianger.isChecked()) {
					cid = cmtInfo[0];
				}
				if (btnXuanxiangsan.isChecked()) {
					cid = cmtInfo[1];
				}
				if (btnXuanxiangsi.isChecked()) {
					cid = cmtInfo[2];
				}
				for (int i = 0; i < optionnum - 3; i++)
					if (((RadioButton) findViewById(i)).isChecked()) {
						cid = cmtList.get(i + 3)[2];
					}*/
				addVote();
			}
		});
		 

		vote_result = (Button) findViewById(R.id.vote_result);
		vote_result.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showDrawActivity(VotingActivity.this,title, content, optionlist);
				
				/*Intent intent = new Intent(VotingActivity.this,
						DrawActivity.class);*/
				/*
				 * intent.putExtra("commentname1", cmtInfo[0]);
				 * intent.putExtra("commentname2", cmtInfo[1]);
				 * intent.putExtra("commentname3", cmtInfo[2]);
				 * intent.putExtra("optionnum", cmtInfonum[0]);
				 * intent.putExtra("option2num", cmtInfonum[1]);
				 * intent.putExtra("option3num", cmtInfonum[2]);
				 * intent.putExtra("width", String.valueOf(dm.widthPixels)); //
				 * intent.putExtra("height", dm.heightPixels);
				 * intent.putExtra("Optionnum", String.valueOf(optionnum)); for
				 * (int i = 0; i < optionnum - 3; i++) { String I =
				 * String.valueOf(i); intent.putExtra("Commentname" + I,
				 * cmtList.get(i + 3)[2]); intent.putExtra("Optionnum" + I,
				 * cmtList.get(i + 3)[1]); }
				 */
			//	startActivity(intent);

			}
		});

	}

	// public void initComments(){
	// cmtList = new ArrayList<String []>();
	// new Thread(){
	// public void run(){
	// ArrayList<String []>cmtList1 = new ArrayList<String []>();
	// try{

	// cmtList= DownloadVotingInformation.save(rid);
	// int size = mc.din.readInt(); //获取评论的个数
	// optionnum=size;
	// btnXuanxianger= (RadioButton)findViewById(R.id.RadioButton00);
	// btnXuanxianger.setText(cmtList.get(0)[0]);
	// btnXuanxiangsan= (RadioButton)findViewById(R.id.RadioButton01);
	// btnXuanxiangsan.setText(cmtList.get(1)[0]);
	// btnXuanxiangsi= (RadioButton)findViewById(R.id.RadioButton02);
	// btnXuanxiangsi.setText(cmtList.get(2)[0]);/**/
	/*
	 * for(int i=0;i<optionnum-3;i++) {
	 * 
	 * 
	 * RadioButton newRadio =new RadioButton(getApplicationContext());
	 * newRadio.setText(cmtList.get(i)[0]); //
	 * newRadio.setBackgroundResource
	 * (R.drawable.zebra_list_single_item_bg_normal);
	 * newRadio.setTextColor(Color.BLACK); newRadio.setWidth(500);
	 * RadioGroup.LayoutParams lp=new
	 * RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	 * LinearLayout.LayoutParams.WRAP_CONTENT); lp.topMargin=20;
	 * newRadio.setLayoutParams(lp); newRadio.setGravity(Gravity.CENTER);
	 * radioGroup.addView(newRadio, radioGroup.getChildCount());
	 * 
	 * }
	 */
	// a=cmtList.get(0)[0];
	// mc.dout.writeUTF("<#READY_TO_READ_COMMENT#>");*/

	/*
	 * if(cmtLis) for(int i=0;i<3;i++){ String msg = ("111|222|333"); //读取每条评论信息
	 * String [] sa = msg.split("\\|"); //切割字符串 cmtList.add(sa); }
	 */
	// }
	// catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// }
	// }.start();
	// return cmtList1;
	// }

	public void addVote() {
		if(mLoginProgressDialog == null) {
    		mLoginProgressDialog = new ProgressDialog(VotingActivity.this);
    		mLoginProgressDialog.setCancelable(true);
    		mLoginProgressDialog.setCanceledOnTouchOutside(false);
    		mLoginProgressDialog.setMessage("正在加载投票信息...");
    	}
    	if(mLoginProgressDialog != null) {
			mLoginProgressDialog.show();
		}
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					boolean result = false;
					result = UploadVotingInformation.save(cid);

					if (result) {
						mLoginProgressDialog.dismiss();	
						Toast.makeText(VotingActivity.this, "投票成功！",							
								Toast.LENGTH_LONG).show();
						
						for (int i = 0; i < optionlist.size(); i++)
							if (((RadioButton) findViewById(i)).isChecked()) {
								optionlist.get(i).setI_num(optionlist.get(i).getI_num()+1);
							}
						
						
						Looper.loop();
					} else {
						mLoginProgressDialog.dismiss();	
						Toast.makeText(VotingActivity.this, "投票失败，请重试！",
								Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
