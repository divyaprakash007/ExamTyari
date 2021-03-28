package dp.ibps.generalawareness.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import dp.ibps.generalawareness.Room.Model.NCERTEnglishModel;
import dp.ibps.generalawareness.Room.Model.NCERTHindiModel;
import dp.ibps.generalawareness.Room.Model.NotificationsModel;

@Dao
public interface MainRoomDB {

    @Insert
    public void notificationInsert(NotificationsModel notificationsModel);

    @Query("Select * from NotificationsModel")
    List<NotificationsModel> getNotifications();

    @Query("DELETE FROM NotificationsModel")
    public void deleteAllNotifications();

    @Insert
    public void hindiBooksDetailsInsert(NCERTHindiModel ncertHindiModel);

    @Query("Select * from NCERTHindiModel")
    List<NCERTHindiModel> getHindiNCERTDetails();

    @Query("DELETE FROM NCERTHindiModel")
    public void deleteAllHindiNCERT();

    @Insert
    public void englishBooksDetailsInsert(NCERTEnglishModel ncertEnglishModel);

    @Query("Select * from NCERTEnglishModel")
    List<NCERTEnglishModel> getEnglishNCERTDetails();

    @Query("DELETE FROM NCERTEnglishModel")
    public void deleteAllEnglishNCERT();




}
