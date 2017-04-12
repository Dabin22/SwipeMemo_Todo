package com.swipememo.swipememo.model.database;

import com.swipememo.swipememo.model.types.Memo;
import com.swipememo.swipememo.model.types.MemoDirectory;
import com.swipememo.swipememo.model.types.SelectedTodo;
import com.swipememo.swipememo.model.types.Todo;

import java.util.Date;

import io.realm.OrderedRealmCollection;

/**
 * Created by DoDo on 2017-02-26.
 */

public interface DBHelper {

    String CREATE= "CREATE";
    String MODIFY = "MODIFY";
    String DELTE = "DELETE";

    String DB_NAME = "SWIPE_MEMO";
    int VERSION = 0;
    String MEMO = "MEMO";
    String DIR = "DIR";
    String DIR_ORDER = "DIR_ORDER";
    String TODO = "TODO";
    String SELECTED_TODO = "SELECTED_TODO";

    //lifeCycle
    void dbInit(Object tag);
    void dbClose(Object tag);
    void taskClose();
    void forecClose();

    void createDir(String name, String pw);
    void modifyDir(String dirCode, long no, String name, String pw);
    void deleteDir(String dirCode);
    OrderedRealmCollection<MemoDirectory> getAllDirs();
    MemoDirectory getADir(String dirCode);
    OrderedRealmCollection<MemoDirectory> getAllLockedDirs();


    void createMemo(boolean importance, String dirCode, String title,String content);
    void modifyMemo(long no, boolean importance,boolean deleted ,String dirCode, String title, String content);
    void deleteMemo(long no);
    OrderedRealmCollection<Memo> readAllMemo();
    Memo readAMemoByNO(long no);
    OrderedRealmCollection<Memo> readImportantMemo(String dirCode);
    OrderedRealmCollection<Memo> readDeletedMemo();
    OrderedRealmCollection<Memo> readMemoByDirCode(String dirCode);
    OrderedRealmCollection<Memo> readMemoBykeyWord(String keyWord);
    OrderedRealmCollection<Memo> readMemoBykeyWord(String keyWord, String dirCode);


    void createTodo(String type, String content, Date date);
    void modifyTodo(long no, String type, String content, boolean done);
    void deleteTodo(long no);
    Todo readATodoByNo(long no);
    OrderedRealmCollection<Todo> readTodoByContent(String keyWord);
    OrderedRealmCollection<Todo> readTodoByType(String type);
    OrderedRealmCollection<Todo> readAllTodo();


    void writeSelectedTodo(String type, String content, Date belongDate, Date putDate);
    void modifySelectedTodo(long no, boolean done, String type, String content, Date belongDate, Date putDate);
    void deleteSelectedTodo(long no);
    SelectedTodo readASelectedTodoByNO(long no);
    OrderedRealmCollection<SelectedTodo> readSelectedTodoByContent(String keyWord);
    OrderedRealmCollection<SelectedTodo> readSelectedTodoByBelongDate(Date date);
    OrderedRealmCollection<SelectedTodo> readOldSelectedTodo(Date date);
}
