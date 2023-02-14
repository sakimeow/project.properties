package com.shixi.jxctest;

import com.shixi.jxctest.BasicInfo;
import com.shixi.jxctest.MainActivity;
import com.shixi.jxctest.R;
import com.shixi.service.DBOpenHelper;
import com.shixi.service.SailService;
import com.shixi.domain.Sail;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;  
import android.widget.Button;

public class MainActivity extends Activity {
	
	private SailService sailService;
	
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuye);
		
		actionBar=getActionBar();
		actionBar.hide();
	
/*************************������******************************************/
		Button button_xiaoshou = (Button)findViewById(R.id.xiaoshou);
		Button button_caigou = (Button)findViewById(R.id.caigou);
		Button button_cangku = (Button)findViewById(R.id.cangku);
		Button dangan = (Button)findViewById(R.id.dangan);
		
		
		button_xiaoshou.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, XiaoShouActivity.class); 
				startActivity(intent);
			}});
		button_caigou.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, CaiGouActivity.class); 
				startActivity(intent);
			}});
		button_cangku.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, CangKuActivity.class); 
				startActivity(intent);
			}});
		dangan.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BasicInfo.class);
				startActivity(intent);
			}});
		
/*************************************************************************/		
	}
	
	public void onClick_Settings(View v) {
		Intent intent = new Intent();
		intent.setClass(this, Settings.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar, menu);
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg));

		return true;
	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item)  
//	{     
//	   switch (item.getItemId())  
//	   {         
//	      case R.id.back:             
//	         Intent intent = new Intent(this, MainActivity.class);             
//	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//	         startActivity(intent);             
//	         return true;         
//	      default:             
//	         return super.onOptionsItemSelected(item);     
//	   } 
//	} 
	
/*****************************�˳���ʾ����*****************************************/
	 @Override 
	   public void onBackPressed() { 
	new AlertDialog.Builder(this).setTitle("ȷ���˳���") 
	    .setIcon(android.R.drawable.ic_dialog_info) 
	    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
	 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        // �����ȷ�ϡ���Ĳ��� 
	        MainActivity.this.finish(); 
	 
	        } 
	    }) 
	    .setNegativeButton("����", new DialogInterface.OnClickListener() { 
	 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        // ��������ء���Ĳ���,���ﲻ����û���κβ��� 
	        } 
	    }).show(); 
	// super.onBackPressed(); 
	   } 
	
 /*************************************************************************/	
	 
}

