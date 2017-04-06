package com.swipememo.swipememo.model.database;

import com.swipememo.swipememo.model.types.MemoDirectory;
import com.swipememo.swipememo.model.types.Memo;
import com.swipememo.swipememo.model.types.SelectedTodo;
import com.swipememo.swipememo.model.types.Todo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import io.realm.annotations.RealmModule;

/**
 * Created by DoDo on 2017-02-26.
 * SingleTone is applied
 */

public class RealmHelper implements DBHelper {

    private static RealmHelper instance = new RealmHelper();

    private RealmConfiguration config;

    private Realm swipeRealm;
    private ArrayList<RealmAsyncTask> taskList;
    private ArrayList<Object>users;
    @RealmModule(classes={Memo.class, MemoDirectory.class, Todo.class, SelectedTodo.class})
    private static class MemoModule{}





    private RealmHelper(){
        configRealm();
    }

    public static RealmHelper getInstance(){
        return instance;
    }
    private void configRealm(){
        config = new RealmConfiguration.Builder()
                .name(DB_NAME)
                //.encryptionKey()
                .schemaVersion(VERSION)
                .modules(new MemoModule())
                .build();
        try {
            Realm.migrateRealm(config, new SwipeMigration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SwipeMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
//            if(newVersion>oldVersion && oldVersion == 0) {
//                schema.create("Memo")
//                        .addField()
//                        .addField();
//
//            }
        }
    }

    //lifeCycle
    public void dbInit(Object tag) {
        swipeRealm = Realm.getInstance(config);
        if(taskList == null)
            taskList = new ArrayList<>();
        if(users == null)
            users = new ArrayList<>();
        users.add(tag);
    }
    public void dbClose(Object tag){
        users.remove(tag);
        if(users.size() == 0 ){
            taskClose();
            swipeRealm.close();
        }
    }
    public void taskClose(){
        for(RealmAsyncTask t: taskList){
            if(!t.isCancelled()){
                t.cancel();
            }
        }
    }
    public void forecClose(){
        taskClose();
        swipeRealm.close();
    }

    private long getLargestNo(String whose){
        long result = 0;
        Number temp = null;
        try{
            switch (whose){
                case DIR:
                    temp = swipeRealm.where(MemoDirectory.class).max("no");
                    break;
                case DIR_ORDER:
                    temp = swipeRealm.where(MemoDirectory.class).max("order");
                    break;
                case MEMO:
                    temp = swipeRealm.where(Memo.class).max("no");
                    break;
                case TODO:
                    temp = swipeRealm.where(Todo.class).max("no");
                    break;
                case SELECTED_TODO:
                    temp = swipeRealm.where(SelectedTodo.class).max("no");
                    break;
            }}catch (Exception e){throw  e;}

        if(temp != null)
            result = temp.longValue();
        return  result;
    }


    public void createDir(String name, String pw) {
        swipeRealm.executeTransaction(new DirTransaction(CREATE, name, pw));
    }
    public void modifyDir(String dirCode, long no, String name, String pw){
        swipeRealm.executeTransaction(new DirTransaction(MODIFY, dirCode ,no, name, pw));
    }
    public void deleteDir(String dirCode){
        swipeRealm.executeTransaction(new DirTransaction(DELTE, dirCode));
    }
    public OrderedRealmCollection<MemoDirectory> getAllDirs(){
        return swipeRealm.where(MemoDirectory.class).findAll();
    }
    public MemoDirectory getADir(String dirCode) {
        return swipeRealm.where(MemoDirectory.class).equalTo("dirCode",dirCode).findFirst();
    }
    public OrderedRealmCollection<MemoDirectory> getAllLockedDirs(){
        return swipeRealm.where(MemoDirectory.class).beginGroup().isNotEmpty("pw").or().isNotNull("pw").endGroup().findAll();
    }


    public void createMemo(boolean importance, String dirCode, String title,String content){
        swipeRealm.executeTransaction(new MemoTransaction(CREATE, importance, dirCode,title ,content));
    }
    public void modifyMemo(long no, boolean importance,boolean deleted ,String dirCode, String title, String content) {
        swipeRealm.executeTransaction(new MemoTransaction(MODIFY, no, importance, deleted, dirCode, title ,content));
    }
    public void deleteMemo(long no) {
        swipeRealm.executeTransaction(new MemoTransaction(DELTE, no));
    }
    public OrderedRealmCollection<Memo> readAllMemo(){
        return swipeRealm.where(Memo.class).findAll();
    }

    public Memo readAMemoByNO(long no){
        return swipeRealm.where(Memo.class).equalTo("no",no).findFirst();
    }
    public OrderedRealmCollection<Memo> readImportantMemo(String dirCode) {
        return swipeRealm.where(Memo.class).equalTo("dirCode",dirCode).equalTo("important",true).findAll();
    }
    public OrderedRealmCollection<Memo> readDeletedMemo() {
        return swipeRealm.where(Memo.class).equalTo("deleted",true).findAll();
    }
    public OrderedRealmCollection<Memo> readMemoByDirCode(String dirCode) {
        return swipeRealm.where(Memo.class).equalTo("deleted",false).equalTo("dirCode",dirCode).findAll();
    }
    public OrderedRealmCollection<Memo> readMemoBykeyWord(String keyWord) {
        return swipeRealm.where(Memo.class).beginGroup().contains("content",keyWord).or().contains("title",keyWord).endGroup().findAll();
    }
    public OrderedRealmCollection<Memo> readMemoBykeyWord(String keyWord, String dirCode) {
        return swipeRealm.where(Memo.class).equalTo("dirCode",dirCode).beginGroup().contains("content",keyWord).or().contains("title",keyWord).endGroup().findAll();
    }



    @Override
    public void createTodo(String type, String content, Date date) {
        swipeRealm.executeTransaction(new TodoTransaction(CREATE,type,content, date));
    }

    @Override
    public void modifyTodo(long no, String type, String content, boolean done) {
        swipeRealm.executeTransaction(new TodoTransaction(MODIFY, no, type, content, done));
    }

    @Override
    public void deleteTodo(long no) {
        swipeRealm.executeTransaction(new TodoTransaction(DELTE, no));
    }

    @Override
    public Todo readATodoByNo(long no) {
        return swipeRealm.where(Todo.class).equalTo("no",no).findFirst();
    }

    @Override
    public OrderedRealmCollection<Todo> readTodoByContent(String keyWord) {
        return swipeRealm.where(Todo.class).equalTo("content",keyWord).findAll();
    }

    @Override
    public OrderedRealmCollection<Todo> readTodoByType(String type) {
        return swipeRealm.where(Todo.class).equalTo("type",type).findAll();
    }

    @Override
    public OrderedRealmCollection<Todo> readAllTodo() {
        return swipeRealm.where(Todo.class).findAll();
    }

    @Override
    public void writeSelectedTodo(long no, String type, String content, Date belongDate, Date putDate) {
        swipeRealm.executeTransaction(new SelectedTodoTransaction(CREATE,no,type,content,belongDate,putDate));
    }

    @Override
    public void modifySelectedTodo(long no, boolean done, String type, String content, Date belongDate, Date putDate) {
        swipeRealm.executeTransaction(new SelectedTodoTransaction(MODIFY,no,done,type,content,belongDate,putDate));
    }

    @Override
    public void deleteSelectedTodo(long no) {
        swipeRealm.executeTransaction(new SelectedTodoTransaction(DELTE,no));
    }

    @Override
    public SelectedTodo readASelectedTodoByNO(long no) {
        return swipeRealm.where(SelectedTodo.class).equalTo("no",no).findFirst();
    }

    @Override
    public OrderedRealmCollection<SelectedTodo> readSelectedTodoByContent(String keyWord) {
        return swipeRealm.where(SelectedTodo.class).equalTo("content",keyWord).findAll();
    }

    @Override
    public OrderedRealmCollection<SelectedTodo> readSelectedTodoByBelongDate(Date date) {
        return swipeRealm.where(SelectedTodo.class).equalTo("belongDate",date).findAll();
    }

    @Override
    public OrderedRealmCollection<SelectedTodo> readOldSelectedTodo(Date date) {
        return swipeRealm.where(SelectedTodo.class).lessThan("belongDate",date).equalTo("done",false).equalTo("type","ONCE_TODO").findAll();
    }



    private class MemoTransaction implements Realm.Transaction{

        private Realm realm;

        private String action;

        private long no;
        private boolean importance, deleted;
        private String dirCode, title, content;

        MemoTransaction(String action, boolean importance ,String dirCode, String title, String content){
            this.action = action;
            this.no = getLargestNo(MEMO)+1;
            this.importance = importance;
            this.deleted = false;
            this.dirCode = dirCode;
            this.title = title;
            this.content = content;
        }
        MemoTransaction(String action, long no, boolean importance, boolean deleted ,String dirCode, String title, String content){
            this.action = action;
            this.no = no;
            this.importance = importance;
            this.deleted = deleted;
            this.dirCode = dirCode;
            this.title = title;
            this.content = content;
        }
        MemoTransaction(String action, long no){
            this.action = action;
            this.no = no;
        }

        @Override
        public void execute(Realm realm) {
            this.realm = realm;
            switch (action){
                case CREATE:
                    createMemo();
                    break;
                case MODIFY:
                    modifyMemo();
                    break;
                case DELTE:
                    deleteMemo();
                    break;
            }
        }

        private void createMemo(){
            Memo memo = realm.createObject(Memo.class,no);
            memo.setImportant(importance);
            memo.setDeleted(deleted);
            memo.setDirCode(dirCode);
            memo.setTitle(title);
            memo.setContent(content);
            Date now = Calendar.getInstance().getTime();
            memo.setCreateDate(now);
            memo.setModifiedDate(now);
        }
        private void modifyMemo(){
            Memo memo = realm.where(Memo.class).equalTo("no",no).findFirst();
            memo.setImportant(importance);
            memo.setDeleted(deleted);
            memo.setDirCode(dirCode);
            memo.setTitle(title);
            memo.setContent(content);
            Date now = Calendar.getInstance().getTime();
            memo.setModifiedDate(now);
            if(deleted){memo.setDeletedDate(now);}
        }
        private void deleteMemo(){
            Memo memo = realm.where(Memo.class).equalTo("no",no).findFirst();
            memo.deleteFromRealm();
        }
    }

    private class DirTransaction implements Realm.Transaction{

        private Realm realm;

        private long no;
        private String dirCode, name, pw;
        private Date createDate;

        private String action = null;

        DirTransaction(String action, String name, String pw) {
            this.action = action;
            this.no = getLargestNo(DIR)+1;
            this.name = name;
            this.pw = pw;
            this.createDate = Calendar.getInstance().getTime();

            this.dirCode = createCode();
        }

        DirTransaction(String action, String dirCode,long no, String name, String pw){
            this.action = action;
            this.dirCode = dirCode;
            this.no = no;
            this.name = name;
            this.pw = pw;
        }

        DirTransaction(String action, String dirCode){
            this.action = action;
            this.dirCode = dirCode;
        }

        @Override
        public void execute(Realm realm) {
            this.realm = realm;

            switch (action) {
                case CREATE:
                    createDir();
                    break;
                case MODIFY:
                    modifyDir();
                    break;
                case DELTE:
                    deleteDir();
                    break;
            }

        }
        private String createCode(){
            StringBuffer sb = new StringBuffer("dir_").append(createDate).append("_").append(name);
            return sb.toString();
        }
        private void createDir(){
            MemoDirectory dir = realm.createObject(MemoDirectory.class,no);
            dir.setNo(no);
            dir.setDirCode(dirCode);
            dir.setName(name);
            dir.setPw(pw);
            dir.setCreateDate(createDate);
        }
        private void modifyDir(){
            MemoDirectory dir = realm.where(MemoDirectory.class).equalTo("dirCode",dirCode).findFirst();
            dir.setName(name);
            dir.setPw(pw);
        }
        private void deleteDir(){
            MemoDirectory dir = realm.where(MemoDirectory.class).equalTo("dirCode",dirCode).findFirst();
            dir.deleteFromRealm();
        }
    }

    private class TodoTransaction implements Realm.Transaction{

        private Realm realm;
        private long no;
        private String type;
        private String content;
        private boolean done;
        private Date createDate;
        private String action = null;

        TodoTransaction(String action, String type, String content,Date date) {
            this.action = action;
            this.no = getLargestNo(TODO)+1;
            this.type = type;
            this.content = content;
            this.createDate = date;
            this.done = false;
        }

        TodoTransaction(String action,long no, String type, String content,boolean done){
            this.action = action;
            this.no = no;
            this.type = type;
            this.content = content;
            this.done = done;
        }

        TodoTransaction(String action, long no){
            this.action = action;
            this.no = no;
        }

        @Override
        public void execute(Realm realm) {
            this.realm = realm;

            switch (action) {
                case CREATE:
                    createTodo();
                    break;
                case MODIFY:
                    modifyTodo();
                    break;
                case DELTE:
                    deleteTodo();
                    break;
            }

        }
        private void createTodo(){
            Todo todo = realm.createObject(Todo.class,no);
            todo.setType(type);
            todo.setContent(content);
            todo.setDone(done);
            todo.setCreateDate(createDate);
        }
        private void modifyTodo(){
            Todo todo = realm.where(Todo.class).equalTo("no",no).findFirst();
            todo.setType(type);
            todo.setContent(content);
            todo.setDone(done);
        }
        private void deleteTodo(){
            Todo todo = realm.where(Todo.class).equalTo("no",no).findFirst();
            todo.deleteFromRealm();
        }
    }
    private class SelectedTodoTransaction implements Realm.Transaction{

        private Realm realm;
        private long no;
        private boolean done;
        private String type;
        private String content;
        private Date belongDate;
        private Date putDate;

        private String action = null;

        SelectedTodoTransaction(String action,long no, String type, String content, Date belongDate, Date putDate) {
            this.action = action;
            this.no = getLargestNo(TODO)+1;
            this.type = type;
            this.content = content;
            this.belongDate = belongDate;
            this.putDate = putDate;
            this.done = false;
        }

        SelectedTodoTransaction(String action,long no, boolean done, String type, String content, Date belongDate, Date putDate){
            this.action = action;
            this.no = no;
            this.done = done;
            this.type = type;
            this.content = content;
            this.belongDate = belongDate;
            this.putDate = putDate;
        }

        SelectedTodoTransaction(String action, long no){
            this.action = action;
            this.no = no;
        }

        @Override
        public void execute(Realm realm) {
            this.realm = realm;

            switch (action) {
                case CREATE:
                    createSelectedTodo();
                    break;
                case MODIFY:
                    modifySelectedTodo();
                    break;
                case DELTE:
                    deleteSelectedTodo();
                    break;
            }

        }
        private void createSelectedTodo(){
            SelectedTodo newTodo = realm.createObject(SelectedTodo.class,no);
            newTodo.setType(type);
            newTodo.setDone(false);
            newTodo.setContent(content);
            newTodo.setBelongDate(belongDate);
            newTodo.setPutDate(putDate);
        }
        private void modifySelectedTodo(){
            SelectedTodo selectedTodo = realm.where(SelectedTodo.class).equalTo("no", no).findFirst();
            selectedTodo.setType(type);
            selectedTodo.setContent(content);
            selectedTodo.setBelongDate(belongDate);
            selectedTodo.setPutDate(putDate);
            selectedTodo.setDone(done);
        }
        private void deleteSelectedTodo(){
            SelectedTodo selectedTodo = realm.where(SelectedTodo.class).equalTo("no",no).findFirst();
            selectedTodo.deleteFromRealm();
        }
    }
}
