# read-excel-by-easyexcel

这是一个我自己自娱自乐，写的一个读取 excel 并验证的工具。

基于：
- 阿里巴巴 出版的 easyexcel
- hibernate validator
- lombok

如果 excel 验证有问题，需要将有问题的 Cell 标红，并导出该 excel。

如果验证都没有问题，我还验证了数据行是否重复，因此还需要将每一行的信息保存起来。

验证数据重复需要重写 POJO 的 equals/hashCode 方法。

因此没有导入、导出模板。
