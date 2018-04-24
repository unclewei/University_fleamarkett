package com.uncle.bomb;

import android.util.Log;

import com.uncle.DTO.CommentZan;
import com.uncle.DTO.Profile;
import com.uncle.DTO.shopGoods;
import com.uncle.administrator.fleamarket.Mine.MineDataActivity;

import java.io.File;
import java.util.ArrayList;
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

    private static BOMBOpenHelper openHelper;

    public static BOMBOpenHelper getInstance() {
        if (openHelper == null) {
            synchronized (BOMBOpenHelper.class) {
                if (openHelper == null) {
                    openHelper = new BOMBOpenHelper();
                }
            }
        }
        return openHelper;
    }

    //----商品-----增删改查-----------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 创建一条商品信息
     */
    public void createPerson(shopGoods goods) {
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


    public void findMyPubGoods(String object, int setSkipNumber, final OnGoodsListCallBack onGoodsListCallBack) {
        BmobQuery<shopGoods> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("owner", object);
        bmobQuery.setSkip(10 * setSkipNumber);
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<shopGoods>() {
            @Override
            public void done(List<shopGoods> list, BmobException e) {
                if (e == null) {
                    onGoodsListCallBack.onDone(list);
                }
            }
        });
    }

    public void findScanList(ArrayList<String> list, int setSkipNumber, final OnGoodsListCallBack onGoodsListCallBack) {
        BmobQuery<shopGoods> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereContainedIn("objectId", list);
        bmobQuery.setSkip(10 * setSkipNumber);
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<shopGoods>() {
            @Override
            public void done(List<shopGoods> list, BmobException e) {
                if (e == null) {
                    onGoodsListCallBack.onDone(list);
                }
            }
        });
    }

    public void findMyGoods(String object, final String type, final int setSkipNumber, final OnGoodsListCallBack onGoodsListCallBack) {
        BmobQuery<Profile> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(object, new QueryListener<Profile>() {
            @Override
            public void done(Profile userAccount, BmobException e) {
                if (e == null || userAccount != null) {
                    if (MineDataActivity.MY_SCAN.equals(type)) {
                        findScanList(userAccount.getScanList(), setSkipNumber, onGoodsListCallBack);
                        return;
                    }
                    if (MineDataActivity.MY_ZAN.equals(type)) {
                        findScanList(userAccount.getZanList(), setSkipNumber, onGoodsListCallBack);
                    }
                }
            }
        });
    }

    public void uploadImg(final shopGoods shopgoods) {
        final String[] filePaths = new String[shopgoods.getImgFileList().size()];
        for (int i = 0; i < shopgoods.getImgFileList().size(); i++) {
            filePaths[i] = shopgoods.getImgFileList().get(i);
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                //注：有多少个文件上传，onSuccess方法就会执行多少次;
                if (urls.size() == shopgoods.getImgFileList().size()) {
                    for (int i = 0; i < urls.size(); i++) {
                        if ("http://bmob-cdn-8783.b0.upaiyun.comnull".equals(urls.get(i))) {
                            return;
                        }
                    }
                    shopgoods.setImgFileList((ArrayList<String>) urls);
                    createPerson(shopgoods);
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
        BmobQuery<shopGoods> query = new BmobQuery<>();
        query.getObject(objID, new QueryListener<shopGoods>() {
            @Override
            public void done(shopGoods object, BmobException e) {
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
        shopGoods shopGoods = new shopGoods();
        shopGoods.setZanNub(zan);
        shopGoods.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
            }
        });
    }

    public void addZan(final String objectId) {
        BmobQuery<shopGoods> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<shopGoods>() {
            @Override
            public void done(shopGoods shopGoods, BmobException e) {
                if (e != null) {
                    return;
                }
                int totalNub = shopGoods.getZanNub() + 1;
                updateZan(objectId, totalNub);
            }
        });
    }

    public void substractZan(final String objectId) {
        BmobQuery<shopGoods> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<shopGoods>() {
            @Override
            public void done(shopGoods shopGoods, BmobException e) {
                if (e != null) {
                    return;
                }
                int totalNub = shopGoods.getZanNub() - 1;
                updateZan(objectId, totalNub);
            }
        });
    }

    //找到评论，商品详细信息内
    public void findComment(String objectId, final getCommentCallback commentCallback) {
        BmobQuery<CommentZan> query = new BmobQuery<>();
        query.addWhereEqualTo("targetObject", objectId);
        query.findObjects(new FindListener<CommentZan>() {
            @Override
            public void done(List<CommentZan> list, BmobException e) {
                if (e == null) {
                    commentCallback.onCommentLoad(list);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }//找评论，


    //```````账号的数据表```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````

    //通过id找到一个账号
    public void findAccount(final String phoneNub, final FindAccountCallback accountCallback) {
        BmobQuery<Profile> query = new BmobQuery<>();
        query.addWhereEqualTo("phoneNub", phoneNub);
        query.findObjects(new FindListener<Profile>() {
            @Override
            public void done(List<Profile> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    accountCallback.onSuccess(list);
                    return;
                }
                if (e != null) {
                    accountCallback.onFail(e.getErrorCode());
                    return;
                }
                accountCallback.onFail(0);
            }
        });
    }


    //通过objectid找到指定账号的信息，名字和头像
    public void findAccountDataAlone(String objectID, final FindAccountDataAloneCallback findAccountDataAloneCallback) {
        BmobQuery<Profile> bmobQuery = new BmobQuery<Profile>();
        bmobQuery.getObject(objectID, new QueryListener<Profile>() {
            @Override
            public void done(Profile object, BmobException e) {
                if (e == null) {
                    findAccountDataAloneCallback.onSuccess(object);
                }
            }
        });
    }


    //增加一个账号信息
    public void addAccount(Profile profile, final AddAccountCallback addAccountCallback) {
        profile.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    addAccountCallback.onSuccess(s);
                    return;
                }
                addAccountCallback.onFail(e.toString());
            }
        });
    }


    //登录界面设置名字的时候更新数据库的名字
    public void Login_update_name(String objectId, String name) {
        Profile userAccount = new Profile();
        userAccount.setName(name);
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }


    //更新学校的名字
    public void loginUpdateSchool(String objectId, String college, String organization, final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {

        Profile userAccount = new Profile();
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
    public void updateAllData(String objectId, Profile userAccount, final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {
        userAccount.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                loginUpdateSchoolCallback.done();
            }
        });
    }

    public void uploadHeadPortrait(final String objectId, final Profile userAccount
            , final LoginUpdateSchoolCallback loginUpdateSchoolCallback) {
        final BmobFile bmobFile = new BmobFile(new File(userAccount.getAvatar()));
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

    public interface OnGoodsListCallBack {
        void onDone(List<shopGoods> list);
    }

    public interface ImageCallback {
        void onImageLoad(shopGoods shopgoods);

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
        public void onSuccess(String objectId);
    }

    //查找聊天表中的聊天信息
    public interface findTalkCallback {
        public void onSuccess(List<IMConversation> list);

        public void onFail();
    }

    public interface FindAccountCallback {
        void onSuccess(List<Profile> list);

        void onFail(int failCode);
    }

    public interface FindAccountDataAloneCallback {
        void onSuccess(Profile object);
    }

    public interface AddAccountCallback {
        public void onSuccess(String object);

        void onFail(String failResult);
    }

    public interface LoginUpdateSchoolCallback {
        void done();

        void fail();
    }

}






