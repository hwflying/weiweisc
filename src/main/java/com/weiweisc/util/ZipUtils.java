package com.weiweisc.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	/**
	 * 使用gzip进行压缩
	 */
	@SuppressWarnings("restriction")
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip=null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(gzip!=null){
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
	}

	/**	
	 * Description:使用gzip进行解压缩</p>
	 * @param compressedStr
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String gunzip(String compressedStr){
		if(compressedStr==null){
			return null;
		}

		ByteArrayOutputStream out= new ByteArrayOutputStream();
		ByteArrayInputStream in=null;
		GZIPInputStream ginzip=null;
		byte[] compressed=null;
		String decompressed = null;
		try {
			compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
			in=new ByteArrayInputStream(compressed);
			ginzip=new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed=out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}

	/**
	 * 使用zip进行压缩
	 * @param str 压缩前的文本
	 * @return 返回压缩后的文本
	 */	
	@SuppressWarnings("restriction")
	public static final String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * @param compressed 压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			@SuppressWarnings("restriction")
			byte[] compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}
	
	public static void main(String[] arge){
		String s="<root>  <cart>1UD26331FJ326742N</cart>  <createTime>2016-05-13T10:31:19Z</createTime>  <id>PAY-0HE68559LV994181SK422Z4I</id>  <intent>sale</intent>  <links.1.href>https://api.sandbox.paypal.com/v1/payments/payment/PAY-0HE68559LV994181SK422Z4I</links.1.href>  <links.1.method>GET</links.1.method>  <links.1.rel>self</links.1.rel>  <links.listSize>1</links.listSize>  <payer.payerInfo.countryCode>C2</payer.payerInfo.countryCode>  <payer.payerInfo.email>sainje-buyer@163.com</payer.payerInfo.email>  <payer.payerInfo.firstName>test</payer.payerInfo.firstName>  <payer.payerInfo.lastName>buyer</payer.payerInfo.lastName>  <payer.payerInfo.payerId>38ESD46Q9LKDE</payer.payerInfo.payerId>  <payer.payerInfo.phone>2199905422</payer.payerInfo.phone>  <payer.payerInfo.shippingAddress.recipientName>test buyer</payer.payerInfo.shippingAddress.recipientName>  <payer.paymentMethod>paypal</payer.paymentMethod>  <payer.status>VERIFIED</payer.status>  <state>approved</state>  <transactions.1.amount.currency>USD</transactions.1.amount.currency>  <transactions.1.amount.details.subtotal>1.00</transactions.1.amount.details.subtotal>  <transactions.1.amount.total>1.00</transactions.1.amount.total>  <transactions.1.custom>RECHARGE_1345</transactions.1.custom>  <transactions.1.description>钻石充值</transactions.1.description>  <transactions.1.itemList.items.1.currency>USD</transactions.1.itemList.items.1.currency>  <transactions.1.itemList.items.1.name>钻石充值</transactions.1.itemList.items.1.name>  <transactions.1.itemList.items.1.price>1.00</transactions.1.itemList.items.1.price>  <transactions.1.itemList.items.1.quantity>1</transactions.1.itemList.items.1.quantity>  <transactions.1.itemList.items.1.sku>3F14957B1D2916EFF57F6D68329C1F58</transactions.1.itemList.items.1.sku>  <transactions.1.itemList.items.1.tax>0.00</transactions.1.itemList.items.1.tax>  <transactions.1.itemList.items.listSize>1</transactions.1.itemList.items.listSize>  <transactions.1.itemList.shippingAddress.recipientName>test buyer</transactions.1.itemList.shippingAddress.recipientName>  <transactions.1.relatedResources.1.sale.amount.currency>USD</transactions.1.relatedResources.1.sale.amount.currency>  <transactions.1.relatedResources.1.sale.amount.details.subtotal>1.00</transactions.1.relatedResources.1.sale.amount.details.subtotal>  <transactions.1.relatedResources.1.sale.amount.total>1.00</transactions.1.relatedResources.1.sale.amount.total>  <transactions.1.relatedResources.1.sale.createTime>2016-05-13T10:31:19Z</transactions.1.relatedResources.1.sale.createTime>  <transactions.1.relatedResources.1.sale.id>642611167T729324H</transactions.1.relatedResources.1.sale.id>  <transactions.1.relatedResources.1.sale.links.1.href>https://api.sandbox.paypal.com/v1/payments/sale/642611167T729324H</transactions.1.relatedResources.1.sale.links.1.href>  <transactions.1.relatedResources.1.sale.links.1.method>GET</transactions.1.relatedResources.1.sale.links.1.method>  <transactions.1.relatedResources.1.sale.links.1.rel>self</transactions.1.relatedResources.1.sale.links.1.rel>  <transactions.1.relatedResources.1.sale.links.2.href>https://api.sandbox.paypal.com/v1/payments/sale/642611167T729324H/refund</transactions.1.relatedResources.1.sale.links.2.href>  <transactions.1.relatedResources.1.sale.links.2.method>POST</transactions.1.relatedResources.1.sale.links.2.method>  <transactions.1.relatedResources.1.sale.links.2.rel>refund</transactions.1.relatedResources.1.sale.links.2.rel>  <transactions.1.relatedResources.1.sale.links.3.href>https://api.sandbox.paypal.com/v1/payments/payment/PAY-0HE68559LV994181SK422Z4I</transactions.1.relatedResources.1.sale.links.3.href>  <transactions.1.relatedResources.1.sale.links.3.method>GET</transactions.1.relatedResources.1.sale.links.3.method>  <transactions.1.relatedResources.1.sale.links.3.rel>parent_payment</transactions.1.relatedResources.1.sale.links.3.rel>  <transactions.1.relatedResources.1.sale.links.listSize>3</transactions.1.relatedResources.1.sale.links.listSize>  <transactions.1.relatedResources.1.sale.parentPayment>PAY-0HE68559LV994181SK422Z4I</transactions.1.relatedResources.1.sale.parentPayment>  <transactions.1.relatedResources.1.sale.paymentMode>INSTANT_TRANSFER</transactions.1.relatedResources.1.sale.paymentMode>  <transactions.1.relatedResources.1.sale.protectionEligibility>ELIGIBLE</transactions.1.relatedResources.1.sale.protectionEligibility>  <transactions.1.relatedResources.1.sale.protectionEligibilityType>ITEM_NOT_RECEIVED_ELIGIBLE,UNAUTHORIZED_PAYMENT_ELIGIBLE</transactions.1.relatedResources.1.sale.protectionEligibilityType>  <transactions.1.relatedResources.1.sale.state>completed</transactions.1.relatedResources.1.sale.state>  <transactions.1.relatedResources.1.sale.transactionFee.currency>USD</transactions.1.relatedResources.1.sale.transactionFee.currency>  <transactions.1.relatedResources.1.sale.transactionFee.value>0.33</transactions.1.relatedResources.1.sale.transactionFee.value>  <transactions.1.relatedResources.1.sale.updateTime>2016-05-13T10:35:06Z</transactions.1.relatedResources.1.sale.updateTime>  <transactions.1.relatedResources.listSize>1</transactions.1.relatedResources.listSize>  <transactions.listSize>1</transactions.listSize>  <updateTime>2016-05-13T10:35:06Z</updateTime></root>";
		System.out.println(s.length());
		String _s=ZipUtils.gzip(s);
		System.out.println(_s.length());
		System.out.println(_s);
		String s_=ZipUtils.gunzip(_s);
		System.out.println(s.equals(s_));
		System.out.println(s_);
		
		/*String __s=ZipUtils.zip(s);
		System.out.println(__s.length());
		System.out.println(__s);*/
		
	}
	
	
}