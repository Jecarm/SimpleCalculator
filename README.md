# SimpleCalculator
This is a simple command-line RPN calculator.

## Support operations
+,-,*,/,sqrt,undo

## Examples
Example1
```
5 2
stack: 5 2
```
Example2
```
2 sqrt
stack:1.4142135623
```

Example3
```
5 2 -
stack:3
3 -
stack:0
clear
stack:
```

Example4
```
5 4 3 2
stack:5 4 3 2
undo undo *
stack:20
5 *
stack:100
undo
stack:20 5
```

Example5
```
7 12 2 /
stack:7 6
*
stack:42
4 /
stack:10.5
```

Example6
```
1 2 3 4 5
stack:1 2 3 4 5
*
stack:1 2 3 20
clear 3 4 -
stack:-1
```

Example7
```
1 2 3 4 5
stack:1 2 3 4 5
* * * *
stack:120
```

Example8
```
1 2 3 * 5 + * * 6 5
operator * (position:15):insucient parameters
stack:11
(the 6 and 5 were not pushed on to the stack due to the previous error)
```


