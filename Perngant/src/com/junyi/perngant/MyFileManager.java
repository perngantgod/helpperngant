package com.junyi.perngant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.junyi.perngant.adapter.MyAdapter;
import com.junyi.perngant.util.EventDiary;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyFileManager extends ListActivity {

    private List<String>items =null;

    private List<String>paths =null;

    private String rootPath = getSDDir();

    private String curPath = getSDDir();

    private TextView mPath;

 

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fileselect);

        mPath = (TextView)findViewById(R.id.mPath);

        getFileDir(rootPath);

    }

 

    private void getFileDir(String filePath) {

        mPath.setText(filePath);

        items = new ArrayList<String>();

        paths = new ArrayList<String>();

        File f = new File(filePath);

        File[] files = f.listFiles();

        if (!filePath.equals(rootPath)) {

            items.add("b1");

            paths.add(rootPath);

            items.add("b2");

            paths.add(f.getParent());

        }

        for (int i = 0; i < files.length; i++) {

            File file = files[i];

            // check file is txt file or not

            // if is,add into list to show

            if (checkShapeFile(file)){

                items.add(file.getName());

                paths.add(file.getPath());

            }

        }

 

        setListAdapter(new MyAdapter(this,items,paths));

    }

 

    // open allocate file

    @Override

    protected void onListItemClick(ListView l, View v, final int position,long id) {
        File file = new File(paths.get(position));
//        if (file.isDirectory()) {
//            curPath = paths.get(position);
//            getFileDir(paths.get(position));
//        }else{
//            Intent data = new Intent(MyFileManager.this,
//                    SettingActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("file",file.getPath());
//            data.putExtras(bundle);
//            setResult(2,data);
//            finish();
//        }
        
        
        if (file.isDirectory()) {
            curPath = paths.get(position);
                 AlertDialog.Builder adb = new AlertDialog.Builder(this);
                 adb.setTitle("选择存储目录为: " + curPath);
//                 adb.setMessage(content);
                 adb.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    Intent data = new Intent(MyFileManager.this,
                                            SettingActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("file",curPath);
                                    data.putExtras(bundle);
                                    setResult(2,data);
                                    finish();
//                                    return;
                                }
                    });
                 adb.setNegativeButton("取消",
                         new DialogInterface.OnClickListener() {
                             public void onClick(
                                     DialogInterface dialoginterface, int i) {
                                 getFileDir(paths.get(position));
//                                 return;
                             }
                 });
                 adb.show();
        }else{ 
            Toast.makeText(this, "不能选择文件奥", Toast.LENGTH_SHORT).show();
        }
        
    }

 

    public boolean checkShapeFile(File file) {

        String fileNameString = file.getName();

        String endNameString = fileNameString.substring(

                fileNameString.lastIndexOf(".") + 1,fileNameString.length())

                .toLowerCase();

        // file is directory or not

        if(file.isDirectory()) {

            return true;

        }

        if (endNameString.equals("txt")) {

            return true;

        }else{

            return false;

        }

    }

 

    protected final String getSDDir() {

        if (!checkSDcard()) {
            Toast.makeText(this,"no sdcard",Toast.LENGTH_SHORT).show();
            return "";
        }

        try {
            String SD_DIR = Environment.getExternalStorageDirectory()
                    .toString();
            return SD_DIR;
        }catch(Exception e) {
            return "";

        }

    }

 

    public boolean checkSDcard() {

        String sdStutusString = Environment.getExternalStorageState();

        if(sdStutusString.equals(Environment.MEDIA_MOUNTED)) {

            return true;

        }else{

            return false;

        }

    }
}