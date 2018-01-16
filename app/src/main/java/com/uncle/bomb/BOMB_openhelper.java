package com.uncle.bomb;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;


public class BOMB_openhelper {


    private final shop_goods goods = new shop_goods();
    private final IM_conversation im = new IM_conversation();
    private final IM_conversation im2 = new IM_conversation();
    private final IM_conversation im3 = new IM_conversation();
    private final comment_zan commentZan = new comment_zan();
    private final User_account user_account = new User_account();
    private int temp_nub;//临时数字，用来记录有多少个图片，后面防止上传重复问题。
    private int temp_nub_image = 0;//临时数字，用来记录有多少个图片，后面防止上传重复问题。






    //----商品-----增删改查-----------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * 创建一条商品信息
     * @param title 标题
     * @param text 描述
     * @param price 价格
     * @param variety 种类
     * @param path1 图片1
     * @param path2 图片2
     * @param path3 图片3
     * @param path4 图片4
     * @param path5 图片5
     * @param path6 图片6
     * @param zan_nub 点赞数
     * @param owner 谁发出来的id
     * @param college 大学
     * @param organization 学院
     */
    public void createPerson(String title, String text, String price, String variety,
                             String path1, String path2, String path3, String path4, String path5, String path6,
                             int zan_nub, String owner,String college,String organization,String head_portrait,String name) {
        goods.setImage1(path1);
        goods.setImage2(path2);
        goods.setImage3(path3);
        goods.setImage4(path4);
        goods.setImage5(path5);
        goods.setImage6(path6);
        goods.setText(text);
        goods.setTitle(title);
        goods.setPrice(price);
        goods.setVariety(variety);
        goods.setZan_nub(zan_nub);
        goods.setOwner(owner);
        goods.setCollege(college);
        goods.setOrganization(organization);
        goods.setHead_portrait(head_portrait);
        goods.setName(name);
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


    public ArrayList<HashMap<String, Object>> query_goods_and_doing() {
        final ArrayList<HashMap<String, Object>> al = new ArrayList<>();
        BmobQuery<shop_goods> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo(null, null);
//        bmobQuery.setLimit(1);
        bmobQuery.findObjects(new FindListener<shop_goods>() {
            @Override
            public void done(List<shop_goods> list, BmobException e) {
                if (e == null) {
                    for (shop_goods goods : list) {
                        System.out.println(list);
                        HashMap<String, Object> map = new HashMap<>();

                        map.put("image1", goods.getImage1());
                        map.put("image2", goods.getImage2());
                        map.put("image3", goods.getImage3());
                        map.put("image4", goods.getImage4());
                        map.put("image5", goods.getImage5());
                        map.put("image6", goods.getImage6());
                        map.put("title", goods.getTitle());
                        map.put("text", goods.getText());
                        map.put("price", goods.getPrice());
                        map.put("variety", goods.getVariety());
                        al.add(map);


                    }

                } else {
                    // TODO Auto-generated method stub
                    System.out.println("找不到数据");
                }
            }
        });
        System.out.println("ai里面的是：" + al);
        return al;
    }


    //上传文件，上传图片进入数据库，然后创建商品，与上连用
    public void upload_img(final String title, final String text, final String price, String variety,
                           final int nub, String img_path1, String img_path2,
                           String img_path3, String img_path4, String img_path5, String img_path6,
                           final String owner,final String college,final String organization,final String head_portrait,final String name) {
        //详细示例可查看BmobExample工程中BmobFileActivity类

        final String[] filePaths = new String[nub];
        temp_nub = nub;
        switch (nub) {
            case 3:
                filePaths[0] = img_path1;
                filePaths[1] = img_path2;
                filePaths[2] = img_path3;
                break;
            case 4:
                filePaths[0] = img_path1;
                filePaths[1] = img_path2;
                filePaths[2] = img_path3;
                filePaths[3] = img_path4;
                break;
            case 5:
                filePaths[0] = img_path1;
                filePaths[1] = img_path2;
                filePaths[2] = img_path3;
                filePaths[3] = img_path4;
                filePaths[4] = img_path5;
                break;
            case 6:
                filePaths[0] = img_path1;
                filePaths[1] = img_path2;
                filePaths[2] = img_path3;
                filePaths[3] = img_path4;
                filePaths[4] = img_path5;
                filePaths[5] = img_path6;
                break;
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                //注：有多少个文件上传，onSuccess方法就会执行多少次;

                if (urls.size() == temp_nub && temp_nub_image ==0) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    switch (nub) {
                        case 3:
                            createPerson(title, text, price, null, urls.get(0), urls.get(1), urls.get(2), null, null, null, 0, owner,college,organization,head_portrait,name);
                            break;
                        case 4:
                            createPerson(title, text, price, null, urls.get(0), urls.get(1), urls.get(2), urls.get(3), null, null, 0, owner,college,organization,head_portrait,name);
                            break;
                        case 5:
                            createPerson(title, text, price, null, urls.get(0), urls.get(1), urls.get(2), urls.get(3), urls.get(4), null, 0, owner,college,organization,head_portrait,name);
                            break;
                        case 6:
                            createPerson(title, text, price, null, urls.get(0), urls.get(1), urls.get(2), urls.get(3), urls.get(4), urls.get(5), 0, owner,college,organization,head_portrait,name);
                            break;
                    }
                    temp_nub_image =1;
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

    public interface Upload_along_Listener{
        public void onsuccess(String url);
    }
    private void upload_alnog(String picPath, final Upload_along_Listener upload_along_listener){
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.i("发布成功了多少次","lalallalalallalalallaalaalalalalla");
                    upload_along_listener.onsuccess(bmobFile.getUrl());
                }else{

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
        public void onImageLoad(HashMap<String, String> map);
        public void onError();
    }

    //通过id找到一条数据，用于点击商品之后的详细信息
    public void find_alone(String objID, final ImageCallback callback) {
        BmobQuery<shop_goods> query = new BmobQuery<>();
        query.getObject(objID, new QueryListener<shop_goods>() {

            @Override
            public void done(shop_goods object, BmobException e) {
                if (e == null) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("image1", object.getImage1());
                    map.put("image2", object.getImage2());
                    map.put("image3", object.getImage3());
                    map.put("image4", object.getImage4());
                    map.put("image5", object.getImage5());
                    map.put("image6", object.getImage6());
                    map.put("title", object.getTitle());
                    map.put("text", object.getText());
                    map.put("price", object.getPrice());
                    map.put("variety", object.getVariety());
                    map.put("objectID", object.getObjectId());
                    map.put("owner", object.getOwner());
                    map.put("college",object.getCollege());
                    map.put("zan_nub",object.getZan_nub()+"");
                    map.put("organization",object.getOrganization());
                    map.put("head_portrait_url",object.getHead_portrait());
                    callback.onImageLoad(map);
                } else {
                    callback.onError();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }










    //--------商品内的评论，点赞表--增删改查------------------------------------------------------------------------------------------------------------------------

    /**
     * 增加评论和回复进入网络数据库
     *
     * @param comment       评论
     * @param blogger       博主
     * @param user          用户
     * @param comment_reply 判断是评论还是回复
     * @param taget_object  确定是评论哪一件商品
     */
    public void add_comment_zan(String comment, String blogger,
                                String user, int comment_reply, String taget_object) {
        commentZan.setComment(comment);
        commentZan.setBlogger(blogger);
        commentZan.setComment_reply(comment_reply);
        commentZan.setTaget_object(taget_object);
        commentZan.setUesr(user);

        commentZan.save(new SaveListener<String>() {

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

    /**
     * 更新评论的赞数
     *
     * @param objectId 确定商品id
     * @param zan      新的赞数
     */
    public void update_zan(String objectId, int zan) {

        goods.setZan_nub(zan);
        goods.update(objectId, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "更新成功");
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public interface getCommentCallback {
        public void oncommentLoad(ArrayList<HashMap<String, String>> arrayList);
    }

    //找到评论，商品详细信息内
    public void find_comment(String objectid, final getCommentCallback commentCallback) {
        BmobQuery<comment_zan> query = new BmobQuery<>();
        query.addWhereEqualTo("taget_object", objectid);//查询taget_object叫“objectid”的数据
//        query.setLimit(10);
//执行查询方法
        query.findObjects(new FindListener<comment_zan>() {
            @Override
            public void done(List<comment_zan> object, BmobException e) {
                if (e == null) {
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                    for (comment_zan gameScore : object) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        //获得playerName的信息
                        hashMap.put("comment", gameScore.getComment());
                        arrayList.add(hashMap);
                    }
                    commentCallback.oncommentLoad(arrayList);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }//找评论，









    //···IM聊天数据表·························································


    public interface Talk_Callback {
        public void onSuccess();

        public void onFail();
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
        BmobQuery<IM_conversation> query = new BmobQuery<>();
        query.addWhereEqualTo("conversation_target", objectid);//查询conversation_target叫“objectid”的数据
        query.findObjects(new FindListener<IM_conversation>() {
            @Override
            public void done(List<IM_conversation> list, BmobException e) {
                if (e == null) {
                    for (IM_conversation im : list) {
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
    public interface Find_talk_callback {
        public void onSuccess(ArrayList<HashMap<String, String>> arrayList);

        public void onFail();
    }

    //寻找指定两个id聊天的数据，并返回arraylist
    public void find_talk_data(String object_id,String target_objectID, final Find_talk_callback find_talk_callback) {
        BmobQuery<IM_conversation> query = new BmobQuery<>();
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        String[] names = {object_id+target_objectID, target_objectID+object_id };
        query.addWhereContainedIn("target", Arrays.asList(names));
        query.findObjects(new FindListener<IM_conversation>() {
            @Override
            public void done(List<IM_conversation> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob_find_talk_data", "成功：" + "查询成功：共" + list.size() + "条数据。");
                    if (list.size() != 0) {
                        for (IM_conversation conversation : list) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("context", conversation.getContext());
                            map.put("who_say", conversation.getState());
                            map.put("objectID", conversation.getObjectId());
                            arrayList.add(map);

                        }
                        find_talk_callback.onSuccess(arrayList);
                    } else {
                        find_talk_callback.onFail();
                    }
                } else {
                    Log.i("bmob_find_talk_data", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }

            }
        });

    }

    public interface Find_account_callback {
        public void onSuccess(ArrayList<HashMap<String, String>> arrayList);

        public void onFail(int fail_code);

    }









    //```````账号的数据表```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````

    //通过id找到一个账号
    public void find_account(final String account, final Find_account_callback find_account_callback) {
        BmobQuery<User_account> query = new BmobQuery<>();
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        query.addWhereEqualTo("account", account);
        query.findObjects(new FindListener<User_account>() {
            @Override
            public void done(List<User_account> list, BmobException e) {
                if (e == null) {
                    for (User_account user_account : list) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("nick_name", user_account.getNick_name());
                        map.put("head_portrait", user_account.getHead_portrait());
                        map.put("object_id", user_account.getObjectId());
                        map.put("college", user_account.getCollege());
                        map.put("organization", user_account.getOrganization());
                        arrayList.add(map);
                        Log.i("account", "dddddd");
                    }
                    find_account_callback.onSuccess(arrayList);
                } else {
                    Log.i("bomb", e.getErrorCode() + "错误代码");
                    find_account_callback.onFail(e.getErrorCode());
                    Log.i("account", "aaaaaaa");
                }
            }
        });
    }


    public interface Find_account_data_alone_callback {
        public void onSuccess(String name, String head);
    }

    //通过objectid找到指定账号的信息，名字和头像
    public void find_account_data_alone(String objectID, final Find_account_data_alone_callback find_account_data_alone_callback) {

        BmobQuery<User_account> bmobQuery = new BmobQuery<User_account>();
        bmobQuery.getObject(objectID, new QueryListener<User_account>() {
            @Override
            public void done(User_account object, BmobException e) {
                if (e == null) {
                    find_account_data_alone_callback.onSuccess(object.getNick_name(), object.getHead_portrait());
                }
            }
        });
    }


    public interface Add_account_callback {
        public void onSuccess(String object);
    }

    //增加一个账号信息
    public void add_account(String name, String head_portrait_adress, String account,String college,String organization, final Add_account_callback add_account_callback) {
        User_account user_account = new User_account();
        user_account.setAccount(account);
        user_account.setNick_name(name);
        user_account.setHead_portrait(head_portrait_adress);
        user_account.setCollege(college);
        user_account.setOrganization(organization);

        user_account.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                add_account_callback.onSuccess(s);
            }
        });
    }


    //登录界面设置名字的时候更新数据库的名字
    public void Login_update_name(String objectId,String name) {
        user_account.setNick_name(name);
        user_account.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    public interface Login_update_school_callback{
        public void done();
        public void fail();
    }
    //更新学校的名字
    public void Login_update_school(String objectId, String college, String organization, final Login_update_school_callback login_update_school_callback) {
        user_account.setCollege(college);
        user_account.setOrganization(organization);
        user_account.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                login_update_school_callback.done();
            }
        });
    }

    //上传头像
    public void upload_head_portrait( String picPath ){
        BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //成功
                }else{
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
    public void updata_all_data(String objectId, String college, String organization,String head_portrait,String name
            , final Login_update_school_callback login_update_school_callback){
        user_account.setNick_name(name);
        user_account.setHead_portrait(head_portrait);
        user_account.setCollege(college);
        user_account.setOrganization(organization);
        user_account.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                login_update_school_callback.done();
            }
        });



    }
    public void upload_head_portrait(final String objectId, final String college, final String organization, String head_portrait, final String name
            , final Login_update_school_callback login_update_school_callback){
        final BmobFile bmobFile = new BmobFile(new File(head_portrait));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    updata_all_data(objectId,college,organization,bmobFile.getFileUrl(),name,login_update_school_callback);
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                }else{
                    login_update_school_callback.fail();
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

}






