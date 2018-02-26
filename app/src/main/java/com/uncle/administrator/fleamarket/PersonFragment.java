package com.uncle.administrator.fleamarket;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class PersonFragment extends Fragment implements View.OnClickListener {
    private LinearLayout  person;
    private TextView tv_name,tv_school;
    private ImageView icon,set,sell,sold,buy,zan;
    private  SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.the_base_button_3, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();


    }
    public void init(){
        set = (ImageView) getActivity().findViewById(R.id.bt3_set);
        sell = (ImageView) getActivity().findViewById(R.id.bt3_my_sell);
        sold = (ImageView) getActivity().findViewById(R.id.bt3_my_sold);
        buy = (ImageView) getActivity().findViewById(R.id.bt3_my_buy);
        zan = (ImageView) getActivity().findViewById(R.id.bt3_my_zan);
        sell.setOnClickListener(this);
        set.setOnClickListener(this);
        sold.setOnClickListener(this);
        buy.setOnClickListener(this);
        zan.setOnClickListener(this);


        person = (LinearLayout) getActivity().findViewById(R.id.bt3_person_data);
        tv_name = (TextView) getActivity().findViewById(R.id.the_base_button_3_name);
        tv_school = (TextView) getActivity().findViewById(R.id.the_base_button_3_school);
        icon = (ImageView) getActivity().findViewById(R.id.the_base_button_3_icon);

        change_person_data();
        get_data_from_sharedPreferences();
    }

    //跳转———修改人物资料
    private void change_person_data(){
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChangePersonDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intent_to_null_activity(){
        Intent intent = new Intent(getContext(),NullActivity.class);
        startActivity(intent);
    }

    private void get_data_from_sharedPreferences(){
         sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("nick_name", null);
        String college = sharedPreferences.getString("college", null);
        String organization = sharedPreferences.getString("organization", null);
        String head_portrait_adress = sharedPreferences.getString("head_portrait_adress", null);
        System.out.println("head_portrait_adress的地址是");
        System.out.println(head_portrait_adress);
        System.out.println(head_portrait_adress);

        tv_name.setText(name);
        tv_school.setText(college+"·"+organization);

        if (head_portrait_adress == null){
            System.out.println("这里被执行了");
            icon.setImageResource(R.drawable.person);
        }else {

                System.out.println("我这妞不错");
                icon.setImageURI(Uri.parse(head_portrait_adress));

        }



    }


    @Override
    public void onStart() {
        super.onStart();
        String refresh = sharedPreferences.getString("refresh", "false");

        if (refresh.equals("ture")){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh","false");
            editor.commit();
            get_data_from_sharedPreferences();
        }
    }
//
//    public void SellActivity(){
//
//        clean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////
////                Chat_data_Dao dao = new Chat_data_Dao(getContext());
////                dao.delete();
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_WORLD_READABLE);
//                SharedPreferences.Editor editor =sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
////                goods_SQL_OpenHelper helper = new goods_SQL_OpenHelper(getContext());//开启数据库
////                helper.getWritableDatabase();
////                goods_Dao dao = new goods_Dao(getContext());
////                dao.delete();
//                Toast.makeText(getContext(),"clean",Toast.LENGTH_SHORT ).show();
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt3_my_buy:
                intent_to_null_activity();
                break;
            case R.id.bt3_my_sell:
                intent_to_null_activity();
                break;
            case R.id.bt3_my_sold:
                intent_to_null_activity();
                break;
            case R.id.bt3_my_zan:
                intent_to_null_activity();
                break;
            case R.id.bt3_set:
                intent_to_null_activity();
                break;
        }
    }
}
