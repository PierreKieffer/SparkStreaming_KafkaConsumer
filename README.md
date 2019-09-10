# KafkaConsumer
Kafka consumer example based on spark streaming  with message formatting to spark dataframe. 

- Input (From Kafka) : 
```
Row01,Value1,value2,value3,value4
Row1,Value11,value12,value13,value14
Row2,Value21,value22,value23,value24
Row3,Value31,value32,value33,value34
```


- Output :
```
+-----+-------+-------+-------+-------+
|  ROW|   VAL1|   VAL2|   VAL3|   VAL4|
+-----+-------+-------+-------+-------+
|Row01| Value1| value2| value3| value4|
| Row1|Value11|value12|value13|value14|
| Row2|Value21|value22|value23|value24|
| Row3|Value31|value32|value33|value34|
+-----+-------+-------+-------+-------+
```


