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


