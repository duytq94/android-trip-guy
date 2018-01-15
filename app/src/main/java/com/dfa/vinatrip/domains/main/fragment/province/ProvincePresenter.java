package com.dfa.vinatrip.domains.main.fragment.province;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/6/2017.
 */

public class ProvincePresenter extends BasePresenter<ProvinceView> {
    private DataService dataService;
    private Subscription subscription;

    @Inject
    public ProvincePresenter(EventBus eventBus, DataService dataService) {
        super(eventBus);
        this.dataService = dataService;
    }

    public void getBanner() {
        List<String> banners = new ArrayList<>();
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/26804879_1981202975473885_4540897757226437014_n.jpg?oh=819a447fdf779d0dbe66582daea5f1ae&oe=5AB48988");
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/26219195_1979809222279927_7685762689911134914_n.png?oh=9dc26297250e10eee3eab42a8788b7b2&oe=5AB172D4");
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/26219701_1978735739053942_3723638708513560545_n.jpg?oh=c2b448f34a991b2ba583b9dfa12ce415&oe=5AB4B7F7");
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/26731574_1978336892427160_1245271809332170042_n.jpg?oh=44b17bf3885b22ec5c66b9b86672d680&oe=5AEEB113");
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/26196200_1976938599233656_1143330661903970871_n.jpg?oh=0eadad66bf73a29bcee5ec6fc4ef8e0d&oe=5AE1D72A");
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/26167286_1975973539330162_774542050158118312_n.jpg?oh=d90f5663e4da82f973902b5a29a2c828&oe=5AFC0AE7");
        banners.add("https://scontent.fsgn5-1.fna.fbcdn.net/v/t1.0-9/25994949_1974099259517590_5697411130198827230_n.jpg?oh=161019e2d904ceffde5a12e3290dc23a&oe=5AFB4E3B");

        getView().getBannerSuccess(banners);
    }

    public void getProvince(long page, long per_page) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = dataService.getProvinces(page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(provinces -> {
                    if (isViewAttached()) {
                        getView().getProvinceSuccess(provinces);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
