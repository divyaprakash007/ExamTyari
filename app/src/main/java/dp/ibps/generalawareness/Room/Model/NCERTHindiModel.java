package dp.ibps.generalawareness.Room.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class NCERTHindiModel {

    @PrimaryKey(autoGenerate = true)
    int hiBookID;
    String className;
    String bookName;
    String subName;
    String linkUrl;

    public NCERTHindiModel(String className, String bookName, String subName, String linkUrl) {
        this.className = className;
        this.bookName = bookName;
        this.subName = subName;
        this.linkUrl = linkUrl;
    }

    public int getHiBookID() {
        return hiBookID;
    }

    public void setHiBookID(int hiBookID) {
        this.hiBookID = hiBookID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
