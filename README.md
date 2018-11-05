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