package com.swipememo.swipememo.model.types;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dodoon 2016-11-07.
 */

public class MemoDirectory extends RealmObject {


    private long no;

    @PrimaryKey
    private String dirCode;
    private String name;

    private String pw;
    private Date createDate;


    public long getNo() {return no;}
    public void setNo(long no) { this.no = no;}
    public String getDirCode() {return dirCode;}
    public void setDirCode(String dirCode) {this.dirCode = dirCode;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPw() {return pw;}
    public void setPw(String pw) {this.pw = pw;}
    public Date getCreateDate() {return createDate;}
    public void setCreateDate(Date createDate) {this.createDate = createDate;}
}
