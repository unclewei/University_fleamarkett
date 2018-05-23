package com.uncle.bomb;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
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
                    return;
                }

            }

        });
    }

    /**
     * 创建一条商品信息
     */
    public void createPerson(shopGoods goods, final OnBmobStringListener onBmobStringListener) {
        goods.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    onBmobStringListener.onDone(objectId);
                    System.out.println("整个id是：" + objectId);
                    return;
                }
                onBmobStringListener.onFail(e.toString());
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
                        findScanList(userAccount.getPublicList(), setSkipNumber, onGoodsListCallBack);
                        return;
                    }
                    if (MineDataActivity.MY_ZAN.equals(type)) {
                        findScanList(userAccount.getZanList(), setSkipNumber, onGoodsListCallBack);
                    }
                }
            }
        });
    }

    public void uploadImg(final shopGoods shopgoods, final Profile profile, final OnBmobStringListener onBmobStringListener) {
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
                    createPerson(shopgoods, new OnBmobStringListener() {
                        @Override
                        public void onDone(String object) {
                            ArrayList<String> list = new ArrayList<>();
                            if (profile.getPublicList() != null) {
                                list = profile.getPublicList();
                            }
                            list.add(object);
                            profile.setPublicList(list);
                            profile.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    //更新成功
                                    if (e == null) {
                                        onBmobStringListener.onDone(new Gson().toJson(profile));
                                        return;
                                    }
                                    onBmobStringListener.onFail(e.toString());
                                }
                            });
                        }

                        @Override
                        public void onFail(String failResult) {
                            onBmobStringListener.onFail(failResult);
                        }
                    });
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                onBmobStringListener.onFail(errormsg);
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


    private void uploadAvatar(String picPath, final UploadAlongListener alongListener) {
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
    public void findAlone(String objID, final ImageCallback callback) {
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
    public void addAccount(Profile profile, final OnBmobStringListener onBmobStringListener) {
        profile.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    onBmobStringListener.onDone(s);
                    return;
                }
                onBmobStringListener.onFail(e.toString());
            }
        });
    }

    //更新头像资料，姓名，学校
    public void updateAllData(String objectId, Profile profile, final OnBmobListener OnBmobListener) {
        profile.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                OnBmobListener.done();
            }
        });
    }

    public void uploadHeadPortrait(final String objectId, final Profile profile
            , final OnBmobListener OnBmobListener) {
        if (TextUtils.isEmpty(profile.getAvatar())) {
            updateAllData(objectId, profile, OnBmobListener);
            return;
        }
        final BmobFile bmobFile = new BmobFile(new File(profile.getAvatar()));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    profile.setAvatar(bmobFile.getFileUrl());
                    updateAllData(objectId, profile, OnBmobListener);
                } else {
                    OnBmobListener.fail();
                }
            }

            @Override
            public void onProgress(Integer value) {
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

    public interface OnBmobStringListener {
        void onDone(String object);

        void onFail(String failResult);
    }

    public interface OnBmobListener {
        void done();

        void fail();
    }

}






