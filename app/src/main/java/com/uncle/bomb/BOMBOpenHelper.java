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
    public void createPerson(shop_goods goods) {
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
        final List<shop_goods> listGoods = new ArrayList<>();
        BmobQuery<shop_goods> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo(null, null);
//        bmobQuery.setLimit(1);
        bmobQuery.findObjects(new FindListener<shop_goods>() {
            @Override
            public void done(List<shop_goods> list, BmobException e) {
                if (e == null) {

                }
            }
        });
    }


    //上传文件，上传图片进入数据库，然后创建商品，与上连用
    public void uploadImg(final shop_goods shopgoods) {
        final String[] filePaths = new String[shopgoods.getPictureNub()];
        temp_nub = shopgoods.getPictureNub();
        filePaths[0] = shopgoods.getImage1();
        filePaths[1] = shopgoods.getImage2();
        filePaths[2] = shopgoods.getImage3();
        switch (shopgoods.getPictureNub()) {
            case 3:
                break;
            case 4:
                filePaths[3] = shopgoods.getImage4();
                break;
            case 5:
                filePaths[3] = shopgoods.getImage4();
                filePaths[4] = shopgoods.getImage5();
                break;
            case 6:
                filePaths[3] = shopgoods.getImage3();
                filePaths[4] = shopgoods.getImage4();
                filePaths[5] = shopgoods.getImage5();
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
                    createPerson(shopgoods);
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



    //通过id找到一条数据，用于点击商品之后的详细信息
    public void find_alone(String objID, final ImageCallback callback) {
        BmobQuery<shop_goods> query = new BmobQuery<>();
        query.getObject(objID, new QueryListener<shop_goods>() {

            @Override
            public void done(shop_goods object, BmobException e) {
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
    public void updateZan(String objectId, String userObject,ArrayList zanList, int zan) {
        shop_goods goods = new shop_goods();
        goods.setZan_nub(zan);
        goods.update(objectId, new UpdateListener() {

            @Override
            public void done(BmobException e) {
            }
        });
        User_account userAccount = new User_account();
        userAccount.setZanList(zanList);
        userAccount.update(userObject, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                }
            }
        });
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



    //增加两条数据，用来当其中有一个变量变的时候，通知系统，更改ui，用于im即时通讯
    public void addTalkDataChange(String objectA, String objectB, final addTalkDataChangeCallback addTalkDataChangeCallback) {
        im2.setName_a(0);
        im2.setConversation_target(objectA + objectB);
        im2.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    addTalkDataChangeCallback.onSueecssATellB(s);
                }
            }
        });
        im3.setName_b(0);
        im3.setConversation_target(objectB + objectA);
        im3.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    addTalkDataChangeCallback.onSueecssBTellA(s);
                }
            }
        });
    }


    public void findTalkDataChangeObjectIdAlone(String objectid, final findTalkDataChangeObjectIdCallback findTalkDataChangeObjectIdCallback) {
        BmobQuery<IMConversation> query = new BmobQuery<>();
        query.addWhereEqualTo("conversation_target", objectid);//查询conversation_target叫“objectid”的数据
        query.findObjects(new FindListener<IMConversation>() {
            @Override
            public void done(List<IMConversation> list, BmobException e) {
                if (e == null) {
                    for (IMConversation im : list) {
                        String object_ID = im.getObjectId();
                        findTalkDataChangeObjectIdCallback.onSuccess(object_ID);
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




    //```````账号的数据表```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````

    //通过id找到一个账号
    public void findAccount(final String account, final FindAccountCallback accountCallback) {
        BmobQuery<User_account> query = new BmobQuery<>();
        query.addWhereEqualTo("account", account);
        query.findObjects(new FindListener<User_account>() {
            @Override
            public void done(List<User_account> list, BmobException e) {
                if (e == null) {

                    accountCallback.onSuccess(list);
                } else {
                    accountCallback.onFail(e.getErrorCode());
                }
            }
        });
    }




    //通过objectid找到指定账号的信息，名字和头像
    public void findAccountDataAlone(String objectID, final FindAccountDataAloneCallback findAccountDataAloneCallback) {
        BmobQuery<User_account> bmobQuery = new BmobQuery<User_account>();
        bmobQuery.getObject(objectID, new QueryListener<User_account>() {
            @Override
            public void done(User_account object, BmobException e) {
                if (e == null) {
                    findAccountDataAloneCallback.onSuccess(object);
                }
            }
        });
    }



    //增加一个账号信息
    public void add_account(String name, String head_portrait_adress, String account, String college, String organization, final AddAccountCallback addAccountCallback) {
        User_account user_account = new User_account();
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
        User_account userAccount = new User_account();
        userAccount.setNick_name(name);
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }


    //更新学校的名字
    public void loginUpdateSchool(String objectId, String college, String organization, final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {

        User_account userAccount = new User_account();
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
    public void updateAllData(String objectId, User_account userAccount, final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                loginUpdateSchoolCallback.done();
            }
        });


    }

    public void uploadHeadPortrait(final String objectId, final User_account userAccount
            , final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {
        final BmobFile bmobFile = new BmobFile(new File(userAccount.getHead_portrait()));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateAllData(objectId, userAccount, loginUpdateSchoolCallback);
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                } else {
                    loginUpdateSchoolCallback.fail();
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }


    public interface OnDoneListener {
        void onDone();
    }

    public interface ImageCallback {
        void onImageLoad(shop_goods shopgoods);
        void onError();
    }

    public interface UploadAlongListener {
        void onSuccess(String url);
    }

    public interface getCommentCallback {
        void onCommentLoad(List<CommentZan> arrayList);
    }

    public interface Talk_Callback {
        void onSuccess();
        void onFail();
    }
    public interface addTalkDataChangeCallback {
        public void onSueecssBTellA(String object);
        public void onSueecssATellB(String object);
    }

    public interface findTalkDataChangeObjectIdCallback {
        public void onSuccess(String object_id);
    }
    //查找聊天表中的聊天信息
    public interface findTalkCallback {
        public void onSuccess(List<IMConversation> list);
        public void onFail();
    }
    public interface FindAccountCallback {
        void onSuccess(List<User_account> list);
        void onFail(int failCode);
    }
    public interface FindAccountDataAloneCallback {
        void onSuccess(User_account object);
    }
    public interface AddAccountCallback {
        public void onSuccess(String object);
    }
    public interface LoginUpdateSchoolCallback {
        void done();
        void fail();
    }

}






