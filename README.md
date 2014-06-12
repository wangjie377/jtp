jtp
===

https://github.com/wangjie377/jtp.git


JohanThreadPool最早起源于jdk-1.4时代，当时如果有完善的ThreadPoolExecutor，或许JohanThreadPool就不会诞生了：）

不过经过多年对JohanThreadPool的小修小改，经过各种使用场合的锤炼，发现不论是开发，还是性能上都明显优于ThreadPoolExecutor。

具体用法不解释，请看demo：johan.util.thread.pool.PoolTest

代码GitHub中，请稍后...(csdn上的有bug，非常sorry）
这里简单比较一下JohanThreadPool和ThreadPoolExecutor异同，简单说一下为什么好用（也欢迎大家提出异议，帮助改进JohanThreadPool）

1、代码更加抽象

将Job和Runnable分离。ThreadPoolExecutor大的缺点是，你每次执行一个任务，都得生成一个Runnable，处理逻辑必须写死在里面的run方法上，待执行的Job与之关联的不优雅。

JohanThreadPool是个模板类，你可以定义一个Job的class，作为待处理任务；实现处理逻辑只要重写其doJob(T t)方法即可。

2、合并coreSize和minSize

在JohanThreadPool，coreSize和minSize是同一个东西；而且去掉了lazy模式，初始化时即生成minSize个worker。

3、去掉了维护线程池的各种没必要的东东

例如好多状态变量什么的。

尽管JohanThreadPool封装上比ThreadPoolExecutor更加复杂，但是核心数据结构简单很多，运行机制也非常简洁；简单总是没错的，而且测试也说明了问题，性能上优于ThreadPoolExecutor。
