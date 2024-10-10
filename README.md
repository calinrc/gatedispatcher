# Gate Dispatcher
Small Android application that monitors incoming SMS and initiates a phone call for specific message

This application is not published in PlayStore due to the permissions restrictions and security reasons.
Note: The application does not handle RCS messages and monitors only SMS ones.
At the writing moment of the application google did not publish any API to monitor and read RCS messages and these are handled only by google Messaging Application or other OEM .
Details in here: https://9to5google.com/2019/07/30/android-rcs-apis-oems-not-third-party-apps/
The application was tested on Google Pixel 6a phone.



Note: If device that install the application and the sender of message are RCS capable, the communication will use this protocol.
In order to correctly receive any incoming message the solution is to unregister the device that install the application from RCS.
Details about how to disable RCS messages can be found on https://support.google.com/messages/answer/7189714?hl=en 

