# SparkTree
Spark技术研究

<pre>
Spark
    Spark是基于内存计算的大数据并行计算框架，Spark基于内存计算，提高了在大数据环境下数据处理的实时性，同时保证了高容错性和高可伸缩性，允许用户将Spark部署在大量廉价硬件之上，形成集群。
</pre>

<pre>
Spark至于Hadoop
    更准确的说，Spark是一个计算框架，而Hadoop中包含计算框架MapReduce和分布式文件系统HDFS，Hadoop更广发的说还包括在其生态系统上的其他系统，如HBASE, HIVE等

    Spark是MapReduce的替代方案，而且兼容HDFS，HIVE等分布式存储层，可融入Hadoop的生态系统，以弥补缺失MapReduce的不足。
    Spark相比Hadoop MapReduce的优势如下
        1）中间结果输出
           基于MapReduce的计算引擎通常会将中间结果输出到磁盘上，进行存储和容错，出于任务管道承接的考虑，当一些查询翻译到MapReduce任务时，往往会产生多个Stage,而这些串联的Stage又依赖于底层文件系统HDFS来存储每一个Stage的输出结果。

           Spark将执行模型抽象为通用的有向无环图执行计划（DAG），这可以将多Stage的任务串联或者并行执行，而无需将Stage中间结果输出到HDFS中.

        2) 数据格式和内存布局
           由于MapReduce Schema on Read处理方式会引起较大的处理开销，Spark抽象出分布式内存存储结果弹性分布式数据集RDD，进行数据额存储,RDD能支持粗粒度写操作，但是对于读取操作，RDD可以精确到每条记录，这使得RDD可以用来作为分布式索引，Spark的特性是能够控制数据在不同节点上的分区，用户可以自定义分区策略，如HASH分区等。

        3）执行策略
           MapReduce的数据Shuffle之前花费了大量的时间来排序，Spark则可减轻上述问题带来的开销，因为Spark任务在Shuffle中不是所有情景都需要排序，所以支持基于Hash分布式聚合，调度中采用更为通用的任务执行计划图（DAG）,每一轮次的输出结果在内存缓存。

        4）任务调度的开销
           传统的MapReduce系统，如Hadoop，是为了运行长达数小时的批量作业而设计的，在某些极端情况下，提交一个任务的延迟非常高。
           Spark采用了事件驱动的类库AKKA来启动任务，通过线程池服用线程来避免进程或线程启动和切换开销 。
Spark 能带来什么
      1）打造全栈多计算范式的高效数据流水线
         Spark支持复杂查询，在简单的Map, Reduce操作之外，Spark还支持SQL查询，流式计算，机器学习和图算法，同时，用户可以在同一个工作流中无缝搭配这些计算范式
      2）轻量级快速处理
         Spark通过将中间结果缓存在呢村减少磁盘I/O来达到性能的提升。
      3）易于使用，Spark支持多语言
      5）与HDFS等存储层兼容
         Spark可以独立运行，除了可以运行在当下的YARN等集群管理系统之外，它还可以读取已有的Hadoop数据，这是个非常大的优势。
</pre>

<pre>
    Spark是基于内存计算的大数据并行计算框架，Spark基于内存计算，提高了在大数据环境下数据处理的实时性，同时保证了高容错性和高可伸缩性，允许用户
    将Spark部署在大量廉价硬件之上，形成集群。
</pre>

BDAS(伯克利大数据分析栈)

![](https://i.imgur.com/LWcJ9JD.png)

![](https://i.imgur.com/iXSxwEG.png)

<pre>
Master架构
      Spark架构采用了分布式计算中的Master-Slave模型，Master是对应集中的含有Master进程的节点，Slave是集群中含有Worker进程的节点，Master作为整个集群的控制器，负责整个集群的正常运行; Worker相当于是计算节点，接收主节点命令与运行状态汇报；Executor负责任务的执行;Client作为用户的客户端负责提交应用，Driver负责控制一个应用的执行
</pre>

<pre>
Spark集群的安装与部署
     Spark在生产环境中，主要部署在安装有Linux系统的集群中，在Linux系统中安装Spark需要预先安装JDK，Scala等所需的依赖，由于Spark是计算框架，所以需要预先在集群内搭建好存储数据的持久化层，如HDFS, Hive等
         1）jdk
         2) scala
         3) SSH免秘钥登录
         5） 安装Hadoop
         6) 安装Spark
</pre>

<pre>
Spark站在巨人的肩膀上，Spark一开始就瞄准性能，将数据（包括部分中间数据）放在内存，在内存计算，用户将重复利用的数据缓存在内存，提高下次计算效率，因此Spark尤其适合迭代型和交互型人物，Spark需要大量的内存，但性能可随着机器数据增多呈线性增长。
</pre>

<pre>
RDD
    在集群背后，有一个非常重要的分布式数据结构，即弹性分布式数据集（RDD），它是逻辑中的实体，在集群中的多台机器上进行了数据分区，通过对多台机器上不同RDD分区的控制，仅能减少机器之间的数据重排（data shuffling）， Spark提供了"partionBy"运算符，能够通过集群中多台机器之间对原始RDD进行数据再分配来创建一个新的RDD，RDD是Spark的核心数据结构，通过RDD的依赖关系形成Spark的调度顺序，通过对RDD的操作形成整个Spark程序。
          RDD的两种创建方式
             1）从Hadoop文件系统（如Hive， HBASE）输入（如HDFS）创建
             2）从父RDD转换得到新的RDD
          RDD的两种操作算子
             1）Transformation（变换）
                Transformation操作是延迟计算的，也就是说从一个RDD转换生成另一个RDD的转换操作不是马上进行的，需要等到有Actions操作时，才真正触发运算
             2）Action(行动)
                Action算法会触发Spark提交作业（Job），并将数据输出到Spark系统
          RDD的重要内部属性
                1）分区列表
                2）计算每个分片的函数
                3）对父RDD的依赖列表
                5）对key-value对数据类型的RDD的分区器，控制分区策略与分区数
                6）每个数据分区的地址列表
</pre>

![](https://i.imgur.com/INXc9mj.png)

<pre>
RDD与分布式系统内存    
</pre>

<pre>
Spark的数据存储
       Spark数据存储的核心是弹性分布式数据集(RDD),RDD可以被抽象地理解为一个大的数组，但是这个数据是分布在集群上的，逻辑上RDD的每隔分区叫一个Partion.
       
       在Spark的执行过程中，RDD经历一个个的Transformatoin算子之后，最后通过action进行触发操作，逻辑上每经历一次变换，就会将RDD转换为一个新的RDD，RDD之间通过Lineage产生依赖关系，RDD会被划分成很多的分区分布到集群的多个节点上中，
</pre>

![](https://i.imgur.com/Y5jje0w.png)

<pre>
Spark工作机制
      Spark的主要模块包括调度和任务分配，I/O模块，通信控制模块，容错模块，Shuffle模块，Spark按照应用，作业，Stage，Task几个层次分别进行调度，采用了经典的FIFO和FAIR等调度算法，在Spark的I/O中，将数据以块为党委进行管理，需要处理的块可以存储在本机内存，磁盘或者集群中的其他机器中，集群中的通信对于命令和状态的传递极为重要，Spark通过AKKA框架进行集群消息通信，分布式系统中的容错性十分重要，Spark通过Lineage和Checkpoint机制进行容错性保证，

      Spark应用提交后经历了一些列的转换，最后成为TASK在每个节点上执行，Spark应用转换：
              RDD的Action算子触发Job的提交，提交到Spark中的Job生成RDD DAG， 由DAGScheduler转化为Stage DAG,每个Stage中产生相应的TASK集合，TaskScheduler将任务分发到Executor执行，每个任务对应相应的一个数据块，使用用户定义的函数处理数据块。

      Spark的底层执行原理，在Spark的底层实现中，通过RDD进行数据的管理，RDD中有一组分布在不同节点的数据块，当Spark的应用在对这个RDD进行操作时，调度器将包含操作的任务分发到指定的机器上执行，在计算节点通过多线程的方式执行任务。一个操作执行完毕，RDD便转换成另一个RDD，这样，用户的操作依次执行，Spark为了系统的内存不至于快速用完，使用延迟执行的方式执行，即只有操作累积到Action，算子才会触发整个操作序列的执行，中间结果不会单独再重新分配内存，而是在同一个数据块上进行流水线操作。

      对RDD的块管理通过BlockManager完成，BlockManager将数据抽象为数据块，在内存或者磁盘进行存储，如果数据不在本节点，则还可以通过远端节点复制到本机进行计算，
</pre>

![](https://i.imgur.com/VJKgetP.png)

<pre>
Spark应用的概念
      Spark应用是用户提交的应用程序，执行模式有Local， Standalone， YARN, Mesos， 根据Spark Application的Driver Program是否在集群中运行，Spark应用的运行方式又可以分为Cluster模式，Client模式
      1）Application 用户自定义的Spark程序，用户提交后，Spark为APP分配资源，将程序转换并执行
      2）Driver Program： 运行Application的main()函数并创建SparkContext.
      3) RDD Graph RDD是Spark的核心结构，可以通过一系列算子进行操作，当RDD遇到ACTION算子时，将之前的所有算子形成一个有向无环图DAG，也就是途中的RDD GRAPH，再在Spark中转换为JOB,提交到集群执行，一个APP中可以包含多个JOB。
      5）JOB 一个RDD GRAPH触发的作业，往往由Spark Action算子触发，在SparkContext中通过runJob方法向Spark提交Job
      6）Stage 每个Job会根据RDD的依赖关系被切分很多Stage，每个Stage中包含一组相同的Task，这一组Task也叫TaskSet
      7) Task 一个分区对应一个Task，Task执行RDD总对应Stage中包含的算子，Task被封装好后放入Executor的线程池中执行。
</pre>

![](https://i.imgur.com/UjowBZg.png)

<pre>
应用的提交与执行方式
     应用的提交包含两种方式
          1）Driver进行运行在客户端，对应用进行管理监控
          2）主节点指定某个Worker节点启动Driver，负责整个应用的监控
     Driver进程是应用的主控进程，负责应用的解析，切分Stage并调度 Task到Executor执行，包含DAGScheduler等重要对象，
</pre>