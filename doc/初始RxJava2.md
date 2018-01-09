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
	* 例子： 

