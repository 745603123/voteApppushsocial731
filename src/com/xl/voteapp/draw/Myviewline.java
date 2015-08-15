package com.xl.voteapp.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class Myviewline extends View {
    public static int num,L, H, B[],b[][]=new int[num][4],count;
    public static String [] commentname;
	Paint mPaint,mPaint1,mPaint2; //����,�����˻�����ͼ�Ρ��ı��ȵ���ʽ����ɫ��Ϣ   
	   public Myviewline(Context context, int[] b2, String[] commentname, int width, int i) {   
	       super(context);   	
	       B=b2;
	       L=width;
	       this.commentname=commentname;
	       num=i;
	       H=L*2/3;
	       count=distinct(B,num);
	   }   	      
	   public Myviewline(Context context, AttributeSet attrs){   
	       super(context, attrs);   
	   }   

public void onDraw(Canvas canvas){   
       super.onDraw(canvas);    	  	  
          mPaint = new Paint(); 
          mPaint1 =new Paint();
          mPaint2 =new Paint();
   	      mPaint.setAntiAlias(true);
   	      mPaint1.setAntiAlias(true);
   	      mPaint2.setAntiAlias(true);
	      mPaint1.setTextSize(20-count);
	      mPaint.setStyle(Style.FILL); //�������   
	      mPaint1.setStyle(Style.FILL);
	      mPaint.setColor(Color.BLACK); 
	      mPaint1.setStrokeWidth(20-num/10);
	      PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
	      mPaint2.setPathEffect(effects);
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
	     {  mPaint1.setColor(Color.rgb(i*70, i*200, i*360)) ;
	        mPaint2.setColor(Color.rgb(i*70, i*200, i*360)) ;
	        canvas.drawLine(40,L-b[i][0],b[i][1]+75,L-b[i][0],mPaint2);
	        canvas.drawText("" + B[i], 10, L-b[i][0], mPaint1);
	        canvas.drawPoint(b[i][1]+75, L-b[i][0], mPaint1);
	        if(i!=num-1)
	        canvas.drawLine(b[i][1]+75, L-b[i][0],b[i+1][1]+75, L-b[i+1][0], mPaint);
	        canvas.drawPoint(/*L+*/50,b[i][1]+65+L, mPaint1);
	        canvas.drawText("" + commentname[i],/*L+*/90, b[i][1]+75+L, mPaint1);
	        canvas.drawText("" + B[i],b[i][1]+75, L-b[i][0]-15, mPaint1);
	        } //���ƾ���   
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

private int distinct(int[] B,int k) {
	// TODO Auto-generated method stub
	int count;
     count=(Max(B)-Min(B))/10;
	return count;
}


public static int[][] getpoint(int B[]) {
	int max=Max(B),A;
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
}
