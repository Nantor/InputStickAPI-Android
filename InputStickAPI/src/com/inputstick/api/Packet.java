package com.inputstick.api;

public class Packet {
	
	public static final byte NONE =							0x00;
	
	public static final byte START_TAG = 					0x55;
	public static final byte FLAG_RESPOND = 				(byte)0x80;
	public static final byte FLAG_ENCRYPTED = 				0x40;
	
	public static final int MAX_SUBPACKETS = 				17;	
	public static final int MAX_LENGTH = 					MAX_SUBPACKETS * 16;
	
	public static final byte CMD_IDENTIFY =		 			0x01;
	public static final byte CMD_LED =				 		0x02;
	public static final byte CMD_RUN_BL =		 			0x03;
	public static final byte CMD_RUN_FW =		 			0x04;
	public static final byte CMD_GET_INFO =		 			0x05;
	public static final byte CMD_BL_ERASE =		 			0x06;
	public static final byte CMD_ADD_DATA =		 			0x07;
	public static final byte CMD_BL_WRITE =		 			0x08;
	
	public static final byte CMD_FW_INFO =		 			0x10;
	public static final byte CMD_INIT =			 			0x11;
	public static final byte CMD_INIT_AUTH =	 			0x12;
	public static final byte CMD_INIT_CON =		 			0x13;
	public static final byte CMD_SET_VALUE =	 			0x14;
	public static final byte CMD_RESTORE_DEFAULTS =	 		0x15;
	public static final byte CMD_RESTORE_STATUS =	 		0x16;
	public static final byte CMD_GET_VALUE =				0x17;	
	public static final byte CMD_SET_PIN =	 				0x18;
	public static final byte CMD_USB_RESUME =	 			0x19;
	public static final byte CMD_USB_POWER =	 			0x1A;	
	
	public static final byte CMD_SYSTEM_NOTIFICATION =		0x1F;	
	
	
	
	public static final byte CMD_HID_STATUS_REPORT = 		0x20;
	public static final byte CMD_HID_DATA_KEYB = 			0x21;
	public static final byte CMD_HID_DATA_CONSUMER =		0x22;
	public static final byte CMD_HID_DATA_MOUSE = 			0x23;
	public static final byte CMD_HID_DATA_GAMEPAD = 		0x24;
	
	public static final byte CMD_HID_DATA_ENDP = 			0x2B;
	public static final byte CMD_HID_DATA_KEYB_FAST = 		0x2C;
	public static final byte CMD_HID_DATA_KEYB_FASTEST =	0x2D;
	//out	
	public static final byte CMD_HID_STATUS =				0x2F;
	
	
	
	public static final byte CMD_DUMMY =	 				(byte)0xFF;
	
	
	public static final byte RESP_OK =						0x01;
	public static final byte RESP_UNKNOWN_CMD =				(byte)0xFF;
	
	
	public static final byte[] RAW_OLD_BOOTLOADER = new byte[] {START_TAG, (byte)0x00, (byte)0x02, (byte)0x83, (byte)0x00, (byte)0xDA};			
	public static final byte[] RAW_DELAY_1_MS = new byte[] {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01}; 
	
	private byte[] mData;
	private int mPos;
	private boolean mRespond;
	
	//do not modify
	public Packet(boolean respond, byte[] data) {
		mRespond = respond;
		mData = data;
		mPos = data.length;		
	}
	
	public Packet(boolean respond, byte cmd, byte param, byte[] data) {
		mRespond = respond;
		mData = new byte[MAX_LENGTH];
		mData[0] = cmd;
		mData[1] = param;
		mPos = 2;		
		if (data != null) {
			addBytes(data);
		}		
	}
	
	public Packet(boolean respond, byte cmd, byte param) {
		this(respond, cmd, param, null);
	}	
	
	public Packet(boolean respond, byte cmd) {
		mRespond = respond;
		mData = new byte[MAX_LENGTH];
		mData[0] = cmd;
		mPos = 1;
	}	
	
	public void modifyByte(int pos, byte b) {
		mData[pos] = b;
	}
	
	public void addBytes(byte[] data) {
		//TODO check null pointer / available size (MAX_PAYLOAD - mPos) 
		System.arraycopy(data, 0, mData, mPos, data.length);
		mPos += data.length;		
	}
	
	public void addByte(byte b) {
		mData[mPos++] = b;
	}
	
	public void addInt16(int val) {
		mData[mPos + 0] = Util.getMSB(val);
		mData[mPos + 1] = Util.getLSB(val);
		mPos += 2;
	}
	
	public void addInt32(long val) {
		mData[mPos + 3] = (byte)val;
		val >>= 8;
		mData[mPos + 2] = (byte)val;
		val >>= 8;
		mData[mPos + 1] = (byte)val;
		val >>= 8;
		mData[mPos + 0] = (byte)val;
		val >>= 8;	
		mPos += 4;
	}	
	
	
	public byte[] getBytes() {
		byte[] result;		
		result = new byte[mPos];
		System.arraycopy(mData, 0, result, 0, mPos);
		return result;
	}
	
	public boolean getRespond() {
		return mRespond;
	}

	public void print() {		
		Util.printHex(mData, "PACKET DATA:");
	}
	
}
