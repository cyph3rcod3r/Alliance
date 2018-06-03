package in.cyberwalker.alliance.data.repo;

import android.text.TextUtils;

import java.util.List;

import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.dao.UserDao;
import in.cyberwalker.alliance.data.entity.User;
import io.reactivex.Single;

public class PeopleRepo {

    private UserDao userDao;

    public PeopleRepo(AppDatabase appDatabase) {
        userDao = appDatabase.userDao();
    }

    public void saveUser(User user) {
        userDao.insert(user);
    }

    public Single<List<User>> listUsers(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return userDao.getAll();
        } else {
            return userDao.loadAllByTag(tag);
        }
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public Single<User> getUserById(int id) {
        return userDao.getUser(id);
    }

    public int delete(User user) {
        return userDao.delete(user);
    }


}
