package com.xl.voteapp.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class Myviewcircle extends View {
	Paint mPaint;
	Paint mPaint1;
	public static int num,L, H, B[],b[][]=new int[num][4],count;
	public static String [] commentname;
	public Myviewcircle(Context context, int[] b, String[] commentname, int width2, int i) {
		super(context);
		   B=b;
	       L=width2;
	       this.commentname=commentname;
	       num=i;
		
	}
	   public Myviewcircle(Context context, AttributeSet attrs){   
	       super(context, attrs);   
	   }
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int B[]=this.B;
		
		for(int i=num;i<B.length;i++){
			B[i]=0;
		}
		
		
		int c[][]=new int[num][2];
	//	int count=distinct(B,MainActivity.i);
		mPaint = new Paint();
		mPaint1 = new Paint();
	//	mPaint2=new Paint();
		mPaint.setAntiAlias(true);
		mPaint1.setAntiAlias(true);
	//	mPaint2.setAntiAlias(true);
	    mPaint.setColor(Color.BLACK); 
	    mPaint1.setTextSize((float) (20));
		mPaint1.setStrokeWidth(15-num/10);
	    mPaint.setStyle(Style.FILL); //�������   
	    mPaint1.setStyle(Style.FILL);
	     float jd[]=new float[B.length];
	     float jdh[]=new float[B.length];
	     c=getpoint(B);    
	      jd=getJD(B);
	      jdh=getJDH(jd);
	      RectF rectF = new RectF(50, 50, L*2/3, L*2/3);	      
	     for(int i=0;i<num;i++)
	     { mPaint.setColor(Color.rgb(i*70, i*200, i*360));
	      mPaint1.setColor(Color.rgb(i*70, i*200, i*360));
		  canvas.drawArc(rectF,jdh[i]*360,jd[i]* 360, true, mPaint);
		  
		 /* if(i<=5 )*/
		  canvas.drawPoint(/*L*2/3+*/50,c[i][1]+L*5/6, mPaint1);
/*		 if(i>=6&&i<=11) 
			 canvas.drawPoint(20,c[i][1]+270, mPaint1);*/
	     
	     
	     canvas.drawText(Math.round(jd[i] * 100) + "%" , /*L*2/3+*/200+commentname[i].length(),c[i][1]+L*5/6+5, mPaint1);
	     canvas.drawText("" + commentname[i],/*L*2/3+*/90,c[i][1]+L*5/6+8, mPaint1);
	     }
	}
	
public static int[][] getpoint(int B[]) {
	int H=100,max=Max(B),L=400,A;
	int a[][]=new int[num][2];
	if(max!=0)
		   A=H/max;
		else
		  A=H;
    for (int i=0;i<num;i++)
    {
   if(i==0)
    {   a[i][0]=(int)(B[i]*A);
    	a[i][1]=0;
    }else{
    	a[i][0]=(int)(B[i]*A);
    	a[i][1]=a[i-1][1]+L/(2*num)+L/(4*num);
    }
    }	
	return a;
} 
	private static int Max(int[] B) {
		   int max=B[0];
		   for (int i= 0; i <num; i++)   			
			   if (B[i] > max) max = B[i];			  
		return max;
	}
	private float[] getJD(int[] B) {
		// TODO Auto-generated method stub
		float sum=0;
		float b[]=new float[B.length];		
		for(int i=0;i<B.length;i++)
			sum=sum+B[i];
		for(int j=0;j<B.length;j++)
		b[j]=B[j]/sum;				     
		return b;
	}  
	   
	private float[] getJDH(float[] b) {
		// TODO Auto-generated method stub
		float jdh[]=new float[b.length];
		float sum=0;
		int i;
		for(i=0;i<b.length;i++)
			if(i==0)jdh[i]=0;
			else
			 {for(int j=i;j>0;j--)
				sum=sum+b[j-1];
		        jdh[i]=sum;
		        sum=0;}	
		return jdh;
	}   
	
	   
}
