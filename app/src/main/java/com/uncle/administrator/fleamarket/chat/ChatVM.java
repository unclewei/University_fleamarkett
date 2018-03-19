package com.uncle.administrator.fleamarket.chat;

import android.view.View;

import com.uncle.Util.KeyboardUtil;
import com.uncle.administrator.fleamarket.databinding.ActivityChatBinding;

/**
 *
 * @author Administrator
 * @date 2018/3/18 0018
 */

public class ChatVM {

    private ChatActivity activity;
    private ActivityChatBinding binding;
    public ChatVM(ChatActivity activity, ActivityChatBinding binding){
        this.activity = activity;
        this.binding = binding;
        binding.setModule(this);
    }


    public void onEditClick(View view) {
        if (binding.input.layoutMore.getVisibility() == View.VISIBLE) {
            binding.input.layoutAdd.setVisibility(View.GONE);
            binding.input.layoutEmo.setVisibility(View.GONE);
            binding.input.layoutMore.setVisibility(View.GONE);
        }
    }

    public void onEmoClick(View view) {
        if (binding.input.layoutMore.getVisibility() == View.GONE) {
            showEditState(true);
        } else {
            if (binding.input.layoutAdd.getVisibility() == View.VISIBLE) {
                binding.input.layoutAdd.setVisibility(View.GONE);
                binding.input.layoutEmo.setVisibility(View.VISIBLE);
            } else {
                binding.input.layoutMore.setVisibility(View.GONE);
            }
        }
    }

    public void onAddClick(View view) {
        if (binding.input.layoutMore.getVisibility() == View.GONE) {
            binding.input.layoutMore.setVisibility(View.VISIBLE);
            binding.input.layoutAdd.setVisibility(View.VISIBLE);
            binding.input.layoutEmo.setVisibility(View.GONE);
            KeyboardUtil.hideSoftInputView(activity);
        } else {
            if (binding.input.layoutEmo.getVisibility() == View.VISIBLE) {
                binding.input.layoutEmo.setVisibility(View.GONE);
                binding.input.layoutAdd.setVisibility(View.VISIBLE);
            } else {
                binding.input.layoutMore.setVisibility(View.GONE);
            }
        }
    }

    public void onVoiceClick(View view) {

        binding.input.editMsg.setVisibility(View.GONE);
        binding.input.layoutMore.setVisibility(View.GONE);
        binding.input.btnChatVoice.setVisibility(View.GONE);
        binding.input.btnChatKeyboard.setVisibility(View.VISIBLE);
        binding.input.btnSpeak.setVisibility(View.VISIBLE);
        KeyboardUtil.hideSoftInputView(activity);
    }

    public void onKeyClick(View view) {
        showEditState(false);
    }

    public void onSendClick(View view) {
        activity.onSendClick(view);
    }

    public void onPictureClick(View view) {
        activity.onPictureClick(view);
    }

    public void onCameraClick(View view) {
        activity.onCameraClick(view);
    }


    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     *
     * @param isEmo 用于区分文字和表情
     * @return void
     */
    public void showEditState(boolean isEmo) {
        binding.input.editMsg.setVisibility(View.VISIBLE);
        binding.input.btnChatKeyboard.setVisibility(View.GONE);
        binding.input.btnChatVoice.setVisibility(View.VISIBLE);
        binding.input.btnSpeak.setVisibility(View.GONE);
        binding.input.editMsg.requestFocus();
        if (isEmo) {
            binding.input.layoutMore.setVisibility(View.VISIBLE);
            binding.input.layoutMore.setVisibility(View.VISIBLE);
            binding.input.layoutEmo.setVisibility(View.VISIBLE);
            binding.input.layoutAdd.setVisibility(View.GONE);
            KeyboardUtil.hideSoftInputView(activity);
        } else {
            binding.input.layoutMore.setVisibility(View.GONE);
            KeyboardUtil.showSoftInputView(activity, binding.input.editMsg);
        }
    }
}
