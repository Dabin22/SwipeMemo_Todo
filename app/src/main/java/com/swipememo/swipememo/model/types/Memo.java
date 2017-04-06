package com.swipememo.swipememo.model.types;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dodo on 2016-11-07.
 */

public class Memo extends RealmObject {
    @Ignore
    public static final String DEFAULT_FOLDER ="DEFAULT_FOLDER";

    @PrimaryKey
    private long no;

    private String dirCode;
    private boolean important;
    private String title;
    private String content;
    private Date createDate;
    private boolean deleted;
    private Date deletedDate;
    private Date modifiedDate;

    public long getNo() { return no;}
    public void setNo(long no) {this.no = no;}
    public String getDirCode() {return dirCode;}
    public void setDirCode(String dirCode) {this.dirCode = dirCode;}
    public boolean isImportant() {return important;}
    public void setImportant(boolean important) {this.important = important;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public Date getCreateDate() {return createDate;}
    public void setCreateDate(Date createDate) {this.createDate = createDate;}
    public boolean isDeleted(){return deleted;}
    public void setDeleted(boolean deleted){this.deleted = deleted;}
    public Date getDeletedDate() {return deletedDate;}
    public void setDeletedDate(Date deletedDate) {this.deletedDate = deletedDate;}
    public Date getModifiedDate(){return modifiedDate;}
    public void setModifiedDate(Date modifiedDate){this.modifiedDate = modifiedDate;}
}
