package com.weiweisc.util;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.ByteArrayOutputStream;

/**
 * 这是产生一个随机sessionid字符串的工具类.
 * 使用36位随机数 + JVM系统时间 + 调用次数 + IP.
 */
public final class IDGenerator {

    private static int count = 0 ;
    //
    private static final byte SESSION_BYTE[] =
    {
       (byte)'a',(byte)'b',(byte)'c',(byte)'d',(byte)'e',(byte)'f',(byte)'g',
       (byte)'h',(byte)'i',(byte)'j',(byte)'k',(byte)'l',(byte)'m',(byte)'n',
       (byte)'o',(byte)'p',(byte)'q',(byte)'r',(byte)'s',(byte)'t',(byte)'u',
       (byte)'v',(byte)'w',(byte)'x',(byte)'y',(byte)'z',
       (byte)'0',(byte)'1',(byte)'2',(byte)'3',(byte)'4',(byte)'5',(byte)'6',
       (byte)'7',(byte)'8',(byte)'9',
       (byte)'A',(byte)'B',(byte)'C',(byte)'D',(byte)'E',(byte)'F',(byte)'G',
       (byte)'H',(byte)'I',(byte)'J',(byte)'K',(byte)'L',(byte)'M',(byte)'N',
       (byte)'O',(byte)'P',(byte)'Q',(byte)'R',(byte)'S',(byte)'T',(byte)'U',
       (byte)'V',(byte)'W',(byte)'X',(byte)'Y',(byte)'Z',
       (byte)'0',(byte)'9'
    };
    //
    private static byte padding = (byte)'1';
    //
    private static String ip = null ;
    //
    static
    {
        try
        {
            byte[] b = InetAddress.getLocalHost().getAddress() ;
            ip = getBytesString( b ) ;
        }
        catch (UnknownHostException e)
        {
            ip = "2aab" ;
        }
    }
    /**
     * protected constructor.
     */
    private IDGenerator(){ }
    /**
     *
     * @return
     */
    public static String generatorID()
    {
        StringBuffer sb = new StringBuffer();

        byte[] b = RandomBytes.nextBytes( 36 ) ;

        sb.append( encode(b) ).append("_")
        .append( System.currentTimeMillis() )
        .append( getCountString() )
        .append( ip ) ;

        return sb.toString();
    }
    /**
     * 获取执行此方法前的执行次数.
     * @return
     */
    public static int getCount( )
    {
        synchronized( IDGenerator.class )
        {
            if ( count==Integer.MAX_VALUE )
                count = 1 ;
            else
                count++;
        }
        return count ;
    }
    /**
     *
     * @return
     */
    /*
    private static String getSystemString( )
    {
        byte[] b = new byte[8] ;
        Converter.longToBytes( System.currentTimeMillis(),b,0 );
        return encode( b ) ;
    }
    */
    /**
     *
     * @return
     */
    private static String getCountString()
    {
        byte[] b = Converter.intToBytes( getCount() ) ;
        return getBytesString( b );
    }
    /**
     *
     * @param b
     * @return
     */
    public static String getBytesString( byte[] b)
    {
        for( int i=0;i<b.length;i++ )
            b[i] = SESSION_BYTE[ b[i]&0x3F ] ;
        return new String( b );
    }
    /**
     * encode the input data producing a base 64 output stream.
     *
     * @return the number of bytes produced.
     */
    private static String encode ( byte[] data )
    {
        if ( data==null )
            return null ;
        //byte data[] = input.getBytes() ;
        if ( data.length==0 )
            return "" ;

        ByteArrayOutputStream out = new ByteArrayOutputStream(8129);
        //int length = data.length() ;
        int modulus = data.length % 3;
        int dataLength = ( data.length - modulus);
        int a1, a2, a3;

        for (int i = 0; i < dataLength; i += 3)
        {
            a1 = data[i] & 0xff;
            a2 = data[i + 1] & 0xff;
            a3 = data[i + 2] & 0xff;

            out.write(SESSION_BYTE[(a1 >>> 2) & 0x3f]);
            out.write(SESSION_BYTE[((a1 << 4) | (a2 >>> 4)) & 0x3f]);
            out.write(SESSION_BYTE[((a2 << 2) | (a3 >>> 6)) & 0x3f]);
            out.write(SESSION_BYTE[a3 & 0x3f]);
        }

        /*
         * process the tail end.
         */
        int    b1, b2, b3;
        int    d1, d2;

        switch (modulus)
        {
            case 0:        /* nothing left to do */
                break;
            case 1:
                d1 = data[dataLength] & 0xff;
                b1 = (d1 >>> 2) & 0x3f;
                b2 = (d1 << 4) & 0x3f;

                out.write(SESSION_BYTE[b1]);
                out.write(SESSION_BYTE[b2]);
                out.write( padding );
                out.write( padding );
                break;
            case 2:
                d1 = data[dataLength] & 0xff;
                d2 = data[dataLength + 1] & 0xff;

                b1 = (d1 >>> 2) & 0x3f;
                b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
                b3 = (d2 << 2) & 0x3f;

                out.write(SESSION_BYTE[b1]);
                out.write(SESSION_BYTE[b2]);
                out.write(SESSION_BYTE[b3]);
                out.write( padding );
                break;
        }
        //out.flush();
        byte result[] = out.toByteArray() ;
        return new String( result ) ;
    }
}
