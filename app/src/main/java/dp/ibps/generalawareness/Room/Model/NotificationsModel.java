package dp.ibps.generalawareness.Room.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotificationsModel {

    @PrimaryKey (autoGenerate = true)
    int noti_id;
    String date;
    String link;
    String message;
    String title;

    public NotificationsModel(String date, String link, String message, String title) {
        this.date = date;
        this.link = link;
        this.message = message;
        this.title = title;
    }

    public int getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(int noti_id) {
        this.noti_id = noti_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
