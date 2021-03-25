package dp.ibps.generalawareness.Room.DAO;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import dp.ibps.generalawareness.Room.MainRoomDB;
import dp.ibps.generalawareness.Room.Model.NotificationsModel;

@Database(entities = {NotificationsModel.class}, version = 1, exportSchema = false)
public abstract class NotificationDB extends RoomDatabase {
    public abstract MainRoomDB mainRoomDB();

}
