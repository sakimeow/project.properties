/**
 * 
 * @author whl
 * @date��2013-7-20 ����3:49:25
 */

package com.shixi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BackupAndRestore {
	private Context mContext = null;
	private String[] fileList = null; // ���ݿ��ļ��б�
	private int choicePostion = -3; // ѡ�����ݿ��б��е�λ��
	private AlertDialog dialog = null;
	private String BACK_FOLDER = "backup";
	private String appName = "myApp";

	public BackupAndRestore(Context context) {
		mContext = context;
	}

	/**
	 * �ָ����ݵ�Dialog
	 */
	public void restoreDB() {
		fileList = getFileList();
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("�ָ�");
		builder.setSingleChoiceItems(getFileList(), -1, new DialogClick());
		builder.setPositiveButton("ȷ��", new DialogClick());
		builder.setNegativeButton("ȡ��", new DialogClick());
		builder.show();
	}

	/**
	 * �������ݿ�
	 */
	public void backupDB() {
		showDialog("�Ƿ񱸷����ݿ�", 'B');
	}

	/**
	 * ��ʾһ��Dialog 
	 * @param title ���� ������������ԴID resource ID
	 * @param sign ���ݱ�ʾ���÷��� I - �ָ�Ĭ������ D - �ָ�Ĭ������ H -ѡ������
	 */
	private void showDialog(String title, char sign) {
		final char s = sign;
		new AlertDialog.Builder(mContext).setTitle(title)
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogI, int which) {
						switch (s) {
						case 'B': // �������ݿ�
							if (dialog == null) {
								dialog = awaitDialog(mContext);
							} else {
								dialog.show();
							}
							new ExecuteTask().execute('B');
							
							break;
						default:
							break;
						}
					}
				}).setNegativeButton("ȡ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	/**
	 * ���ݲ���
	 * @return
	 */
	private boolean backUp() {
		boolean isOk = false;
		//File.separator��ʾ�ļ�·���ķָ���
		String sp = File.separator;
		File sdFile = sdCardOk();
		if (sdFile != null) {
			try {
				String[] dbNames = { "basicInfo.db" };
				// ���������ļ���
				String folder_date = datePrefix();
				File f = new File(sdFile.getAbsolutePath() + sp + folder_date);
				if (!f.exists()) {
					f.mkdirs();		//�����ڲ����ڵ�·���д����ļ���
				}
				for (int i = 0; i < dbNames.length; i++) {
					String dbName = dbNames[i];
					File dbFile = dbOk(dbName);
					if (dbFile != null) {
						File backFile = new File(f.getAbsolutePath() + sp
								+ dbFile.getName());
						backFile.createNewFile();
						isOk = fileCopy(backFile, dbFile.getAbsoluteFile());
						if (!isOk) {
							break;
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isOk;
	}

	/**
	 * ʱ��ǰ׺
	 * @return
	 */
	private String datePrefix() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date(System.currentTimeMillis());
		String str = format.format(date);
		return str;
	}

	/**
	 * �ļ����б�
	 * @return
	 */
	private String[] getFileList() {
		String[] fileList = null;
		File file = sdCardOk();
		if (file != null) {
			File[] list = file.listFiles();
			if (list != null && list.length > 0) {
				fileList = new String[list.length];
				for (int i = 0; i < list.length; i++) {
					fileList[i] = list[i].getName();
				}
			}
		}
		return fileList;
	}

	/**
	 * sdCard�Ƿ���� ���ݵ��ļ����Ƿ����
	 * @return null����ʹ��
	 */
	private File sdCardOk() {
		File bkFile = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			String sp = File.separator;
			String backUpPath = Environment.getExternalStorageDirectory() + sp
					+ appName + sp + BACK_FOLDER;
			bkFile = new File(backUpPath);
			if (!bkFile.exists()) {
				bkFile.mkdirs();
			} else
				return bkFile;
		} else
			Toast.makeText(mContext, "Sdcard ������", Toast.LENGTH_SHORT).show();
		return bkFile;
	}

	/**
	 * �ָ����ݿ�
	 * 
	 * @param name ѡ����ļ����� ѡ�е����ݿ�����
	 * @param resoreDbName ��Ҫ�ָ������ݿ�����
	 * @return
	 */
	public boolean restore(String name, File f) {
		boolean isOk = false;
		if (f != null) {
			File dbFile = dbOk(name);
			try {
				// System.out.println("���ǵ�����"+dbName);
				if (dbFile != null) {
					isOk = fileCopy(dbFile, f.getAbsoluteFile());
				} else
					isOk = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isOk;
	}

	/**
	 * ���ݿ��ļ��Ƿ���ڣ�������ʹ��
	 * @return
	 */
	private File dbOk(String dbName) {
		String sp = File.separator;
		String absPath = Environment.getDataDirectory().getAbsolutePath();
		String pakName = mContext.getPackageName();
		
		String dbPath = sp + "data" + sp + absPath + sp + pakName + sp + "databases"
				+ sp + dbName;
		
		File file = new File(dbPath);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	/**
	 * �Ⱥ򶯻�
	 */
	public AlertDialog awaitDialog(Context context) {
		ProgressBar bar = new ProgressBar(context);
		bar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = 50;
		params.height = 50;
		window.setAttributes(params);
		window.setContentView(bar);
		return dialog;
	}

	/**
	 * 
	 * @param outFile д��
	 * @param inFile ��ȡ
	 * @throws FileNotFoundException
	 */
	private boolean fileCopy(File outFile, File inFile) throws IOException {
		if (outFile == null || inFile == null) {
			return false;
		}
		boolean isOk = true;
		FileChannel inChannel = new FileInputStream(inFile).getChannel();// ֻ��
		FileChannel outChannel = new FileOutputStream(outFile).getChannel();// ֻд
		try {
			long size = inChannel.transferTo(0, inChannel.size(), outChannel);
			if (size <= 0) {
				isOk = false;
			}
		} catch (IOException e) {
			isOk = false;
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
		return isOk;
	}

	private class DialogClick implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == -1) {// ȷ��
				if (choicePostion < 0) {
					Toast.makeText(mContext, "ѡ�����ݿ�", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String sp = File.separator;
				String folderName = fileList[choicePostion];
				String backUpPath = Environment.getExternalStorageDirectory()
						+ sp + appName + sp + BACK_FOLDER + sp + folderName;
				
				File file = new File(backUpPath);
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					boolean isOk = false;
					for (int i = 0; i < files.length; i++) {
						File f = files[i];
						isOk = restore(f.getName(), f);
						if (!isOk) {
							String fail_msg = "�ָ�ʧ��" + ":" + f.getName();
							Toast.makeText(mContext, fail_msg,
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
					if (isOk) {
						// �����������������Ҫˢ�³��µ�����

					}
				}
			} else if (which == -2) {// ȡ��
			} else if (which >= 0) {
				choicePostion = which;
			}
		}
	}

	/**
	 * ִ������
	 * 
	 * @author Administrator
	 * 
	 */
	private class ExecuteTask extends AsyncTask<Character, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Character... params) {
			char c = params[0];
			if (c == 'B') {
				backUp();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}
}
