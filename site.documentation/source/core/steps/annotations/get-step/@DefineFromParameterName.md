# @DefineFromParameterName

Название параметра, обозначающего шаг, цепочку шагов или значение, посредством которых вычисляется конечный результат.

Данная аннотация применяется для тех классов get-шагов, для которых предусмотрено получение конечного результата 
через промежуточную цепочку шагов или других вычислений. Примеры были описаны: 

- [Возврат объекта](./../../steps/get_step_supplier/get_object_step_supplier_with_criteria_chained.rst)
- [Возврат элемента коллекции/Iterable](./../../steps/get_step_supplier/get_object_from_iterable_step_supplier_chained.rst)
- [Возврат элемента массива](./../../steps/get_step_supplier/get_object_from_array_step_supplier_chained.rst)
- [Возврат листа](./../../steps/get_step_supplier/get_list_step_supplier_chained.rst)
- [Возврат массива](./../../steps/get_step_supplier/get_array_step_supplier_chained.rst)


В остальных случаях ее присутствие игнорируется.

```{eval-rst}
.. include:: from_example.rst
```