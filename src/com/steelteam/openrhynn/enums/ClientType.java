/*
MIT License
-----------

Copyright (c) 2019 Ivan Yurkov (MB "Stylo tymas" http://steel-team.net)
Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/
package com.steelteam.openrhynn.enums;

public enum ClientType {
    J2ME,
    PC,
    TOUCH,
    ANDROID,
    NOKIA,
    OUYA,
    UNKNOWN;

    public static ClientType fromInt(int clientType) {
        switch(clientType) {
            case 0:
                return J2ME;
            case 1:
                return PC;
            case 2:
                return TOUCH;
            case 3:
                return ANDROID;
            case 4:
                return NOKIA;
            case 5:
                return OUYA;
        }
        return UNKNOWN;
    }

    public static int toInt(ClientType clientType) {
        switch(clientType) {
            case J2ME:
                return 0;
            case PC:
                return 1;
            case TOUCH:
                return 2;
            case ANDROID:
                return 3;
            case NOKIA:
                return 4;
            case OUYA:
                return 5;
        }
        return 6;
    }
}
