# SomaggiaInFesta

Android app that allows communication between one kitchen and many cash desks. 

The goals of this communication are:

* share the menu that the kitchen offers 
* send orders from cash desks to kitchen
* notify cash desks when an order is ready

![alt text](https://github.com/nicholasala/SomaggiaInFesta/blob/master/images/scheme.png)

Connections are made through WebSocket protocol. At every menu changes all the connected cash desk are notified and obtain the updated menu. 