package com.xl.voteapp.draw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class Myviewbar extends View {

    public static int num,L, H, B[],b[][]=new int[num][4],count;
    public static String [] commentname;
    Paint mPaint,mPaint1,mPaint2;
	   public Myviewbar(Context context, int[] b2, String[] commentname, int width, int i) {   
	       super(context);   	           
	       B=b2;
	       L=width;
	       this.commentname=commentname;
	       num=i;
	       H=L*2/3;
	       count=distinct(B,num);
	   }   	      
	   public Myviewbar(Context context, AttributeSet attrs){   
	       super(context, attrs);   
	   }   

public void onDraw(Canvas canvas){   
       super.onDraw(canvas);   
       //Canvas�к��кܶ໭ͼ�Ľӿڣ�������Щ�ӿڣ����ǿ��Ի���������Ҫ��ͼ��   
          
          mPaint = new Paint();
          mPaint1 = new Paint();
          mPaint2 = new Paint();
   	      mPaint.setAntiAlias(true);
   	      mPaint1.setAntiAlias(true);
   	      mPaint2.setAntiAlias(true);
	      mPaint.setStyle(Style.FILL); //�������   
	      PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
	      mPaint2.setPathEffect(effects);
	      mPaint1.setTextSize(20-count);
	    //  mPaint.setStrokeWidth(10-DrawActivity.i/10);
	      mPaint1.setStrokeWidth(20-num/10);
	        //Y�ᡢX��
	      canvas.drawLine(40, 40, 40, L, mPaint);
		  canvas.drawLine(40, L, L+40, L, mPaint);
			// ���ҵļ�ͷ
		   Path path1 = new Path();
		   path1.moveTo(L+40, L);
		   path1.lineTo(L+35, L-5);
		   path1.lineTo(L+45, L);
		   path1.lineTo(L+35, L+5);
		   canvas.drawPath(path1, mPaint);
		   // ���ϵļ�ͷ
		   Path path = new Path();
		   path.moveTo(40, 40);
		   path.lineTo(35, 45);
		   path.lineTo(40, 35);
		   path.lineTo(45, 45);
		   canvas.drawPath(path, mPaint);		  
	      
	      b=getpoint(B);      
	      for(int i=0;i<num;i++)
           //  canvas.drawRect(b[i][0], b[i][1], b[i][2], b[i][3], mPaint); //���ƾ���   
	      { mPaint.setColor(Color.rgb(i*70, i*200, i*360));
	        mPaint1.setColor(Color.rgb(i*70, i*200, i*360));
	        mPaint2.setColor(Color.rgb(i*70, i*200, i*360));
	        canvas.drawLine(40,L-b[i][2],b[i][1]+75,L-b[i][2],mPaint2);
	        canvas.drawText("" + B[i], 10, L-b[i][2], mPaint1);
	        canvas.drawRect(b[i][1]+75, L-b[i][2], b[i][3]+75, b[i][0]+L, mPaint);
	        canvas.drawPoint(/*L+*/50,b[i][1]+65+L, mPaint1);
	        canvas.drawText("" + commentname[i],/*L+*/90, b[i][1]+75+L, mPaint1);
	        canvas.drawText("" + B[i],b[i][1]+L/(4*num)+75, L-b[i][2]-10, mPaint1);
	        } //���ƾ���  
	      
	   }   

private int distinct(int[] B,int k) {
	// TODO Auto-generated method stub
	int count;
     count=(Max(B)-Min(B))/10;
	return count;
}

private static int Min(int[] B) {
	   int min=B[0];
	   for (int i= 0; i <num; i++)   			
		   if (B[i] < min) min = B[i];			  
	return min;
}

private static int Max(int[] B) {
	   int max=B[0];
	   for (int i= 0; i <num; i++)   			
		   if (B[i] > max) max = B[i];			  
	return max;
}
public static int[][] getpoint(int B[]) {
	int max=Max(B),A;
	int a[][]=new int[num][4];
	if(max!=0)
	   A=H/max;
	else
	  A=H;
    for (int i=0;i<num;i++)
    {
   if(i==0)
    {  	a[i][0]=0;
    	a[i][1]=0;
    	a[i][2]=(int)(B[i]*A);
    	a[i][3]=L/(2*num);
    }else{
    	a[i][0]=0;
    	a[i][1]=a[i-1][1]+L/(2*num)+L/(4*num);
    	a[i][2]=(int)(B[i]*A);
    	a[i][3]=a[i-1][3]+L/(2*num)+L/(4*num);
    }
    }	
	return a;
}  
}
