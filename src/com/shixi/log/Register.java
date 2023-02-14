/**
 * 
 * @author whl
 * @date��2013-7-20 ����3:49:25
 */

package com.shixi.log;

import com.shixi.domain.Staff;
import com.shixi.domain.StaffAccount;
import com.shixi.jxctest.MainActivity;
import com.shixi.jxctest.R;
import com.shixi.service.DBOpenHelper;
import com.shixi.service.TableService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	private DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
	ActionBar actionBar;
	private StaffAccount staffaccount;
	private TableService tableService = new TableService(this);
	
	private Spinner mySpinner;
	
	private static final String[] question = {"������������ǣ�", "����ϲ���ĵ�Ӱ�ǣ�", "�Ҹ��е������ǣ�", "����ϲ����ʲô��", "����ϲ�������ǣ�"};
	private Integer select = -1;
	
	private ArrayAdapter<String> adapter;
	
	Animation myAnimation;
	
	private String name;
	private String pwd;
	private String pwd_conf;	
	private String ans;	
	
	private Button btn_reg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		btn_reg = (Button)findViewById(R.id.button_reg);
		
		actionBar=getActionBar();
		actionBar.show();
		
		myAnimation = AnimationUtils.loadAnimation(this, R.anim.my_anim);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, question);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		
		mySpinner = (Spinner)findViewById(R.id.reg_spinner);
		mySpinner.setAdapter(adapter);
		mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(select==-1) {
					((TextView)arg1).setText("���ѡ������");
				}
				//����Ŀ�����ֶ�Ӧ
				select = arg2;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		mySpinner.setOnTouchListener(new Spinner.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}});
		
		mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
			}});
		
	}
	
	
	public void onClick_ok(View v) {
		name = ((EditText)findViewById(R.id.reg_name)).getText().toString();
		pwd = ((EditText)findViewById(R.id.reg_pwd)).getText().toString();
		pwd_conf = ((EditText)findViewById(R.id.reg_pwd2)).getText().toString();
		ans = ((EditText)findViewById(R.id.reg_ans)).getText().toString();
		
		if(name==""||pwd==""||pwd_conf==""||ans=="") {
			Toast.makeText(this, "��������������Ϣ", Toast.LENGTH_SHORT).show();
		}
		
		if(select==-1)
			Toast.makeText(this, "��ѡ�����뱣������", Toast.LENGTH_SHORT).show();
		
		if(pwd!=""&&pwd_conf!=""&&name!=""&&pwd.equals(pwd_conf)) {
			Intent intent = new Intent();
			
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();	//������ݿ�Ĳ���ʵ��		
			db.execSQL("insert into staff(name, usrgroup, password, question, answer) values(?, ?, ?, ?, ?)",
					new Object[] {name, "����Ա", pwd, select, ans});
			
			Toast.makeText(getApplicationContext(), "ע��ɹ���",
				     Toast.LENGTH_SHORT).show();
			
			staffaccount.id = tableService.find_byname_s_id(name);
			
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		
		else if(!(pwd.equals(pwd_conf))&&pwd!=""&&pwd_conf!=""&&name!="")
			Toast.makeText(this, "������������벻һ��", Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_nomenu, menu);
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg));
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
