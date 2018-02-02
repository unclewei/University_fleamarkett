package com.uncle.bomb;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;


public class BOMBOpenHelper {


    private final IMConversation im = new IMConversation();
    private final IMConversation im2 = new IMConversation();
    private final IMConversation im3 = new IMConversation();
    private int temp_nub;//临时数字，用来记录有多少个图片，后面防止上传重复问题。
    private int temp_nub_image = 0;//临时数字，用来记录有多少个图片，后面防止上传重复问题。


    //----商品-----增删改查-----------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * 创建一条商品信息
     */
    public void createPerson(ShopGoods goods) {
        goods.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    System.out.println("整个id是：" + objectId);
                } else {
                    System.out.println("错误的信息是是：" + e.getErrorCode() + "-----------" + e.getMessage());
                }
            }

        });
    }


    public void queryGoodsAndDoing() {
        final List<ShopGoods> listGoods = new ArrayList<>();
        BmobQuery<ShopGoods> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo(null, null);
//        bmobQuery.setLimit(1);
        bmobQuery.findObjects(new FindListener<ShopGoods>() {
            @Override
            public void done(List<ShopGoods> list, BmobException e) {
                if (e == null) {

                }
            }
        });
    }


    //上传文件，上传图片进入数据库，然后创建商品，与上连用
    public void uploadImg(final ShopGoods shopGoods) {
        final String[] filePaths = new String[shopGoods.getPictureNub()];
        temp_nub = shopGoods.getPictureNub();
        filePaths[0] = shopGoods.getImage1();
        filePaths[1] = shopGoods.getImage2();
        filePaths[2] = shopGoods.getImage3();
        switch (shopGoods.getPictureNub()) {
            case 3:
                break;
            case 4:
                filePaths[3] = shopGoods.getImage4();
                break;
            case 5:
                filePaths[3] = shopGoods.getImage4();
                filePaths[4] = shopGoods.getImage5();
                break;
            case 6:
                filePaths[3] = shopGoods.getImage3();
                filePaths[4] = shopGoods.getImage4();
                filePaths[5] = shopGoods.getImage5();
                break;
            default:
                break;
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                //注：有多少个文件上传，onSuccess方法就会执行多少次;
                if (urls.size() == temp_nub && temp_nub_image == 0) {//如果数量相等，则代表文件全部上传完成
                    createPerson(shopGoods);
                    temp_nub_image = 1;
                    temp_nub++;//防止重复上传文件。
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
//				ShowToast("错误码"+statuscode +",错误描述："+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                System.out.println("上传进度是：" + totalPercent);
                if (totalPercent == 100) {
                    System.out.println("上传wancheng");
                }
            }
        });

    }

    public interface UploadAlongListener {
        void onSuccess(String url);
    }

    private void upload_alnog(String picPath, final UploadAlongListener alongListener) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    alongListener.onSuccess(bmobFile.getUrl());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    // 图片的回调函数
    public interface ImageCallback {
        void onImageLoad(ShopGoods shopGoods);

        void onError();
    }

    //通过id找到一条数据，用于点击商品之后的详细信息
    public void find_alone(String objID, final ImageCallback callback) {
        BmobQuery<ShopGoods> query = new BmobQuery<>();
        query.getObject(objID, new QueryListener<ShopGoods>() {

            @Override
            public void done(ShopGoods object, BmobException e) {
                if (e == null) {
                    callback.onImageLoad(object);
                } else {
                    callback.onError();
                }
            }
        });
    }


    //--------商品内的评论，点赞表--增删改查------------------------------------------------------------------------------------------------------------------------

    /**
     * 增加评论和回复进入网络数据库
     */
    public void addCommentZan(CommentZan commentZan) {
        commentZan.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                }
            }

        });
    }

    /**
     * 更新评论的赞数
     *
     * @param objectId 确定商品id
     * @param zan      新的赞数
     */
    public void updateZan(String objectId, int zan) {
        ShopGoods goods = new ShopGoods();
        goods.setZan_nub(zan);
        goods.update(objectId, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                }
            }
        });
    }

    public interface getCommentCallback {
        void onCommentLoad(List<CommentZan> arrayList);
    }

    //找到评论，商品详细信息内
    public void find_comment(String objectId, final getCommentCallback commentCallback) {
        BmobQuery<CommentZan> query = new BmobQuery<>();
        query.addWhereEqualTo("taget_object", objectId);//查询taget_object叫“objectid”的数据
//        query.setLimit(10);
        query.findObjects(new FindListener<CommentZan>() {
            @Override
            public void done(List<CommentZan> object, BmobException e) {
                if (e == null) {
                    commentCallback.onCommentLoad(object);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }//找评论，


    //···IM聊天数据表·························································


    public interface Talk_Callback {
        void onSuccess();

        void onFail();
    }

    /**
     * @param who           目标，哪两个用户交流，两个object的结合
     * @param context       聊天内容
     * @param who_say       谁说的，自己说的写自己的object
     * @param talk_callback 回调，显示界面是否发送成功
     */
    public void add_talk_data(String who, String context, String who_say, final Talk_Callback talk_callback) {
        im.setContext(context);
        im.setState(who_say);
        im.setTarget(who);
        im.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    talk_callback.onSuccess();
                } else {
                    System.out.println("错误的信息是是：" + e.getErrorCode() + "-----------" + e.getMessage());
                    talk_callback.onFail();
                }
            }
        });
    }

    public interface Add_talk_data_change_callback {
        public void onSueecss_b_tell_a(String object);

        public void onSueecss_a_tell_b(String object);
    }

    //增加两条数据，用来当其中有一个变量变的时候，通知系统，更改ui，用于im即时通讯
    public void add_talk_data_change(String object_a, String object_b, final Add_talk_data_change_callback add_talk_data_change_callback) {
        im2.setName_a(0);
        im2.setConversation_target(object_a + object_b);
        im2.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    add_talk_data_change_callback.onSueecss_a_tell_b(s);
                }
            }
        });
        im3.setName_b(0);
        im3.setConversation_target(object_b + object_a);
        im3.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    add_talk_data_change_callback.onSueecss_b_tell_a(s);
                }
            }
        });
    }

    public interface Find_talk_data_change_object_id_callback {
        public void onSuccess(String object_id);
    }

    public void find_talk_data_change_object_id_alone(String objectid, final Find_talk_data_change_object_id_callback find_talk_data_change_object_id_callback) {
        BmobQuery<IMConversation> query = new BmobQuery<>();
        query.addWhereEqualTo("conversation_target", objectid);//查询conversation_target叫“objectid”的数据
        query.findObjects(new FindListener<IMConversation>() {
            @Override
            public void done(List<IMConversation> list, BmobException e) {
                if (e == null) {
                    for (IMConversation im : list) {
                        String object_ID = im.getObjectId();
                        find_talk_data_change_object_id_callback.onSuccess(object_ID);
                    }
                }
            }
        });

    }

    //当b对a说话的时候，a的信息，加一
    public void update_name_A(String objectId) {
        im2.setName_a(im2.getName_a() + 1);
        im2.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    //当a对b说话的时候，a的信息，加一
    public void update_name_B(String objectId) {
        im3.setName_b(im3.getName_b() + 1);
        im3.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }


    //查找聊天表中的聊天信息
    public interface findTalkCallback {
        public void onSuccess(List<IMConversation> list);

        public void onFail();
    }

    //寻找指定两个id聊天的数据，并返回arraylist
    public void findTalkData(String object_id, String target_objectID, final findTalkCallback findTalkCallback) {
        BmobQuery<IMConversation> query = new BmobQuery<>();
        String[] names = {object_id + target_objectID, target_objectID + object_id};
        query.addWhereContainedIn("target", Arrays.asList(names));
        query.findObjects(new FindListener<IMConversation>() {
            @Override
            public void done(List<IMConversation> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        findTalkCallback.onSuccess(list);
                    }
                } else {
                    findTalkCallback.onFail();
                }

            }
        });

    }

    public interface FindAccountCallback {
        void onSuccess(List<UserAccount> list);

        void onFail(int fail_code);

    }


    //```````账号的数据表```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````

    //通过id找到一个账号
    public void find_account(final String account, final FindAccountCallback accountCallback) {
        BmobQuery<UserAccount> query = new BmobQuery<>();
        query.addWhereEqualTo("account", account);
        query.findObjects(new FindListener<UserAccount>() {
            @Override
            public void done(List<UserAccount> list, BmobException e) {
                if (e == null) {

                    accountCallback.onSuccess(list);
                } else {
                    accountCallback.onFail(e.getErrorCode());
                }
            }
        });
    }


    public interface FindAccountDataAloneCallback {
        void onSuccess(String name, String head);
    }

    //通过objectid找到指定账号的信息，名字和头像
    public void findAccountDataAlone(String objectID, final FindAccountDataAloneCallback findAccountDataAloneCallback) {

        BmobQuery<UserAccount> bmobQuery = new BmobQuery<UserAccount>();
        bmobQuery.getObject(objectID, new QueryListener<UserAccount>() {
            @Override
            public void done(UserAccount object, BmobException e) {
                if (e == null) {
                    findAccountDataAloneCallback.onSuccess(object.getNick_name(), object.getHead_portrait());
                }
            }
        });
    }


    public interface AddAccountCallback {
        public void onSuccess(String object);
    }

    //增加一个账号信息
    public void add_account(String name, String head_portrait_adress, String account, String college, String organization, final AddAccountCallback addAccountCallback) {
        UserAccount user_account = new UserAccount();
        user_account.setAccount(account);
        user_account.setNick_name(name);
        user_account.setHead_portrait(head_portrait_adress);
        user_account.setCollege(college);
        user_account.setOrganization(organization);

        user_account.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                addAccountCallback.onSuccess(s);
            }
        });
    }


    //登录界面设置名字的时候更新数据库的名字
    public void Login_update_name(String objectId, String name) {
        UserAccount userAccount = new UserAccount();
        userAccount.setNick_name(name);
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    public interface LoginUpdateSchoolCallback {
        void done();

        void fail();
    }

    //更新学校的名字
    public void loginUpdateSchool(String objectId, String college, String organization, final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {

        UserAccount userAccount = new UserAccount();
        userAccount.setCollege(college);
        userAccount.setOrganization(organization);
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                loginUpdateSchoolCallback.done();
            }
        });
    }

    //上传头像
    public void uploadHeadPortrait(String picPath) {
        BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //成功
                } else {
                    //fail
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    //更新头像资料，姓名，学校
    public void updateAllData(String objectId, UserAccount userAccount, final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                loginUpdateSchoolCallback.done();
            }
        });


    }

    public void uploadHeadPortrait(final String objectId, final UserAccount userAccount
            , final LoginUpdateSchoolCallback loginUpdateSchoolCallback){
        final BmobFile bmobFile = new BmobFile(new File(userAccount.getHead_portrait()));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    updateAllData(objectId,userAccount,loginUpdateSchoolCallback);
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                }else{
                    loginUpdateSchoolCallback.fail();
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }


}






