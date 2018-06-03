package in.cyberwalker.alliance.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import in.cyberwalker.alliance.data.entity.User;
import io.reactivex.Single;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user ORDER BY id DESC")
    Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE id = :id")
    Single<User> getUser(int id);

    @Query("SELECT * FROM user WHERE tag = :tag")
    Single<List<User>> loadAllByTag(String tag);

    @Insert
    void insert(User user);

    @Delete
    int delete(User user);

    @Update
    void update(User user);
}
