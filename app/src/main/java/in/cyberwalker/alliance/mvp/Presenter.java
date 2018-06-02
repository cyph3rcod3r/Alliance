package in.cyberwalker.alliance.mvp;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @param <V> extends BaseView
 * @author Pankaj
 * @version 1.0
 * @since 6/4/18
 */

public class Presenter<V extends View> {

    private volatile V view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @NonNull
    protected V view() {
        if (view == null) {
            throw new IllegalStateException("You've not added a view");
        }

        return view;
    }

    @CallSuper
    public void bindView(@NonNull V view) {
        final V previousView = this.view;
        if (previousView != null) {
            throw new IllegalStateException("Clear previousView first= " + previousView);
        }

        this.view = view;
    }

    @CallSuper
    public void unbindView(@NonNull V view) {
        final V previousView = this.view;

        if (previousView == view) {
            this.view = null;
        } else {
            throw new IllegalStateException("Not expecting this previousView = " + previousView + ", view to unbind = " + view);
        }

        compositeDisposable.clear();
    }

    protected final void addDisposablesToUnsubscribe(@NonNull Disposable... disposables) {

        for (Disposable d : disposables) {
            this.compositeDisposable.add(d);
        }
    }
}
