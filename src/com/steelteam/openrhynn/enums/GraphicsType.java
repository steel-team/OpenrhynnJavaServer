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

public enum GraphicsType {
    BACKGROUND,
    CHARACTER;

    public static GraphicsType fromString(String graphicsType) {
        switch (graphicsType) {
            case "background":
                return BACKGROUND;
            case "character":
                return CHARACTER;
        }
        return BACKGROUND;
    }

    public static GraphicsType fromInt(int graphicsType) {
        switch(graphicsType) {
            case 0:
                return BACKGROUND;
            case 1:
                return CHARACTER;
        }
        return BACKGROUND;
    }

    public static int toInt(GraphicsType graphicsType) {
        switch(graphicsType) {
            case BACKGROUND:
                return 0;
            case CHARACTER:
                return 1;
        }
        return 0;
    }

    public static String toString(GraphicsType graphicsType) {
        switch(graphicsType) {
            case BACKGROUND:
                return "background";
            case CHARACTER:
                return "character";
        }
        return "background";
    }
}
