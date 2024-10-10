# Gate Dispatcher
## Description
Small Android application that monitors incoming SMS and initiates a phone call for specific message

This application is not published in PlayStore due to the permissions restrictions and security reasons.


## Message Types (SMS vs RCS)
The application does not handle RCS messages and monitors only SMS ones.
At the writing moment of the application google has not published any API to monitor and read RCS messages 
and these are handled only by google Messaging Application or other OEM applications.

Details in here: https://9to5google.com/2019/07/30/android-rcs-apis-oems-not-third-party-apps/

## Testing
The application was tested on Google Pixel 6a phone.


## Troubleshooting
Note: If device that install the application and the sender of message are RCS capable, the communication will use this protocol.
In order to correctly receive any incoming message the solution is to unregister the device that install the application from RCS.

Details about how to disable RCS messages can be found on https://support.google.com/messages/answer/7189714?hl=en 

## Permissions
The application requires three types of permission:
 - Read SMS
 - Make Calls
 - Display over other apps
These permissions must be allowed in order to properly run the applications