## RxJava2

在Android中使用RxJava2需要在Gradle的配置中添加(Android Studio3.0)：

	implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'

在查看相关资料，对RxJava2的描述大概有两种比较典型的：

* 根据观察者模式去理解RxJava2

	* 

* 用水管来代替观察者和被观察者

	* 我们把观察者（Observer）当作水管的下游，被观察者（Observable）当作水管的上游
	* 上游和下游之间通过闸（subscribe）给关联起来
	
* ObservableEmitter: Emitter是发射器，它可以发射出三种事件类型：onNext,onError, onComplete

这三种事件的发射需要满足以下的规则：

上游可以发送无限个onNext，下游也可以接收无限个oNext

当上游发送了一个onComplete后，上游onComplete之后的事件将会继续发送，而下游收到onComplete事件后将不再继续接受事件

当上游发送了一个onError后，上游onError之后的事件将继续发送，而下游收到onError事件之后将不在继续接收事件

上游可以不发送onComplete或onError

onComplete和onError必须唯一并且不能同时出现， 既不能发送多个onComplete,也不能发多个onError,也不能先发送一个onComplete再发送一个onError

上游发送的onError事件的参数不能为null

* Disposable

调用dispose（）后会切断上游和下游的联系，上游可以继续发送，但下游却无法接受


## 线程切换


正常情况下，上游和下游会处在同一个线程当中， 可以通过RxJava内部的调度器，可以轻松的切换线程

observeOn ---> 指定观察者所在的线程
subscribeOn ---> 指定被观察者所在的线程

多次调用subscribeOn,只有第一次会生效
多次调用observeOn,每一次都会生效

在RxJava中, 已经内置了很多线程选项供我们选择, 例如有

Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
Schedulers.newThread() 代表一个常规的新线程
AndroidSchedulers.mainThread() 代表Android的主线程
这些内置的Scheduler已经足够满足我们开发的需求, 因此我们应该使用内置的这些选项, 在RxJava内部使用的是线程池来维护这些线程, 所有效率也比较高.


## 操作符

操作符都工作在下游

* Map

map是RxJava中最简单的一个变换操作符了, 它的作用就是对上游发送的每一个事件应用一个函数, 使得每一个事件都按照指定的函数去变化

* flatMap

FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.
flatMap 无序的

* concatMap

作用和flatMap一致，但是它是有序的

* zip

Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件. 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。

* distinct 去重
* filter 过滤
* buffer 缓冲
* timer
* interval 定时执行
* skip 跳过
* take 获取几个
* debounce 操作符是对源Observable间隔期产生的结果进行过滤，如果在这个规定的间隔期内没有别的结果产生，则将这个结果提交给订阅者，否则忽略该结果，原理有点像光学防抖.
* last
* merge
* reduce
* window
* 与interval相比，它可以指定第一个发送数据项的时延、指定发送数据项的个数。
与range相比，它可以指定两项数据之间发送的时延。
intervalRange的接收参数的含义为：

start：发送数据的起始值，为Long型。
count：总共发送多少项数据。
initialDelay：发送第一个数据项时的起始时延。
period：两项数据之间的间隔时间。
TimeUnit：时间单位。

* repeatWhen 可以实现变长延时

之所以可以通过repeatWhen来实现轮询，是因为它为我们提供了重订阅的功能，而重订阅有两点要素：

上游告诉我们一次订阅已经完成，这就需要上游回调onComplete函数。
我们告诉上游是否需要重订阅，通过repeatWhen的Function函数所返回的Observable确定，如果该Observable发送了onComplete或者onError则表示不需要重订阅，结束整个流程；否则触发重订阅的操作。

* retryWhen

retryWhen提供了 重订阅 的功能，对于retryWhen来说，它的重订阅触发有两点要素：
上游通知retryWhen本次订阅流已经完成，询问其是否需要重订阅，该询问是以onError事件触发的。
retryWhen根据onError的类型，决定是否需要重订阅，它通过返回一个ObservableSource<?>来通知，如果该ObservableSource返回onComplete/onError，那么不会触发重订阅；如果发送onNext，那么会触发重订阅。

可以看到，retryWhen和repeatWhen最大的不同就是：retryWhen是收到onError后触发是否要重订阅的询问，而repeatWhen是通过onComplete触发。

* combineLatest

该操作符接受多个Observable以及一个函数作为参数，并且函数的签名为这些Observable发射的数据类型。当以上的任意一个Observable发射数据之后，会去取其它Observable 最近一次发射的数据，回调到函数当中，但是该函数回调的前提是所有的Observable都至少发射过一个数据项。

* concat

它会连接多个Observable，并且必须要等到前一个Observable的所有数据项都发送完之后，才会开始下一个Observable数据的发送。

* concatEager

和concat最大的不同就是多个Observable可以同时开始发射数据，如果后一个Observable发射完成后，前一个Observable还有发射完数据，那么它会将后一个Observable的数据先缓存起来，等到前一个Observable发射完毕后，才将缓存的数据发射出去。
* merge

它和concatEager一样，会让多个Observable同时开始发射数据，但是它不需要Observable之间的互相等待，而是直接发送给下游。

* publish

## 背压

背压： 上游和下游发送和接收的速度不同，就会导致OOM

Flowable 背压大小128，默认

BackpressureStrategy.ERROR

BackpressureStrategy.BUFFER

BackpressureStrategy.DROP

BackpressureStrategy.LATEST

onBackpressureBuffer()
onBackpressureDrop()
onBackpressureLatest()