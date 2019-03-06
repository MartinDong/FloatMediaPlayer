package com.dong.floatmediaplayer.presenter

import com.dong.floatmediaplayer.base.BasePresenter
import com.dong.floatmediaplayer.bean.SongHiBaiResponse
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.dong.floatmediaplayer.model.SongHiBaiListModel
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class SongHiBaiListPresenter() :
    BasePresenter<SongHiBaiListContract.View>(),
    SongHiBaiListContract.Presenter {

    private var mode: SongHiBaiListModel = SongHiBaiListModel()

    override fun getSongList() {
        if (!isViewAttached()) {
            return
        }
        mView?.showLoading()

        mode.getSongList()
            //create方法中多了一个BackpressureStrategy类型的参数
            //为上下游分别指定各自的线程
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(Schedulers.newThread())
            ?.subscribe(
                object : Subscriber<SongHiBaiResponse> {
                    //onSubscribe回调的参数不是Disposable而是Subscription
                    override fun onSubscribe(s: Subscription) {
                        //注意此处，暂时先这么设置
                        s.request(java.lang.Long.MAX_VALUE)
                    }

                    override fun onNext(response: SongHiBaiResponse?) {
                        println("接收----> " + response!!)
                        mView?.onSuccess(response)
                    }

                    override fun onError(t: Throwable) {}

                    override fun onComplete() {
                        println("接收----> 完成")
                    }
                })
    }

}