package in.cyberwalker.alliance.mvp.presenter;

import java.util.Collections;

import in.cyberwalker.alliance.data.AppDatabase;
import in.cyberwalker.alliance.data.repo.PeopleRepo;
import in.cyberwalker.alliance.mvp.Presenter;
import in.cyberwalker.alliance.mvp.view.HomeView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends Presenter<HomeView> {

    private PeopleRepo peopleRepo;

    public HomePresenter(AppDatabase appDatabase) {
        peopleRepo = new PeopleRepo(appDatabase);
    }

    public void init() {
        view().showProgress();
        Disposable d = peopleRepo.listUsers(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    view().onUserList(list, false);
                    view().hideProgress();
                }, e -> {
                    view().hideProgress();
                });
        addDisposablesToUnsubscribe(d);

    }

    /**
     * filtering and sorting
     */
    public void beginFilteringSorting() {
        if (view().getAdapterSize() < 1) {
            return;
        }
        String tag = null;
        if (!"all".equalsIgnoreCase(view().filterVal())) {
            tag = view().filterVal();
        }

        Disposable d = peopleRepo.listUsers(tag)
                .map(list -> {
                    if ("name".equalsIgnoreCase(view().sortVal()))
                        Collections.sort(list);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    view().onUserList(list, true);
                });
        addDisposablesToUnsubscribe(d);
    }
}
