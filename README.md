# SomaggiaInFesta

Android app that allows communication between one kitchen and many cash desks. 

The goals of this communication are:

* share the menu that the kitchen offers 
* send orders from cash desks to kitchen
* notify cash desks when an order is ready

<p align="center">
  <img src="https://github.com/nicholasala/SomaggiaInFesta/blob/master/images/scheme.png">
</p>

Connections are made through WebSocket protocol. At every menu changes all connected cash desks are notified and get the updated menu.

The architecture used is a peer-to-peer architecture: there is no a fixed kicthen device. A device can choose if work as cash desk or as kitchen at the start of application. In this first version only one kictchen per net is allowed but in the future this structure can be extended.
