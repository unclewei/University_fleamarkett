package com.uncle.administrator.fleamarket.Login;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uncle.Base.BaseBindingActivity;
import com.uncle.DTO.Profile;
import com.uncle.Util.DialogUtil;
import com.uncle.Util.SPUtil;
import com.uncle.Util.ToastUtil;
import com.uncle.administrator.fleamarket.MainActivity;
import com.uncle.administrator.fleamarket.R;
import com.uncle.administrator.fleamarket.databinding.SetUserDataActivityBinding;
import com.uncle.bomb.BOMBOpenHelper;

/**
 * @author Administrator
 * @date 2017/5/10 0010
 */

public class SetUesrDataActivity extends BaseBindingActivity<SetUserDataActivityBinding> {
    private String college, organization;
    private String[] mItems1, mItems2, mItems3, mItems4;

    @Override
    protected void bindData(SetUserDataActivityBinding dataBinding) {
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.set_user_data_activity;
    }

    private void init() {
        mItems1 = getResources().getStringArray(R.array.spinnername1);
        mItems2 = getResources().getStringArray(R.array.spinnername2);
        mItems3 = getResources().getStringArray(R.array.spinnername3);
        mItems4 = getResources().getStringArray(R.array.spinnername4);

        ArrayAdapter<String> adapterProvince = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems1);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems2);
        ArrayAdapter<String> adapterSchool = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems3);
        ArrayAdapter<String> adapterOrganization = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems4);

        binding.spProvince.setDropDownVerticalOffset(30);
        binding.spCity.setDropDownVerticalOffset(30);
        binding.spSchool.setDropDownVerticalOffset(30);
        binding.spOrganization.setDropDownVerticalOffset(30);

        binding.spProvince.setAdapter(adapterProvince);
        binding.spCity.setAdapter(adapterCity);
        binding.spSchool.setAdapter(adapterSchool);
        binding.spOrganization.setAdapter(adapterOrganization);

        binding.spProvince.setSelection(0);
        binding.spCity.setSelection(0);
        binding.spSchool.setSelection(0);
        binding.spOrganization.setSelection(0);

        spinnerSetOnItemSelectedListener();
        click();
    }

    private void click() {
        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {
        DialogUtil.getInstance(SetUesrDataActivity.this).show();
        if (binding.etName.getText().toString().length() == 0) {
            Toast.makeText(SetUesrDataActivity.this, "请输入你的昵称", Toast.LENGTH_SHORT).show();
            DialogUtil.getInstance(SetUesrDataActivity.this).dismiss();
            return;
        }
        Intent intent = getIntent();
        String nub = intent.getStringExtra("phoneNub");
        String name = binding.etName.getText().toString();
        Profile profile = new Profile(nub, null, name, college, organization, null, null, null);
        SPUtil.getInstance(SetUesrDataActivity.this).saveSP("profile", new Gson().toJson(profile));
        BOMBOpenHelper.getInstance().addAccount(profile, new BOMBOpenHelper.OnBmobStringListener() {
            @Override
            public void onDone(String object) {
                DialogUtil.getInstance(SetUesrDataActivity.this).dismiss();
                Intent intent = new Intent(SetUesrDataActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String failResult) {
                ToastUtil.show(SetUesrDataActivity.this, failResult);
                DialogUtil.getInstance(SetUesrDataActivity.this).dismiss();
            }
        });
    }

    private void spinnerSetOnItemSelectedListener() {
        binding.spSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                college = mItems3[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                college = mItems3[0];
            }
        });

        binding.spOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organization = mItems4[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                organization = mItems4[0];
            }
        });
    }
}
