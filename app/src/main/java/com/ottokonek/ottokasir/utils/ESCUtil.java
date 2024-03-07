package com.ottokonek.ottokasir.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ESCUtil {

    public static final byte ESC = 0x1B;// Escape
    public static final byte FS = 0x1C;// Text delimiter
    public static final byte GS = 0x1D;// Group separator
    public static final byte DLE = 0x10;// data link escape
    public static final byte EOT = 0x04;// End of transmission
    public static final byte ENQ = 0x05;// Enquiry character
    public static final byte SP = 0x20;// Spaces
    public static final byte HT = 0x09;// Horizontal list
    public static final byte LF = 0x0A;//Print and wrap (horizontal orientation)
    public static final byte CR = 0x0D;// Home key
    public static final byte FF = 0x0C;// Carriage control (print and return to the standard mode (in page mode))
    public static final byte CAN = 0x18;// Canceled (cancel print data in page mode)

    public static final byte[] UNICODE_TEXT = new byte[] {0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23};

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = { "0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111" };




    //初始化打印机
    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 0x40;
        return result;
    }

    //打印浓度指令
    public static byte[] setPrinterDarkness(int value) {
        byte[] result = new byte[9];
        result[0] = GS;
        result[1] = 40;
        result[2] = 69;
        result[3] = 4;
        result[4] = 0;
        result[5] = 5;
        result[6] = 5;
        result[7] = (byte) (value >> 8);
        result[8] = (byte) value;
        return result;
    }

    /**
     *       * Print a single QR code sunmi custom instructions
     *       * @param code: QR code data
     *       * @param modulesize: Size of QR code block (unit: point, value 1 to 16)
     *       * @param errorlevel: 2D code error correction level (0 to 3)
     *       * 0 -- Error Correction Level L ( 7%)
     *       * 1 -- Error Correction Level M (15%)
     *       * 2 -- Error Correction Level Q (25%)
     *       * 3 -- Error Correction Level H (30%)
     */
    public static byte[] getPrintQRCode(String code, int modulesize, int errorlevel) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code));
            buffer.write(getBytesForPrintQRCode(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    /**
     * Horizontal two-dimensional code sunmi custom instructions
     *
     * @param code1:      QR code data
     * @param code2:      QR code data
     * @param modulesize: QR code block size (unit: point, value 1 to 16)
     * @param errorlevel: QR code error correction level (0 to 3)
     *                    0 -- Error correction level L ( 7%)
     *                    1 -- Error correction level M (15%)
     *                    2 -- Error correction level Q (25%)
     *                    3 -- Error correction level H (30%)
     */
    public static byte[] getPrintDoubleQRCode(String code1, String code2, int modulesize, int errorlevel) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code1));
            buffer.write(getBytesForPrintQRCode(false));
            buffer.write(getQCodeBytes(code2));

            //加入横向间隔
            buffer.write(new byte[]{0x1B, 0x5C, 0x18, 0x00});

            buffer.write(getBytesForPrintQRCode(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    /**
     * 光栅打印二维码
     */
    public static byte[] getPrintQRCode2(String data, int size) {
        byte[] bytes1 = new byte[4];
        bytes1[0] = GS;
        bytes1[1] = 0x76;
        bytes1[2] = 0x30;
        bytes1[3] = 0x00;

        byte[] bytes2 = BytesUtil.getZXingQRCode(data, size);
        return BytesUtil.byteMerger(bytes1, bytes2);
    }

    /**
     * 打印一维条形码
     */
    public static byte[] getPrintBarCode(String data, int symbology, int height, int width, int textposition) {
        if (symbology < 0 || symbology > 10) {
            return new byte[]{LF};
        }

        if (width < 2 || width > 6) {
            width = 2;
        }

        if (textposition < 0 || textposition > 3) {
            textposition = 0;
        }

        if (height < 1 || height > 255) {
            height = 162;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(new byte[]{0x1D, 0x66, 0x01, 0x1D, 0x48, (byte) textposition,
                    0x1D, 0x77, (byte) width, 0x1D, 0x68, (byte) height, 0x0A});

            byte[] barcode;
            if (symbology == 10) {
                barcode = BytesUtil.getBytesFromDecString(data);
            } else {
                barcode = data.getBytes("GB18030");
            }


            if (symbology > 7) {
                buffer.write(new byte[]{0x1D, 0x6B, 0x49, (byte) (barcode.length + 2), 0x7B, (byte) (0x41 + symbology - 8)});
            } else {
                buffer.write(new byte[]{0x1D, 0x6B, (byte) (symbology + 0x41), (byte) barcode.length});
            }
            buffer.write(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    //光栅位图打印
    public static byte[] printBitmap(Bitmap bitmap) {
        byte[] bytes1 = new byte[4];
        bytes1[0] = GS;
        bytes1[1] = 0x76;
        bytes1[2] = 0x30;
        bytes1[3] = 0x00;

        byte[] bytes2 = BytesUtil.getBytesFromBitMap(bitmap);
        return BytesUtil.byteMerger(bytes1, bytes2);
    }

    //光栅位图打印 设置mode
    public static byte[] printBitmap(Bitmap bitmap, int mode) {
        byte[] bytes1 = new byte[4];
        bytes1[0] = GS;
        bytes1[1] = 0x76;
        bytes1[2] = 0x30;
        bytes1[3] = (byte) mode;

        byte[] bytes2 = BytesUtil.getBytesFromBitMap(bitmap);
        return BytesUtil.byteMerger(bytes1, bytes2);
    }

    //光栅位图打印
    public static byte[] printBitmap(byte[] bytes) {
        byte[] bytes1 = new byte[4];
        bytes1[0] = GS;
        bytes1[1] = 0x76;
        bytes1[2] = 0x30;
        bytes1[3] = 0x00;

        return BytesUtil.byteMerger(bytes1, bytes);
    }

    /*
     *	选择位图指令 设置mode
     *	需要设置1B 33 00将行间距设为0
     */
    public static byte[] selectBitmap(Bitmap bitmap, int mode) {
        return BytesUtil.byteMerger(BytesUtil.byteMerger(new byte[]{0x1B, 0x33, 0x00}, BytesUtil.getBytesFromBitMap(bitmap, mode)), new byte[]{0x0A, 0x1B, 0x32});
    }

    /**
     * 跳指定行数
     */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    // ------------------------underline-----------------------------
    //设置下划线1点
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    //设置下划线2点
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    //取消下划线
    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }

    // ------------------------bold-----------------------------

    /**
     * 字体加粗
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消字体加粗
     */
    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    // ------------------------character-----------------------------
    /*
     *单字节模式开启
     */
    public static byte[] singleByte() {
        byte[] result = new byte[2];
        result[0] = FS;
        result[1] = 0x2E;
        return result;
    }

    /*
     *单字节模式关闭
     */
    public static byte[] singleByteOff() {
        byte[] result = new byte[2];
        result[0] = FS;
        result[1] = 0x26;
        return result;
    }

    /**
     * 设置单字节字符集
     */
    public static byte[] setCodeSystemSingle(byte charset) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 0x74;
        result[2] = charset;
        return result;
    }

    /**
     * 设置多字节字符集
     */
    public static byte[] setCodeSystem(byte charset) {
        byte[] result = new byte[3];
        result[0] = FS;
        result[1] = 0x43;
        result[2] = charset;
        return result;
    }

    // ------------------------Align-----------------------------

    /**
     * 居左
     */
    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }

    /**
     * 居中对齐
     */
    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    /**
     * 居右
     */
    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////          private                /////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    private static byte[] setQRCodeSize(int modulesize) {
        //二维码块大小设置指令
        byte[] dtmp = new byte[8];
        dtmp[0] = GS;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x43;
        dtmp[7] = (byte) modulesize;
        return dtmp;
    }

    private static byte[] setQRCodeErrorLevel(int errorlevel) {
        //二维码纠错等级设置指令
        byte[] dtmp = new byte[8];
        dtmp[0] = GS;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x45;
        dtmp[7] = (byte) (48 + errorlevel);
        return dtmp;
    }


    private static byte[] getBytesForPrintQRCode(boolean single) {
        //打印已存入数据的二维码
        byte[] dtmp;
        if (single) {        //同一行只打印一个QRCode， 后面加换行
            dtmp = new byte[9];
            dtmp[8] = 0x0A;
        } else {
            dtmp = new byte[8];
        }
        dtmp[0] = 0x1D;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x51;
        dtmp[7] = 0x30;
        return dtmp;
    }

    private static byte[] getQCodeBytes(String code) {
        //二维码存入指令
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            byte[] d = code.getBytes("GB18030");
            int len = d.length + 3;
            if (len > 7092) len = 7092;
            buffer.write((byte) 0x1D);
            buffer.write((byte) 0x28);
            buffer.write((byte) 0x6B);
            buffer.write((byte) len);
            buffer.write((byte) (len >> 8));
            buffer.write((byte) 0x31);
            buffer.write((byte) 0x50);
            buffer.write((byte) 0x30);
            for (int i = 0; i < d.length && i < len; i++) {
                buffer.write(d[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }


    public static byte[] decodeBitmap(Bitmap bmp){
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;


        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
            Log.e("decodeBitmap error", " width is too large");
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            Log.e("decodeBitmap error", " height is too large");
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString+widthHexString+heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);

                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;

    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }

        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}