package dp.ibps.generalawareness.Room.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import dp.ibps.generalawareness.Room.MainRoomDB;
import dp.ibps.generalawareness.Room.Model.NCERTEnglishModel;
import dp.ibps.generalawareness.Room.Model.NCERTHindiModel;
import dp.ibps.generalawareness.Room.Model.NotificationsModel;

@Database(entities = {NotificationsModel.class,
        NCERTHindiModel.class,
        NCERTEnglishModel.class}, version = 1, exportSchema = false)
public abstract class MainDAOClass extends RoomDatabase {
    public abstract MainRoomDB mainRoomDB();

}
