package in.cyberwalker.alliance.mvp.view;

import java.util.List;

import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.mvp.View;

public interface HomeView extends View {
    void onUserList(List<User> list, boolean filterApplied);

    void showHideBottomSheet(int state);

    String filterVal();

    String sortVal();

    int getAdapterSize();
}
